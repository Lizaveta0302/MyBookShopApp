package org.app.repository;

import org.apache.log4j.Logger;
import org.app.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class BookRepository implements ProjectRepository<Book> {

    private final Logger logger = Logger.getLogger(BookRepository.class);

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Book> retrieveAll() {
        return jdbcTemplate.query("SELECT * FROM books", (ResultSet rs, int rowNum) ->
        {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setSize(rs.getInt("size"));
            return book;
        });
    }

    @Override
    public void save(Book book) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", book.getAuthor());
        parameterSource.addValue("title", book.getTitle());
        parameterSource.addValue("size", book.getSize());
        jdbcTemplate.update("INSERT INTO books(author, title, size) VALUES(:author, :title, :size)", parameterSource);
        logger.info("store new book: " + book);
    }

    @Override
    public void removeItemByField(String itemField, String itemValue) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("fieldName", itemField);
        parameterSource.addValue("fieldValue", itemValue);
        String deleteQuery = "DELETE FROM books WHERE " + parameterSource.getValue("fieldName") + " = :fieldValue";
        jdbcTemplate.update(deleteQuery, parameterSource);
    }
}
