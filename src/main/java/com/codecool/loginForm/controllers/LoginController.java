package com.codecool.loginForm.controllers;

import com.codecool.loginForm.models.User;
import com.codecool.loginForm.models.UserRepository;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.util.Scanner;
import java.util.UUID;
import java.util.Map;
import java.util.TreeMap;

public class LoginController implements HttpHandler {

    private HttpExchange httpExchange;
    private String response;

    @Override
    public void handle(HttpExchange httpExchange) {

        this.httpExchange = httpExchange;

        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        UserRepository userRepository = UserRepository.instance();
        String method = httpExchange.getRequestMethod();
        System.out.println(method + " - " + httpExchange.getRequestURI().toString());

        boolean isNewSession = checkCookies(cookieStr);
        try {
            if (method.equalsIgnoreCase("GET")) {
                handleGet(isNewSession, cookieStr, userRepository);
            } else {
                handlePost(userRepository, cookieStr);
            }
        } catch (Exception err) {
            err.printStackTrace();
        }

    }


    private void handleGet(boolean isNewSession, String cookieStr, UserRepository userRepository) throws IOException {
        if (isNewSession) {
            this.response = buildHtmlPage("index.html");
            sendResponse();
        } else {
            checkIfLogged(userRepository.getUserBySessionId(HttpCookie.parse(cookieStr).get(0).toString()));
        }
    }


    private void handlePost(UserRepository userRepository, String cookieStr) throws IOException {
        String sessionId = HttpCookie.parse(cookieStr).get(0).toString();

        Map<String, String> formData = readAndParseFormData();
        User user = userRepository.getUserByLogin(formData.get("login"));

        if (user != null && user.getPassword().equals(formData.get("password"))) {
            user.setSessionId(sessionId);
            redirect("welcomePage");
        } else {
            this.response = "<html><body><script> alert('Bad login or password!'); window.location.href = '/login';" +
                    "</script></body><html>";
            sendResponse();
        }
    }


    private Map<String, String> readAndParseFormData() throws IOException {
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String formData = br.readLine();

        System.out.println(formData);
        return parseFormData(formData);
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

    private void checkIfLogged(User user) throws IOException {
        System.out.println(" " + user + "\u001B[0m");
        if (user != null && user.isUserLogged()) {
            redirect("welcomePage");
        } else {
            this.response = buildHtmlPage("index.html");
            sendResponse();
        }
    }

    private static Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new TreeMap<>();
        String[] pairs = formData.split("&");
        for(String pair : pairs){
            String[] keyValue = pair.split("=");
            // We have to decode the value because it's urlencoded. see: https://en.wikipedia.org/wiki/POST_(HTTP)#Use_for_submitting_web_forms
            String value = new URLDecoder().decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return map;
    }
}
