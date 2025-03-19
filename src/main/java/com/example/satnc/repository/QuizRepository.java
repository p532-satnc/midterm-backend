package com.example.satnc.repository;



import com.example.satnc.model.Question;
import com.example.satnc.model.Quiz;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class QuizRepository {
    private static final String FILE_PATH = "db1.txt";

    public Quiz save(Quiz quiz) {

        if (quiz == null) {
            throw new IllegalArgumentException("Question cannot be null");
        }
        List<Quiz> quizzes = findAll();
        if (quizzes == null) {
            quizzes = new ArrayList<>();
        }
        quiz.setId(generateId(quizzes));
        quizzes.add(quiz);
        writeToFile(quizzes);
        return quiz;
    }

    public List<Quiz> findAll() {
        return readFromFile();
    }

    public Optional<Quiz> findById(Long id) {
        return findAll().stream().filter(q -> q.getId().equals(id)).findFirst();
    }

    public Quiz update(Quiz updatedQuiz) {
        List<Quiz> quizzes = findAll();
        for (int i = 0; i < quizzes.size(); i++) {
            if (quizzes.get(i).getId().equals(updatedQuiz.getId())) {
                quizzes.set(i, updatedQuiz);
                writeToFile(quizzes);
                return updatedQuiz;
            }
        }
        return null;
    }

    public void deleteById(Long id) {
        List<Quiz> quizzes = findAll().stream()
                .filter(q -> !q.getId().equals(id))
                .collect(Collectors.toList());
        writeToFile(quizzes);
    }

    private Long generateId(List<Quiz> quizzes) {
        return quizzes.stream().mapToLong(Quiz::getId).max().orElse(0) + 1;
    }

    private List<Quiz> readFromFile() {
        List<Quiz> quizzes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                Quiz quiz = parseQuiz(line);
                if (quiz != null) {  // Only add valid questions
                    quizzes.add(quiz);
                }
            }
        } catch (IOException e) {
            // File might not exist initially, which is okay.
        }
        return quizzes;
    }

    private void writeToFile(List<Quiz> quizzes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Quiz q : quizzes) {
                bw.write(formatQuiz(q));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Quiz parseQuiz(String line) {
        if (line == null || line.trim().isEmpty()) {  // Handle empty lines
            System.out.println("Skipping empty line in database");
            return null;
        }

        String[] parts = line.split(";");

        Long id = Long.parseLong(parts[0]);
        String title = parts[1];
        List<Long> questionIds = Arrays.stream(parts[2].split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return new Quiz(id, title, questionIds);
    }

    private String formatQuiz(Quiz quiz) {
        List<Long> questionIds = quiz.getQuestionIds();
        String questions = (questionIds != null) ?
                questionIds.stream().map(String::valueOf).collect(Collectors.joining(",")) :
                "";
        return quiz.getId() + ";" + quiz.getTitle() + ";" + questions;
    }
}

