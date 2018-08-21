package com.codecool.loginForm;

import com.codecool.loginForm.controllers.LoginController;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) throws Exception {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8000), 0);

        httpServer.createContext("/login", new LoginController());

        httpServer.setExecutor(null);

        httpServer.start();
    }
}