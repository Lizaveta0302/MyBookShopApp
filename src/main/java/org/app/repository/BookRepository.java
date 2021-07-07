package org.app.repository;

import org.apache.log4j.Logger;
import org.app.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class BookRepository implements ProjectRepository<Book> {

    private final Logger logger = Logger.getLogger(BookRepository.class);

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Book> retrieveAll() {
        List<Book> books = jdbcTemplate.query("SELECT * FROM books", (ResultSet rs, int rowNum) ->
        {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setSize(rs.getInt("size"));
            return book;
        });
        return books;
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
    public void removeItemByField(String field, String value) {
        try {
            for (Book book : retrieveAll()) {
                Field bookField = book.getClass().getDeclaredField(field);
                bookField.setAccessible(true);
                Object bookValue = bookField.get(book);
                if (Objects.nonNull(bookValue)) {
                    if (bookValue.toString().equals(value)) {
                        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
                        parameterSource.addValue("id", value);
                        jdbcTemplate.update("DELETE FROM books WHERE id = :id", parameterSource);
                        logger.info("book is removed: " + book);
                    }
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            logger.info("remove book by field exception: " + ex.getMessage());
        }
    }

    @Override
    public List<Book> filterItemsByField(String field, String value) {
        List<Book> filteredBooks = new ArrayList<>();
        try {
            for (Book book : retrieveAll()) {
                Field bookField = book.getClass().getDeclaredField(field);
                bookField.setAccessible(true);
                Object fieldValue = bookField.get(book);
                if (Objects.nonNull(fieldValue)) {
                    if (fieldValue.toString().contains(value)) {
                        filteredBooks.add(book);
                    }
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            logger.info("filter books by field exception: " + ex.getMessage());
        }
        return filteredBooks;
    }
}
