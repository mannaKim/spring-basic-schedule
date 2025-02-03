package com.example.schedule.exception.custom;

import com.example.schedule.exception.code.CustomErrorCode;

public class InvalidRequestException extends CustomException {
    public InvalidRequestException() {
        super(CustomErrorCode.INVALID_REQUEST);
    }
}
