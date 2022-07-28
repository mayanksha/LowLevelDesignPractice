package com.practice.lld.splitwise.service;

import com.practice.lld.splitwise.exception.DuplicateUserException;
import com.practice.lld.splitwise.exception.UserNotFoundException;
import com.practice.lld.splitwise.model.User;
import com.practice.lld.splitwise.repository.UserRepository;

public class UserService {
    private UserRepository userRepository = UserRepository.getInstance();

    public String createUser(String name) throws DuplicateUserException {
        User user = new User(name);
        userRepository.insertUserForId(user.getId(), user);

        return user.getId();
    }

    public User getUser(String userId) throws UserNotFoundException {
        return userRepository.getUserForId(userId);
    }

    private static UserService instance;
    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    private UserService() {
    }
}
