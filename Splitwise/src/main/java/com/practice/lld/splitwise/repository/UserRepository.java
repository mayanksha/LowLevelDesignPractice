package com.practice.lld.splitwise.repository;

import com.practice.lld.splitwise.exception.DuplicateUserException;
import com.practice.lld.splitwise.exception.UserNotFoundException;
import com.practice.lld.splitwise.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private final Map<String, User> db = new HashMap<>();

    public User getUserForId(String Id) throws UserNotFoundException {
        if (!db.containsKey(Id)) {
            throw new UserNotFoundException(Id);
        }

        return db.get(Id);
    }

    public void insertUserForId(String Id, User user) throws DuplicateUserException {
        if (db.containsKey(Id)) {
            throw new DuplicateUserException(Id);
        }

        db.put(Id, user);
    }

    private static UserRepository instance;

    public static UserRepository getInstance() {
        if (instance == null)
            instance = new UserRepository();
        return instance;
    }

    private UserRepository() {

    }

}
