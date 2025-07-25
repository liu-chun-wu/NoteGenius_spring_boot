package com.jeffery.notegenius.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFindPasswordRequestDto {
    private String username;
    private String email;
}
