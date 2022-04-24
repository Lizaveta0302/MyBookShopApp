package com.example.bookshop_app.security;

import com.example.bookshop_app.entity.BookstoreUser;
import com.example.bookshop_app.repo.BookstoreUserRepository;
import com.example.bookshop_app.security.jwt.JWTUtil;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class BookstoreUserRegisterTest {

    private final BookstoreUserRegister userRegister;
    private final PasswordEncoder passwordEncoder;

    private RegistrationForm registrationForm;

    @MockBean
    private BookstoreUserRepository bookstoreUserRepositoryMock;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private BookstoreUserDetailsService bookstoreUserDetailsService;
    @MockBean
    private JWTUtil jwtUtil;

    @Autowired
    public BookstoreUserRegisterTest(BookstoreUserRegister userRegister, PasswordEncoder passwordEncoder) {
        this.userRegister = userRegister;
        this.passwordEncoder = passwordEncoder;
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
        BookstoreUser savedUser = mapUser(registrationForm);
        doReturn(savedUser).when(bookstoreUserRepositoryMock).save(Matchers.any());
        BookstoreUser user = userRegister.registerNewUser(registrationForm);
        assertNotNull(user);
        assertTrue(passwordEncoder.matches(registrationForm.getPass(), user.getPassword()));
        assertTrue(CoreMatchers.is(user.getName()).matches(registrationForm.getName()));
        assertTrue(CoreMatchers.is(user.getEmail()).matches(registrationForm.getEmail()));

        Mockito.verify(bookstoreUserRepositoryMock, Mockito.times(1))
                .save(Mockito.any(BookstoreUser.class));
    }

    @Test
    void registerNewUserFail() {
        doReturn(mapUser(registrationForm))
                .when(bookstoreUserRepositoryMock)
                .findBookstoreUserByEmail(registrationForm.getEmail());

        BookstoreUser user = userRegister.registerNewUser(registrationForm);
        assertNull(user);
    }

    @Test
    void testJwtLogin() {
        ContactConfirmationPayload payload = new ContactConfirmationPayload();
        payload.setContact(registrationForm.getEmail());
        payload.setCode(registrationForm.getPass());
        String jwtToken = "";
        doReturn(jwtToken).when(jwtUtil).generateToken(Matchers.any());
        ContactConfirmationResponse response = userRegister.jwtLogin(payload);
        System.out.println(response.getResult());
        Mockito.verify(authenticationManager, Mockito.times(1))
                .authenticate(any());
        Mockito.verify(bookstoreUserDetailsService, Mockito.times(1))
                .loadUserByUsername(any());

        assertTrue(CoreMatchers.is(response.getResult()).matches(jwtToken));
    }

    private BookstoreUser mapUser(RegistrationForm registrationForm) {
        BookstoreUser user = new BookstoreUser();
        user.setEmail(registrationForm.getEmail());
        user.setPassword(passwordEncoder.encode(registrationForm.getPass()));
        user.setPhone(registrationForm.getPhone());
        user.setName(registrationForm.getName());
        return user;
    }
}