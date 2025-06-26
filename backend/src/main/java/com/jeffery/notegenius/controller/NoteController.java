package com.jeffery.notegenius.controller;

import com.jeffery.notegenius.dto.NoteCreateDto;
import com.jeffery.notegenius.dto.NoteUpdateDto;
import com.jeffery.notegenius.service.NoteService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    public ResponseEntity<?> getAllNotes(HttpSession session) {
        Long userId = (Long) session.getAttribute("user_id");
        if (userId == null) return ResponseEntity.status(401).body("尚未登入");
        return ResponseEntity.ok(noteService.getAllNotesByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNote(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("user_id");
        if (userId == null) return ResponseEntity.status(401).body("尚未登入");
        return noteService.getNoteById(id, userId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body("找不到筆記"));
    }

    @PostMapping
    public ResponseEntity<?> createNote(@RequestBody NoteCreateDto dto, HttpSession session) {
        Long userId = (Long) session.getAttribute("user_id");
        if (userId == null) return ResponseEntity.status(401).body("尚未登入");
        return ResponseEntity.ok(noteService.createNote(dto, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNote(@PathVariable Long id,
                                        @RequestBody NoteUpdateDto dto,
                                        HttpSession session) {
        Long userId = (Long) session.getAttribute("user_id");
        if (userId == null) return ResponseEntity.status(401).body("尚未登入");
        return ResponseEntity.ok(noteService.updateNote(id, dto, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("user_id");
        if (userId == null) return ResponseEntity.status(401).body("尚未登入");
        noteService.deleteNote(id, userId);
        return ResponseEntity.ok("筆記已刪除");
    }
}