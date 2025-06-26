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

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public NoteResponseDto createNote(NoteCreateDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "使用者不存在"));

        Set<Tag> tags = dto.getTagNames().stream()
                .map(name -> tagRepository.findByUserIdAndName(userId, name)
                        .orElseGet(() -> {
                            Tag tag = new Tag(); // 使用無參數建構子
                            tag.setName(name);
                            tag.setUser(user);
                            return tagRepository.save(tag);
                        }))
                .collect(Collectors.toSet());


        Note note = new Note();
        note.setTitle(dto.getTitle());
        note.setContent(dto.getContent());
        note.setCreatedAt(LocalDateTime.now());
        note.setUser(user);
        note.setTags(tags);

        if (dto.getParentId() != null) {
            noteRepository.findByIdAndUserId(dto.getParentId(), userId)
                    .ifPresent(note::setParent);
        }

        Note savedNote = noteRepository.save(note);
        return toResponseDto(savedNote);
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

    public NoteResponseDto updateNote(Long noteId, NoteUpdateDto dto, Long userId) {
        Note note = noteRepository.findByIdAndUserId(noteId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "筆記不存在或無權限"));

        if (dto.getTitle() != null) note.setTitle(dto.getTitle());
        if (dto.getContent() != null) note.setContent(dto.getContent());

        if (dto.getTagNames() != null) {
            Set<Tag> tags = dto.getTagNames().stream()
                    .map(name -> tagRepository.findByUserIdAndName(userId, name)
                            .orElseGet(() -> {
                                Tag tag = new Tag();
                                tag.setName(name);
                                tag.setUser(note.getUser());
                                return tagRepository.save(tag);
                            }))
                    .collect(Collectors.toSet());
            note.setTags(tags);
        }

        if (dto.getParentId() != null) {
            noteRepository.findByIdAndUserId(dto.getParentId(), userId)
                    .ifPresent(note::setParent);
        }

        Note savedNote = noteRepository.save(note);
        return toResponseDto(savedNote);
    }

    public void deleteNote(Long noteId, Long userId) {
        Note note = noteRepository.findByIdAndUserId(noteId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "筆記不存在或無權限"));
        noteRepository.delete(note);
    }

}
