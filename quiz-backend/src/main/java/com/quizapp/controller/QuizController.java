package com.quizapp.controller;

import com.quizapp.dto.ApiResponse;
import com.quizapp.dto.QuizRequestDto;
import com.quizapp.dto.QuizResponseDto;
import com.quizapp.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Quiz management CRUD operations.
 */
@RestController
@RequestMapping("/api/v1/quizzes")
@Tag(name = "Quiz Management", description = "Endpoints for creating, updating, searching, and viewing quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    @Operation(summary = "Get all available quizzes with optional keyword search")
    public ResponseEntity<ApiResponse<List<QuizResponseDto>>> getAllQuizzes(
            @RequestParam(required = false) String search) {
        List<QuizResponseDto> quizzes = quizService.getAllQuizzes(search);
        return ResponseEntity.ok(ApiResponse.success("Quizzes retrieved successfully", quizzes));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get full quiz details with questions and options by quiz ID")
    public ResponseEntity<ApiResponse<QuizResponseDto>> getQuizById(@PathVariable Long id) {
        QuizResponseDto quiz = quizService.getQuizById(id);
        return ResponseEntity.ok(ApiResponse.success("Quiz retrieved successfully", quiz));
    }

    @GetMapping("/creator/{userId}")
    @Operation(summary = "Get all quizzes created by a specific instructor/user")
    public ResponseEntity<ApiResponse<List<QuizResponseDto>>> getQuizzesByCreator(@PathVariable Long userId) {
        List<QuizResponseDto> quizzes = quizService.getQuizzesByCreator(userId);
        return ResponseEntity.ok(ApiResponse.success("Quizzes retrieved successfully", quizzes));
    }

    @PostMapping
    @Operation(summary = "Create a new quiz (with optional nested questions and options)")
    public ResponseEntity<ApiResponse<QuizResponseDto>> createQuiz(@Valid @RequestBody QuizRequestDto requestDto) {
        QuizResponseDto createdQuiz = quizService.createQuiz(requestDto);
        return new ResponseEntity<>(ApiResponse.success("Quiz created successfully", createdQuiz), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing quiz's title, description, time limit, or passing score")
    public ResponseEntity<ApiResponse<QuizResponseDto>> updateQuiz(
            @PathVariable Long id,
            @Valid @RequestBody QuizRequestDto requestDto) {
        QuizResponseDto updatedQuiz = quizService.updateQuiz(id, requestDto);
        return ResponseEntity.ok(ApiResponse.success("Quiz updated successfully", updatedQuiz));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a quiz by ID (cascades to questions and options)")
    public ResponseEntity<ApiResponse<Void>> deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.ok(ApiResponse.success("Quiz deleted successfully", null));
    }
}
