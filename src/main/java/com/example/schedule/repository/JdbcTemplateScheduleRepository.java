package com.example.schedule.repository;

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
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public long saveSchedule(Schedule schedule) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("schedule")
                .usingGeneratedKeyColumns("id");

        jdbcInsert.usingColumns("author_name", "password", "task");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("author_name", schedule.getAuthorName());
        parameters.put("password", schedule.getPassword());
        parameters.put("task", schedule.getTask());

        return jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters)).longValue();
    }

    @Override
    public Schedule findScheduleByIdOrElseThrow(Long id) {
        String sql = "SELECT id, created_at, updated_at, author_name, password, task"
                    + " FROM schedule"
                    + " WHERE id = ?";
        List<Schedule> result = jdbcTemplate.query(sql, scheduleRowMapper(), id);

        return result.stream()
                .findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dose not exist id = " + id));
    }

    @Override
    public List<ScheduleResponseDto> findSchedulesByFilters(String authorName, String updatedAt) {
        StringBuilder sql = new StringBuilder("SELECT id, created_at, updated_at, author_name, task"
                + " FROM schedule"
                + " WHERE 1 = 1");
        List<Object> params = new ArrayList<>();

        if (authorName != null) {
            sql.append(" AND author_name = ?");
            params.add(authorName);
        }

        if (updatedAt != null) {
            sql.append(" AND DATE_FORMAT(updated_at, '%Y-%m-%d') = ?");
            params.add(updatedAt);
        }

        sql.append(" ORDER BY updated_at DESC");

        return jdbcTemplate.query(sql.toString(), scheduleResponseDtoRowMapper(), params.toArray());
    }

    @Override
    public String findPasswordById(Long id) {
        String sql = "SELECT password FROM schedule WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, id);
    }

    @Override
    public int deleteSchedule(Long id) {
        String sql = "DELETE FROM schedule WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existById(Long id) {
        String sql = "SELECT COUNT(*) FROM schedule WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public int updateSchedule(Long id, String task, String authorName) {
        StringBuilder sql = new StringBuilder("UPDATE schedule SET");
        List<Object> params = new ArrayList<>();

        if (task != null) {
            sql.append(" task = ?,");
            params.add(task);
        }

        if (authorName != null) {
            sql.append(" author_name = ?,");
            params.add(authorName);
        }

        sql.setLength(sql.length() - 1); // 마지막 쉼표 제거
        sql.append(" WHERE id = ?");
        params.add(id);

        return jdbcTemplate.update(sql.toString(), params.toArray());
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

    private RowMapper<ScheduleResponseDto> scheduleResponseDtoRowMapper() {
        return new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ScheduleResponseDto(
                        rs.getLong("id"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime(),
                        rs.getString("author_name"),
                        rs.getString("task")
                );
            }
        };
    }
}
