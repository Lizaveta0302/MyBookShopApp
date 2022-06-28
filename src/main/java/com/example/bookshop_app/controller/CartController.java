package com.example.bookshop_app.controller;

import com.example.bookshop_app.dto.SearchWordDto;
import com.example.bookshop_app.entity.BalanceTransaction;
import com.example.bookshop_app.entity.BookstoreUser;
import com.example.bookshop_app.entity.book.Book;
import com.example.bookshop_app.entity.book.Status;
import com.example.bookshop_app.security.BookstoreUserDetails;
import com.example.bookshop_app.security.BookstoreUserRegister;
import com.example.bookshop_app.service.BalanceTransactionService;
import com.example.bookshop_app.service.BookService;
import com.example.bookshop_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Secured("ROLE_USER")
@RequestMapping("/cart")
public class CartController {

    private static final String IS_POSTPONED_LIST_EMPTY = "isPostponedListEmpty";
    private static final String POSTPONED_CONTENTS = "postponeContents";
    private static final String CART_PATH = "/cart";
    private static final String REDIRECT_BOOKS_SLUG_URL = "redirect:/books/slug/";
    private static final String REDIRECT_CART_URL = "redirect:/cart";
    private static final String CART_CONTENTS = "cartContents";
    private static final String IS_CART_EMPTY = "isCartEmpty";

    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookstoreUserRegister userRegister;
    @Autowired
    private BalanceTransactionService balanceTransactionService;

    @ModelAttribute(name = "bookCart")
    public List<Book> bookCart() {
        return new ArrayList<>();
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @GetMapping("/postponed")
    public String handlePostponedRequest(@CookieValue(value = POSTPONED_CONTENTS, required = false) String postponeContents, Model model) {
        if (postponeContents == null || postponeContents.equals("")) {
            model.addAttribute(IS_POSTPONED_LIST_EMPTY, true);
        } else {
            model.addAttribute(IS_POSTPONED_LIST_EMPTY, false);
            postponeContents = postponeContents.startsWith("/") ? postponeContents.substring(1) : postponeContents;
            postponeContents = postponeContents.endsWith("/") ? postponeContents.substring(0, postponeContents.length() - 1) : postponeContents;
            String[] cookieSlugs = postponeContents.split("/");
            List<Book> booksFromCookieSlug = bookService.findBooksBySlugIn(cookieSlugs);
            model.addAttribute("postponedBooks", booksFromCookieSlug);
        }
        return "/postponed";
    }

    @PostMapping("/changeBookStatus/postpone/{slug}")
    public String handleChangePostponedBookStatus(@PathVariable("slug") String slug, @CookieValue(name = POSTPONED_CONTENTS,
            required = false) String postponeContents, HttpServletResponse response, Model model) {
        if (postponeContents == null || postponeContents.equals("")) {
            Cookie cookie = new Cookie(POSTPONED_CONTENTS, slug);
            cookie.setPath(CART_PATH);
            response.addCookie(cookie);
            model.addAttribute(IS_POSTPONED_LIST_EMPTY, false);
        } else if (!postponeContents.contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(postponeContents).add(slug);
            Cookie cookie = new Cookie(POSTPONED_CONTENTS, stringJoiner.toString());
            cookie.setPath(CART_PATH);
            response.addCookie(cookie);
            model.addAttribute(IS_POSTPONED_LIST_EMPTY, false);
        }
        bookService.updateNumberOfPostponed(slug, bookService.findBookBySlug(slug).getNumberOfPostponed() + 1);
        return REDIRECT_BOOKS_SLUG_URL + slug;
    }

    @PostMapping("/changeBookStatus/archive/{slug}")
    public String handleChangeArchivedBookStatus(@PathVariable("slug") String slug) {
        Book book = bookService.findBookBySlug(slug);
        Object curUser = userRegister.getCurrentUser();
        BookstoreUser currentUser;
        if (curUser instanceof BookstoreUserDetails) {
            currentUser = userService.getUserById(((BookstoreUserDetails) curUser).getBookstoreUser().getId());
            if (Objects.nonNull(book) && Objects.nonNull(book.getStatus()) && book.getStatus().equals(Status.PAID)
                    && Optional.ofNullable(currentUser).map(BookstoreUser::getId).isPresent()
                    && balanceTransactionService.getTransactionHistoryByUserId(currentUser.getId()).stream()
                    .map(BalanceTransaction::getBookId).collect(Collectors.toList()).contains(book.getId())) {
                bookService.updateStatus(book.getId(), Status.ARCHIVED);
            }
        }
        return REDIRECT_BOOKS_SLUG_URL + slug;
    }

    @PostMapping("/changeBookStatus/postpone/remove/{slug}")
    public String handleRemovingBookFromPostponeRequest(@PathVariable("slug") String slug, @CookieValue(name = POSTPONED_CONTENTS,
            required = false) String postponeContents, HttpServletResponse response, Model model) {
        if (postponeContents != null && !postponeContents.equals("")) {
            splitCookie(slug, postponeContents, response);
            model.addAttribute(IS_POSTPONED_LIST_EMPTY, false);
        } else {
            model.addAttribute(IS_POSTPONED_LIST_EMPTY, true);
        }
        bookService.updateNumberOfPostponed(slug, bookService.findBookBySlug(slug).getNumberOfPostponed() - 1);
        return REDIRECT_CART_URL;
    }

    private void splitCookie(@PathVariable("slug") String slug, @CookieValue(name = POSTPONED_CONTENTS, required = false) String postponeContents, HttpServletResponse response) {
        List<String> cookieBooks = new ArrayList<>(Arrays.asList(postponeContents.split("/")));
        cookieBooks.remove(slug);
        Cookie cookie = new Cookie(POSTPONED_CONTENTS, String.join("/", cookieBooks));
        cookie.setPath(CART_PATH);
        response.addCookie(cookie);
    }

    @PostMapping("/changeBookStatus/cart/moveToPostponed/{slug}")
    public String handleMovingBookToPostponedFromCart(@PathVariable("slug") String slug,
                                                      @CookieValue(name = POSTPONED_CONTENTS, required = false) String postponeContents,
                                                      @CookieValue(value = CART_CONTENTS, required = false) String cartContents,
                                                      HttpServletResponse response, Model model) {
        if (cartContents != null && !cartContents.equals("")) {
            List<String> cookieCartBooks = new ArrayList<>(Arrays.asList(cartContents.split("/")));
            cookieCartBooks.remove(slug);
            Cookie cartCookie = new Cookie(CART_CONTENTS, String.join("/", cookieCartBooks));
            cartCookie.setPath("/");
            response.addCookie(cartCookie);
            List<String> cookiePostponedBooks = new ArrayList<>(Arrays.asList(postponeContents.split("/")));
            cookiePostponedBooks.add(slug);
            Cookie cookie = new Cookie(POSTPONED_CONTENTS, String.join("/", cookiePostponedBooks));
            cookie.setPath(CART_PATH);
            response.addCookie(cookie);
            model.addAttribute(IS_POSTPONED_LIST_EMPTY, false);
        } else {
            model.addAttribute(IS_CART_EMPTY, true);
        }
        Book book = bookService.findBookBySlug(slug);
        bookService.updateQuantityInBasketAndNumberOfPostponed(slug, book.getNumberOfPostponed() + 1, book.getQuantityInBasket() - 1);
        return REDIRECT_CART_URL;
    }

    @PostMapping("/changeBookStatus/postpone/moveToCart/{slug}")
    public String handleMovingToCartBookFromPostponed(@PathVariable("slug") String slug,
                                                      @CookieValue(name = POSTPONED_CONTENTS, required = false) String postponeContents,
                                                      @CookieValue(value = CART_CONTENTS, required = false) String cartContents,
                                                      HttpServletResponse response, Model model) {
        if (postponeContents != null && !postponeContents.equals("")) {
            splitCookie(slug, postponeContents, response);
            List<String> cookieCartBooks = new ArrayList<>(Arrays.asList(cartContents.split("/")));
            cookieCartBooks.add(slug);
            Cookie cartCookie = new Cookie(CART_CONTENTS, String.join("/", cookieCartBooks));
            cartCookie.setPath("/");
            response.addCookie(cartCookie);
            model.addAttribute(IS_CART_EMPTY, false);
        } else {
            model.addAttribute(IS_POSTPONED_LIST_EMPTY, true);
        }
        Book book = bookService.findBookBySlug(slug);
        bookService.updateQuantityInBasketAndNumberOfPostponed(slug, book.getNumberOfPostponed() - 1, book.getQuantityInBasket() + 1);
        return REDIRECT_CART_URL;
    }

    @PostMapping("/changeBookStatus/postpone/buyAllPostponed")
    public String handleMovingToCartAllPostponedBooks(@CookieValue(name = POSTPONED_CONTENTS, required = false) String postponeContents,
                                                      @CookieValue(value = CART_CONTENTS, required = false) String cartContents,
                                                      HttpServletResponse response, Model model) {
        if (postponeContents != null && !Objects.equals(postponeContents, "")) {
            List<String> cookiePostponedBooks = new ArrayList<>(Arrays.asList(postponeContents.split("/")));
            List<String> cookieCartBooks = new ArrayList<>(Arrays.asList(cartContents.split("/")));
            cookieCartBooks.addAll(cookiePostponedBooks);
            Cookie postponedCookie = new Cookie(POSTPONED_CONTENTS, "");
            postponedCookie.setPath(CART_PATH);
            response.addCookie(postponedCookie);
            Cookie cartCookie = new Cookie(CART_CONTENTS, String.join("/", cookieCartBooks));
            cartCookie.setPath("/");
            response.addCookie(cartCookie);
            model.addAttribute(IS_CART_EMPTY, false);
        }
        model.addAttribute(IS_POSTPONED_LIST_EMPTY, true);
        return REDIRECT_CART_URL;
    }

    @GetMapping
    public String handleCartRequest(@CookieValue(value = CART_CONTENTS, required = false) String cartContents, Model model) {
        if (cartContents == null || cartContents.equals("")) {
            model.addAttribute(IS_CART_EMPTY, true);
        } else {
            model.addAttribute(IS_CART_EMPTY, false);
            cartContents = cartContents.startsWith("/") ? cartContents.substring(1) : cartContents;
            cartContents = cartContents.endsWith("/") ? cartContents.substring(0, cartContents.length() - 1) : cartContents;
            String[] cookieSlugs = cartContents.split("/");
            List<Book> booksFromCookieSlug = bookService.findBooksBySlugIn(cookieSlugs);
            model.addAttribute("bookCart", booksFromCookieSlug);
        }
        return "cart";
    }

    @PostMapping("/changeBookStatus/{slug}")
    public String handleChangeBookStatus(@PathVariable("slug") String slug, @CookieValue(name = CART_CONTENTS,
            required = false) String cartContents, HttpServletResponse response, Model model) {
        if (cartContents == null || cartContents.equals("")) {
            Cookie cookie = new Cookie(CART_CONTENTS, slug);
            cookie.setPath("/");
            response.addCookie(cookie);
            model.addAttribute(IS_CART_EMPTY, false);
        } else if (!cartContents.contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(cartContents).add(slug);
            Cookie cookie = new Cookie(CART_CONTENTS, stringJoiner.toString());
            cookie.setPath("/");
            response.addCookie(cookie);
            model.addAttribute(IS_CART_EMPTY, false);
        }
        Book book = bookService.findBookBySlug(slug);
        if (Objects.nonNull(book)) {
            int quantityInBasket = book.getQuantityInBasket() == null ? 0 : book.getQuantityInBasket();
            bookService.updateQuantityInBasket(slug, quantityInBasket + 1);
        }
        return REDIRECT_BOOKS_SLUG_URL + slug;
    }

    @PostMapping("/changeBookStatus/cart/remove/{slug}")
    public String handleRemoveBookFromCartRequest(@PathVariable("slug") String slug, @CookieValue(name = CART_CONTENTS,
            required = false) String cartContents, HttpServletResponse response, Model model) {
        if (cartContents != null && !cartContents.isEmpty()) {
            List<String> cookieBooks = new ArrayList<>(Arrays.asList(cartContents.split("/")));
            cookieBooks.remove(slug);
            Cookie cookie = new Cookie(CART_CONTENTS, String.join("/", cookieBooks));
            cookie.setPath("/");
            response.addCookie(cookie);
            model.addAttribute(IS_CART_EMPTY, false);
        } else {
            model.addAttribute(IS_CART_EMPTY, true);
        }
        Book book = bookService.findBookBySlug(slug);
        if (Objects.nonNull(book)) {
            int quantityInBasket = book.getQuantityInBasket() == null ? 0 : book.getQuantityInBasket();
            bookService.updateQuantityInBasket(slug, quantityInBasket - 1);
        }
        return REDIRECT_CART_URL;
    }
}