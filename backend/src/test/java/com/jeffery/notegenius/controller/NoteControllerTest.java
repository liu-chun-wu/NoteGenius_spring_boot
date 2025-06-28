package com.jeffery.notegenius.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeffery.notegenius.dto.NoteResponseDto;
import com.jeffery.notegenius.service.NoteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled
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
    void setup() {
        session = new MockHttpSession();
        session.setAttribute("userId", 1L);
    }

//    @Test
//    void testCreateNote() throws Exception {
//        NoteCreateDto request = new NoteCreateDto();
//        request.setTitle("Test Note");
//        request.setContent("Content");
//        request.setTagNames(List.of("Java", "Spring"));
//
//        NoteResponseDto response = new NoteResponseDto();
//        response.setId(1L);
//        response.setTitle("Test Note");
//        response.setContent("Content");
//        response.setCreatedAt(LocalDateTime.now());
//        response.setTagNames(List.of("Java", "Spring"));
//
//        Mockito.when(noteService.createNote(any(NoteCreateDto.class), eq(1L)))
//                .thenReturn(response);
//
//        mockMvc.perform(post("/api/notes/")
//                        .session(session)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value("Test Note"));
//    }

    @Test
    void testGetAllNotes() throws Exception {
        NoteResponseDto dto = new NoteResponseDto();
        dto.setId(1L);
        dto.setTitle("Test");
        dto.setContent("Content");
        dto.setCreatedAt(LocalDateTime.now());
        dto.setTagNames(List.of("Tag"));

        Mockito.when(noteService.getAllNotesByUser(1L)).thenReturn(List.of(dto));

        MvcResult result = mockMvc.perform(get("/api/notes/").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test"))
                .andReturn();
    }

    @Test
    void testGetNoteById() throws Exception {
        NoteResponseDto dto = new NoteResponseDto();
        dto.setId(1L);
        dto.setTitle("Test");
        dto.setContent("Content");
        dto.setCreatedAt(LocalDateTime.now());
        dto.setTagNames(List.of("Tag"));

        Mockito.when(noteService.getNoteById(1L, 1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/notes/1/").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test"));
    }

//    @Test
//    void testUpdateNote() throws Exception {
//        NoteUpdateDto updateDto = new NoteUpdateDto();
//        updateDto.setTitle("Updated");
//        updateDto.setContent("Updated Content");
//
//        NoteResponseDto updated = new NoteResponseDto();
//        updated.setId(1L);
//        updated.setTitle("Updated");
//        updated.setContent("Updated Content");
//        updated.setCreatedAt(LocalDateTime.now());
//        updated.setTagNames(List.of("UpdatedTag"));
//
//        Mockito.when(noteService.updateNote(eq(1L), any(NoteUpdateDto.class), eq(1L)))
//                .thenReturn(updated);
//
//        mockMvc.perform(put("/api/notes/1/")
//                        .session(session)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value("Updated"));
//    }

    @Test
    void testDeleteNote() throws Exception {
        Mockito.doNothing().when(noteService).deleteNote(1L, 1L);

        mockMvc.perform(delete("/api/notes/1/").session(session))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllNotes_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/notes/")) // ❌ 沒有 session
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("尚未登入"));
    }
}
