package com.jeffery.notegenius.service;

import com.jeffery.notegenius.model.User;
import com.jeffery.notegenius.repository.UserRepository;
import com.jeffery.notegenius.dto.*;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
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

    @Test
    void testLogin_shouldReturnSuccessMessage() {
        // Arrange
        UserLoginRequestDto loginDto = new UserLoginRequestDto();
        loginDto.setUsername("jeffery");
        loginDto.setPassword("123456");

        User user = new User();
        user.setId(1L);
        user.setUsername("jeffery");
        user.setPassword("123456");

        HttpSession mockSession = new MockHttpSession();

        when(userRepository.findByUsername("jeffery")).thenReturn(Optional.of(user));

        // Act
        String result = userService.login(loginDto, mockSession);

        // Assert
        assertEquals("登入成功", result);
        assertEquals(1L, mockSession.getAttribute("userId"));

        verify(userRepository).findByUsername("jeffery");
    }

    @Test
    void testLogin_userNotFound_shouldThrowException() {
        // Arrange
        UserLoginRequestDto loginDto = new UserLoginRequestDto();
        loginDto.setUsername("notfound");
        loginDto.setPassword("123456");

        when(userRepository.findByUsername("notfound")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.login(loginDto, new MockHttpSession())
        );
        assertEquals("使用者不存在", exception.getMessage());
    }

    @Test
    void testLogin_wrongPassword_shouldThrowException() {
        // Arrange
        UserLoginRequestDto loginDto = new UserLoginRequestDto();
        loginDto.setUsername("jeffery");
        loginDto.setPassword("wrongpass");

        User user = new User();
        user.setId(1L);
        user.setUsername("jeffery");
        user.setPassword("correctpass");

        when(userRepository.findByUsername("jeffery")).thenReturn(Optional.of(user));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.login(loginDto, new MockHttpSession())
        );
        assertEquals("密碼錯誤", exception.getMessage());
    }

    @Test
    void testLogout_shouldInvalidateSession() {
        // Arrange：建立並放入 userId 模擬登入狀態
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", 1L);
        assertNotNull(session.getAttribute("userId")); // 確保 session 有資料

        // Act：執行 logout
        userService.logout(session);

        // Assert：確認 session 已失效（Spring Mock session 的方式）
        assertTrue(session.isInvalid());
    }

}
