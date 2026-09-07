package com.quizapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * JPA Entity representing the 'answers' table.
 * Records the specific option selected by a user for a question during an attempt.
 */
@Entity
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many answers belong to one attempt session
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false)
    private Attempt attempt;

    // The question being answered
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // The choice/option chosen by the student
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "selected_option_id", nullable = false)
    private Option selectedOption;

    public Answer() {
    }

    public Answer(Attempt attempt, Question question, Option selectedOption) {
        this.attempt = attempt;
        this.question = question;
        this.selectedOption = selectedOption;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Attempt getAttempt() {
        return attempt;
    }

    public void setAttempt(Attempt attempt) {
        this.attempt = attempt;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Option getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(Option selectedOption) {
        this.selectedOption = selectedOption;
    }
}
