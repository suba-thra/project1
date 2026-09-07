package com.quizapp.service.impl;

import com.quizapp.dto.UserRequestDto;
import com.quizapp.dto.UserResponseDto;
import com.quizapp.entity.User;
import com.quizapp.exception.ResourceNotFoundException;
import com.quizapp.repository.UserRepository;
import com.quizapp.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for User CRUD operations.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return mapToResponseDto(user);
    }

    @Override
    public UserResponseDto createUser(UserRequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new IllegalArgumentException("Username '" + requestDto.getUsername() + "' is already taken");
        }
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("Email '" + requestDto.getEmail() + "' is already registered");
        }

        User user = new User(
                requestDto.getUsername(),
                requestDto.getEmail(),
                requestDto.getPassword(),
                requestDto.getRole()
        );

        User savedUser = userRepository.save(user);
        return mapToResponseDto(savedUser);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto requestDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        // Check if username changed and is unique
        if (!existingUser.getUsername().equals(requestDto.getUsername()) &&
                userRepository.existsByUsername(requestDto.getUsername())) {
            throw new IllegalArgumentException("Username '" + requestDto.getUsername() + "' is already taken");
        }

        // Check if email changed and is unique
        if (!existingUser.getEmail().equals(requestDto.getEmail()) &&
                userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("Email '" + requestDto.getEmail() + "' is already registered");
        }

        existingUser.setUsername(requestDto.getUsername());
        existingUser.setEmail(requestDto.getEmail());
        if (requestDto.getPassword() != null && !requestDto.getPassword().isBlank()) {
            existingUser.setPassword(requestDto.getPassword());
        }
        if (requestDto.getRole() != null && !requestDto.getRole().isBlank()) {
            existingUser.setRole(requestDto.getRole());
        }

        User updatedUser = userRepository.save(existingUser);
        return mapToResponseDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepository.delete(user);
    }

    private UserResponseDto mapToResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
