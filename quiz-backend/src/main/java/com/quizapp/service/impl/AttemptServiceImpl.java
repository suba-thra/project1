package com.quizapp.service.impl;

import com.quizapp.dto.AttemptResponseDto;
import com.quizapp.entity.Attempt;
import com.quizapp.entity.Quiz;
import com.quizapp.entity.User;
import com.quizapp.exception.ResourceNotFoundException;
import com.quizapp.repository.AttemptRepository;
import com.quizapp.repository.QuizRepository;
import com.quizapp.repository.UserRepository;
import com.quizapp.service.AttemptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for Quiz Attempts with AttemptResponseDto transformation.
 */
@Service
@Transactional
public class AttemptServiceImpl implements AttemptService {

    private final AttemptRepository attemptRepository;
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;

    public AttemptServiceImpl(AttemptRepository attemptRepository,
                              UserRepository userRepository,
                              QuizRepository quizRepository) {
        this.attemptRepository = attemptRepository;
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
    }

    @Override
    public AttemptResponseDto startAttempt(Long userId, Long quizId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));

        Attempt attempt = new Attempt(user, quiz);
        Attempt savedAttempt = attemptRepository.save(attempt);
        return mapToResponseDto(savedAttempt);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttemptResponseDto> getAllAttempts() {
        return attemptRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AttemptResponseDto getAttemptById(Long id) {
        Attempt attempt = attemptRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attempt", "id", id));
        return mapToResponseDto(attempt);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttemptResponseDto> getAttemptsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        return attemptRepository.findByUserId(userId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttemptResponseDto> getAttemptsByQuiz(Long quizId) {
        if (!quizRepository.existsById(quizId)) {
            throw new ResourceNotFoundException("Quiz", "id", quizId);
        }
        return attemptRepository.findByQuizId(quizId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private AttemptResponseDto mapToResponseDto(Attempt attempt) {
        return new AttemptResponseDto(
                attempt.getId(),
                attempt.getUser().getId(),
                attempt.getUser().getUsername(),
                attempt.getQuiz().getId(),
                attempt.getQuiz().getTitle(),
                attempt.getScore(),
                attempt.getPassed(),
                attempt.getStatus(),
                attempt.getStartedAt(),
                attempt.getCompletedAt()
        );
    }
}
