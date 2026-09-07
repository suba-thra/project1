package com.quizapp.service;

import com.quizapp.dto.AttemptResponseDto;
import java.util.List;

/**
 * Service interface for Quiz Attempt operations.
 */
public interface AttemptService {

    AttemptResponseDto startAttempt(Long userId, Long quizId);

    List<AttemptResponseDto> getAllAttempts();

    AttemptResponseDto getAttemptById(Long id);

    List<AttemptResponseDto> getAttemptsByUser(Long userId);

    List<AttemptResponseDto> getAttemptsByQuiz(Long quizId);
}
