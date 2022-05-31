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
@RequestMapping("/cart")
public class CartController {

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
    public String handlePostponedRequest(@CookieValue(value = "postponeContents", required = false) String postponeContents, Model model) {
        if (postponeContents == null || postponeContents.equals("")) {
            model.addAttribute("isPostponedListEmpty", true);
        } else {
            model.addAttribute("isPostponedListEmpty", false);
            postponeContents = postponeContents.startsWith("/") ? postponeContents.substring(1) : postponeContents;
            postponeContents = postponeContents.endsWith("/") ? postponeContents.substring(0, postponeContents.length() - 1) : postponeContents;
            String[] cookieSlugs = postponeContents.split("/");
            List<Book> booksFromCookieSlug = bookService.findBooksBySlugIn(cookieSlugs);
            model.addAttribute("postponedBooks", booksFromCookieSlug);
        }
        Object curUser = userRegister.getCurrentUser();
        BookstoreUser currentUser = null;
        if (curUser instanceof BookstoreUserDetails) {
            currentUser = userService.getUserById(((BookstoreUserDetails) curUser).getBookstoreUser().getId());
        }
        model.addAttribute("currentUser", currentUser);
        return "/postponed";
    }

    @PostMapping("/changeBookStatus/postpone/{slug}")
    public String handleChangePostponedBookStatus(@PathVariable("slug") String slug, @CookieValue(name = "postponeContents",
            required = false) String postponeContents, HttpServletResponse response, Model model) {
        if (postponeContents == null || postponeContents.equals("")) {
            Cookie cookie = new Cookie("postponeContents", slug);
            cookie.setPath("/cart");
            response.addCookie(cookie);
            model.addAttribute("isPostponedListEmpty", false);
        } else if (!postponeContents.contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(postponeContents).add(slug);
            Cookie cookie = new Cookie("postponeContents", stringJoiner.toString());
            cookie.setPath("/cart");
            response.addCookie(cookie);
            model.addAttribute("isPostponedListEmpty", false);
        }
        bookService.updateNumberOfPostponed(slug, bookService.findBookBySlug(slug).getNumberOfPostponed() + 1);
        return "redirect:/books/slug/" + slug;
    }

    @Secured("ROLE_USER")
    @PostMapping("/changeBookStatus/archive")
    public String handleChangeArchivedBookStatus(@RequestBody Map<String, String> payload) {
        Book book = bookService.findBookBySlug(payload.get("booksIds"));
        Object curUser = userRegister.getCurrentUser();
        BookstoreUser currentUser;
        if (curUser instanceof BookstoreUserDetails) {
            currentUser = userService.getUserById(((BookstoreUserDetails) curUser).getBookstoreUser().getId());
            if (Objects.nonNull(book) && Objects.nonNull(book.getStatus())
                    && Optional.ofNullable(currentUser).map(BookstoreUser::getId).isPresent()
                    && balanceTransactionService.getTransactionHistoryByUserId(currentUser.getId()).stream()
                    .map(BalanceTransaction::getBookId).collect(Collectors.toList()).contains(book.getId())) {
                if (book.getStatus().equals(Status.PAID)) {
                    bookService.updateStatus(book.getId(), Status.ARCHIVED);
                } else {
                    bookService.updateStatus(book.getId(), Status.PAID);
                }
            }
        }
        return "redirect:/books/slug/" + payload.get("booksIds");
    }

    @PostMapping("/changeBookStatus/postpone/remove/{slug}")
    public String handleRemovingBookFromPostponeRequest(@PathVariable("slug") String slug, @CookieValue(name = "postponeContents",
            required = false) String postponeContents, HttpServletResponse response, Model model) {
        if (postponeContents != null || !postponeContents.equals("")) {
            List<String> cookieBooks = new ArrayList<>(Arrays.asList(postponeContents.split("/")));
            cookieBooks.remove(slug);
            Cookie cookie = new Cookie("postponeContents", String.join("/", cookieBooks));
            cookie.setPath("/cart");
            response.addCookie(cookie);
            model.addAttribute("isPostponedListEmpty", false);
        } else {
            model.addAttribute("isPostponedListEmpty", true);
        }
        bookService.updateNumberOfPostponed(slug, bookService.findBookBySlug(slug).getNumberOfPostponed() - 1);
        return "redirect:/cart";
    }

    @PostMapping("/changeBookStatus/cart/moveToPostponed/{slug}")
    public String handleMovingBookToPostponedFromCart(@PathVariable("slug") String slug,
                                                      @CookieValue(name = "postponeContents", required = false) String postponeContents,
                                                      @CookieValue(value = "cartContents", required = false) String cartContents,
                                                      HttpServletResponse response, Model model) {
        if (cartContents != null || !cartContents.equals("")) {
            List<String> cookieCartBooks = new ArrayList<>(Arrays.asList(cartContents.split("/")));
            cookieCartBooks.remove(slug);
            Cookie cartCookie = new Cookie("cartContents", String.join("/", cookieCartBooks));
            cartCookie.setPath("/");
            response.addCookie(cartCookie);
            List<String> cookiePostponedBooks = new ArrayList<>(Arrays.asList(postponeContents.split("/")));
            cookiePostponedBooks.add(slug);
            Cookie cookie = new Cookie("postponeContents", String.join("/", cookiePostponedBooks));
            cookie.setPath("/cart");
            response.addCookie(cookie);
            model.addAttribute("isPostponedListEmpty", false);
        } else {
            model.addAttribute("isCartEmpty", true);
        }
        Book book = bookService.findBookBySlug(slug);
        bookService.updateQuantityInBasketAndNumberOfPostponed(slug, book.getNumberOfPostponed() + 1, book.getQuantityInBasket() - 1);
        return "redirect:/cart";
    }

    @PostMapping("/changeBookStatus/postpone/moveToCart/{slug}")
    public String handleMovingToCartBookFromPostponed(@PathVariable("slug") String slug,
                                                      @CookieValue(name = "postponeContents", required = false) String postponeContents,
                                                      @CookieValue(value = "cartContents", required = false) String cartContents,
                                                      HttpServletResponse response, Model model) {
        if (postponeContents != null || !postponeContents.equals("")) {
            List<String> cookiePostponedBooks = new ArrayList<>(Arrays.asList(postponeContents.split("/")));
            cookiePostponedBooks.remove(slug);
            Cookie cookie = new Cookie("postponeContents", String.join("/", cookiePostponedBooks));
            cookie.setPath("/cart");
            response.addCookie(cookie);
            List<String> cookieCartBooks = new ArrayList<>(Arrays.asList(cartContents.split("/")));
            cookieCartBooks.add(slug);
            Cookie cartCookie = new Cookie("cartContents", String.join("/", cookieCartBooks));
            cartCookie.setPath("/");
            response.addCookie(cartCookie);
            model.addAttribute("isCartEmpty", false);
        } else {
            model.addAttribute("isPostponedListEmpty", true);
        }
        Book book = bookService.findBookBySlug(slug);
        bookService.updateQuantityInBasketAndNumberOfPostponed(slug, book.getNumberOfPostponed() - 1, book.getQuantityInBasket() + 1);
        return "redirect:/cart";
    }

    @Secured("ROLE_USER")
    @PostMapping("/changeBookStatus/postpone/buyAllPostponed")
    public String handleMovingToCartAllPostponedBooks(@CookieValue(name = "postponeContents", required = false) String postponeContents,
                                                      @CookieValue(value = "cartContents", required = false) String cartContents,
                                                      HttpServletResponse response, Model model) {
        if (postponeContents != null || !postponeContents.equals("")) {
            List<String> cookiePostponedBooks = new ArrayList<>(Arrays.asList(postponeContents.split("/")));
            List<String> cookieCartBooks = new ArrayList<>(Arrays.asList(cartContents.split("/")));
            cookieCartBooks.addAll(cookiePostponedBooks);
            Cookie postponedCookie = new Cookie("postponeContents", "");
            postponedCookie.setPath("/cart");
            response.addCookie(postponedCookie);
            Cookie cartCookie = new Cookie("cartContents", String.join("/", cookieCartBooks));
            cartCookie.setPath("/");
            response.addCookie(cartCookie);
            model.addAttribute("isCartEmpty", false);
        }
        model.addAttribute("isPostponedListEmpty", true);
        return "redirect:/cart";
    }

    @GetMapping
    public String handleCartRequest(@CookieValue(value = "cartContents", required = false) String cartContents, Model model) {
        if (cartContents == null || cartContents.equals("")) {
            model.addAttribute("isCartEmpty", true);
        } else {
            model.addAttribute("isCartEmpty", false);
            cartContents = cartContents.startsWith("/") ? cartContents.substring(1) : cartContents;
            cartContents = cartContents.endsWith("/") ? cartContents.substring(0, cartContents.length() - 1) : cartContents;
            String[] cookieSlugs = cartContents.split("/");
            List<Book> booksFromCookieSlug = bookService.findBooksBySlugIn(cookieSlugs);
            model.addAttribute("bookCart", booksFromCookieSlug);
            model.addAttribute("totalPrice", booksFromCookieSlug.stream().mapToDouble(Book::getPrice).sum());
            model.addAttribute("totalOldPrice", booksFromCookieSlug.stream().mapToDouble(Book::getPriceOld).sum());
        }
        Object curUser = userRegister.getCurrentUser();
        BookstoreUser currentUser = null;
        if (curUser instanceof BookstoreUserDetails) {
            currentUser = userService.getUserById(((BookstoreUserDetails) curUser).getBookstoreUser().getId());
        }
        model.addAttribute("currentUser", currentUser);
        return "cart";
    }

    @PostMapping("/changeBookStatus/{slug}")
    public String handleChangeBookStatus(@PathVariable("slug") String slug, @CookieValue(name = "cartContents",
            required = false) String cartContents, HttpServletResponse response, Model model) {
        if (cartContents == null || cartContents.equals("")) {
            Cookie cookie = new Cookie("cartContents", slug);
            cookie.setPath("/");
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);
        } else if (!cartContents.contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(cartContents).add(slug);
            Cookie cookie = new Cookie("cartContents", stringJoiner.toString());
            cookie.setPath("/");
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);
        }
        Book book = bookService.findBookBySlug(slug);
        if (Objects.nonNull(book)) {
            int quantityInBasket = book.getQuantityInBasket() == null ? 0 : book.getQuantityInBasket();
            bookService.updateQuantityInBasket(slug, quantityInBasket + 1);
        }
        return "redirect:/books/slug/" + slug;
    }

    @PostMapping("/changeBookStatus/cart/remove/{slug}")
    public String handleRemoveBookFromCartRequest(@PathVariable("slug") String slug, @CookieValue(name = "cartContents",
            required = false) String cartContents, HttpServletResponse response, Model model) {
        if (cartContents != null && !cartContents.isEmpty()) {
            List<String> cookieBooks = new ArrayList<>(Arrays.asList(cartContents.split("/")));
            cookieBooks.remove(slug);
            Cookie cookie = new Cookie("cartContents", String.join("/", cookieBooks));
            cookie.setPath("/");
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);
        } else {
            model.addAttribute("isCartEmpty", true);
        }
        Book book = bookService.findBookBySlug(slug);
        if (Objects.nonNull(book)) {
            int quantityInBasket = book.getQuantityInBasket() == null ? 0 : book.getQuantityInBasket();
            bookService.updateQuantityInBasket(slug, quantityInBasket - 1);
        }
        return "redirect:/cart";
    }
}
