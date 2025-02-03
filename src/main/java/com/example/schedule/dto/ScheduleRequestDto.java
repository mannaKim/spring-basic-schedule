package com.example.schedule.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ScheduleRequestDto {
    @NotNull(message = "작성자 ID는 필수 입력값입니다.")
    private Long authorId;

    private String authorName;

    @NotNull(message = "할일은 필수 입력값입니다.")
    @Size(max=200, message = "할일은 최대 200자까지 입력 가능합니다.")
    private String task;

    @NotNull(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}
