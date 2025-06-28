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
    void testCreateTag_shouldCreateTagAndReturnTagResponseDto() {
        Long userId = 1L;
        String tagName = "Work";

        User user = new User();
        user.setId(userId);

        Tag newTag = new Tag();
        newTag.setId(10L);
        newTag.setName(tagName);
        newTag.setUser(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(tagRepository.findByUserIdAndName(userId, tagName)).thenReturn(Optional.empty());
        when(tagRepository.save(any(Tag.class))).thenReturn(newTag);

        TagResponseDto result = tagService.createTag(userId, tagName);

        assertEquals("Work", result.getName());
        assertEquals(10L, result.getId());
        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    @Test
    void testGetAllTagsByUserId_shouldReturnTagResponseDto() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Tag tag1 = new Tag(); tag1.setId(1L); tag1.setName("Work"); tag1.setUser(user);
        Tag tag2 = new Tag(); tag2.setId(2L); tag2.setName("Study"); tag2.setUser(user);

        List<Tag> mockTags = List.of(tag1, tag2);
        when(tagRepository.findAllByUserId(userId)).thenReturn(mockTags);

        List<TagResponseDto> result = tagService.getTagsByUserId(userId);

        assertEquals(2, result.size());
        assertEquals("Work", result.get(0).getName());
        assertEquals("Study", result.get(1).getName());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        verify(tagRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    void testUpdateTag_shouldUpdateTagAndReturnTagResponseDto() {
        Long userId = 1L;
        Long tagId = 10L;
        String newName = "UpdatedTag";

        User user = new User(); user.setId(userId);

        Tag existingTag = new Tag();
        existingTag.setId(tagId);
        existingTag.setName("OldTag");
        existingTag.setUser(user);

        when(tagRepository.findByIdAndUserId(tagId, userId)).thenReturn(Optional.of(existingTag));
        when(tagRepository.save(any(Tag.class))).thenReturn(existingTag);

        TagResponseDto result = tagService.updateTag(tagId, userId, newName);

        assertEquals("UpdatedTag", result.getName());
        assertEquals(tagId, result.getId());
        verify(tagRepository).save(existingTag);
    }

    @Test
    void testDeleteTag_shouldDeleteTagAndReturnNothing() {
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
