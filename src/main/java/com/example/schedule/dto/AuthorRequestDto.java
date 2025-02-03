package com.example.schedule.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class AuthorRequestDto {
    private String name;

    @Email
    private String email;
}
