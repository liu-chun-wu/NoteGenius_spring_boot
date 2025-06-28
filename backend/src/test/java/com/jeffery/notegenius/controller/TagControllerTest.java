package com.jeffery.notegenius.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeffery.notegenius.service.TagService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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
        session.setAttribute("userId", 1L); // 模擬已登入 userId = 1
    }

//    @Test
//    void testGetTagsByUser_shouldReturnTagList() throws Exception {
//        List<TagResponseDto> mockTags = List.of(
//                new TagResponseDto(1L, "AI"),
//                new TagResponseDto(2L, "Study")
//        );
//
//        Mockito.when(tagService.getTagsByUserId(1L)).thenReturn(mockTags);
//
//        mockMvc.perform(get("/api/tags/")
//                        .session(session))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2))
//                .andExpect(jsonPath("$[0].id").value(1))
//                .andExpect(jsonPath("$[0].name").value("AI"))
//                .andExpect(jsonPath("$[1].name").value("Study"));
//    }
//
//    @Test
//    void testCreateTag_shouldReturnCreatedTag() throws Exception {
//        Long userId = 1L;
//        TagCreateDto request = new TagCreateDto("NewTag");
//        TagResponseDto response = new TagResponseDto(1L, "NewTag");
//
//        // 模擬 session 行為
//        session.setAttribute("userId", userId);
//
//        // 模擬 service 回傳
//        Mockito.when(tagService.createTag(Mockito.eq(userId), Mockito.any(TagCreateDto.class)))
//                .thenReturn(response);
//
//        mockMvc.perform(post("/api/tags/")
//                        .session(session)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.name").value("NewTag"));
//
//        // 驗證 userId + DTO 都有正確傳入
//        Mockito.verify(tagService).createTag(Mockito.eq(userId), Mockito.any(TagCreateDto.class));
//    }
//
//
//    @Test
//    void testDeleteTag_shouldReturnNoContent() throws Exception {
//        Long tagId = 1L;
//
//        mockMvc.perform(delete("/api/tags/{id}/", tagId)
//                        .session(session))
//                .andExpect(status().isNoContent());
//
//        Mockito.verify(tagService).deleteTag(tagId, 1L); // 需驗證 userId 有傳入
//    }
//
//    @Test
//    void testGetTagsByUser_withoutLogin_shouldReturnUnauthorized() throws Exception {
//        mockMvc.perform(get("/api/tags/"))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    void testCreateTag_withoutLogin_shouldReturnUnauthorized() throws Exception {
//        TagCreateDto request = new TagCreateDto("NewTag");
//
//        mockMvc.perform(post("/api/tags/")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    void testDeleteTag_withoutLogin_shouldReturnUnauthorized() throws Exception {
//        mockMvc.perform(delete("/api/tags/{id}/", 1L))
//                .andExpect(status().isUnauthorized());
//    }

}
