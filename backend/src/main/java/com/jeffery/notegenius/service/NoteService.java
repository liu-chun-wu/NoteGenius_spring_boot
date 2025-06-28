package com.jeffery.notegenius.service;

import com.jeffery.notegenius.dto.NoteResponseDto;
import com.jeffery.notegenius.model.*;
import com.jeffery.notegenius.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public NoteResponseDto createNote(Long userId, String title, String content, Long parentId, List<String> tagNames) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "使用者不存在"));

        Set<Tag> tags = tagNames.stream()
                .map(name -> tagRepository.findByUserIdAndName(userId, name)
                        .orElseGet(() -> {
                            Tag tag = new Tag();
                            tag.setName(name);
                            tag.setUser(user);
                            return tagRepository.save(tag);
                        }))
                .collect(Collectors.toSet());

        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setCreatedAt(LocalDateTime.now());
        note.setUser(user);
        note.setTags(tags);

        if (parentId != null) {
            noteRepository.findByIdAndUserId(parentId, userId)
                    .ifPresent(note::setParent);
        }

        Note savedNote = noteRepository.save(note);
        return toResponseDto(savedNote);
    }

    public Optional<NoteResponseDto> getNoteById(Long noteId, Long userId) {
        return noteRepository.findByIdAndUserId(noteId, userId)
                .map(this::toResponseDto);
    }

    public List<NoteResponseDto> getAllNotesByUser(Long userId) {
        return noteRepository.findAllByUserId(userId)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public NoteResponseDto updateNote(Long noteId, Long userId, String title, String content, Long parentId, List<String> tagNames) {
        Note note = noteRepository.findByIdAndUserId(noteId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "筆記不存在或無權限"));

        if (title != null) note.setTitle(title);
        if (content != null) note.setContent(content);

        if (parentId != null) {
            noteRepository.findByIdAndUserId(parentId, userId)
                    .ifPresent(note::setParent);
        }

        if (tagNames != null) {
            User user = note.getUser();
            Set<Tag> tags = tagNames.stream()
                    .map(name -> tagRepository.findByUserIdAndName(userId, name)
                            .orElseGet(() -> {
                                Tag tag = new Tag();
                                tag.setName(name);
                                tag.setUser(user);
                                return tagRepository.save(tag);
                            }))
                    .collect(Collectors.toSet());
            note.setTags(tags);
        }

        Note savedNote = noteRepository.save(note);
        return toResponseDto(savedNote);
    }

    public void deleteNote(Long noteId, Long userId) {
        Note note = noteRepository.findByIdAndUserId(noteId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "筆記不存在或無權限"));
        noteRepository.delete(note);
    }

    private NoteResponseDto toResponseDto(Note note) {
        NoteResponseDto dto = new NoteResponseDto();
        dto.setId(note.getId());
        dto.setTitle(note.getTitle());
        dto.setContent(note.getContent());
        dto.setCreatedAt(note.getCreatedAt());
        dto.setTagNames(note.getTags().stream().map(Tag::getName).collect(Collectors.toList()));
        return dto;
    }
}
