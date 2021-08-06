package org.app.controller;

import org.apache.log4j.Logger;
import org.app.dto.Book;
import org.app.dto.BookFieldValueDto;
import org.app.exception.FilterOrRemoveByFieldException;
import org.app.exception.UploadFileException;
import org.app.repository.ProcessFileService;
import org.app.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@Validated
@RequestMapping(value = "/books")
public class BookShelfController {

    private final Logger logger = Logger.getLogger(BookShelfController.class);
    private final BookService bookService;
    private final ProcessFileService fileService;

    @Autowired
    public BookShelfController(BookService bookService, ProcessFileService fileService) {
        this.bookService = bookService;
        this.fileService = fileService;
    }

    @GetMapping("/shelf")
    public String books(Model model) {
        logger.info("got book shelf");
        model.addAttribute("book", new Book());
        model.addAttribute("bookFieldValue", new BookFieldValueDto());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(@Valid Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            model.addAttribute("bookFieldValue", new BookFieldValueDto());
            model.addAttribute("bookList", bookService.getAllBooks());
            logger.info("errors during book saving");
            return "book_shelf";
        } else {
            bookService.saveBook(book);
            logger.info("current repository size: " + bookService.getAllBooks().size());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/filter")
    public String filterBooks(@RequestParam(value = "bookField", required = true) String bookFieldToFilter,
                              @RequestParam(value = "bookFieldValue", required = true) String bookFieldValueToFilter,
                              Model model) throws FilterOrRemoveByFieldException {
        if (bookFieldToFilter == null || bookFieldValueToFilter == null) {
            return "redirect:/books/shelf";
        }
        List<Book> filteredBooks = bookService.filterBooksByField(bookFieldToFilter, bookFieldValueToFilter);
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", filteredBooks);
        return "book_shelf";
    }

    @PostMapping("/remove")
    public String removeBook(@RequestParam(value = "bookField") String bookFieldToRemove,
                             @Valid BookFieldValueDto bookFieldValueToRemove,
                             BindingResult bindingResult, Model model) throws FilterOrRemoveByFieldException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookFieldValue", bookFieldValueToRemove);
            model.addAttribute("bookList", bookService.getAllBooks());
            logger.info("errors during book removing");
            return "book_shelf";
        } else {
            logger.info("bookFieldToRemove: " + bookFieldToRemove + " " + "bookFieldValueToRemove: " + bookFieldValueToRemove);
            bookService.removeBookByField(bookFieldToRemove, bookFieldValueToRemove);
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        fileService.uploadFile(file);
        return "redirect:/books/shelf";
    }

    @ResponseBody
    @PostMapping("/downloadFile")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (Objects.isNull(file) || file.isEmpty()) {
            throw new UploadFileException("File download error, file is empty. Choose the correct file, please");
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(file.getBytes());
    }
}
