package com.example.schedule.exception.dto;

import com.example.schedule.exception.code.CustomErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CustomErrorResponse {
    private final int code;
    private final String message;
    private final LocalDateTime timestamp;

    public static CustomErrorResponse of(CustomErrorCode errorCode, String customMessage) {
        return new CustomErrorResponse(
                errorCode.getHttpStatus().value(),
                (customMessage != null) ? customMessage : errorCode.getMessage(),
                LocalDateTime.now()
        );
    }
}