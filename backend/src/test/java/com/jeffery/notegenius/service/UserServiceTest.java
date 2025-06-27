package com.jeffery.notegenius.service;

import com.jeffery.notegenius.model.*;
import com.jeffery.notegenius.repository.*;
import com.jeffery.notegenius.dto.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreateUser() {
        // Arrange
        UserCreateDto dto = new UserCreateDto();
        dto.setUsername("jeffery");
        dto.setEmail("jeffery@example.com");
        dto.setPassword("plaintext123");

        // Act & Assert
        assertDoesNotThrow(() -> userService.createUser(dto));

        // Verify repository was called with user containing expected fields
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User savedUser = captor.getValue();

        assertEquals("jeffery", savedUser.getUsername());
        assertEquals("jeffery@example.com", savedUser.getEmail());
        assertEquals("plaintext123", savedUser.getPassword());
    }

}
