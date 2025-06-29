package com.jeffery.notegenius.controller;

import com.jeffery.notegenius.dto.TagRequestDto;
import com.jeffery.notegenius.dto.TagResponseDto;
import com.jeffery.notegenius.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/")
    public List<TagResponseDto> getTagsByUser(@SessionAttribute Long userId) {
        return tagService.getTagsByUserId(userId);
    }

    @PostMapping("/")
    public TagResponseDto createTag(@SessionAttribute Long userId,
                                    @RequestBody TagRequestDto requestDto) {
        return tagService.createTag(userId, requestDto.getName());
    }

    @DeleteMapping("/{id}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable Long id,
                          @SessionAttribute Long userId) {
        tagService.deleteTag(id, userId);
    }

    @ExceptionHandler(org.springframework.web.bind.ServletRequestBindingException.class)
    @ResponseStatus(org.springframework.http.HttpStatus.UNAUTHORIZED)
    public String handleMissingSessionAttr(Exception ex) {
        return "Unauthorized - Missing session";
    }
}
