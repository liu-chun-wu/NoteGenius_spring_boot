package com.jeffery.notegenius.service;

import com.jeffery.notegenius.dto.UserResponseDto;
import com.jeffery.notegenius.model.*;

import com.jeffery.notegenius.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto registerUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    public UserResponseDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return convertToDto(user);
    }

    public String getPasswordByUsernameAndEmail(String username, String email) {
        Optional<User> userOptional = userRepository.findByEmailAndUsername(username, email);
        return userOptional.map(User::getPassword)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private UserResponseDto convertToDto(User user) {
        return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail());
    }
}
