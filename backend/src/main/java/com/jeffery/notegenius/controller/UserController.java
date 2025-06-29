package com.jeffery.notegenius.controller;

import com.jeffery.notegenius.dto.UserRegisterRequestDto;
import com.jeffery.notegenius.dto.UserFindPasswordRequestDto;
import com.jeffery.notegenius.dto.UserResponseDto;
import com.jeffery.notegenius.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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
}
