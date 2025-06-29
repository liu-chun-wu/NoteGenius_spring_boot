package com.jeffery.notegenius.controller;

import com.jeffery.notegenius.dto.*;
import com.jeffery.notegenius.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/")
    public UserResponseDto registerUser(@RequestBody UserRegisterRequestDto dto) {
        return userService.registerUser(dto.getUsername(), dto.getEmail(), dto.getPassword());
    }

    @PostMapping("/query-password/")
    public Map<String, String> findPassword(@RequestBody UserFindPasswordRequestDto dto) {
        String password = userService.getPasswordByUsernameAndEmail(dto.getUsername(), dto.getEmail());
        return Map.of("password", password);
    }

    @GetMapping("/me/")
    public UserResponseDto getCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return userService.getUserById(userId);
    }

    // ✅ 新增：登入 API（不需登入）
    @PostMapping("/login/")
    public ResponseEntity<String> login(@RequestBody UserLoginRequestDto loginDto, HttpSession session) {
        String message = userService.login(loginDto, session);
        return ResponseEntity.ok(message + "，userId=" + session.getAttribute("userId"));
    }

    // ✅ 新增：登出 API（需登入）
    @PostMapping("/logout/")
    public ResponseEntity<String> logout(HttpSession session) {
        userService.logout(session);
        return ResponseEntity.ok("登出成功");
    }
}
