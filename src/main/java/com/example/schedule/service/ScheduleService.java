package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;

import java.util.List;

public interface ScheduleService {
    ScheduleResponseDto createSchedule(ScheduleRequestDto dto);

    ScheduleResponseDto findScheduleById(Long id);

    List<ScheduleResponseDto> findSchedulesByFilters(Long authorId, String updatedAt);

    void deleteSchedule(Long id, String password);

    ScheduleResponseDto updateSchedule(Long id, String password, String task, String authorName);
}
