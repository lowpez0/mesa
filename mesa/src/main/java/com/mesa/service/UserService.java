package com.mesa.service;

import com.mesa.model.User;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private List<User> users = new ArrayList<>();
    private String currentUser = null;

    public boolean signUp(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        users.add(new User(username, password));
        return true;
    }

    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = username;
                return true;
            }
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}