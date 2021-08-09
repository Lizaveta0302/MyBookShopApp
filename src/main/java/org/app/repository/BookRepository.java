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

    private static final String SELECT_ALL_BOOKS = "SELECT * FROM books";
    private static final String INSERT_BOOK = "INSERT INTO books(author, title, size) VALUES(:author, :title, :size)";
    private static final String DELETE_BOOK = "DELETE FROM books WHERE %s = :fieldValue";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> retrieveAll() {
        return jdbcTemplate.query(SELECT_ALL_BOOKS, (ResultSet rs, int rowNum) ->
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
        jdbcTemplate.update(INSERT_BOOK, parameterSource);
        logger.info("store new book: " + book);
    }

    @Override
    public void removeItemByField(String itemField, String itemValue) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("fieldName", itemField);
        parameterSource.addValue("fieldValue", itemValue);
        String deleteQuery = String.format(DELETE_BOOK, parameterSource.getValue("fieldName"));
        jdbcTemplate.update(deleteQuery, parameterSource);
    }
}
