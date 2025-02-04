package com.example.schedule.controller;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "일정 관리", description = "일정 관련 API")
@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @Operation(
            summary = "일정 생성",
            description = "작성자의 ID, 할 일, 비밀번호를 입력받아 새로운 일정을 생성합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "정상 생성",
                    content = @Content(schema = @Schema(implementation = ScheduleResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (필수 입력값 누락)",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "생성할 일정 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "authorId": 1,
                                    "task": "회의 참석",
                                    "password": "Pass123"
                                }
                                """)
                    )
            ) @Valid @RequestBody ScheduleRequestDto dto) {
        return new ResponseEntity<>(scheduleService.createSchedule(dto), HttpStatus.CREATED);
    }


    @Operation(
            summary = "특정 일정 조회",
            description = "ID를 기준으로 특정 일정을 조회합니다."
    )
    @Parameter(name = "id", description = "일정 ID", example = "1")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 조회",
                    content = @Content(schema = @Schema(implementation = ScheduleResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 일정 ID",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long id) {
        return new ResponseEntity<>(scheduleService.findScheduleById(id), HttpStatus.OK);
    }


    @Operation(
            summary = "일정 목록 조회",
            description = "작성자 ID 또는 수정일을 기준으로 일정 목록을 조회합니다. 페이징 처리가 가능합니다."
    )
    @Parameter(name = "updatedAt", description = "수정일 (yyyy-MM-dd)", example = "2025-01-31")
    @Parameter(name = "authorId", description = "작성자 ID", example = "1")
    @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0")
    @Parameter(name = "size", description = "페이지 크기", example = "10")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 조회",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 작성자 ID",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping
    public ResponseEntity<Page<ScheduleResponseDto>> findSchedulesByFilters(
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) String updatedAt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ScheduleResponseDto> schedules = scheduleService.findSchedulesByFilters(authorId, updatedAt, pageable);
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }


    @Operation(
            summary = "특정 일정 삭제",
            description = "ID를 기준으로 특정 일정을 삭제합니다. 비밀번호를 입력해야 합니다."
    )
    @Parameter(name = "id", description = "일정 ID", example = "1")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "정상 삭제"),
            @ApiResponse(responseCode = "403", description = "비밀번호 불일치"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 일정 ID")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "삭제할 일정의 비밀번호",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "password": "Pass123"
                                }
                                """)
                    )
            ) @RequestBody ScheduleRequestDto dto
    ) {
        scheduleService.deleteSchedule(id, dto.getPassword());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @Operation(
            summary = "특정 일정 수정",
            description = "일정의 작성자명 또는 할 일을 수정할 수 있습니다. 하나 이상의 필드를 입력해야 합니다."
    )
    @Parameter(name = "id", description = "일정 ID", example = "1")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 수정",
                    content = @Content(schema = @Schema(implementation = ScheduleResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (수정할 필드가 없음)",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "비밀번호 불일치",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 일정 ID",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "수정할 일정의 수정 정보와 비밀번호",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "authorName": "Kim",
                                    "task": "청소",
                                    "password": "Pass123"
                                }
                                """)
                    )
            ) @RequestBody ScheduleRequestDto dto
    ) {
        ScheduleResponseDto responseDto = scheduleService.updateSchedule(id, dto.getPassword(), dto.getTask(), dto.getAuthorName());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
