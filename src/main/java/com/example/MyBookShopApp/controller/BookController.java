package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.entity.Author;
import com.example.MyBookShopApp.entity.Book;
import com.example.MyBookShopApp.service.AuthorService;
import com.example.MyBookShopApp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;

    @Autowired
    public BookController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @ModelAttribute("popularBooks")
    public List<Book> popularBooks() {
        return bookService.getBooksData();
    }

    @ModelAttribute("recentBooks")
    public List<Book> recentrBooks() {
        return bookService.getBooksData();
    }

    @GetMapping("/recent")
    public String recentBooksPage() {
        return "/books/recent";
    }

    @GetMapping("/popular")
    public String popularBooksPage() {
        return "/books/popular";
    }

    @GetMapping("/author/{authorId}")
    public String allBooksByAuthorId(@PathVariable String authorId, Model model) {
        List<Book> books = bookService.getBooksByAuthorId(authorId);
        Author author = authorService.getAuthorById(authorId);
        model.addAttribute("authorName", author.getLastName().concat(" ").concat(author.getFirstName()));
        model.addAttribute("authorBooks", books);
        return "/books/author";
    }
}
