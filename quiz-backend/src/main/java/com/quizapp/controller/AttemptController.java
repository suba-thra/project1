package com.quizapp.controller;

import com.quizapp.dto.ApiResponse;
import com.quizapp.dto.AttemptResponseDto;
import com.quizapp.service.AttemptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for quiz attempts and session management.
 */
@RestController
@RequestMapping("/api/v1/attempts")
@Tag(name = "Quiz Attempts", description = "Endpoints for starting, tracking, and viewing quiz attempt histories")
public class AttemptController {

    private final AttemptService attemptService;

    public AttemptController(AttemptService attemptService) {
        this.attemptService = attemptService;
    }

    @PostMapping("/start")
    @Operation(summary = "Start a new quiz attempt session")
    public ResponseEntity<ApiResponse<AttemptResponseDto>> startAttempt(
            @RequestParam Long userId,
            @RequestParam Long quizId) {

        AttemptResponseDto attempt = attemptService.startAttempt(userId, quizId);
        return new ResponseEntity<>(ApiResponse.success("Quiz attempt started successfully", attempt), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all quiz attempts across the platform")
    public ResponseEntity<ApiResponse<List<AttemptResponseDto>>> getAllAttempts() {
        List<AttemptResponseDto> attempts = attemptService.getAllAttempts();
        return ResponseEntity.ok(ApiResponse.success("Attempts retrieved successfully", attempts));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get attempt details by ID")
    public ResponseEntity<ApiResponse<AttemptResponseDto>> getAttemptById(@PathVariable Long id) {
        AttemptResponseDto attempt = attemptService.getAttemptById(id);
        return ResponseEntity.ok(ApiResponse.success("Attempt retrieved successfully", attempt));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all quiz attempts by a specific user")
    public ResponseEntity<ApiResponse<List<AttemptResponseDto>>> getAttemptsByUser(@PathVariable Long userId) {
        List<AttemptResponseDto> attempts = attemptService.getAttemptsByUser(userId);
        return ResponseEntity.ok(ApiResponse.success("User attempts retrieved successfully", attempts));
    }

    @GetMapping("/quiz/{quizId}")
    @Operation(summary = "Get all attempts for a specific quiz")
    public ResponseEntity<ApiResponse<List<AttemptResponseDto>>> getAttemptsByQuiz(@PathVariable Long quizId) {
        List<AttemptResponseDto> attempts = attemptService.getAttemptsByQuiz(quizId);
        return ResponseEntity.ok(ApiResponse.success("Quiz attempts retrieved successfully", attempts));
    }
}
