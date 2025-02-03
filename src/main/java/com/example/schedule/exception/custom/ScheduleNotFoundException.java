package com.example.schedule.exception.custom;

import com.example.schedule.exception.code.CustomErrorCode;

public class ScheduleNotFoundException extends CustomException {
    public ScheduleNotFoundException(Long id) {
        super(CustomErrorCode.SCHEDULE_NOT_FOUND, "존재하지 않는 일정입니다. id = " + id);
    }
}
