package com.jeffery.notegenius.controller;

import com.jeffery.notegenius.dto.NoteRequestDto;
import com.jeffery.notegenius.dto.NoteResponseDto;
import com.jeffery.notegenius.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @ExceptionHandler(ServletRequestBindingException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleMissingSessionAttr(ServletRequestBindingException ex) {
        return "Unauthorized - Missing session attribute";
    }

    @GetMapping("/")
    public List<NoteResponseDto> getAllNotes(@SessionAttribute Long userId) {
        return noteService.getAllNotesByUser(userId);
    }

    @GetMapping("/{noteId}/")
    public NoteResponseDto getNoteById(@SessionAttribute Long userId,
                                       @PathVariable Long noteId) {
        Optional<NoteResponseDto> note = noteService.getNoteById(userId, noteId);
        return note.orElseThrow(() -> new RuntimeException("Note not found"));
    }

    @PostMapping("/")
    public NoteResponseDto createNote(@SessionAttribute Long userId,
                                      @RequestBody NoteRequestDto requestDto) {
        return noteService.createNote(
                userId,
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getParentId(),
                requestDto.getTagNames()
        );
    }

    @PutMapping("/{noteId}/")
    public NoteResponseDto updateNote(@SessionAttribute Long userId,
                                      @PathVariable Long noteId,
                                      @RequestBody NoteRequestDto requestDto) {
        return noteService.updateNote(
                userId,
                noteId,
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getParentId(),
                requestDto.getTagNames()
        );
    }

    @DeleteMapping("/{noteId}/")
    public void deleteNote(@SessionAttribute Long userId,
                           @PathVariable Long noteId) {
        noteService.deleteNote(userId, noteId);
    }
}
