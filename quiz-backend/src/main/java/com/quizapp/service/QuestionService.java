package com.quizapp.service;

import com.quizapp.dto.QuestionRequestDto;
import com.quizapp.dto.QuestionResponseDto;
import java.util.List;

/**
 * Service interface for question management operations.
 */
public interface QuestionService {

    QuestionResponseDto addQuestionToQuiz(Long quizId, QuestionRequestDto requestDto);

    List<QuestionResponseDto> getQuestionsByQuizId(Long quizId);

    QuestionResponseDto getQuestionById(Long id);

    QuestionResponseDto updateQuestion(Long id, QuestionRequestDto requestDto);

    void deleteQuestion(Long id);
}
