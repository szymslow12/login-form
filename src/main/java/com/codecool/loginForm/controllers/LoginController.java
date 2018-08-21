package com.codecool.loginForm.controllers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.util.Scanner;
import java.util.UUID;

public class LoginController implements HttpHandler {

    private HttpExchange httpExchange;
    private String response;

    @Override
    public void handle(HttpExchange httpExchange) {

        this.httpExchange = httpExchange;

        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");


        boolean isNewSession = checkCookies(cookieStr);
        try {
            if (isNewSession) {
                this.response = buildHtmlPage("index.html");
                sendResponse();
            } else {
                checkIfLogged();
            }
        } catch (Exception err) {
            err.printStackTrace();
        }

    }


    void setHttpExchange(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }


    void setResponse(String response) {
        this.response = response;
    }


    void sendResponse() throws IOException {
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }


    void redirect(String location) throws IOException
    {
        Headers headers = httpExchange.getResponseHeaders();
        headers.add("Location", location);
        httpExchange.sendResponseHeaders(302, -1);
        httpExchange.close();
    }


    private boolean checkCookies(String cookieStr) {
        HttpCookie cookie;
        if (cookieStr != null) {  // Cookie already exists
            cookie = HttpCookie.parse(cookieStr).get(0);
            System.out.println(cookie.toString() + " <- USER COOKIE -> " + cookieStr);
            return false;
        } else { // Create a new cookie
            String uuid = UUID.randomUUID().toString();
            cookie = new HttpCookie("sessionId", uuid); // This isn't a good way to create sessionId. Find out better!
            httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString());
            System.out.println(cookie.toString() + " <- USER COOKIE (NEW SESSION)-> " + cookieStr);
            return true;
        }
    }


    String buildHtmlPage(String fileName) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/main/resources/static/html/" + fileName));
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine()).append("\n");
        }
        return stringBuilder.toString();
    }

    private void checkIfLogged() throws IOException {
        boolean isLogged = false;
        if (isLogged) {
            redirect("welcomePage");
        } else {
            this.response = buildHtmlPage("index.html");
            sendResponse();
        }
    }
}
