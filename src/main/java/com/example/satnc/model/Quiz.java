package com.example.satnc.model;

import java.util.List;

public class Quiz {

    private Long id;

    private String title;

    private List<Long> questionIds;

    private List<Question> questions;

    public Quiz(Long id, String title, List<Long> questionIds) {
        this.id = id;
        this.title = title;
        this.questionIds = questionIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Long> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<Long> questionIds) {
        this.questionIds = questionIds;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}


