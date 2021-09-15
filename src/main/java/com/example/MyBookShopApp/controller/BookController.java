package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.entity.Book;
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

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("popularBooks")
    public List<Book> popularBooks() {
        return bookService.getBooksData();
    }

    @ModelAttribute("recentBooks")
    public List<Book> recentBooks() {
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
        model.addAttribute("authorBooks", bookService.getBooksByAuthorId(authorId));
        return "/books/author";
    }

    @GetMapping("/{id}")
    public String BooksById(@PathVariable String id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        return "/books/slug";
    }
}
