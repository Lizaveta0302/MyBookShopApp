package com.example.bookshop_app.security;

import com.example.bookshop_app.dto.SearchWordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
public class AuthUserController {

    private final BookstoreUserRegister userRegister;

    @Autowired
    public AuthUserController(BookstoreUserRegister userRegister) {
        this.userRegister = userRegister;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @GetMapping("/signin")
    public String handleSignin() {
        return "signin";
    }

    @GetMapping("/signup")
    public String handleSignUp(Model model) {
        model.addAttribute("regForm", new RegistrationForm());
        return "signup";
    }

    @PostMapping("/requestContactConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestContactConfirmation(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/reg")
    public String handleUserRegistration(RegistrationForm registrationForm, Model model) {
        userRegister.registerNewUser(registrationForm);
        model.addAttribute("regOk", true);
        return "signin";
    }

    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload, HttpServletResponse httpServletResponse) {
        ContactConfirmationResponse response = userRegister.jwtLogin(payload);
        Cookie cookie = new Cookie("token", response.getResult());
        httpServletResponse.addCookie(cookie);
        return response;
    }

    @GetMapping("/my")
    public String handleMy() {
        return "my";
    }

    @GetMapping("/profile")
    public String handleProfile(Model model) {
        Object curUser = userRegister.getCurrentUser();
        String curUsrName = null;
        String curUsrEmail = null;
        String curUsrPhone = null;
        if (curUser instanceof BookstoreUserDetails) {
            curUsrName = ((BookstoreUserDetails) curUser).getBookstoreUser().getName();
            curUsrEmail = ((BookstoreUserDetails) curUser).getBookstoreUser().getName();
            curUsrPhone = ((BookstoreUserDetails) curUser).getBookstoreUser().getName();
        } else if (curUser instanceof DefaultOAuth2User) {
            curUsrName = ((DefaultOAuth2User) curUser).getAttributes().get("name").toString();
            curUsrEmail = ((DefaultOAuth2User) curUser).getAttributes().get("email").toString();
            curUsrPhone = Optional.ofNullable(((DefaultOAuth2User) curUser).getAttributes().get("phone"))
                    .map(Object::toString).orElse(null);
        }
        model.addAttribute("curUsrName", curUsrName);
        model.addAttribute("curUsrEmail", curUsrEmail);
        model.addAttribute("curUsrPhone", curUsrPhone);
        return "profile";
    }
}
