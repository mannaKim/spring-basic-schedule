package com.example.schedule.repository;

import com.example.schedule.dto.AuthorResponseDto;
import com.example.schedule.entity.Author;
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
public class JdbcTemplateAuthorRepository implements AuthorRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateAuthorRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Long saveAuthor(Author author) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("author")
                .usingGeneratedKeyColumns("id");

        jdbcInsert.usingColumns("name", "email");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", author.getName());
        parameters.put("email", author.getEmail());

        return jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters)).longValue();
    }

    @Override
    public Author findAuthorByIdOrElseThrow(Long id) {
        String sql = "SELECT id, name, email, created_at, updated_at"
                + " FROM author"
                + " WHERE id = ?";
        List<Author> result = jdbcTemplate.query(sql, authorRowMapper(), id);

        return result.stream()
                .findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dose not exist id = " + id));
    }

    @Override
    public List<AuthorResponseDto> findAllAuthors() {
        String sql = "SELECT id, name, email, created_at, updated_at"
                + " FROM author"
                + " ORDER BY updated_at DESC";
        List<Author> result = jdbcTemplate.query(sql, authorRowMapper());

        return result.stream()
                .map(AuthorResponseDto::new)
                .toList();
    }

    @Override
    public boolean existById(Long id) {
        String sql = "SELECT COUNT(*) FROM author WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public int updateAuthorName(Long id, String name) {
        String sql = "UPDATE author SET name = ? WHERE id = ?";
        return jdbcTemplate.update(sql, name, id);
    }

    private RowMapper<Author> authorRowMapper() {
        return new RowMapper<Author>() {
            @Override
            public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Author(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime()
                );
            }
        };
    }
}
