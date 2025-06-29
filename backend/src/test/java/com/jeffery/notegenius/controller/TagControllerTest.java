package com.jeffery.notegenius.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeffery.notegenius.dto.TagRequestDto;
import com.jeffery.notegenius.dto.TagResponseDto;
import com.jeffery.notegenius.service.TagService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(TagController.class)
@AutoConfigureMockMvc
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        session.setAttribute("userId", 1L);
    }

    @Test
    void testGetTagsByUser_shouldReturnTagList() throws Exception {
        List<TagResponseDto> mockTags = List.of(
                new TagResponseDto(1L, "AI"),
                new TagResponseDto(2L, "Study")
        );

        Mockito.when(tagService.getTagsByUserId(1L)).thenReturn(mockTags);

        mockMvc.perform(get("/api/tags/").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("AI"))
                .andExpect(jsonPath("$[1].name").value("Study"));
    }

    @Test
    void testCreateTag_shouldReturnCreatedTag() throws Exception {
        TagRequestDto request = new TagRequestDto();
        request.setName("NewTag");

        TagResponseDto response = new TagResponseDto(1L, "NewTag");

        Mockito.when(tagService.createTag(eq(1L), eq("NewTag")))
                .thenReturn(response);

        mockMvc.perform(post("/api/tags/")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("NewTag"));

        Mockito.verify(tagService).createTag(eq(1L), eq("NewTag"));
    }

    @Test
    void testDeleteTag_shouldReturnNoContent() throws Exception {
        Long tagId = 1L;

        mockMvc.perform(delete("/api/tags/{id}/", tagId).session(session))
                .andExpect(status().isNoContent());

        Mockito.verify(tagService).deleteTag(eq(tagId), eq(1L));
    }

    @Test
    void testGetTagsByUser_withoutLogin_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/tags/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testCreateTag_withoutLogin_shouldReturnUnauthorized() throws Exception {
        TagRequestDto request = new TagRequestDto();
        request.setName("NewTag");

        mockMvc.perform(post("/api/tags/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDeleteTag_withoutLogin_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/tags/{id}/", 1L))
                .andExpect(status().isUnauthorized());
    }
}
