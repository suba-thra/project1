package com.quizapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * JPA Entity representing the 'options' table.
 * Stores individual choices for a question in a quiz.
 */
@Entity
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Option text cannot be blank")
    @Size(max = 500, message = "Option text must not exceed 500 characters")
    @Column(name = "option_text", nullable = false, length = 500)
    private String optionText;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect = false;

    // Many options belong to one question
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    public Option() {
    }

    public Option(String optionText, Boolean isCorrect) {
        this.optionText = optionText;
        this.isCorrect = isCorrect != null ? isCorrect : false;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
