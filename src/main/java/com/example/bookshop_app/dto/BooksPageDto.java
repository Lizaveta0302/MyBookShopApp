package com.example.bookshop_app.dto;

import com.example.bookshop_app.entity.book.Book;

import java.util.List;

public class BooksPageDto {

    private Integer count;
    private List<Book> books;

    public BooksPageDto(List<Book> content) {
        this.books = content;
        this.count = content.size();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
