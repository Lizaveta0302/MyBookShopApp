package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.entity.book.Book;
import com.example.MyBookShopApp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping
public class MainController {

    private final BookService bookService;

    @Autowired
    public MainController(BookService bookService) {
        this.bookService = bookService;
    }


    @ModelAttribute("recommendedBooks")
    public List<Book> recommendedBooks() {
        return bookService.getBooksData();
    }

    @GetMapping("/")
    public String indexPage() {
        return "redirect:/bookshop/main";
    }

    @GetMapping("/bookshop/main")
    public String mainPage() {
        return "index";
    }

    @GetMapping("/bookshop/documents")
    public String documents() {
        return "/documents/index";
    }

    @GetMapping("/bookshop/about")
    public String about() {
        return "/about";
    }

    @GetMapping("/bookshop/faq")
    public String faq() {
        return "/faq";
    }

    @GetMapping("/bookshop/contacts")
    public String contacts() {
        return "/contacts";
    }

}
