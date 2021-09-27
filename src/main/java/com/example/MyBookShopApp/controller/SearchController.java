package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.entity.book.Book;
import com.example.MyBookShopApp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/search")
public class SearchController {

    private final BookService bookService;

    @Autowired
    public SearchController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public String search(@ModelAttribute Book book, Model model) {
        model.addAttribute("foundBooks", bookService.getBooksByTitle(book.getTitle()));
        return "redirect:/search/index";
    }

    @GetMapping
    public String search() {
        return "/search/index";
    }
}
