package com.jeffery.notegenius.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tags/")
@RequiredArgsConstructor
public class TagController {

//    private final TagService tagService;
//
//    @GetMapping
//    public ResponseEntity<?> getTagsByUser(HttpSession session) {
//        Long userId = (Long) session.getAttribute("userId");
//        if (userId == null) return ResponseEntity.status(401).body("尚未登入");
//        List<TagResponseDto> tags = tagService.getTagsByUserId(userId);
//        return ResponseEntity.ok(tags);
//    }
//
//    @PostMapping
//    public ResponseEntity<?> createTag(@RequestBody TagCreateDto tagCreateDto,
//                                                    HttpSession session) {
//        Long userId = (Long) session.getAttribute("userId");
//        if (userId == null) return ResponseEntity.status(401).body("尚未登入");
//        TagResponseDto createdTag = tagService.createTag(userId,tagCreateDto);
//        return ResponseEntity.ok(createdTag);
//    }
//
//    @DeleteMapping("{id}/")
//    public ResponseEntity<?> deleteTag(@PathVariable Long id, HttpSession session) {
//        Long userId = (Long) session.getAttribute("userId");
//        if (userId == null) return ResponseEntity.status(401).body("尚未登入");
//        tagService.deleteTag(id, userId);  // 假設刪除需要驗證此 userId 是否為擁有者
//        return ResponseEntity.noContent().build();
//    }
}
