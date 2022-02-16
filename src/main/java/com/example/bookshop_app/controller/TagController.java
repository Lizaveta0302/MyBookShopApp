package com.example.bookshop_app.controller;

import com.example.bookshop_app.service.TagService;
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
