package com.jeffery.notegenius.service;

import com.jeffery.notegenius.dto.*;
import com.jeffery.notegenius.model.*;
import com.jeffery.notegenius.repository.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NoteService noteService;

    @Test
    void testCreateNote_shouldCreateNoteAndReturnNoteResponseDto() {
        Long userId = 1L;
        String title = "Test Title";
        String content = "Test Content";
        Long parentId = null;
        List<String> tagNames = List.of("Tag1", "Tag2");

        User mockUser = new User();
        mockUser.setId(userId);

        Tag tag1 = new Tag(); tag1.setName("Tag1"); tag1.setUser(mockUser);
        Tag tag2 = new Tag(); tag2.setName("Tag2"); tag2.setUser(mockUser);

        Note savedNote = new Note();
        savedNote.setId(100L);
        savedNote.setTitle(title);
        savedNote.setContent(content);
        savedNote.setUser(mockUser);
        savedNote.setTags(Set.of(tag1, tag2));

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(tagRepository.findByUserIdAndName(userId, "Tag1")).thenReturn(Optional.empty());
        when(tagRepository.findByUserIdAndName(userId, "Tag2")).thenReturn(Optional.empty());
        when(tagRepository.save(any(Tag.class))).thenAnswer(inv -> inv.getArgument(0));
        when(noteRepository.save(any(Note.class))).thenReturn(savedNote);

        NoteResponseDto result = noteService.createNote(userId, title, content, parentId, tagNames);

        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Content", result.getContent());
        assertEquals(Set.of("Tag1", "Tag2"), new HashSet<>(result.getTagNames()));
    }

    @Test
    void testGetNoteById_shouldReturnNoteResponseDto() {
        Long userId = 1L;
        Long noteId = 100L;

        User user = new User(); user.setId(userId);
        Note note = new Note();
        note.setId(noteId);
        note.setUser(user);
        note.setTitle("My Note");
        note.setContent("Details");
        note.setTags(Set.of());

        when(noteRepository.findByIdAndUserId(noteId, userId)).thenReturn(Optional.of(note));

        Optional<NoteResponseDto> result = noteService.getNoteById(noteId, userId);
        assertTrue(result.isPresent());
        assertEquals("My Note", result.get().getTitle());
    }

    @Test
    void testGetAllNotesByUserId_shouldReturnNoteResponseDto() {
        Long userId = 1L;
        User user = new User(); user.setId(userId);

        Note note1 = new Note(); note1.setId(1L); note1.setTitle("Note1"); note1.setUser(user); note1.setTags(Set.of());
        Note note2 = new Note(); note2.setId(2L); note2.setTitle("Note2"); note2.setUser(user); note2.setTags(Set.of());

        when(noteRepository.findAllByUserId(userId)).thenReturn(List.of(note1, note2));

        List<NoteResponseDto> results = noteService.getAllNotesByUser(userId);
        assertEquals(2, results.size());
        assertEquals("Note1", results.get(0).getTitle());
        assertEquals("Note2", results.get(1).getTitle());
    }

    @Test
    void testUpdateNote_shouldUpdateNoteAndReturnNoteResponseDto() {
        Long userId = 1L;
        Long noteId = 100L;
        String newTitle = "Updated Title";
        String newContent = "Updated Content";
        Long parentId = null;
        List<String> newTags = List.of("Updated");

        User user = new User(); user.setId(userId);

        Note existingNote = new Note();
        existingNote.setId(noteId);
        existingNote.setUser(user);
        existingNote.setTitle("Old Title");
        existingNote.setContent("Old Content");
        existingNote.setTags(Set.of());

        Tag newTag = new Tag(); newTag.setName("Updated"); newTag.setUser(user);

        when(noteRepository.findByIdAndUserId(noteId, userId)).thenReturn(Optional.of(existingNote));
        when(tagRepository.findByUserIdAndName(userId, "Updated")).thenReturn(Optional.empty());
        when(tagRepository.save(any(Tag.class))).thenReturn(newTag);
        when(noteRepository.save(any(Note.class))).thenReturn(existingNote);

        NoteResponseDto result = noteService.updateNote(noteId, userId, newTitle, newContent, parentId, newTags);

        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Content", result.getContent());
        assertTrue(result.getTagNames().contains("Updated"));
    }

    @Test
    void testDeleteNote_shouldDeleteNoteAndReturnNothing() {
        Long userId = 1L;
        Long noteId = 100L;

        User user = new User();
        user.setId(userId);

        Note note = new Note();
        note.setId(noteId);
        note.setUser(user);

        when(noteRepository.findByIdAndUserId(noteId, userId)).thenReturn(Optional.of(note));

        assertDoesNotThrow(() -> noteService.deleteNote(noteId, userId));
        verify(noteRepository).delete(note);
    }

}
