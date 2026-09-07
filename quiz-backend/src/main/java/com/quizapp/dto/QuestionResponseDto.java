package com.quizapp.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Response DTO for Question entity including its options.
 */
public class QuestionResponseDto {

    private Long id;
    private String content;
    private Integer points;
    private String questionType;
    private List<OptionResponseDto> options = new ArrayList<>();

    public QuestionResponseDto() {
    }

    public QuestionResponseDto(Long id, String content, Integer points, String questionType, List<OptionResponseDto> options) {
        this.id = id;
        this.content = content;
        this.points = points;
        this.questionType = questionType;
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

    public List<OptionResponseDto> getOptions() {
        return options;
    }

    public void setOptions(List<OptionResponseDto> options) {
        this.options = options;
    }
}
