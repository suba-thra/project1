package com.quizapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Entity representing the 'attempts' table.
 * Tracks each time a user takes a quiz, along with their score and status.
 */
@Entity
@Table(name = "attempts")
public class Attempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user who made this attempt
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // The quiz being attempted
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(nullable = false)
    private Integer score = 0;

    @Column(nullable = false)
    private Boolean passed = false;

    @Column(name = "started_at", nullable = false, updatable = false)
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(nullable = false, length = 20)
    private String status = "IN_PROGRESS"; // "IN_PROGRESS", "COMPLETED", "ABANDONED"

    // Answers submitted during this attempt
    @OneToMany(mappedBy = "attempt", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Answer> answers = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.startedAt = LocalDateTime.now();
    }

    public Attempt() {
    }

    public Attempt(User user, Quiz quiz) {
        this.user = user;
        this.quiz = quiz;
        this.status = "IN_PROGRESS";
        this.score = 0;
        this.passed = false;
    }

    // Helper methods for managing answers
    public void addAnswer(Answer answer) {
        answers.add(answer);
        answer.setAttempt(this);
    }

    public void removeAnswer(Answer answer) {
        answers.remove(answer);
        answer.setAttempt(null);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Boolean getPassed() {
        return passed;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
