package com.codecool.loginForm.controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;



public class WelcomePageController extends LoginController implements HttpHandler {

//    private HttpExchange httpExchange;
//    String response;

    @Override
    public void handle(HttpExchange httpExchange) {

        setHttpExchange(httpExchange);
//        this.httpExchange = httpExchange;
        boolean isLogged = false;
        try {
            if (isLogged) {
                setResponse(buildHtmlPage("welcome-page.html"));
                sendResponse();
            } else {
                redirect("login");
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}
