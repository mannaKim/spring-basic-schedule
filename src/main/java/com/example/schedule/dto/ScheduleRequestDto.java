package com.example.schedule.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ScheduleRequestDto {
    private Long authorId;
    private String authorName;

    @NotNull(message = "Task is required.")
    @Size(max=200)
    private String task;

    @NotNull(message = "Password is required.")
    private String password;
}
