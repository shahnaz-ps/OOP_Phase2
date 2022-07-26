package com.example.demo.model;

import java.util.Scanner;

public class LoggedInAccount {
    private static LoggedInAccount instance;

    private Account loggedIn = null;

    private LoggedInAccount() {

    }

    public static LoggedInAccount getInstance() {
        if (instance == null) instance = new LoggedInAccount();
        return instance;
    }

    public Account getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(Account loggedIn) {
        this.loggedIn = loggedIn;
    }
}
