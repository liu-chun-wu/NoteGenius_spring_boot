package com.jeffery.notegenius.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/")
@RequiredArgsConstructor
public class UserController {

//    @GetMapping("me/")
//    public ResponseEntity<?> getCurrentUser(HttpSession session) {
//        Long userId = (Long) session.getAttribute("userId");
//        if (userId == null) return ResponseEntity.status(401).body("尚未登入");
//        return ResponseEntity.ok(userService.getUserById(userId));
//    }
//
//    @PostMapping
//    public ResponseEntity<?> register(@RequestBody UserRegisterDto dto) {
//        Long userId = (Long) session.getAttribute("userId");
//        if (userId == null) return ResponseEntity.status(401).body("尚未登入");
//        return ResponseEntity.ok(userService.registerUser(dto));
//    }
}
