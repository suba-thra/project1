package com.quizapp.controller;

import com.quizapp.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Health check endpoint to verify backend operational readiness.
 */
@RestController
@RequestMapping("/api/v1/health")
@Tag(name = "Health Check", description = "Endpoints for checking system health")
public class HealthController {

    @GetMapping
    @Operation(summary = "Check backend server status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkHealth() {
        Map<String, Object> status = new HashMap<>();
        status.put("application", "Online Quiz Application Backend");
        status.put("status", "UP");
        status.put("version", "1.0.0");
        return ResponseEntity.ok(ApiResponse.success("Quiz Application Backend is running smoothly", status));
    }
}
