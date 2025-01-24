package com.example.schedule.entity;

import com.example.schedule.dto.ScheduleRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Schedule {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String authorName;
    private String password;
    private String task;

    public Schedule(ScheduleRequestDto dto) {
        this.authorName = dto.getAuthorName();
        this.password = dto.getPassword();
        this.task = dto.getTask();
    }
}
