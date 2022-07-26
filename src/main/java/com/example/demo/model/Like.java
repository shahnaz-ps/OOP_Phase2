package com.example.demo.model;

import java.util.Date;

public class Like {
    private Account account;
    private Date date;

    public Like(Account account) {
        this.account = account;
        date = new Date();
    }

    public Account getAccount() {
        return account;
    }
}
