package com.jeffery.notegenius.service;

import com.jeffery.notegenius.dto.NoteCreateDto;
import com.jeffery.notegenius.dto.NoteResponseDto;
import com.jeffery.notegenius.dto.NoteUpdateDto;
import com.jeffery.notegenius.model.Note;
import com.jeffery.notegenius.model.Tag;
import com.jeffery.notegenius.model.User;
import com.jeffery.notegenius.repository.NoteRepository;
import com.jeffery.notegenius.repository.TagRepository;
import com.jeffery.notegenius.repository.UserRepository;

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
    void testCreateNote() {
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);

        NoteCreateDto dto = new NoteCreateDto();
        dto.setTitle("Test Title");
        dto.setContent("Test Content");
        dto.setTagNames(List.of("Tag1", "Tag2"));

        Tag tag1 = new Tag(); tag1.setName("Tag1"); tag1.setUser(mockUser);
        Tag tag2 = new Tag(); tag2.setName("Tag2"); tag2.setUser(mockUser);

        Note savedNote = new Note();
        savedNote.setId(100L);
        savedNote.setTitle(dto.getTitle());
        savedNote.setContent(dto.getContent());
        savedNote.setUser(mockUser);
        savedNote.setTags(Set.of(tag1, tag2));

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(tagRepository.findByUserIdAndName(userId, "Tag1")).thenReturn(Optional.empty());
        when(tagRepository.findByUserIdAndName(userId, "Tag2")).thenReturn(Optional.empty());
        when(tagRepository.save(any(Tag.class))).thenAnswer(inv -> inv.getArgument(0));
        when(noteRepository.save(any(Note.class))).thenReturn(savedNote);

        NoteResponseDto result = noteService.createNote(dto, userId);

        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Content", result.getContent());
        assertEquals(Set.of("Tag1", "Tag2"), new HashSet<>(result.getTagNames()));
    }

    @Test
    void testGetNoteById() {
        Long userId = 1L;
        Long noteId = 100L;

        User user = new User();
        user.setId(userId);

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
    void testUpdateNote() {
        Long userId = 1L;
        Long noteId = 100L;

        User user = new User();
        user.setId(userId);

        Note note = new Note();
        note.setId(noteId);
        note.setUser(user);
        note.setContent("Original Content");

        NoteUpdateDto dto = new NoteUpdateDto();
        dto.setTitle("Updated");
        dto.setContent("Updated Content");
        dto.setTagNames(List.of("T1"));

        Tag tag = new Tag();
        tag.setName("T1");
        tag.setUser(user);

        // ✅ 正確 mock：資料存在
        when(noteRepository.findByIdAndUserId(noteId, userId)).thenReturn(Optional.of(note));

        // ✅ 模擬 tag 不存在 → 要創一個新的
        when(tagRepository.findByUserIdAndName(userId, "T1")).thenReturn(Optional.empty());
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        // ✅ 模擬更新後的 note 儲存
        when(noteRepository.save(any(Note.class))).thenReturn(note);

        NoteResponseDto result = noteService.updateNote(noteId, dto, userId);
        assertEquals("Updated", result.getTitle());
        assertEquals("Updated Content", result.getContent());
        assertEquals(List.of("T1"), result.getTagNames());
    }

    @Test
    void testDeleteNote() {
        Long userId = 1L;
        Long noteId = 100L;

        User user = new User();
        user.setId(userId);

        Note note = new Note();
        note.setId(noteId);
        note.setUser(user);

        when(noteRepository.findByIdAndUserId(noteId, userId)).thenReturn(Optional.of(note)); // ✅ 這一行是關鍵

        assertDoesNotThrow(() -> noteService.deleteNote(noteId, userId));
        verify(noteRepository).delete(note);
    }


    @Test
    void testGetAllNotesByUser() {
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
}