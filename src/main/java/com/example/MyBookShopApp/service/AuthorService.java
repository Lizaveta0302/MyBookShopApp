package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.entity.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final JdbcTemplate jdbcTemplate;
    private static final String SELECT_ALL_AUTHORS_QUERY = "SELECT * FROM shop.authors ORDER BY name";

    @Autowired
    public AuthorService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Author> getAllAuthors() {
        List<Author> authors = jdbcTemplate.query(SELECT_ALL_AUTHORS_QUERY, (ResultSet rs, int rowNum) -> {
            Author author = new Author();
            author.setId(rs.getInt("author_id"));
            author.setName(rs.getString("name"));
            return author;
        });
        return new ArrayList<>(authors);
    }

    public Map<String, List<Author>> getFilteredAuthors() {
        Map<String, List<Author>> authorsMap = new HashMap<>();
        List<Author> authors = getAllAuthors();
        List<String> firstLettersOfAuthorsName = authors.stream()
                .map(Author::getName)
                .map(name -> name.substring(0, 1))
                .collect(Collectors.toList());
        int authorsSize = firstLettersOfAuthorsName.size();
        List<Author> filteredAuthors = new ArrayList<>();
        for (int i = 0; i < authorsSize; i++) {
            filteredAuthors.add(authors.get(i));
            if (i != authorsSize - 1 && !firstLettersOfAuthorsName.get(i).equals(firstLettersOfAuthorsName.get(i + 1))) {
                authorsMap.put(firstLettersOfAuthorsName.get(i), filteredAuthors);
                filteredAuthors = new ArrayList<>();
            } else if (i == authorsSize - 1) {
                if (Objects.isNull(authorsMap.get(firstLettersOfAuthorsName.get(i)))) {
                    authorsMap.put(firstLettersOfAuthorsName.get(i), filteredAuthors);
                } else {
                    authorsMap.get(firstLettersOfAuthorsName.get(i)).add(authors.get(i));
                }
            }
        }
        return authorsMap;
    }
}
