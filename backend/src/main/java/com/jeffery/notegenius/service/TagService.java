package com.jeffery.notegenius.service;

import com.jeffery.notegenius.model.*;
import com.jeffery.notegenius.dto.*;
import com.jeffery.notegenius.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public TagResponseDto createTag(Long userId, TagCreateDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "使用者不存在"));

        tagRepository.findByUserIdAndName(userId, dto.getName())
                .ifPresent(t -> { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "標籤名稱已存在"); });

        Tag tag = new Tag();
        tag.setName(dto.getName());
        tag.setUser(user);

        Tag savedTag = tagRepository.save(tag);

        return new TagResponseDto(savedTag.getId(), savedTag.getName());
    }

    public List<TagResponseDto> getTagsByUserId(Long userId) {
        return tagRepository.findAllByUserId(userId).stream()
                .map(tag -> new TagResponseDto(tag.getId(),tag.getName()))
                .collect(Collectors.toList());
    }

    public TagResponseDto updateTag(Long tagId, Long userId, TagUpdateDto dto) {
        Tag tag = tagRepository.findByIdAndUserId(tagId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag 不存在或無權限"));

        tag.setName(dto.getName());
        Tag updated = tagRepository.save(tag);

        return new TagResponseDto(updated.getId(), updated.getName());
    }

    public void deleteTag(Long tagId, Long userId) {
        Tag tag = tagRepository.findByIdAndUserId(tagId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag 不存在或無權限"));

        tagRepository.delete(tag);
    }

}
