package com.example.schedule.repository;

import com.example.schedule.dto.AuthorResponseDto;
import com.example.schedule.entity.Author;

import java.util.List;

public interface AuthorRepository {
    Long saveAuthor(Author author);

    Author findAuthorByIdOrElseThrow(Long id);

    List<AuthorResponseDto> findAllAuthors();
}
