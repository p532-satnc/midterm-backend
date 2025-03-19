package com.example.satnc.controllers;

import com.example.satnc.model.Quiz;
import com.example.satnc.model.Question;
import com.example.satnc.repository.QuizRepository;
import com.example.satnc.repository.QuestionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/quizzes")
public class QuizController {
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;

    public QuizController(QuizRepository quizRepository, QuestionRepository questionRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
    }

    @PostMapping
    public ResponseEntity<Long> addQuiz(@RequestBody Quiz quiz) {
        Quiz savedQuiz = quizRepository.save(quiz);
        return ResponseEntity.ok(savedQuiz.getId());
    }

    @GetMapping
    public List<Quiz> getAllQuizzes() {
        List<Quiz> quizzes = quizRepository.findAll();


        for (Quiz quiz : quizzes) {
            List<Question> questions = quiz.getQuestionIds().stream()
                    .map(id -> questionRepository.findById(id)
                            .orElse(null))
                    .filter(q -> q != null)
                    .collect(Collectors.toList());

            quiz.setQuestions(questions);
        }

        return quizzes;
    }
    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable Long id) {
        Optional<Quiz> quizOptional = quizRepository.findById(id);
        if (quizOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Quiz quiz = quizOptional.get();
        List<Question> questions = quiz.getQuestionIds().stream()
                .map(questionRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        quiz.setQuestions(questions);
        return ResponseEntity.ok(quiz);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateQuiz(@PathVariable Long id, @RequestBody Quiz updatedQuiz) {
        Optional<Quiz> quizOptional = quizRepository.findById(id);
        if (quizOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Quiz quiz = quizOptional.get();
        if (updatedQuiz.getTitle() != null) {
            quiz.setTitle(updatedQuiz.getTitle());
        }
        if (updatedQuiz.getQuestionIds() != null) {
            quiz.setQuestionIds(updatedQuiz.getQuestionIds());
        }

        quizRepository.update(quiz);
        return ResponseEntity.ok("Quiz updated successfully.");
    }
}

