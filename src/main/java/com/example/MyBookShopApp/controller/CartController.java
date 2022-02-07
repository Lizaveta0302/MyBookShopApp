package com.example.MyBookShopApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartController {

    @GetMapping("/postponed")
    public String postponedPage() {
        return "/postponed";
    }

    @GetMapping
    public String genresDescriptionPage() {
        return "/cart";
    }
}
