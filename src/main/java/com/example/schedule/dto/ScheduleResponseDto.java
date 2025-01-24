package com.example.schedule.dto;

import com.example.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

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

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.authorName = schedule.getAuthorName();
        this.task = schedule.getTask();
        this.createdAt = schedule.getCreatedAt().toString();
        this.updatedAt = schedule.getUpdatedAt().toString();
    }
}
