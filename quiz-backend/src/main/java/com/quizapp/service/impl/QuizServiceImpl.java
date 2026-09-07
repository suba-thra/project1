package com.quizapp.service.impl;

import com.quizapp.dto.*;
import com.quizapp.entity.Option;
import com.quizapp.entity.Question;
import com.quizapp.entity.Quiz;
import com.quizapp.entity.User;
import com.quizapp.exception.ResourceNotFoundException;
import com.quizapp.repository.QuizRepository;
import com.quizapp.repository.UserRepository;
import com.quizapp.service.QuizService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for Quiz operations with full DTO transformations.
 */
@Service
@Transactional
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;

    public QuizServiceImpl(QuizRepository quizRepository, UserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuizResponseDto> getAllQuizzes(String keyword) {
        List<Quiz> quizzes;
        if (keyword != null && !keyword.trim().isEmpty()) {
            quizzes = quizRepository.findByTitleContainingIgnoreCase(keyword.trim());
        } else {
            quizzes = quizRepository.findAll();
        }

        return quizzes.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public QuizResponseDto getQuizById(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));
        return mapToResponseDto(quiz);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuizResponseDto> getQuizzesByCreator(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }

        return quizRepository.findByCreatedById(userId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public QuizResponseDto createQuiz(QuizRequestDto requestDto) {
        User creator = userRepository.findById(requestDto.getCreatedById())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", requestDto.getCreatedById()));

        Quiz quiz = new Quiz();
        quiz.setTitle(requestDto.getTitle());
        quiz.setDescription(requestDto.getDescription());
        quiz.setTimeLimitMinutes(requestDto.getTimeLimitMinutes());
        quiz.setPassingScore(requestDto.getPassingScore());
        quiz.setCreatedBy(creator);

        // Add nested questions and options if provided
        if (requestDto.getQuestions() != null && !requestDto.getQuestions().isEmpty()) {
            for (QuestionRequestDto qDto : requestDto.getQuestions()) {
                Question question = new Question();
                question.setContent(qDto.getContent());
                question.setPoints(qDto.getPoints() != null ? qDto.getPoints() : 1);
                question.setQuestionType(qDto.getQuestionType() != null ? qDto.getQuestionType() : "SINGLE_CHOICE");

                if (qDto.getOptions() != null) {
                    for (OptionRequestDto optDto : qDto.getOptions()) {
                        Option option = new Option(optDto.getOptionText(), optDto.getIsCorrect());
                        question.addOption(option);
                    }
                }

                quiz.addQuestion(question);
            }
        }

        Quiz savedQuiz = quizRepository.save(quiz);
        return mapToResponseDto(savedQuiz);
    }

    @Override
    public QuizResponseDto updateQuiz(Long id, QuizRequestDto requestDto) {
        Quiz existingQuiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));

        existingQuiz.setTitle(requestDto.getTitle());
        existingQuiz.setDescription(requestDto.getDescription());
        if (requestDto.getTimeLimitMinutes() != null) {
            existingQuiz.setTimeLimitMinutes(requestDto.getTimeLimitMinutes());
        }
        if (requestDto.getPassingScore() != null) {
            existingQuiz.setPassingScore(requestDto.getPassingScore());
        }

        Quiz updatedQuiz = quizRepository.save(existingQuiz);
        return mapToResponseDto(updatedQuiz);
    }

    @Override
    public void deleteQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));
        quizRepository.delete(quiz);
    }

    private QuizResponseDto mapToResponseDto(Quiz quiz) {
        UserResponseDto creatorDto = new UserResponseDto(
                quiz.getCreatedBy().getId(),
                quiz.getCreatedBy().getUsername(),
                quiz.getCreatedBy().getEmail(),
                quiz.getCreatedBy().getRole(),
                quiz.getCreatedBy().getCreatedAt()
        );

        List<QuestionResponseDto> questionDtos = quiz.getQuestions().stream()
                .map(q -> {
                    List<OptionResponseDto> optDtos = q.getOptions().stream()
                            .map(o -> new OptionResponseDto(o.getId(), o.getOptionText(), o.getIsCorrect()))
                            .collect(Collectors.toList());

                    return new QuestionResponseDto(
                            q.getId(),
                            q.getContent(),
                            q.getPoints(),
                            q.getQuestionType(),
                            optDtos
                    );
                })
                .collect(Collectors.toList());

        int totalPoints = quiz.getQuestions().stream()
                .mapToInt(Question::getPoints)
                .sum();

        return new QuizResponseDto(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getDescription(),
                quiz.getTimeLimitMinutes(),
                quiz.getPassingScore(),
                creatorDto,
                quiz.getQuestions().size(),
                totalPoints,
                questionDtos,
                quiz.getCreatedAt(),
                quiz.getUpdatedAt()
        );
    }
}
