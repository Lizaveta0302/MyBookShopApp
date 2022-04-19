package com.example.bookshop_app.security;

import com.example.bookshop_app.entity.BookstoreUser;
import com.example.bookshop_app.repo.BookstoreUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class BookstoreUserRegisterTest {

    private final BookstoreUserRegister userRegister;
    private final PasswordEncoder encoder;
    private RegistrationForm registrationForm;

    @MockBean
    private BookstoreUserRepository bookstoreUserRepositoryMock;

    @Autowired
    public BookstoreUserRegisterTest(BookstoreUserRegister userRegister, PasswordEncoder encoder) {
        this.userRegister = userRegister;
        this.encoder = encoder;
    }

    @BeforeEach
    void setUp() {
        registrationForm = new RegistrationForm();
        registrationForm.setEmail("test@mail.org");
        registrationForm.setName("Tester");
        registrationForm.setPass("iddqd");
        registrationForm.setPhone("9031232323");
    }

    @AfterEach
    void tearDown() {
        registrationForm = null;
    }

    @Test
    void registerNewUser() {
        userRegister.registerNewUser(registrationForm);
        Mockito.verify(bookstoreUserRepositoryMock, Mockito.times(1))
                .save(Mockito.any(BookstoreUser.class));
    }

    @Test
    void registerNewUserFail() {
        Mockito.doReturn(null)
                .when(bookstoreUserRepositoryMock)
                .findBookstoreUserByEmail(registrationForm.getEmail());
        BookstoreUser user = userRegister.registerNewUser(registrationForm);
        assertNull(user);
    }
}