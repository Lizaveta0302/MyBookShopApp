package com.example.bookshop_app.controller;

import com.example.bookshop_app.dto.BooksPageDto;
import com.example.bookshop_app.dto.SearchWordDto;
import com.example.bookshop_app.entity.BookstoreUser;
import com.example.bookshop_app.entity.Tag;
import com.example.bookshop_app.entity.book.Book;
import com.example.bookshop_app.exception.EmptySearchException;
import com.example.bookshop_app.security.BookstoreUserDetails;
import com.example.bookshop_app.security.BookstoreUserRegister;
import com.example.bookshop_app.service.BookService;
import com.example.bookshop_app.service.TagService;
import com.example.bookshop_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping
public class MainController {

    private final BookService bookService;
    private final TagService tagService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookstoreUserRegister userRegister;

    @Autowired
    public MainController(BookService bookService, TagService tagService) {
        this.bookService = bookService;
        this.tagService = tagService;
    }

    @ModelAttribute("recommendedBooks")
    public List<Book> recommendedBooks() {
        return bookService.getPageOfRecommendedBooks(0, 6).getContent();
    }

    @ModelAttribute("popularBooks")
    public List<Book> popularBooks() {
        return bookService.getPageOfPopularBooks(0, 6).getContent();
    }

    @ModelAttribute("recentBooks")
    public List<Book> recentBooks() {
        return bookService.getPageOfRecentBooks(0, 6).getContent();
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("searchResults")
    public List<Book> searchResults() {
        return new ArrayList<>();
    }

    @ModelAttribute("allTags")
    public List<Tag> allTags() {
        return tagService.getAllTags();
    }

    @GetMapping(value = {"/", "/main/{partOfPage}"})
    public String indexPage(@PathVariable(value = "partOfPage", required = false) String partOfPage) {
        if (Objects.nonNull(partOfPage)) {
            return "redirect:/bookshop/main#".concat(partOfPage);
        }
        return "redirect:/bookshop/main";
    }

    @GetMapping("/bookshop/main")
    public String mainPage(Model model) {
        Object curUser = userRegister.getCurrentUser();
        BookstoreUser currentUser = null;
        if (curUser instanceof BookstoreUserDetails) {
            currentUser = userService.getUserById(((BookstoreUserDetails) curUser).getBookstoreUser().getId());
        }
        model.addAttribute("currentUser", currentUser);
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

    @GetMapping(value = {"/search", "/search/{searchWord}"})
    public String getSearchResult(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
                                  Model model) throws EmptySearchException {
        if (Objects.nonNull(searchWordDto)) {
            model.addAttribute("searchWordDto", searchWordDto);
            model.addAttribute("searchResults", bookService.getPageOfGoogleBooksApiSearchResult(searchWordDto.getExample(), 0, 20));
            return "/search/index";
        } else {
            throw new EmptySearchException("Search is impossible by null");
        }
    }

    @GetMapping("/search/page/{searchWord}")
    @ResponseBody
    public BooksPageDto getNextSearchPage(@RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit,
                                          @PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto) {
        return new BooksPageDto(bookService.getPageOfGoogleBooksApiSearchResult(searchWordDto.getExample(), offset, limit));
    }
}
