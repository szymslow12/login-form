package com.codecool.loginForm.models;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private List<User> userList;

    public UserRepository() {
        userList = new ArrayList<>();
        addUsersToRepository();
    }

    private void addUsersToRepository() {
        userList.add(new User("john13", "johnpassword", "John", "Last"));
        userList.add(new User("jumbo12", "krowa1", "Szymon", "Slowik"));
    }

    public User getUserByLogin(String login) {
        for (User user: userList) {
            if (user.getLogin().equals(login)) {
                return user;
            }
        }
        return null;
    }
}
