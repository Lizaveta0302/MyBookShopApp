package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.entity.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final JdbcTemplate jdbcTemplate;
    private static final String SELECT_ALL_AUTHORS_QUERY = "SELECT * FROM shop.authors ORDER BY last_name";
    private static final String SELECT_AUTHOR_BY_ID_QUERY = "SELECT * FROM shop.authors WHERE author_id = ?";

    @Autowired
    public AuthorService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Author> getAllAuthors() {
        List<Author> authors = jdbcTemplate.query(SELECT_ALL_AUTHORS_QUERY, (rs, rowNum) -> {
            Author author = new Author();
            author.setId(rs.getInt("author_id"));
            author.setFirstName(rs.getString("first_name"));
            author.setLastName(rs.getString("last_name"));
            return author;
        });
        return new ArrayList<>(authors);
    }

    public Map<String, List<Author>> getFilteredAuthors() {
        return getAllAuthors().stream().collect(Collectors.groupingBy((Author a) -> a.getLastName().substring(0, 1)));
    }

    public Author getAuthorById(String id) {
        return jdbcTemplate.queryForObject(SELECT_AUTHOR_BY_ID_QUERY,  new Object[]{id}, (rs, rowNum) -> {
            Author author = new Author();
            author.setId(rs.getInt("author_id"));
            author.setFirstName(rs.getString("first_name"));
            author.setLastName(rs.getString("last_name"));
            return author;
        });
    }
}
