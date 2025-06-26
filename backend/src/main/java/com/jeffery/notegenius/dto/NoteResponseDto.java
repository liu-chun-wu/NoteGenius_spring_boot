package com.jeffery.notegenius.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NoteResponseDto {
    private Long id;
    private String title;
    private String content;
    private List<String> tagNames;
    private LocalDateTime createdAt;
}
