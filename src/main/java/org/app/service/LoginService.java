package org.app.service;

import org.apache.log4j.Logger;
import org.app.dto.User;
import org.app.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LoginService {
    private final Logger logger = Logger.getLogger(LoginService.class);

    private final ProjectRepository<User> userRepo;

    @Autowired
    public LoginService(ProjectRepository<User> userRepo) {
        this.userRepo = userRepo;
    }

    public boolean authenticate(User user) {
        logger.info("try auth with user-form: " + user);
        return userRepo.retrieveAll().stream().anyMatch(u ->
                u.getUsername().equals(user.getUsername())
                        && u.getPassword().equals(user.getPassword()));
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
