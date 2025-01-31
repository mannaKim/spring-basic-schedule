package com.example.schedule.entity;

import com.example.schedule.dto.ScheduleRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Schedule {
    private Long id;
    private Long authorId;
    private String task;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Schedule(ScheduleRequestDto dto) {
        this.authorId = dto.getAuthorId();
        this.password = dto.getPassword();
        this.task = dto.getTask();
    }
}
