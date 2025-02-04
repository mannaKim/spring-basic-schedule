package com.example.schedule.controller;

import com.example.schedule.dto.AuthorRequestDto;
import com.example.schedule.dto.AuthorResponseDto;
import com.example.schedule.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "작성자 관리", description = "작성자 관련 API")
@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Operation(
            summary = "작성자 생성",
            description = "작성자의 이름과 이메일을 입력받아 새로운 작성자를 생성합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "정상 생성",
                    content = @Content(schema = @Schema(implementation = AuthorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (부적절한 이메일 형식)",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping
    public ResponseEntity<AuthorResponseDto> createAuthor(@Valid @RequestBody AuthorRequestDto dto) {
        return new ResponseEntity<>(authorService.createAuthor(dto), HttpStatus.CREATED);
    }


    @Operation(
            summary = "특정 작성자 조회",
            description = "ID를 기준으로 특정 작성자를 조회합니다."
    )
    @Parameter(name = "id", description = "작성자 ID", example = "1")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 조회",
                    content = @Content(schema = @Schema(implementation = AuthorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 작성자 ID",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDto> findAuthorById(@PathVariable Long id) {
        return new ResponseEntity<>(authorService.findAuthorById(id), HttpStatus.OK);
    }


    @Operation(
            summary = "작성자 목록 조회",
            description = "등록된 모든 작성자를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 조회",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AuthorResponseDto.class))))
    })
    @GetMapping
    public ResponseEntity<List<AuthorResponseDto>> findAllAuthors() {
        return new ResponseEntity<>(authorService.findAllAuthors(), HttpStatus.OK);
    }
}
