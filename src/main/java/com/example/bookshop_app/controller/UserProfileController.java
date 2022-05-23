package com.example.bookshop_app.controller;

import com.example.bookshop_app.dto.SearchWordDto;
import com.example.bookshop_app.dto.TransactionsPageDto;
import com.example.bookshop_app.dto.form.UserProfileForm;
import com.example.bookshop_app.entity.BalanceTransaction;
import com.example.bookshop_app.entity.BookstoreUser;
import com.example.bookshop_app.entity.book.Book;
import com.example.bookshop_app.entity.book.Status;
import com.example.bookshop_app.exception.OutOfBalanceException;
import com.example.bookshop_app.security.BookstoreUserDetails;
import com.example.bookshop_app.security.BookstoreUserRegister;
import com.example.bookshop_app.service.BalanceTransactionService;
import com.example.bookshop_app.service.BookService;
import com.example.bookshop_app.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class UserProfileController {

    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookstoreUserRegister userRegister;
    @Autowired
    private BalanceTransactionService balanceTransactionService;


    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @GetMapping("/my")
    public String handleMy(Model model) {
        List<Book> purchasedBooks = new ArrayList<>();
        Object curUser = userRegister.getCurrentUser();
        BookstoreUser currentUser;
        if (curUser instanceof BookstoreUserDetails) {
            currentUser = userService.getUserById(((BookstoreUserDetails) curUser).getBookstoreUser().getId());
            if (Optional.ofNullable(currentUser).map(BookstoreUser::getId).isPresent()) {
                List<Integer> purchasedBooksIds = balanceTransactionService.getTransactionHistoryByUserId(currentUser.getId())
                        .stream()
                        .map(BalanceTransaction::getBookId)
                        .collect(Collectors.toList());
                purchasedBooks = bookService.getBooksByIds(purchasedBooksIds).stream()
                        .filter(b -> {
                            Status status = Optional.ofNullable(b.getStatus()).orElse(null);
                            return !Objects.isNull(status) && status.equals(Status.PAID);
                        })
                        .collect(Collectors.toList());
            }
        }
        model.addAttribute("myBooks", purchasedBooks);
        return "my";
    }

    @GetMapping("/myarchive")
    public String handleMyArchive(Model model) {
        List<Book> archiveBooks = new ArrayList<>();
        Object curUser = userRegister.getCurrentUser();
        BookstoreUser currentUser;
        if (curUser instanceof BookstoreUserDetails) {
            currentUser = userService.getUserById(((BookstoreUserDetails) curUser).getBookstoreUser().getId());
            if (Optional.ofNullable(currentUser).map(BookstoreUser::getId).isPresent()) {
                List<Integer> purchasedBooksIds = balanceTransactionService.getTransactionHistoryByUserId(currentUser.getId())
                        .stream()
                        .map(BalanceTransaction::getBookId)
                        .collect(Collectors.toList());
                archiveBooks = bookService.getBooksByIds(purchasedBooksIds).stream()
                        .filter(b -> !Objects.isNull(b.getStatus()) && b.getStatus().equals(Status.ARCHIVED))
                        .collect(Collectors.toList());
            }
        }
        model.addAttribute("archiveBooks", archiveBooks);
        return "myarchive";
    }

    @PostMapping("/profile/save")
    public String updateProfile(UserProfileForm profileForm) throws JsonProcessingException {
        userService.confirmChangingUserProfile(profileForm);
        return "redirect:/profile";
    }

    @GetMapping("/profile/verify/{token}")
    public String handleProfileVerification(@PathVariable String token) throws JsonProcessingException {
        userService.changeUserProfile(token);
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

    @GetMapping("/payment")
    public String handlePay(@CookieValue(value = "cartContents", required = false) String cartContents, HttpServletResponse response) throws OutOfBalanceException {
        cartContents = cartContents.startsWith("/") ? cartContents.substring(1) : cartContents;
        cartContents = cartContents.endsWith("/") ? cartContents.substring(0, cartContents.length() - 1) : cartContents;
        String[] cookieSlugs = cartContents.split("/");
        List<Book> booksFromCookieSlugs = bookService.findBooksBySlugIn(cookieSlugs);
        balanceTransactionService.buyAllBooksInCart(booksFromCookieSlugs);
        Cookie cookie = new Cookie("cartContents", "");
        response.addCookie(cookie);
        return "redirect:/cart";
    }
}
