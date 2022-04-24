package com.example.bookshop_app.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class AuthUserControllerTest {

    private final MockMvc mockMvc;

    @MockBean
    private BookstoreUserRegister userRegister;

    @Autowired
    AuthUserControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void handleUserRegistration() throws Exception {
        mockMvc.perform(post("/reg")
                .content(asJsonString(new RegistrationForm()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        Mockito.verify(userRegister, Mockito.times(1)).registerNewUser(any());
    }

    @Test
    void handleLogin() throws Exception {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("");
        doReturn(response)
                .when(userRegister)
                .jwtLogin(any());
        MvcResult mvcResult = mockMvc.perform(post("/login")
                .content(asJsonString(new ContactConfirmationPayload()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        Mockito.verify(userRegister, Mockito.times(1)).jwtLogin(any());
        assertEquals(response.getResult(), Objects.requireNonNull(mvcResult.getResponse().getCookie("token")).getValue());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}