package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;

import java.util.List;

public interface ScheduleRepository {
    ScheduleResponseDto saveSchedule(Schedule schedule);

    Schedule findScheduleByIdOrElseThrow(Long id);

    List<ScheduleResponseDto> findSchedulesByFilters(String authorName, String updatedAt);

    String findPasswordById(Long id);

    int deleteSchedule(Long id);

    boolean existById(Long id);
}
