package com.example.bookshop_app.controller;

import com.example.bookshop_app.dto.SearchWordDto;
import com.example.bookshop_app.dto.TransactionsPageDto;
import com.example.bookshop_app.dto.form.UserProfileForm;
import com.example.bookshop_app.entity.BalanceTransaction;
import com.example.bookshop_app.entity.BookstoreUser;
import com.example.bookshop_app.security.BookstoreUserDetails;
import com.example.bookshop_app.security.BookstoreUserRegister;
import com.example.bookshop_app.service.BalanceTransactionService;
import com.example.bookshop_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class UserProfileController {

    @Autowired
    private UserService userService;
    @Autowired
    private BalanceTransactionService balanceTransactionService;
    @Autowired
    private BookstoreUserRegister userRegister;


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
        Integer userId;
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
            currentUser = userService.getUserByEmail(((DefaultOAuth2User) curUser).getAttributes().get("email").toString());
            if (Objects.isNull(currentUser)) {
                BookstoreUser newUser = new BookstoreUser();
                newUser.setName(((DefaultOAuth2User) curUser).getAttributes().get("name").toString());
                newUser.setEmail(((DefaultOAuth2User) curUser).getAttributes().get("email").toString());
                newUser.setPhone(Optional.ofNullable(((DefaultOAuth2User) curUser).getAttributes().get("phone"))
                        .map(Object::toString).orElse(null));
            }
        }
        List<BalanceTransaction> transactionHistory = new ArrayList<>();
        if (Optional.ofNullable(currentUser).map(BookstoreUser::getId).isPresent()) {
            transactionHistory = balanceTransactionService.getTransactionHistoryByUserId(currentUser.getId());
        }
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("profileForm", new UserProfileForm());
        model.addAttribute("transactionHistory", transactionHistory);
        return "profile";
    }

    @GetMapping("/profile/transactions")
    @ResponseBody
    public TransactionsPageDto getTransactionsHistory(@RequestParam("sort") String sort, @RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {
        return new TransactionsPageDto(balanceTransactionService.getTransactionHistory(offset, limit).getContent());
    }
}
