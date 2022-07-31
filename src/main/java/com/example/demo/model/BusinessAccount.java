package com.example.demo.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class BusinessAccount extends Account{

    public BusinessAccount(String username, String password, File file) {
        super(username, password,file);
    }


    public static BusinessAccount createAccount(String username, String password) {
        BusinessAccount account = new BusinessAccount(username, password,null);
        accounts.put(username, account);
        return account;
    }
}
