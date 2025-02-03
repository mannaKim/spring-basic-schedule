package com.example.schedule.exception.custom;

import com.example.schedule.exception.code.CustomErrorCode;

public class InvalidPasswordException extends CustomException {
    public InvalidPasswordException() {
        super(CustomErrorCode.PASSWORD_MISMATCH);
    }
}
