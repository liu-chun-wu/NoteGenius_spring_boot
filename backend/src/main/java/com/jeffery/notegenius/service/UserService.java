package com.jeffery.notegenius.service;

import com.jeffery.notegenius.dto.*;
import com.jeffery.notegenius.model.*;

import com.jeffery.notegenius.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
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

    public String login(UserLoginRequestDto loginDto, HttpSession session) {
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new RuntimeException("使用者不存在"));

        if (!user.getPassword().equals(loginDto.getPassword())) {
            throw new RuntimeException("密碼錯誤");
        }

        session.setAttribute("userId", user.getId());
        return "登入成功";
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }
}
