package com.jeffery.notegenius.service;

import com.jeffery.notegenius.dto.*;
import com.jeffery.notegenius.model.*;
import com.jeffery.notegenius.repository.*;

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

    public void createUser(UserCreateDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // 暫時明文存儲
        userRepository.save(user);
    }

}
