package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.entity.Author;
import com.example.MyBookShopApp.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private JdbcTemplate jdbcTemplate;
    private static final String SELECT_ALL_BOOKS_QUERY = "SELECT * FROM shop.books LEFT JOIN shop.authors ON shop.books.author_id = shop.authors.author_id";
    private static final String SELECT_ALL_BOOKS_BY_AUTHOR_ID_QUERY = " SELECT * FROM shop.books LEFT JOIN shop.authors ON shop.books.author_id = shop.authors.author_id WHERE shop.books.author_id = ?";
    private static final String SELECT_BOOK_BY_ID_QUERY = " SELECT * FROM shop.books LEFT JOIN shop.authors ON shop.books.author_id = shop.authors.author_id WHERE shop.books.id = ?";
    private static final String SELECT_BOOK_BY_TITLE_QUERY = " SELECT * FROM shop.books LEFT JOIN shop.authors ON shop.books.author_id = shop.authors.author_id WHERE shop.books.title = ?";

    @Autowired
    public BookService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> getBooksData() {
        List<Book> books = jdbcTemplate.query(SELECT_ALL_BOOKS_QUERY, (ResultSet rs, int rowNum) -> {
            return mapBook(rs);
        });
        return new ArrayList<>(books);
    }

    public List<Book> getBooksByAuthorId(String id) {
        List<Book> books = jdbcTemplate.query(SELECT_ALL_BOOKS_BY_AUTHOR_ID_QUERY, new Object[]{id},
                (rs, rowNum) -> {
                    return mapBook(rs);
                });
        return new ArrayList<>(books);
    }

    public Book getBookById(String id) {
        return jdbcTemplate.queryForObject(SELECT_BOOK_BY_ID_QUERY, new Object[]{id}, (rs, rowNum) -> {
            return mapBook(rs);
        });
    }

    public Book getBooksByTitle(String title) {
        return jdbcTemplate.queryForObject(SELECT_BOOK_BY_TITLE_QUERY, new Object[]{title}, (rs, rowNum) -> mapBook(rs));
    }


    private Book mapBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setTitle(rs.getString("title"));
        book.setPriceOld(rs.getString("price_old"));
        book.setPrice(rs.getString("price"));
        Author author = new Author();
        author.setId(rs.getInt("author_id"));
        author.setFirstName(rs.getString("first_name"));
        author.setLastName(rs.getString("last_name"));
        book.setAuthor(author);
        return book;
    }

}
