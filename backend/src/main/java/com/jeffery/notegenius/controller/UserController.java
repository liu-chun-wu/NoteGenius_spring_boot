package com.jeffery.notegenius.controller;

import com.jeffery.notegenius.dto.UserCreateDto;
import com.jeffery.notegenius.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 註冊使用者
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserCreateDto userDto) {
        return userService.registerUser(userDto);
    }

    // 模擬 /users/me，用 session 取得當前使用者資訊
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("user_id");
        if (userId == null) {
            return ResponseEntity.status(401).body("尚未登入");
        }
        return userService.getUserById(userId);
    }
}
