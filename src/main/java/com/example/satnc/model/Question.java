package com.example.satnc.model;

import java.util.List;


public class Question {

    private Long id;

    private String description;


    private List<String> choices;

    private String answer;

    private String imagePath = "";

    public Question(Long id, String description, List<String> choices, String answer) {
        this.id = id;
        this.description = description;
        this.choices = choices;
        this.answer = answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}



