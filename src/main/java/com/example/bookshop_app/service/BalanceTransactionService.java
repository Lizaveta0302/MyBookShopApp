package com.example.bookshop_app.service;

import com.example.bookshop_app.entity.BalanceTransaction;
import com.example.bookshop_app.entity.BookstoreUser;
import com.example.bookshop_app.entity.book.Book;
import com.example.bookshop_app.exception.OutOfBalanceException;
import com.example.bookshop_app.repo.BalanceTransactionRepository;
import com.example.bookshop_app.security.BookstoreUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BalanceTransactionService {

    @Autowired
    private UserService userService;
    @Autowired
    private BalanceTransactionRepository balanceTransactionRepository;

    public List<BalanceTransaction> getTransactionHistoryByUserId(Integer id) {
        return balanceTransactionRepository.findAllByUserId(id);
    }

    public Page<BalanceTransaction> getTransactionHistory(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return balanceTransactionRepository.findAllByOrderByTimeDesc(nextPage);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void buyAllBooksInCart(List<Book> booksFromCookieSlugs) throws OutOfBalanceException {
        BookstoreUserDetails principal = (BookstoreUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BookstoreUser user = principal.getBookstoreUser();
        Double userBalance = user.getBalance();
        for (Book book : booksFromCookieSlugs) {
            if (userBalance >= book.getPrice()) {
                balanceTransactionRepository.save(map(book, user));
                userBalance -= book.getPrice();
            } else {
                throw new OutOfBalanceException("Insufficient funds. Replenish the balance");
            }
        }
        userService.updateUserBalance(userBalance, user.getId());
    }

    private BalanceTransaction map(Book book, BookstoreUser currentUser) {
        BalanceTransaction balanceTransaction = new BalanceTransaction();
        balanceTransaction.setBookId(book.getId());
        balanceTransaction.setIncome(false);
        balanceTransaction.setTime(LocalDateTime.now());
        balanceTransaction.setUserId(currentUser.getId());
        balanceTransaction.setValue(book.getPrice().intValue());
        balanceTransaction.setDescription("Покупка книги " + book.getTitle());
        return balanceTransaction;
    }
}
