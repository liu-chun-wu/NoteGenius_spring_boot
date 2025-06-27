package com.jeffery.notegenius.service;

import com.jeffery.notegenius.dto.*;
import com.jeffery.notegenius.model.*;
import com.jeffery.notegenius.repository.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TagService tagService;

    @Test
    void testCreateTag() {
        Long userId = 1L;

        TagCreateDto dto = new TagCreateDto();
        dto.setName("Work");

        User user = new User();
        user.setId(userId);

        Tag newTag = new Tag();
        newTag.setId(10L);
        newTag.setName(dto.getName());
        newTag.setUser(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(tagRepository.findByUserIdAndName(userId, dto.getName())).thenReturn(Optional.empty());
        when(tagRepository.save(any(Tag.class))).thenReturn(newTag);

        TagResponseDto result = tagService.createTag(userId, dto);

        // ✅ 驗證回傳值是剛新增的 tag 名稱
        assertEquals("Work", result.getName());
        assertEquals(10L, result.getId());

        // ✅ 驗證儲存動作有被觸發
        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    @Test
    void testGetAllTagsByUserId() {
        // Arrange
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("Work");
        tag1.setUser(user);

        Tag tag2 = new Tag();
        tag2.setId(2L);
        tag2.setName("Study");
        tag2.setUser(user);

        List<Tag> mockTags = List.of(tag1, tag2);

        when(tagRepository.findAllByUserId(userId)).thenReturn(mockTags);

        // Act
        List<TagResponseDto> result = tagService.getTagsByUserId(userId);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Work", result.get(0).getName());
        assertEquals("Study", result.get(1).getName());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(tagRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    void testUpdateTag() {
        // Arrange
        Long userId = 1L;
        Long tagId = 10L;

        TagUpdateDto dto = new TagUpdateDto();
        dto.setName("UpdatedTag");

        User user = new User();
        user.setId(userId);

        Tag tag = new Tag();
        tag.setId(tagId);
        tag.setName("OldName");
        tag.setUser(user);

        when(tagRepository.findByIdAndUserId(tagId, userId)).thenReturn(Optional.of(tag));
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        // Act
        TagResponseDto result = tagService.updateTag(tagId, userId, dto);

        // Assert
        assertEquals("UpdatedTag", result.getName());
        assertEquals(10L, result.getId());
        verify(tagRepository).save(tag);
    }

    @Test
    void testDeleteTag() {
        // Arrange
        Long userId = 1L;
        Long tagId = 100L;

        User user = new User();
        user.setId(userId);

        Tag tag = new Tag();
        tag.setId(tagId);
        tag.setName("ToDelete");
        tag.setUser(user);

        when(tagRepository.findByIdAndUserId(tagId, userId)).thenReturn(Optional.of(tag));

        // Act & Assert
        assertDoesNotThrow(() -> tagService.deleteTag(tagId, userId));

        verify(tagRepository, times(1)).delete(tag);
    }


}
