package com.example.bookshop_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/signin")
    public String login() {
        return "/signin";
    }

    @GetMapping("/signup")
    public String signup() {
        return "/signup";
    }

}
