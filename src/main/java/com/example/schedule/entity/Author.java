package com.example.schedule.entity;

import com.example.schedule.dto.AuthorRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Author {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Author(AuthorRequestDto dto) {
        this.name = dto.getName();
        this.email = dto.getEmail();
    }
}
