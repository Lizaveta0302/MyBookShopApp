package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.dto.SearchWordDto;
import com.example.MyBookShopApp.entity.Author;
import com.example.MyBookShopApp.service.AuthorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/authors")
@Api(description = "authors data")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @GetMapping
    public String authorsPage(Model model) {
        model.addAttribute("authors", authorService.getFilteredAuthors());
        return "/authors/index";
    }

    @GetMapping("/{id}")
    public String getAuthorByAuthorId(@PathVariable String id, Model model) {
        model.addAttribute("author", authorService.getAuthorById(id));
        return "/authors/biography";
    }

    @ApiOperation("method to get map of authors")
    @GetMapping("/api/authors")
    @ResponseBody
    public Map<String, List<Author>> authors() {
        return authorService.getFilteredAuthors();
    }
}
