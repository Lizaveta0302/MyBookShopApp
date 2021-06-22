package org.app.controllers;

import org.apache.log4j.Logger;
import org.app.dto.Book;
import org.app.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(value = "/books")
public class BookShelfController {

    private final Logger logger = Logger.getLogger(BookShelfController.class);
    private final BookService bookService;

    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/shelf")
    public String books(Model model) {
        logger.info("got book shelf");
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(Book book) {
        bookService.saveBook(book);
        logger.info("current repository size: " + bookService.getAllBooks().size());
        return "redirect:/books/shelf";
    }

    @PostMapping("/remove")
    public String removeBook(@RequestParam(value = "bookField") String bookFieldToRemove,
                             @RequestParam(value = "bookFieldValue") String bookFieldValueToRemove) {
        logger.info("bookFieldToRemove: " + bookFieldToRemove + " " + "bookFieldValueToRemove: " + bookFieldValueToRemove);
        bookService.removeBookByField(bookFieldToRemove, bookFieldValueToRemove);
        return "redirect:/books/shelf";
    }

    @PostMapping("/filter")
    public String filterBooks(@RequestParam(value = "bookField", required = false) String bookFieldToFilter,
                              @RequestParam(value = "bookFieldValue", required = false) String bookFieldValueToFilter,
                              Model model) {
        if (bookFieldToFilter == null || bookFieldValueToFilter == null) {
            return "redirect:/books/shelf";
        }
        List<Book> filteredBooks = bookService.filterBooksByField(bookFieldToFilter, bookFieldValueToFilter);
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", filteredBooks);
        return "book_shelf";
    }
}
