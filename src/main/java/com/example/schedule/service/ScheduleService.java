package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;

import java.util.List;

public interface ScheduleService {
    ScheduleResponseDto saveSchedule(ScheduleRequestDto dto);

    ScheduleResponseDto findScheduleById(Long id);

    List<ScheduleResponseDto> findSchedulesByFilters(String authorName, String updatedAt);

    void deleteSchedule(Long id, String password);
}
