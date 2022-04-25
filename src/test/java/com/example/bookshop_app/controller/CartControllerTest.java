package com.example.bookshop_app.controller;

import com.example.bookshop_app.entity.book.Book;
import com.example.bookshop_app.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {

    private final MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    CartControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @WithUserDetails("liza.shpinkovaa@mail.ru")
    void handleChangeBookStatus() throws Exception {
        doReturn(mock(Book.class))
                .when(bookService)
                .findBookBySlug(anyString());
        MvcResult mvcResult = mockMvc.perform(post("/cart/changeBookStatus/{slug}", "slug"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/slug/slug"))
                .andReturn();

        Mockito.verify(bookService, Mockito.times(1)).findBookBySlug(anyString());
        Mockito.verify(bookService, Mockito.times(1)).updateQuantityInBasket(anyString(), anyInt());

        assertNotNull(mvcResult.getResponse().getCookie("cartContents"));
    }

    @Test
    @WithUserDetails("liza.shpinkovaa@mail.ru")
    void handleRemoveBookFromCartRequest() throws Exception {
        doReturn(mock(Book.class))
                .when(bookService)
                .findBookBySlug(anyString());
        mockMvc.perform(post("/cart/changeBookStatus/cart/remove/{slug}", "slug"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"))
                .andReturn();

        Mockito.verify(bookService, Mockito.times(1)).findBookBySlug(anyString());
        Mockito.verify(bookService, Mockito.times(1)).updateQuantityInBasket(anyString(), anyInt());
    }
}