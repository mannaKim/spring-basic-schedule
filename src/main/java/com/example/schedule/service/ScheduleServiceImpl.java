package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto) {
        Schedule schedule = new Schedule(dto);
        return scheduleRepository.saveSchedule(schedule);
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id);
        return new ScheduleResponseDto(schedule);
    }

    @Override
    public List<ScheduleResponseDto> findSchedulesByFilters(String authorName, String updatedAt) {
        return scheduleRepository.findSchedulesByFilters(authorName, updatedAt);
    }

    @Override
    public void deleteSchedule(Long id, String password) {
        validateScheduleAndPassword(id, password);

        int deletedRow = scheduleRepository.deleteSchedule(id);
        if (deletedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dose not exist id = " + id);
        }
    }

    @Transactional
    @Override
    public ScheduleResponseDto updateSchedule(Long id, String password, String task, String authorName) {
        validateScheduleAndPassword(id, password);

        if (task == null || authorName == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The task and authorName are required values.");
        }

        int updatedRow = scheduleRepository.updateSchedule(id, task, authorName);
        if (updatedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dose not exist id = " + id);
        }

        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id);
        return new ScheduleResponseDto(schedule);
    }

    private void validateScheduleAndPassword(Long id, String password) {
        if (!scheduleRepository.existById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }

        String savedPassword = scheduleRepository.findPasswordById(id);
        if (!savedPassword.equals(password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다.");
        }
    }
}
