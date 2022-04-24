package com.example.bookshop_app.service;

import com.example.bookshop_app.repo.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Random;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class BookServiceTest {

    private final BookService bookService;
    private Random rand;

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    BookServiceTest(BookService bookService) {
        this.bookService = bookService;
    }

    @BeforeEach
    void setUp() {
        rand = new Random();
    }

    @Test
    void getBestsellers() {
        bookService.getBestsellers();
        Mockito.verify(bookRepository, Mockito.times(1))
                .getBestsellers();
    }

    @Test
    void getPageOfRecommendedBooks() {
        bookService.getPageOfRecommendedBooks(rand.nextInt(10) + 1, rand.nextInt(10) + 1);
        Mockito.verify(bookRepository, Mockito.times(1))
                .findAllByIsBestsellerTrue(any());
    }

    @Test
    void getPageOfRecentBooks() {
        bookService.getPageOfRecentBooks(rand.nextInt(10) + 1, rand.nextInt(10) + 1);
        Mockito.verify(bookRepository, Mockito.times(1))
                .findAllByOrderByPubDateDesc(any());
    }

    @Test
    void getPageOfPopularBooks() {
        bookService.getPageOfPopularBooks(rand.nextInt(10) + 1, rand.nextInt(10) + 1);
        Mockito.verify(bookRepository, Mockito.times(1))
                .getAllBooksByPopularity(any());
    }
}