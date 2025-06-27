package com.jeffery.notegenius.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeffery.notegenius.dto.*;
import com.jeffery.notegenius.service.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@WebMvcTest(TagController.class)
@AutoConfigureMockMvc
public class TagControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllTags_shouldReturnTagList() throws Exception {
        List<TagResponseDto> mockTags = List.of(
                new TagResponseDto(1L, "AI"),
                new TagResponseDto(2L, "Study")
        );

        Mockito.when(tagService.getAllTags()).thenReturn(mockTags);

        mockMvc.perform(get("/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("AI"))
                .andExpect(jsonPath("$[1].name").value("Study"));

        Mockito.verify(tagService).getAllTags();
    }

    @Test
    void testCreateTag_shouldReturnCreatedTag() throws Exception {
        TagCreateDto request = new TagRequestDto("NewTag");
        TagResponseDto response = new TagResponseDto(1L, "NewTag");

        Mockito.when(tagService.createTag(Mockito.any())).thenReturn(response);

        mockMvc.perform(post("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("NewTag"));

        Mockito.verify(tagService).createTag(Mockito.any());
    }

    @Test
    void testDeleteTag_shouldReturnNoContent() throws Exception {
        Long tagId = 1L;

        mockMvc.perform(delete("/tags/{id}", tagId))
                .andExpect(status().isNoContent());

        Mockito.verify(tagService).deleteTag(tagId);
    }



}
