package com.jeffery.notegenius.service;

import com.jeffery.notegenius.dto.UserCreateDto;
import com.jeffery.notegenius.dto.UserResponseDto;
import com.jeffery.notegenius.model.User;
import com.jeffery.notegenius.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ResponseEntity<?> registerUser(UserCreateDto userDto) {
        Map<String, String> errors = new HashMap<>();

        // 驗證 username
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            errors.put("username", "帳號已存在");
        }

        // 驗證 email
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            errors.put("email", "Email 已存在");
        }

        // 驗證密碼長度
        if (userDto.getPassword() == null || userDto.getPassword().length() < 6) {
            errors.put("password", "密碼需至少 6 字元");
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        // ✅ DTO → Entity
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setCreatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);
        return ResponseEntity.ok(toResponseDto(saved));
    }

    public ResponseEntity<?> getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(this::toResponseDto)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body("使用者不存在"));
    }

    private UserResponseDto toResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}
