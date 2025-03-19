package com.example.satnc.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import com.example.satnc.repository.QuestionRepository;
import com.example.satnc.model.Question;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/questions")
public class QuestionController {
        private final QuestionRepository questionRepository;
        private static final String IMAGE_DIRECTORY = "images/";

        public QuestionController(QuestionRepository questionRepository) {
            this.questionRepository = questionRepository;
        }

        @PostMapping
        public ResponseEntity<Long> addQuestion(@RequestBody Question question) {
            try {

                Question savedQuestion = questionRepository.save(question);
                return ResponseEntity.ok(savedQuestion.getId());
            } catch (Exception e) {
                e.printStackTrace(); // This will print the full stack trace in logs
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        @GetMapping
        public ResponseEntity<List<Question>> getAllQuestions() {
            return ResponseEntity.ok(questionRepository.findAll());
        }

        @GetMapping("/{id}")
        public ResponseEntity<Question> getQuestion(@PathVariable Long id) {
            return questionRepository.findById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }

        @PostMapping("/{id}/image")
        public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty.");
            }
            Optional<Question> questionOptional = questionRepository.findById(id);
            if (questionOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            try {
                // Define the original file path (without modification)
                String originalFilePath = IMAGE_DIRECTORY + file.getOriginalFilename();

                // Save the file at the original path
                Files.createDirectories(Paths.get(IMAGE_DIRECTORY));
                Path imagePath = Paths.get(originalFilePath);
                Files.write(imagePath, file.getBytes());

                // Update the database with the original path
                boolean updated = questionRepository.updateQuestionImage(id, originalFilePath);
                if (!updated) {
                    return ResponseEntity.notFound().build();
                }

                return ResponseEntity.ok("Image uploaded successfully.");
            } catch (IOException e) {
                return ResponseEntity.internalServerError().body("Error saving image");
            }
        }

        @GetMapping("/{id}/image")
        public ResponseEntity<String> getImagePath(@PathVariable Long id) {
            Optional<Question> questionOptional = questionRepository.findById(id);
            if (questionOptional.isEmpty() || questionOptional.get().getImagePath() == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(questionOptional.get().getImagePath());
        }

        @GetMapping("/search")
        public ResponseEntity<List<Question>> searchQuestions(@RequestParam String name) {
            return ResponseEntity.ok(questionRepository.findByDescriptionContainingIgnoreCase(name));
        }
}
