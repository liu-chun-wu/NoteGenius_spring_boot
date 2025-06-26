package com.jeffery.notegenius.controller;

import com.jeffery.notegenius.model.Tag;
import com.jeffery.notegenius.service.TagService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<?> getAllTags(HttpSession session) {
        Long userId = (Long) session.getAttribute("user_id");
        if (userId == null) return ResponseEntity.status(401).body("尚未登入");

        List<Tag> tags = tagService.getAllTagsByUser(userId);
        return ResponseEntity.ok(tags);
    }

    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody Tag tag, HttpSession session) {
        Long userId = (Long) session.getAttribute("user_id");
        if (userId == null) return ResponseEntity.status(401).body("尚未登入");

        return tagService.createTag(tag, userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTag(@PathVariable Long id,
                                       @RequestBody Tag tag,
                                       HttpSession session) {
        Long userId = (Long) session.getAttribute("user_id");
        if (userId == null) return ResponseEntity.status(401).body("尚未登入");

        return tagService.updateTag(id, tag, userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("user_id");
        if (userId == null) return ResponseEntity.status(401).body("尚未登入");

        return tagService.deleteTag(id, userId);
    }
}
