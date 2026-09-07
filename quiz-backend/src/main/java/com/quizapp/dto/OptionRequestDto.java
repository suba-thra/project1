package com.quizapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating a question choice/option.
 */
public class OptionRequestDto {

    private Long id; // Optional, used for updates

    @NotBlank(message = "Option text cannot be blank")
    @Size(max = 500, message = "Option text must not exceed 500 characters")
    private String optionText;

    @NotNull(message = "isCorrect flag must be specified (true or false)")
    private Boolean isCorrect = false;

    public OptionRequestDto() {
    }

    public OptionRequestDto(String optionText, Boolean isCorrect) {
        this.optionText = optionText;
        this.isCorrect = isCorrect != null ? isCorrect : false;
    }

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
}
