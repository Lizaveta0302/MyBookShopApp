package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.entity.book.Book;
import com.example.MyBookShopApp.repo.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BookService {

    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBooksData() {
        return bookRepository.findAll();
    }

    public List<Book> getBooksByAuthorId(String id) {
        return bookRepository.findBooksByAuthorId(Integer.valueOf(id)).orElse(Collections.emptyList());
    }

    public Book getBookById(String id) {
        return bookRepository.findById(Integer.valueOf(id)).orElse(new Book());
    }

    public List<Book> getBooksByTitle(String title) {
        return bookRepository.findBooksByTitle(title).orElse(Collections.emptyList());
    }
}
