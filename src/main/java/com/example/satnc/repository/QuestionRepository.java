package com.example.satnc.repository;

import com.example.satnc.model.Question;

import java.util.List;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class QuestionRepository {
    private static final String FILE_PATH = "db.txt";

    public Question save(Question question) {

        if (question == null) {
            throw new IllegalArgumentException("Question cannot be null");
        }

        if (question.getChoices() == null) {
            question.setChoices(new ArrayList<>());
        }

        if (question.getImagePath() == null) {
            question.setImagePath("");
        }

        List<Question> questions = findAll();
        if (questions == null) {
            questions = new ArrayList<>();
        }
        question.setId(generateId(questions));
        questions.add(question);
        writeToFile(questions);
        return question;
    }

    public List<Question> findAll() {
        return readFromFile();
    }

    public Optional<Question> findById(Long id) {
        return findAll().stream().filter(q -> q.getId().equals(id)).findFirst();
    }

    public List<Question> findByDescriptionContainingIgnoreCase(String name) {
        return findAll().stream()
                .filter(q -> q.getDescription().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Question update(Question updatedQuestion) {
        List<Question> questions = findAll();
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getId().equals(updatedQuestion.getId())) {
                questions.set(i, updatedQuestion);
                writeToFile(questions);
                return updatedQuestion;
            }
        }
        return null;
    }

    public void deleteById(Long id) {
        List<Question> questions = findAll().stream()
                .filter(q -> !q.getId().equals(id))
                .collect(Collectors.toList());
        writeToFile(questions);
    }

    private long generateId(List<Question> questions) {
        if (questions == null || questions.isEmpty()) {
            return 1L; // Start from 1
        }
        Long lastId = questions.get(questions.size() - 1).getId();
        return (lastId != null) ? lastId + 1 : 1L;
    }

    private List<Question> readFromFile() {
        List<Question> questions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                Question question = parseQuestion(line);
                if (question != null) {  // Only add valid questions
                    questions.add(question);
                }
            }
        } catch (IOException e) {
            // File might not exist initially, which is okay.
        }
        return questions;
    }

    private void writeToFile(List<Question> questions) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Question q : questions) {
                bw.write(formatQuestion(q));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean updateQuestionImage(Long id, String imagePath) {
        List<Question> questions = findAll();
        if (questions == null || questions.isEmpty()) {
            return false; // No questions exist
        }

        boolean updated = false;
        for (Question q : questions) {
            if (q.getId().equals(id)) {
                if (!imagePath.equals(q.getImagePath())) {
                    q.setImagePath(imagePath);
                    updated = true;
                }
                break;
            }
        }

        if (updated) {
            writeToFile(questions); // Save only if changes were made
        }

        return updated;
    }

    private Question parseQuestion(String line) {
        if (line == null || line.trim().isEmpty()) {  // Handle empty lines
            System.out.println("Skipping empty line in database");
            return null;
        }
        String[] parts = line.split(";");
//        System.out.println(Arrays.toString(parts));
//        System.out.println("Parsing ID: '" + parts[0] + "'");

        Long id = Long.parseLong(parts[0]);
        String description = parts[1];
        List<String> choices = Arrays.asList(parts[2].split(","));
        String answer = parts[3];
        String imagePath = (parts.length > 4) ? parts[4] : "";

        Question question = new Question(id, description, choices, answer);
        question.setImagePath(imagePath);
        return question;
    }

    private String formatQuestion(Question question) {
        String choicesString = (question.getChoices() != null) ? String.join(",", question.getChoices()) : "";
        String imagePath = (question.getImagePath() != null) ? question.getImagePath() : "";

        return question.getId() + ";" + question.getDescription() + ";" +
                choicesString + ";" + question.getAnswer() + ";" + imagePath;
    }
}


