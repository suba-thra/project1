package com.quizapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Entity representing the 'questions' table.
 * Stores question content, points, and choice options.
 */
@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Question content cannot be blank")
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Min(value = 1, message = "Points must be at least 1")
    @Column(nullable = false)
    private Integer points = 1;

    @Column(name = "question_type", nullable = false, length = 30)
    private String questionType = "SINGLE_CHOICE"; // e.g. "SINGLE_CHOICE", "MULTIPLE_CHOICE"

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Many questions belong to one quiz
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    // A question has multiple options (choices)
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Option> options = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Question() {
    }

    public Question(String content, Integer points, String questionType) {
        this.content = content;
        this.points = (points != null) ? points : 1;
        this.questionType = (questionType != null) ? questionType : "SINGLE_CHOICE";
    }

    // Helper methods to maintain bidirectional relationship consistency
    public void addOption(Option option) {
        options.add(option);
        option.setQuestion(this);
    }

    public void removeOption(Option option) {
        options.remove(option);
        option.setQuestion(null);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}
