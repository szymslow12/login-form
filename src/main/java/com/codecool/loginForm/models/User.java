package com.codecool.loginForm.models;

public class User {

    String login;
    String password;
    String firstName;
    String lastName;
    String sessionId = null;

    public User(String login, String password, String firstName, String lastName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String toString() {
        return "Login: " + login + " Fullname: " + firstName + " " + lastName + " Is logged: " + isUserLogged() + " Session: " + sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }


    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


    public boolean isUserLogged() {
        if (sessionId != null) {
            return true;
        } else {
            return false;
        }
    }


    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public String getLogin() {
        return login;
    }


    public String getPassword() {
        return password;
    }
}
