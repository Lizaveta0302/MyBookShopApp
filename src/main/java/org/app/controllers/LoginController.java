package org.app.controllers;

import org.apache.log4j.Logger;
import org.app.dto.LoginForm;
import org.app.dto.User;
import org.app.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final Logger logger = Logger.getLogger(LoginController.class);
    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        logger.info("GET /login returns login_page.html");
        model.addAttribute("loginForm", new LoginForm());
        return "login_page";
    }

    @GetMapping("/registerPage")
    public String getRegisterPage(Model model) {
        logger.info("GET /getRegisterPage returns registerPage.html");
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String register(User user) {
        if (loginService.register(user)) {
            logger.info("register OK redirect to login page");
            return "redirect:/login";
        } else {
            logger.info("register FAIL redirect back to registerPage");
            return "redirect:/registerPage";
        }
    }

    @PostMapping("/login/auth")
    public String authenticate(LoginForm loginFrom) {
        if (loginService.authenticate(loginFrom)) {
            logger.info("login OK redirect to book shelf");
            return "redirect:/books/shelf";
        } else {
            logger.info("login FAIL redirect back to login");
            return "redirect:/login";
        }
    }
}
