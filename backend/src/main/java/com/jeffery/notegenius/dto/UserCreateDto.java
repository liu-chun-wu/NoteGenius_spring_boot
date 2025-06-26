package com.jeffery.notegenius.dto;

import lombok.Data;

@Data
public class UserCreateDto {
    private String username;
    private String email;
    private String password; // 可加驗證註解
}
