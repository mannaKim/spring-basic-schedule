package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.exception.custom.AuthorNotFoundException;
import com.example.schedule.exception.custom.InvalidPasswordException;
import com.example.schedule.exception.custom.InvalidRequestException;
import com.example.schedule.exception.custom.ScheduleNotFoundException;
import com.example.schedule.repository.AuthorRepository;
import com.example.schedule.repository.ScheduleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final AuthorRepository authorRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, AuthorRepository authorRepository) {
        this.scheduleRepository = scheduleRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public ScheduleResponseDto createSchedule(ScheduleRequestDto dto) {
        validateAuthorId(dto.getAuthorId());

        Schedule schedule = new Schedule(dto);
        Long id = scheduleRepository.saveSchedule(schedule);

        return scheduleRepository.findScheduleByIdOrElseThrow(id);
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        return scheduleRepository.findScheduleByIdOrElseThrow(id);
    }

    @Override
    public Page<ScheduleResponseDto> findSchedulesByFilters(Long authorId, String updatedAt, Pageable pageable) {
        long totalCount = scheduleRepository.countSchedules();
        List<ScheduleResponseDto> schedules = scheduleRepository.findSchedulesByFilters(authorId, updatedAt, pageable);
        return new PageImpl<>(schedules, pageable, totalCount);
    }

    @Override
    public void deleteSchedule(Long id, String password) {
        validateScheduleAndPassword(id, password);

        int deletedRow = scheduleRepository.deleteSchedule(id);
        if (deletedRow == 0) {
            throw new ScheduleNotFoundException(id);
        }
    }

    @Transactional
    @Override
    public ScheduleResponseDto updateSchedule(Long id, String password, String task, String authorName) {
        validateScheduleAndPassword(id, password);

        if (task == null && authorName == null) {
            throw new InvalidRequestException();
        }

        if (task != null) {
            int updatedRow = scheduleRepository.updateSchedule(id, task);
            if (updatedRow == 0) {
                throw new ScheduleNotFoundException(id);
            }
        }

        if (authorName != null) {
            Long authorId = scheduleRepository.findAuthorIdByScheduleId(id);
            int updatedRow = authorRepository.updateAuthorName(authorId, authorName);
            if (updatedRow == 0) {
                throw new AuthorNotFoundException(authorId);
            }
        }

        return scheduleRepository.findScheduleByIdOrElseThrow(id);
    }

    private void validateScheduleAndPassword(Long id, String password) {
        if (!scheduleRepository.existById(id)) {
            throw new ScheduleNotFoundException(id);
        }

        String savedPassword = scheduleRepository.findPasswordById(id);
        if (!savedPassword.equals(password)) {
            throw new InvalidPasswordException();
        }
    }

    private void validateAuthorId(Long authorId) {
        if (!authorRepository.existById(authorId)) {
            throw new AuthorNotFoundException(authorId);
        }
    }
}
