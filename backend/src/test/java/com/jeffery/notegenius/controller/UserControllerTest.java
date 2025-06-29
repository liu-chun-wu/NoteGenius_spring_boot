package com.jeffery.notegenius.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeffery.notegenius.dto.UserFindPasswordRequestDto;
import com.jeffery.notegenius.dto.UserRegisterRequestDto;
import com.jeffery.notegenius.dto.UserResponseDto;
import com.jeffery.notegenius.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
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

        Mockito.when(userService.registerUser(
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

        Mockito.when(userService.getPasswordByUsernameAndEmail(
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

        Mockito.when(userService.getUserById(eq(1L))).thenReturn(responseDto);

        mockMvc.perform(get("/api/users/me/")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("jeffery"))
                .andExpect(jsonPath("$.email").value("jeffery@example.com"));
    }
}
