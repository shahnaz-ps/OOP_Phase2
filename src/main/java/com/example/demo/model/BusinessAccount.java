package com.example.demo.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class BusinessAccount extends Account{

    public BusinessAccount(String username, String password, File file) {
        super(username, password,file);
    }


    public static BusinessAccount createAccount(String username, String password) {
        File f = new File("src/main/resources/Image/Menu/pro1.png");
        BusinessAccount account = new BusinessAccount(username, password,f);
        accounts.put(username, account);
        return account;
    }
}
