package com.example.schedule.dto;

import com.example.schedule.entity.Author;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AuthorResponseDto {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AuthorResponseDto(Author author) {
        this.id = author.getId();
        this.name = author.getName();
        this.email = author.getEmail();
        this.createdAt = author.getCreatedAt();
        this.updatedAt = author.getUpdatedAt();
    }
}
