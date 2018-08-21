package com.codecool.loginForm.models;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static UserRepository userRepository;
    private List<User> userList;

    private UserRepository() {
        userList = new ArrayList<>();
        addUsersToRepository();
    }

    private void addUsersToRepository() {
        userList.add(new User("john13", "johnpassword", "John", "Last"));
        userList.add(new User("jumbo12", "krowa1", "Szymon", "Slowik"));
    }

    public User getUserByLogin(String login) {
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            if (user.getLogin().equals(login)) {
                return user;
            }
        }
        return null;
    }


    public User getUserBySessionId(String sessionId) {
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            String userSession = user.getSessionId();
            if (userSession != null && userSession.equals(sessionId)) {
                return user;
            }
        }
        return null;
    }


    public static UserRepository instance() {
        if (userRepository == null) {
            userRepository = new UserRepository();
            return userRepository;
        } else {
            return userRepository;
        }
    }
}
