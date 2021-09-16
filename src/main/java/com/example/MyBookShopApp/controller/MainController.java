package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.entity.Book;
import com.example.MyBookShopApp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/bookshop")
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

    @GetMapping("/main")
    public String mainPage(Model model) {
        model.addAttribute("book", new Book());
        return "index";
    }

    @GetMapping("/documents")
    public String documents() {
        return "/documents/index";
    }

    @GetMapping("/about")
    public String about() {
        return "/about";
    }

    @GetMapping("/faq")
    public String faq() {
        return "/faq";
    }

    @GetMapping("/contacts")
    public String contacts() {
        return "/contacts";
    }

}
