package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.service.AuthorService;
import com.example.MyBookShopApp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final BookService bookService;

    @Autowired
    public AuthorController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @GetMapping
    public String authorsPage(Model model) {
        model.addAttribute("authors", authorService.getFilteredAuthors());
        return "/authors/index";
    }

    @GetMapping("/{id}")
    public String countOfBooksByAuthorId(@PathVariable String id, Model model) {
        model.addAttribute("author", authorService.getAuthorById(id));
        model.addAttribute("amountOfBooks", bookService.getBooksByAuthorId(id).size());
        return "/authors/biography";
    }

}
