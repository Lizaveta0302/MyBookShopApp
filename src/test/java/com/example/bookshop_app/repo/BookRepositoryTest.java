package com.example.bookshop_app.repo;

import com.example.bookshop_app.entity.book.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class BookRepositoryTest {

    private final BookRepository bookRepository;

    @Autowired
    public BookRepositoryTest(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Test
    void findBooksByAuthorFirstNameContaining() {
        String token = "Jelene";
        List<Book> bookListByAuthorFirstName = bookRepository.findBooksByAuthorFirstNameContaining(token);

        assertNotNull(bookListByAuthorFirstName);
        assertFalse(bookListByAuthorFirstName.isEmpty());
        for (Book book : bookListByAuthorFirstName) {
            assertThat(book.getAuthor().getFirstName()).contains(token);
        }
    }

    @Test
    void findBooksByTitleContaining() {
        String token = "Sudden";
        List<Book> bookListByTitle = bookRepository.findBooksByTitleContaining(token, PageRequest.of(1, 1)).getContent();

        assertNotNull(bookListByTitle);
        assertFalse(bookListByTitle.isEmpty());
        for (Book book : bookListByTitle) {
            assertThat(book.getTitle()).contains(token);
        }
    }

    @Test
    void getBestsellers() {
        List<Book> bestsellersBooks = bookRepository.getBestsellers();

        assertNotNull(bestsellersBooks);
        assertFalse(bestsellersBooks.isEmpty());
        assertThat(bestsellersBooks.size()).isGreaterThan(1);
    }
}