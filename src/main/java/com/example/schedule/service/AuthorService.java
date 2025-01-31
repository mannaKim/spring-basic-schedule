package com.example.schedule.service;

import com.example.schedule.dto.AuthorRequestDto;
import com.example.schedule.dto.AuthorResponseDto;

import java.util.List;

public interface AuthorService {
    AuthorResponseDto createAuthor(AuthorRequestDto dto);

    AuthorResponseDto findAuthorById(Long id);

    List<AuthorResponseDto> findAllAuthors();
}
