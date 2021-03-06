package com.codecool.loginForm.controllers;

import com.codecool.loginForm.models.User;
import com.codecool.loginForm.models.UserRepository;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpCookie;

public class UserDataController extends LoginController implements HttpHandler {

    private User user;

    @Override
    public void handle(HttpExchange httpExchange) {
        setHttpExchange(httpExchange);
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        user = UserRepository.instance().getUserBySessionId(HttpCookie.parse(cookieStr).get(0).toString());


        try {
            setResponse(constructResponse());
            httpExchange.getResponseHeaders().set("Content-Type", "application/json");
            sendResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String constructResponse() throws IOException {
        if (user != null) {
            return getJSON(user.getFirstName(), user.getLastName());
        } else {
            redirect("login");
            return getJSON("undefined", "undefined");
        }
    }

    private String getJSON(String name, String lastName) {
        JSONObject object = new JSONObject();
        object.put("firstName", name);
        object.put("lastName", lastName);
        return object.toString();
    }
}
