package com.quizapp.service;

import com.quizapp.dto.QuizRequestDto;
import com.quizapp.dto.QuizResponseDto;
import java.util.List;

/**
 * Service interface for Quiz management operations.
 */
public interface QuizService {

    List<QuizResponseDto> getAllQuizzes(String keyword);

    QuizResponseDto getQuizById(Long id);

    List<QuizResponseDto> getQuizzesByCreator(Long userId);

    QuizResponseDto createQuiz(QuizRequestDto requestDto);

    QuizResponseDto updateQuiz(Long id, QuizRequestDto requestDto);

    void deleteQuiz(Long id);
}
