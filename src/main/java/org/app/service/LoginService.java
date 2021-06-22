package org.app.service;

import org.apache.log4j.Logger;
import org.app.dto.LoginForm;
import org.app.dto.User;
import org.app.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LoginService {

    private Logger logger = Logger.getLogger(LoginService.class);

    private final ProjectRepository<User> userRepo;

    @Autowired
    public LoginService(ProjectRepository<User> userRepo) {
        this.userRepo = userRepo;
    }

    public boolean authenticate(LoginForm loginFrom) {
        logger.info("try auth with user-form: " + loginFrom);
        return userRepo.retrieveAll().stream().anyMatch(user ->
                user.getUsername().equals(loginFrom.getUsername())
                        && user.getPassword().equals(loginFrom.getPassword()));
    }

    public boolean register(User user) {
        logger.info("register user");
        if (Objects.nonNull(user)) {
            userRepo.save(user);
            return true;
        }
        return false;
    }
}
