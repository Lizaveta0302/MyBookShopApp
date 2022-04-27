package com.example.bookshop_app.service;

import com.example.bookshop_app.entity.book.Book;
import com.example.bookshop_app.repo.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BooksRatingAndPopularityServiceTest {

    private final BooksRatingAndPopularityService service;
    private final BookRepository repository;

    private List<Book> books;

    @Autowired
    public BooksRatingAndPopularityServiceTest(BooksRatingAndPopularityService service, BookRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @BeforeEach
    void setUp() {
        books = repository.getAllBooksByPopularity(Pageable.ofSize(10)).getContent();
    }

    @AfterEach
    void tearDown() {
        books = null;
    }

    @Test
    void sortBooksAccordingItsPopularity() {
        List<Book> booksForSorting = new ArrayList<>(books);
        Collections.shuffle(booksForSorting);
        List<Book> sortedBooks = service.sortBooksAccordingItsPopularity(books);
        assertEquals(sortedBooks, books);
    }
}