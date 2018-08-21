package com.codecool.loginForm.controllers;

import com.codecool.loginForm.models.User;
import com.codecool.loginForm.models.UserRepository;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpCookie;


public class WelcomePageController extends LoginController implements HttpHandler {

    private User user;

    @Override
    public void handle(HttpExchange httpExchange) {
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        setHttpExchange(httpExchange);

        try {
            checkCookie(cookieStr);
            if (user != null && user.isUserLogged()) {
                setResponse(buildHtmlPage("welcome-page.html"));
                sendResponse();
            } else {
                redirect("login");
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private void checkCookie(String cookieStr) throws IOException {
        if (cookieStr != null) {
            UserRepository userRepository = UserRepository.instance();
            user = userRepository.getUserBySessionId(HttpCookie.parse(cookieStr).get(0).toString());
        } else {
            user = null;
        }
    }
}
