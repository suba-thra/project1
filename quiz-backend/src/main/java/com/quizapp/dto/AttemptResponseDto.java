package com.quizapp.dto;

import java.time.LocalDateTime;

/**
 * Response DTO representing a quiz attempt session and user performance.
 */
public class AttemptResponseDto {

    private Long id;
    private Long userId;
    private String username;
    private Long quizId;
    private String quizTitle;
    private Integer score;
    private Boolean passed;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    public AttemptResponseDto() {
    }

    public AttemptResponseDto(Long id, Long userId, String username, Long quizId, String quizTitle,
                              Integer score, Boolean passed, String status,
                              LocalDateTime startedAt, LocalDateTime completedAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.quizId = quizId;
        this.quizTitle = quizTitle;
        this.score = score;
        this.passed = passed;
        this.status = status;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
