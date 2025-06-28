package com.jeffery.notegenius.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeffery.notegenius.dto.NoteRequestDto;
import com.jeffery.notegenius.dto.NoteResponseDto;
import com.jeffery.notegenius.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(NoteController.class)
@AutoConfigureMockMvc
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NoteService noteService;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        session.setAttribute("userId", 1L);
    }

    @Test
    void testGetNoteById_shouldReturnNoteResponseDto() throws Exception {
        NoteResponseDto response = new NoteResponseDto();
        response.setId(1L);
        response.setTitle("Test Title");
        response.setContent("Test Content");
        response.setTagNames(List.of("tag1", "tag2"));
        response.setCreatedAt(LocalDateTime.now());

        Mockito.when(noteService.getNoteById(1L, 1L)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/notes/1/").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.tagNames[0]").value("tag1"));
    }

    @Test
    void testCreateNote_shouldCreateNoteAndReturnNoteResponseDto() throws Exception {
        // Arrange - 準備請求資料
        NoteRequestDto requestDto = new NoteRequestDto();
        requestDto.setTitle("New Note");
        requestDto.setContent("New Content");
        requestDto.setParentId(null);
        requestDto.setTagNames(List.of("tagA", "tagB"));

        // Arrange - 模擬 Service 回傳資料
        NoteResponseDto responseDto = new NoteResponseDto();
        responseDto.setId(100L);
        responseDto.setTitle("New Note");
        responseDto.setContent("New Content");
        responseDto.setTagNames(List.of("tagA", "tagB"));
        responseDto.setCreatedAt(LocalDateTime.now());

        // 修正這裡：mock 參數不是 DTO，而是展開的欄位
        Mockito.when(noteService.createNote(
                eq(1L),
                eq("New Note"),
                eq("New Content"),
                isNull(),
                eq(List.of("tagA", "tagB"))
        )).thenReturn(responseDto);

        // Act + Assert
        mockMvc.perform(post("/api/notes/")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.title").value("New Note"))
                .andExpect(jsonPath("$.tagNames[1]").value("tagB"));
    }

    @Test
    void testUpdateNote_shouldUpdateNoteAndReturnNoteResponseDto() throws Exception {
        NoteRequestDto updateDto = new NoteRequestDto();
        updateDto.setTitle("Updated Title");
        updateDto.setContent("Updated Content");
        updateDto.setParentId(null);
        updateDto.setTagNames(List.of("tagX"));

        NoteResponseDto responseDto = new NoteResponseDto();
        responseDto.setId(1L);
        responseDto.setTitle("Updated Title");
        responseDto.setContent("Updated Content");
        responseDto.setTagNames(List.of("tagX"));
        responseDto.setCreatedAt(LocalDateTime.now());

        Mockito.when(noteService.updateNote(eq(1L), eq(1L),
                        eq("Updated Title"), eq("Updated Content"), isNull(), eq(List.of("tagX"))))
                .thenReturn(responseDto);

        mockMvc.perform(put("/api/notes/1/")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void testDeleteNote_shouldDeleteNoteAndReturnNothing() throws Exception {
        Mockito.doNothing().when(noteService).deleteNote(1L, 1L);

        mockMvc.perform(delete("/api/notes/1/").session(session))
                .andExpect(status().isOk());

        Mockito.verify(noteService).deleteNote(1L, 1L);
    }

    @Test
    void testUnauthorized_shouldReturn401() throws Exception {
        mockMvc.perform(get("/api/notes/1/")) // no session
                .andExpect(status().isUnauthorized());
    }
}
