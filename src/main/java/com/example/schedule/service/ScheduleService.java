package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScheduleService {
    ScheduleResponseDto createSchedule(ScheduleRequestDto dto);

    ScheduleResponseDto findScheduleById(Long id);

    Page<ScheduleResponseDto> findSchedulesByFilters(Long authorId, String updatedAt, Pageable pageable);

    void deleteSchedule(Long id, String password);

    ScheduleResponseDto updateSchedule(Long id, String password, String task, String authorName);
}
