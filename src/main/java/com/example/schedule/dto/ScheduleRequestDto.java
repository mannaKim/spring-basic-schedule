package com.example.schedule.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class ScheduleRequestDto {
    private String authorName;

    @NotNull(message = "Password is required.")
    private String password;

    @NotNull(message = "Task is required.")
    @Size(max=200)
    private String task;
}
