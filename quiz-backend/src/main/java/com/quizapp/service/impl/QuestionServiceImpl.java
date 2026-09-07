package com.quizapp.service.impl;

import com.quizapp.dto.OptionResponseDto;
import com.quizapp.dto.QuestionRequestDto;
import com.quizapp.dto.QuestionResponseDto;
import com.quizapp.entity.Option;
import com.quizapp.entity.Question;
import com.quizapp.entity.Quiz;
import com.quizapp.exception.ResourceNotFoundException;
import com.quizapp.repository.QuestionRepository;
import com.quizapp.repository.QuizRepository;
import com.quizapp.service.QuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for Question CRUD operations.
 */
@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository, QuizRepository quizRepository) {
        this.questionRepository = questionRepository;
        this.quizRepository = quizRepository;
    }

    @Override
    public QuestionResponseDto addQuestionToQuiz(Long quizId, QuestionRequestDto requestDto) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));

        Question question = new Question();
        question.setContent(requestDto.getContent());
        question.setPoints(requestDto.getPoints() != null ? requestDto.getPoints() : 1);
        question.setQuestionType(requestDto.getQuestionType() != null ? requestDto.getQuestionType() : "SINGLE_CHOICE");
        question.setQuiz(quiz);

        if (requestDto.getOptions() != null) {
            for (var optDto : requestDto.getOptions()) {
                Option option = new Option(optDto.getOptionText(), optDto.getIsCorrect());
                question.addOption(option);
            }
        }

        Question savedQuestion = questionRepository.save(question);
        return mapToResponseDto(savedQuestion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponseDto> getQuestionsByQuizId(Long quizId) {
        if (!quizRepository.existsById(quizId)) {
            throw new ResourceNotFoundException("Quiz", "id", quizId);
        }
        return questionRepository.findByQuizId(quizId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionResponseDto getQuestionById(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));
        return mapToResponseDto(question);
    }

    @Override
    public QuestionResponseDto updateQuestion(Long id, QuestionRequestDto requestDto) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));

        question.setContent(requestDto.getContent());
        if (requestDto.getPoints() != null) {
            question.setPoints(requestDto.getPoints());
        }
        if (requestDto.getQuestionType() != null) {
            question.setQuestionType(requestDto.getQuestionType());
        }

        // Update options if provided
        if (requestDto.getOptions() != null && !requestDto.getOptions().isEmpty()) {
            question.getOptions().clear();
            for (var optDto : requestDto.getOptions()) {
                Option option = new Option(optDto.getOptionText(), optDto.getIsCorrect());
                question.addOption(option);
            }
        }

        Question updatedQuestion = questionRepository.save(question);
        return mapToResponseDto(updatedQuestion);
    }

    @Override
    public void deleteQuestion(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));
        questionRepository.delete(question);
    }

    private QuestionResponseDto mapToResponseDto(Question question) {
        List<OptionResponseDto> optionDtos = question.getOptions().stream()
                .map(opt -> new OptionResponseDto(opt.getId(), opt.getOptionText(), opt.getIsCorrect()))
                .collect(Collectors.toList());

        return new QuestionResponseDto(
                question.getId(),
                question.getContent(),
                question.getPoints(),
                question.getQuestionType(),
                optionDtos
        );
    }
}
