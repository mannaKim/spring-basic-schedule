package com.example.schedule.repository;

import com.example.schedule.controller.ScheduleController;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcTemplateScheduleRepository implements ScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateScheduleRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public ScheduleResponseDto saveSchedule(Schedule schedule) {

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("schedule")
                .usingGeneratedKeyColumns("id");


        jdbcInsert.usingColumns("author_name", "password", "task");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("author_name", schedule.getAuthorName());
        parameters.put("password", schedule.getPassword());
        parameters.put("task", schedule.getTask());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        return new ScheduleResponseDto(key.longValue(), schedule.getAuthorName(), schedule.getTask());
    }

    @Override
    public Schedule findScheduleByIdOrElseThrow(Long id) {

        String sql = "SELECT id, created_at, updated_at, author_name, password, task  "
                    + "FROM schedule "
                    + "WHERE id = ?";

        List<Schedule> result = jdbcTemplate.query(sql, scheduleRowMapper(), id);
        return result.stream()
                .findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dose not exist id = " + id));
    }

    private RowMapper<Schedule> scheduleRowMapper() {
        return new RowMapper<Schedule>() {
            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Schedule(
                        rs.getLong("id"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime(),
                        rs.getString("author_name"),
                        rs.getString("password"),
                        rs.getString("task")
                );
            }
        };
    }




}
