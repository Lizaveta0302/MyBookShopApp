package com.example.bookshop_app.security;

import com.example.bookshop_app.entity.BookstoreUser;
import com.example.bookshop_app.repo.BookstoreUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BookstoreUserDetailsService implements UserDetailsService {

    private final BookstoreUserRepository bookstoreUserRepository;

    @Autowired
    public BookstoreUserDetailsService(BookstoreUserRepository bookstoreUserRepository) {
        this.bookstoreUserRepository = bookstoreUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        BookstoreUser bookstoreUser = bookstoreUserRepository.findBookstoreUserByEmail(s);
        if (bookstoreUser != null) {
            return new BookstoreUserDetails(bookstoreUser);
        }
        bookstoreUser = bookstoreUserRepository.findBookstoreUserByPhone(s);
        if (bookstoreUser != null) {
            return new PhoneNumberUserDetails(bookstoreUser);
        } else {
            throw new UsernameNotFoundException("user not found!");
        }
    }
}
