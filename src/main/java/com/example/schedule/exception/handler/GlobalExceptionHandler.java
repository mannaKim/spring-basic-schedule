package com.example.schedule.exception.handler;

import com.example.schedule.exception.dto.CustomErrorResponse;
import com.example.schedule.exception.custom.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomErrorResponse> handleCustomException(CustomException ex) {
        return ResponseEntity
                .status(ex.getErrorCode().getHttpStatus())
                .body(CustomErrorResponse.of(ex.getErrorCode()));
    }
}
