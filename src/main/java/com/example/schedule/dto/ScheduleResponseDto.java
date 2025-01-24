package com.example.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {

    private Long id;
    private String authorName;
    private String task;
    private String createdAt;
    private String updatedAt;

    public ScheduleResponseDto(long id, String authorName, String task) {
        this.id = id;
        this.authorName = authorName;
        this.task = task;
    }
}
