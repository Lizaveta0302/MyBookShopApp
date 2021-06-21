package org.app.repos;

import org.apache.log4j.Logger;
import org.app.dto.Book;
import org.app.dto.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository implements ProjectRepository<User> {

    private final Logger logger = Logger.getLogger(UserRepository.class);
    private final List<User> userRepo = new ArrayList<>();

    @Override
    public List<User> retrieveAll() {
        return new ArrayList<>(userRepo);
    }

    @Override
    public void save(User user) {
        user.setId(user.hashCode());
        user.setUsername(user.getUsername());
        user.setPassword(user.getPassword());
        logger.info("save new user: " + user);
        userRepo.add(user);
    }

    @Override
    public void removeItemByField(String userField, String userValue) {
        userRepo.remove(userField);
    }

    @Override
    public List<User> filterItemsByField(String itemField, String itemValue) {
        return new ArrayList<>();
    }
}
