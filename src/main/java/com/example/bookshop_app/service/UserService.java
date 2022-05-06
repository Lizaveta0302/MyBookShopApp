package com.example.bookshop_app.service;

import com.example.bookshop_app.dto.form.UserProfileForm;
import com.example.bookshop_app.entity.BookstoreUser;
import com.example.bookshop_app.repo.BookstoreUserRepository;
import com.example.bookshop_app.security.BookstoreUserDetails;
import com.example.bookshop_app.security.BookstoreUserRegister;
import com.example.bookshop_app.util.VerifyTokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private BookstoreUserRegister userRegister;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private VerifyTokenUtil verifyTokenUtil;
    private BookstoreUserRepository userRepository;

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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void changeUserProfile(String token) throws JsonProcessingException {
        Integer userId;
        Object curUser = userRegister.getCurrentUser();
        userId = ((BookstoreUserDetails) curUser).getBookstoreUser().getId();
        UserProfileForm profileForm = verifyTokenUtil.extractProfileForm(token);
        updateUserProfile(profileForm, userId);
    }

    public void confirmChangingUserProfile(UserProfileForm profileForm) throws JsonProcessingException {
        Object curUser = userRegister.getCurrentUser();
        if (curUser instanceof BookstoreUserDetails) {
            confirmChanges(profileForm);
        } else {
            saveNewUser(profileForm);
        }
    }

    private void confirmChanges(UserProfileForm profileForm) throws JsonProcessingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("bookstoreapp@mail.ru");
        message.setTo(profileForm.getMail());
        message.setSubject("User profile update verification!");
        String token = verifyTokenUtil.generateToken(profileForm);
        message.setText("Verification link is: " + "http://localhost:8080/profile/verify/" + token + " please, follow it.");
        javaMailSender.send(message);
    }
}
