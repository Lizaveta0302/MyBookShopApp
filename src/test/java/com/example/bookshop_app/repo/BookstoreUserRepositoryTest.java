package com.example.bookshop_app.repo;

import com.example.bookshop_app.entity.BookstoreUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class BookstoreUserRepositoryTest {

    private final BookstoreUserRepository bookstoreUserRepository;

    @Autowired
    public BookstoreUserRepositoryTest(BookstoreUserRepository bookstoreUserRepository) {
        this.bookstoreUserRepository = bookstoreUserRepository;
    }

    @Test
    public void testAddNewUser() {
        BookstoreUser user = new BookstoreUser();
        user.setPassword("1234567890");
        user.setPhone("9483948343");
        user.setName("Tester");
        user.setEmail("test@mail.org");

        assertNotNull(bookstoreUserRepository.save(user));
    }
}