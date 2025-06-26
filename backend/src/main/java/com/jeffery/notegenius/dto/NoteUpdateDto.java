package com.jeffery.notegenius.dto;

import lombok.Data;

import java.util.List;

@Data
public class NoteUpdateDto {
    private String title;
    private String content;
    private Long parentId; // 可選
    private List<String> tagNames;
}
