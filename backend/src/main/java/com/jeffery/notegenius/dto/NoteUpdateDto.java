package com.jeffery.notegenius.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteUpdateDto {
    private String title;
    private String content;
    private Long parentId; // 可選
    private List<String> tagNames;
}
