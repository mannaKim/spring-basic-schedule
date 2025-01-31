package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.repository.AuthorRepository;
import com.example.schedule.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
    public List<ScheduleResponseDto> findSchedulesByFilters(Long authorId, String updatedAt) {
        return scheduleRepository.findSchedulesByFilters(authorId, updatedAt);
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

        if (task == null && authorName == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Either task or authorName is required.");
        }

        if (task != null) {
            int updatedRow = scheduleRepository.updateSchedule(id, task);
            if (updatedRow == 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dose not exist id = " + id);
            }
        }

        if (authorName != null) {
            Long authorId = scheduleRepository.findAuthorIdByScheduleId(id);
            int updatedRow = authorRepository.updateAuthorName(authorId, authorName);
            if (updatedRow == 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dose not exist author_id  = " + authorId);
            }
        }

        return scheduleRepository.findScheduleByIdOrElseThrow(id);
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

    private void validateAuthorId(Long authorId) {
        if (!authorRepository.existById(authorId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist author_id = " + authorId);
        }
    }
}
