package com.example.bookshop_app.service;

import com.example.bookshop_app.dto.form.UserProfileForm;
import com.example.bookshop_app.entity.BookstoreUser;
import com.example.bookshop_app.repo.BookstoreUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private BookstoreUserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(BookstoreUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<BookstoreUser> getAllUsers() {
        return userRepository.findAll();
    }

    public BookstoreUser getUserById(Integer id) {
        return userRepository.findBookstoreUserById(id);
    }

    public BookstoreUser getUserByEmail(String email) {
        return userRepository.findBookstoreUserByEmail(email);
    }

    @Transactional
    public void saveNewUser(UserProfileForm profileForm) {
        BookstoreUser user = getUserByEmail(profileForm.getMail());
        if (Objects.isNull(user)) {
            BookstoreUser newUser = new BookstoreUser();
            newUser.setName(profileForm.getName());
            newUser.setEmail(profileForm.getMail());
            newUser.setPhone(profileForm.getPhone());
            newUser.setPassword(passwordEncoder.encode(profileForm.getPassword()));
            userRepository.save(newUser);
        } else {
            logger.info("User with email {} is already present in the system", profileForm.getMail());
        }
    }

    @Transactional
    public void updateUserProfile(UserProfileForm profileForm, Integer userId) {
        BookstoreUser user = userRepository.findBookstoreUserById(userId);
        if (Objects.nonNull(user)) {
            userRepository.updateUserProfile(profileForm.getName(), profileForm.getMail(), profileForm.getPhone(),
                    passwordEncoder.encode(profileForm.getPassword()), userId);
        } else {
            logger.info("User with id {} was not found", userId);
        }
    }

    public void updateUserBalance(Double userBalance, Integer userId) {
        userRepository.updateUserBalance(userBalance, userId);
    }
}
