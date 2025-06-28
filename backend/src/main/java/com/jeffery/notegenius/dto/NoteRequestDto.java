package com.jeffery.notegenius.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteRequestDto {
    private String title;
    private String content;
    private Long parentId;              // 可選的父筆記 ID（null 表示無父筆記）
    private List<String> tagNames;      // 關聯的標籤名稱列表
}