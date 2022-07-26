package com.example.demo.model;

import java.util.ArrayList;
import java.util.Date;

public class BusinessAccount extends Account{

    public BusinessAccount(String username, String password) {
        super(username, password);
    }


    public static BusinessAccount createAccount(String username, String password) {
        BusinessAccount account = new BusinessAccount(username, password);
        accounts.put(username, account);
        return account;
    }
}
