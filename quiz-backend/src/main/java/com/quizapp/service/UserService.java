package com.quizapp.service;

import com.quizapp.dto.UserRequestDto;
import com.quizapp.dto.UserResponseDto;
import java.util.List;

/**
 * Service interface defining user management CRUD operations.
 */
public interface UserService {

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserById(Long id);

    UserResponseDto createUser(UserRequestDto requestDto);

    UserResponseDto updateUser(Long id, UserRequestDto requestDto);

    void deleteUser(Long id);
}
