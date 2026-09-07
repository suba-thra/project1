package com.quizapp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Request DTO for creating or updating a quiz.
 */
public class QuizRequestDto {

    @NotBlank(message = "Quiz title is required")
    @Size(max = 150, message = "Quiz title cannot exceed 150 characters")
    private String title;

    private String description;

    @Min(value = 1, message = "Time limit must be at least 1 minute")
    private Integer timeLimitMinutes = 10;

    @Min(value = 0, message = "Passing score must be at least 0")
    @Max(value = 100, message = "Passing score cannot exceed 100")
    private Integer passingScore = 60;

    @NotNull(message = "Creator user ID (createdById) is required")
    private Long createdById;

    @Valid
    private List<QuestionRequestDto> questions = new ArrayList<>();

    public QuizRequestDto() {
    }

    public QuizRequestDto(String title, String description, Integer timeLimitMinutes, Integer passingScore, Long createdById, List<QuestionRequestDto> questions) {
        this.title = title;
        this.description = description;
        this.timeLimitMinutes = (timeLimitMinutes != null) ? timeLimitMinutes : 10;
        this.passingScore = (passingScore != null) ? passingScore : 60;
        this.createdById = createdById;
        this.questions = (questions != null) ? questions : new ArrayList<>();
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

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public List<QuestionRequestDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionRequestDto> questions) {
        this.questions = questions;
    }
}
