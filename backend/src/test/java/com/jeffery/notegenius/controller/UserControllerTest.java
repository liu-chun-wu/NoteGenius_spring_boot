package com.jeffery.notegenius.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeffery.notegenius.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled
@ActiveProfiles("test")
@WebMvcTest(TagController.class)
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
        session.setAttribute("userId", 1L); // 模擬已登入 userId
    }

//    @Test
//    void testGetCurrentUser_shouldReturnUserInfo() throws Exception {
//        UserResponseDto mockUser = new UserResponseDto(1L, "jeffery", "jeffery@mail.com",);
//
//        Mockito.when(userService.getUserById(1L)).thenReturn(mockUser);
//
//        mockMvc.perform(get("/users/me")
//                        .session(session))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.username").value("jeffery"))
//                .andExpect(jsonPath("$.email").value("jeffery@mail.com"));
//
//        Mockito.verify(userService).getUserById(1L);
//    }

//    @Test
//    void testRegisterUser_shouldReturnCreatedUser() throws Exception {
//        UserCreateDto request = new UserCreateDto("jeffery", "jeffery@mail.com", "123456");
//        UserResponseDto response = new UserResponseDto(1L, "jeffery", "jeffery@mail.com");
//
//        Mockito.when(userService.registerUser(Mockito.any(UserCreateDto.class))).thenReturn(response);
//
//        mockMvc.perform(post("/users/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.username").value("jeffery"))
//                .andExpect(jsonPath("$.email").value("jeffery@mail.com"));
//
//        Mockito.verify(userService).registerUser(Mockito.any(UserCreateDto.class));
//    }

    @Test
    void testGetCurrentUser_withoutSession_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized());
    }
}
