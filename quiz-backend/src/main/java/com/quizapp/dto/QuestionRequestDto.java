package com.quizapp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * Request DTO for creating or updating a question with its options.
 */
public class QuestionRequestDto {

    private Long id; // Optional, used for updates

    @NotBlank(message = "Question content cannot be blank")
    private String content;

    @Min(value = 1, message = "Points must be at least 1")
    private Integer points = 1;

    private String questionType = "SINGLE_CHOICE"; // "SINGLE_CHOICE", "MULTIPLE_CHOICE"

    @NotEmpty(message = "A question must contain at least one option")
    @Valid
    private List<OptionRequestDto> options = new ArrayList<>();

    public QuestionRequestDto() {
    }

    public QuestionRequestDto(String content, Integer points, String questionType, List<OptionRequestDto> options) {
        this.content = content;
        this.points = (points != null) ? points : 1;
        this.questionType = (questionType != null) ? questionType : "SINGLE_CHOICE";
        this.options = (options != null) ? options : new ArrayList<>();
    }

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

    public List<OptionRequestDto> getOptions() {
        return options;
    }

    public void setOptions(List<OptionRequestDto> options) {
        this.options = options;
    }
}
