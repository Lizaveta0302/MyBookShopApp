package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.dto.SearchWordDto;
import com.example.MyBookShopApp.service.GenresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/genres")
public class GenresController {

    private final GenresService genresService;

    @Autowired
    public GenresController(GenresService genresService) {
        this.genresService = genresService;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @GetMapping
    public String genresPage() {
        return "/genres/index";
    }

    @GetMapping("/slug")
    public String genresDescriptionPage() {
        return "/genres/slug";
    }
}
