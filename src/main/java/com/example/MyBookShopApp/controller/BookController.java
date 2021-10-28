package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.dto.BooksPageDto;
import com.example.MyBookShopApp.dto.SearchWordDto;
import com.example.MyBookShopApp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @GetMapping("/recent/page")
    public String getRecentBooksPage(Model model) {
        BooksPageDto recentBooksPageDto = new BooksPageDto(bookService.getPageOfRecentBooks(0, 6).getContent());
        model.addAttribute("recentBooks", recentBooksPageDto.getBooks());
        return "/books/recent";
    }

    @GetMapping("/popular/page")
    public String getPopularBooksPage(Model model) {
        BooksPageDto popularBooksPageDto = new BooksPageDto(bookService.getPageOfPopularBooks(0, 6).getContent());
        model.addAttribute("popularBooks", popularBooksPageDto.getBooks());
        return "/books/popular";
    }

    @GetMapping("/recommended")
    @ResponseBody
    public BooksPageDto getRecommendedBooksPage(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit, Model model) {
        return new BooksPageDto(bookService.getPageOfRecommendedBooks(offset, limit).getContent());
    }

    @GetMapping("/recent")
    @ResponseBody
    public BooksPageDto getRecentBooksPage(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfRecentBooks(offset, limit).getContent());
    }

    @GetMapping("/popular")
    @ResponseBody
    public BooksPageDto getPopularBooksPage(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfPopularBooks(offset, limit).getContent());
    }

    @GetMapping("/author/{authorId}")
    @ResponseBody
    public BooksPageDto allPageOfBooksByAuthorId(@PathVariable String authorId, @RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getBooksByAuthorId(offset, limit, authorId).getContent());
    }

    @GetMapping("/authors/{authorId}")
    public String getAllBooksByAuthorId(@PathVariable String authorId, Model model) {
        model.addAttribute("authorBooks", bookService.getBooksByAuthorId(0, 6, authorId).getContent());
        return "/books/author";
    }

    @GetMapping("/{id}")
    public String getBookById(@PathVariable String id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        return "/books/slug";
    }
}
