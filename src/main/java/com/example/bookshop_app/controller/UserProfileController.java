package com.example.bookshop_app.controller;

import com.example.bookshop_app.dto.SearchWordDto;
import com.example.bookshop_app.dto.form.UserProfileForm;
import com.example.bookshop_app.entity.BookstoreUser;
import com.example.bookshop_app.security.BookstoreUserDetails;
import com.example.bookshop_app.security.BookstoreUserRegister;
import com.example.bookshop_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class UserProfileController {

    @Autowired
    private BookstoreUserRegister userRegister;
    @Autowired
    private UserService userService;


    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }


    @GetMapping("/my")
    public String handleMy() {
        return "my";
    }

    @PostMapping("/profile/save")
    public String updateProfile(UserProfileForm profileForm) {
        Integer userId = null;
        Object curUser = userRegister.getCurrentUser();
        if (curUser instanceof BookstoreUserDetails) {
            userId = ((BookstoreUserDetails) curUser).getBookstoreUser().getId();
            userService.updateUserProfile(profileForm, userId);
        } else {
            userService.saveNewUser(profileForm);
        }
        return "redirect:/profile";
    }

    @GetMapping("/profile")
    public String handleProfile(Model model) {
        Object curUser = userRegister.getCurrentUser();
        BookstoreUser currentUser = new BookstoreUser();
        if (curUser instanceof BookstoreUserDetails) {
            currentUser = userService.getUserById(((BookstoreUserDetails) curUser).getBookstoreUser().getId());
        } else if (curUser instanceof DefaultOAuth2User) {
            currentUser.setName(((DefaultOAuth2User) curUser).getAttributes().get("name").toString());
            currentUser.setEmail(((DefaultOAuth2User) curUser).getAttributes().get("email").toString());
            currentUser.setPhone(Optional.ofNullable(((DefaultOAuth2User) curUser).getAttributes().get("phone"))
                    .map(Object::toString).orElse(null));
        }
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("profileForm", new UserProfileForm());
        return "profile";
    }
}
