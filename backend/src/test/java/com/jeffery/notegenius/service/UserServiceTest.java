package com.jeffery.notegenius.service;

import com.jeffery.notegenius.model.User;
import com.jeffery.notegenius.repository.UserRepository;
import com.jeffery.notegenius.dto.UserResponseDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testRegisterUser_shouldSaveUserAndReturnUserResponseDto() {
        // 原始參數直接傳入
        String username = "jeff";
        String email = "jeff@example.com";
        String password = "securePassword";

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername(username);
        savedUser.setEmail(email);
        savedUser.setPassword(password);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDto result = userService.registerUser(username, email, password);

        assertEquals(1L, result.getId());
        assertEquals(username, result.getUsername());
        assertEquals(email, result.getEmail());
    }

    @Test
    void testGetUserById_shouldReturnUserResponseDto() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("jeff");
        user.setEmail("jeff@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponseDto result = userService.getUserById(userId);

        assertEquals(userId, result.getId());
        assertEquals("jeff", result.getUsername());
        assertEquals("jeff@example.com", result.getEmail());
    }

    @Test
    void testGetPasswordByUsernameAndEmail_shouldReturnPassword() {
        String username = "jeff";
        String email = "jeff@example.com";
        String password = "securePassword";

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        when(userRepository.findByEmailAndUsername(username, email)).thenReturn(Optional.of(user));

        String result = userService.getPasswordByUsernameAndEmail(username, email);

        assertEquals(password, result);
    }
}
