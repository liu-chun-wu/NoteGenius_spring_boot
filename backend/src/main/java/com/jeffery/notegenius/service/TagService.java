package com.jeffery.notegenius.service;

import com.jeffery.notegenius.model.Tag;
import com.jeffery.notegenius.model.User;
import com.jeffery.notegenius.repository.TagRepository;
import com.jeffery.notegenius.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public List<Tag> getAllTagsByUser(Long userId) {
        return tagRepository.findAllByUserId(userId);
    }

    public ResponseEntity<?> createTag(Tag tag, Long userId) {
        if (tagRepository.existsByUserIdAndName(userId, tag.getName())) {
            return ResponseEntity.badRequest().body("標籤名稱已存在");
        }
        User user = userRepository.findById(userId).orElseThrow();
        tag.setUser(user);
        return ResponseEntity.ok(tagRepository.save(tag));
    }

    public ResponseEntity<?> updateTag(Long id, Tag updated, Long userId) {
        Tag tag = tagRepository.findByIdAndUserId(id, userId)
                .orElse(null);
        if (tag == null) {
            return ResponseEntity.status(404).body("無此標籤或無權限修改");
        }

        // 若名稱重複也要擋
        if (!tag.getName().equals(updated.getName()) &&
                tagRepository.existsByUserIdAndName(userId, updated.getName())) {
            return ResponseEntity.badRequest().body("標籤名稱重複");
        }

        tag.setName(updated.getName());
        return ResponseEntity.ok(tagRepository.save(tag));
    }

    @Transactional
    public ResponseEntity<?> deleteTag(Long id, Long userId) {
        Tag tag = tagRepository.findByIdAndUserId(id, userId)
                .orElse(null);
        if (tag == null) {
            return ResponseEntity.status(404).body("無此標籤或無權限刪除");
        }

        // 清除所有筆記中的這個標籤（避免關聯錯誤）
        tag.getNotes().forEach(note -> note.getTags().remove(tag));

        tagRepository.delete(tag);
        return ResponseEntity.ok("標籤已刪除");
    }
}
