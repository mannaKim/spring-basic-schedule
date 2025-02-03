package com.example.schedule.exception.custom;

import com.example.schedule.exception.code.CustomErrorCode;

public class ScheduleNotFoundException extends CustomException {
    public ScheduleNotFoundException() {
        super(CustomErrorCode.SCHEDULE_NOT_FOUND);
    }
}
