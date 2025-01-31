package com.example.schedule.service;

import com.example.schedule.dto.AuthorRequestDto;
import com.example.schedule.dto.AuthorResponseDto;
import com.example.schedule.entity.Author;
import com.example.schedule.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorResponseDto createAuthor(AuthorRequestDto dto) {
        Author author = new Author(dto);
        Long id = authorRepository.saveAuthor(author);

        Author createdAuthor = authorRepository.findAuthorByIdOrElseThrow(id);
        return new AuthorResponseDto(createdAuthor);
    }

    @Override
    public AuthorResponseDto findAuthorById(Long id) {
        Author author = authorRepository.findAuthorByIdOrElseThrow(id);
        return new AuthorResponseDto(author);
    }

    @Override
    public List<AuthorResponseDto> findAllAuthors() {
        return authorRepository.findAllAuthors();
    }
}
