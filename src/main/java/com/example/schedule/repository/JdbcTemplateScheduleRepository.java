package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import org.springframework.data.domain.Pageable;
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
    public Long saveSchedule(Schedule schedule) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("schedule")
                .usingGeneratedKeyColumns("id");

        jdbcInsert.usingColumns("author_id", "task", "password");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("author_id", schedule.getAuthorId());
        parameters.put("task", schedule.getTask());
        parameters.put("password", schedule.getPassword());

        return jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters)).longValue();
    }

    @Override
    public ScheduleResponseDto findScheduleByIdOrElseThrow(Long id) {
        String sql = "SELECT s.id, s.author_id, a.name AS author_name,"
                    + " s.task, s.password, s.created_at, s.updated_at"
                    + " FROM schedule s"
                    + " INNER JOIN author a ON s.author_id = a.id"
                    + " WHERE s.id = ?";
        List<ScheduleResponseDto> result = jdbcTemplate.query(sql, scheduleResponseDtoRowMapper(), id);

        return result.stream()
                .findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dose not exist id = " + id));
    }

    @Override
    public List<ScheduleResponseDto> findSchedulesByFilters(Long authorId, String updatedAt, Pageable pageable) {
        StringBuilder sql = new StringBuilder("SELECT s.id, s.author_id, a.name AS author_name,"
                + " s.task, s.password, s.created_at, s.updated_at"
                + " FROM schedule s"
                + " INNER JOIN author a ON s.author_id = a.id"
                + " WHERE 1 = 1");
        List<Object> params = new ArrayList<>();

        if (authorId != null) {
            sql.append(" AND s.author_id = ?");
            params.add(authorId);
        }

        if (updatedAt != null) {
            sql.append(" AND DATE_FORMAT(s.updated_at, '%Y-%m-%d') = ?");
            params.add(updatedAt);
        }

        sql.append(" ORDER BY s.updated_at DESC");
        sql.append(" LIMIT ? OFFSET ?");
        params.add(pageable.getPageSize());
        params.add(pageable.getOffset());

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
    public int updateSchedule(Long id, String task) {
        StringBuilder sql = new StringBuilder("UPDATE schedule SET");
        List<Object> params = new ArrayList<>();

        sql.append(" task = ?");
        params.add(task);

        sql.append(" WHERE id = ?");
        params.add(id);

        return jdbcTemplate.update(sql.toString(), params.toArray());
    }

    @Override
    public Long findAuthorIdByScheduleId(Long id) {
        String sql = "SELECT author_id FROM schedule WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, id);
    }

    @Override
    public long countSchedules() {
        String sql = "SELECT COUNT(*) FROM schedule";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return (count != null) ? count : 0L;
    }

    private RowMapper<ScheduleResponseDto> scheduleResponseDtoRowMapper() {
        return new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ScheduleResponseDto(
                        rs.getLong("id"),
                        rs.getLong("author_id"),
                        rs.getString("author_name"),
                        rs.getString("task"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime()
                );
            }
        };
    }
}
