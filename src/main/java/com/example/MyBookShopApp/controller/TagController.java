package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/tags")
    public String getAllTags(Model model) {
        model.addAttribute("allTags", tagService.getAllTags(0, 6).getContent());
        return "/bookshop/main";
    }
}
