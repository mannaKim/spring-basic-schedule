package com.example.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {
    private Long id;
    private Long authorId;
    private String authorName;
    private String task;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
