package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;

import java.util.List;

public interface ScheduleRepository {
    Long saveSchedule(Schedule schedule);

    ScheduleResponseDto findScheduleByIdOrElseThrow(Long id);

    List<ScheduleResponseDto> findSchedulesByFilters(Long authorId, String updatedAt);

    String findPasswordById(Long id);

    int deleteSchedule(Long id);

    boolean existById(Long id);

    int updateSchedule(Long id, String task, String authorName);
}
