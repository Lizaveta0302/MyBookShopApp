package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.entity.Author;
import com.example.MyBookShopApp.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private JdbcTemplate jdbcTemplate;
    private static final String SELECT_ALL_BOOKS_QUERY = "SELECT * FROM shop.books LEFT JOIN shop.authors ON shop.books.author_id = shop.authors.author_id";

    @Autowired
    public BookService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> getBooksData() {
        List<Book> books = jdbcTemplate.query(SELECT_ALL_BOOKS_QUERY, (ResultSet rs, int rowNum) -> {
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
        });
        return new ArrayList<>(books);
    }
}
