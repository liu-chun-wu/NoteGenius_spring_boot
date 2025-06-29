package com.jeffery.notegenius.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeffery.notegenius.dto.*;
import com.jeffery.notegenius.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        session.setAttribute("userId", 1L);
    }

    @Test
    void testRegisterUser_shouldReturnUserResponse() throws Exception {
        UserRegisterRequestDto requestDto = new UserRegisterRequestDto();
        requestDto.setUsername("jeffery");
        requestDto.setEmail("jeffery@example.com");
        requestDto.setPassword("123456");

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(1L);
        responseDto.setUsername("jeffery");
        responseDto.setEmail("jeffery@example.com");

        when(userService.registerUser(
                eq("jeffery"),
                eq("jeffery@example.com"),
                eq("123456")
        )).thenReturn(responseDto);

        mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("jeffery"))
                .andExpect(jsonPath("$.email").value("jeffery@example.com"));
    }

    @Test
    void testFindPassword_shouldReturnPasswordAsJson() throws Exception {
        UserFindPasswordRequestDto requestDto = new UserFindPasswordRequestDto();
        requestDto.setUsername("jeffery");
        requestDto.setEmail("jeffery@example.com");

        String expectedPassword = "abc123xyz";

        when(userService.getPasswordByUsernameAndEmail(
                eq("jeffery"),
                eq("jeffery@example.com")
        )).thenReturn(expectedPassword);

        mockMvc.perform(post("/api/users/query-password/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").value(expectedPassword));
    }

    @Test
    void testGetCurrentUser_shouldReturnUserInfo() throws Exception {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(1L);
        responseDto.setUsername("jeffery");
        responseDto.setEmail("jeffery@example.com");

        when(userService.getUserById(eq(1L))).thenReturn(responseDto);

        mockMvc.perform(get("/api/users/me/")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("jeffery"))
                .andExpect(jsonPath("$.email").value("jeffery@example.com"));
    }

    @Test
    void testLogin_shouldReturnSuccessMessageWithUserId() throws Exception {
        UserLoginRequestDto loginDto = new UserLoginRequestDto();
        loginDto.setUsername("jeffery");
        loginDto.setPassword("123456");

        String message = "登入成功";

        // 模擬 service 傳回登入成功
        when(userService.login(any(UserLoginRequestDto.class), any(HttpSession.class)))
                .thenAnswer(invocation -> {
                    HttpSession session = invocation.getArgument(1);
                    session.setAttribute("userId", 1L); // 模擬內部設定 session
                    return message;
                });

        mockMvc.perform(post("/api/users/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto))
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("登入成功")))
                .andExpect(content().string(containsString("userId=1")));

        verify(userService).login(any(UserLoginRequestDto.class), any(HttpSession.class));
    }

    @Test
    void testLogout_shouldClearSessionAndReturnSuccess() throws Exception {
        // 模擬登入狀態
        session.setAttribute("userId", 1L);

        mockMvc.perform(post("/api/users/logout/")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string("登出成功"));

        verify(userService).logout(any(HttpSession.class));
    }

}
