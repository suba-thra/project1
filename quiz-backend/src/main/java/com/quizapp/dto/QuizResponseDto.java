package com.quizapp.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Response DTO for Quiz representation including nested questions and summary metrics.
 */
public class QuizResponseDto {

    private Long id;
    private String title;
    private String description;
    private Integer timeLimitMinutes;
    private Integer passingScore;
    private UserResponseDto createdBy;
    private Integer questionCount;
    private Integer totalPoints;
    private List<QuestionResponseDto> questions = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public QuizResponseDto() {
    }

    public QuizResponseDto(Long id, String title, String description, Integer timeLimitMinutes, Integer passingScore,
                           UserResponseDto createdBy, Integer questionCount, Integer totalPoints,
                           List<QuestionResponseDto> questions, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timeLimitMinutes = timeLimitMinutes;
        this.passingScore = passingScore;
        this.createdBy = createdBy;
        this.questionCount = questionCount;
        this.totalPoints = totalPoints;
        this.questions = (questions != null) ? questions : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTimeLimitMinutes() {
        return timeLimitMinutes;
    }

    public void setTimeLimitMinutes(Integer timeLimitMinutes) {
        this.timeLimitMinutes = timeLimitMinutes;
    }

    public Integer getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(Integer passingScore) {
        this.passingScore = passingScore;
    }

    public UserResponseDto getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserResponseDto createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public List<QuestionResponseDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionResponseDto> questions) {
        this.questions = questions;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
