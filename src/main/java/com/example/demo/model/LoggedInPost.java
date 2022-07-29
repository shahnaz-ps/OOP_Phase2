package com.example.demo.model;

public class LoggedInPost {
    private static LoggedInPost instance;

    private Post loggedIn = null;


    private LoggedInPost() {

    }

    public static LoggedInPost getInstance() {
        if (instance == null) instance = new LoggedInPost();
        return instance;
    }

    public Post getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(Post loggedIn) {
        this.loggedIn = loggedIn;
    }



}
