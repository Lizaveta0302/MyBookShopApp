package com.example.bookshop_app.controller;

import com.example.bookshop_app.dto.SearchWordDto;
import com.example.bookshop_app.entity.book.Book;
import com.example.bookshop_app.service.BookService;
import com.example.bookshop_app.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Controller
@Secured("ROLE_USER")
@RequestMapping("/pay")
public class PaymentController {

    @Autowired
    private BookService bookService;
    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public RedirectView handlePay(@CookieValue(value = "cartContents", required = false) String cartContents) throws NoSuchAlgorithmException {
        cartContents = cartContents.startsWith("/") ? cartContents.substring(1) : cartContents;
        cartContents = cartContents.endsWith("/") ? cartContents.substring(0, cartContents.length() - 1) : cartContents;
        String[] cookieSlugs = cartContents.split("/");
        List<Book> booksFromCookieSlugs = bookService.findBooksBySlugIn(cookieSlugs);
        String paymentUrl = paymentService.getPaymentUrl(booksFromCookieSlugs);
        return new RedirectView(paymentUrl);
    }
}
