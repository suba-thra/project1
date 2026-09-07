package com.quizapp.controller;

import com.quizapp.dto.ApiResponse;
import com.quizapp.dto.QuestionRequestDto;
import com.quizapp.dto.QuestionResponseDto;
import com.quizapp.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Question and Option CRUD operations.
 */
@RestController
@Tag(name = "Question Management", description = "Endpoints for managing quiz questions and multiple-choice options")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/api/v1/quizzes/{quizId}/questions")
    @Operation(summary = "Add a new question with choices to an existing quiz")
    public ResponseEntity<ApiResponse<QuestionResponseDto>> addQuestionToQuiz(
            @PathVariable Long quizId,
            @Valid @RequestBody QuestionRequestDto requestDto) {
        QuestionResponseDto createdQuestion = questionService.addQuestionToQuiz(quizId, requestDto);
        return new ResponseEntity<>(ApiResponse.success("Question added successfully", createdQuestion), HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/quizzes/{quizId}/questions")
    @Operation(summary = "Get all questions and choices for a specific quiz")
    public ResponseEntity<ApiResponse<List<QuestionResponseDto>>> getQuestionsByQuiz(@PathVariable Long quizId) {
        List<QuestionResponseDto> questions = questionService.getQuestionsByQuizId(quizId);
        return ResponseEntity.ok(ApiResponse.success("Questions retrieved successfully", questions));
    }

    @GetMapping("/api/v1/questions/{id}")
    @Operation(summary = "Get a single question by ID")
    public ResponseEntity<ApiResponse<QuestionResponseDto>> getQuestionById(@PathVariable Long id) {
        QuestionResponseDto question = questionService.getQuestionById(id);
        return ResponseEntity.ok(ApiResponse.success("Question retrieved successfully", question));
    }

    @PutMapping("/api/v1/questions/{id}")
    @Operation(summary = "Update question content, points, or options")
    public ResponseEntity<ApiResponse<QuestionResponseDto>> updateQuestion(
            @PathVariable Long id,
            @Valid @RequestBody QuestionRequestDto requestDto) {
        QuestionResponseDto updatedQuestion = questionService.updateQuestion(id, requestDto);
        return ResponseEntity.ok(ApiResponse.success("Question updated successfully", updatedQuestion));
    }

    @DeleteMapping("/api/v1/questions/{id}")
    @Operation(summary = "Delete a question by ID (cascades to options)")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok(ApiResponse.success("Question deleted successfully", null));
    }
}
