package com.example.demo.model;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class Account {
    private String username;
    private String password;
    private HashSet<Account> followers;
    private HashSet<Account> followings;
    private ArrayList<Post> posts;
    protected static HashMap<String, Account> accounts = new HashMap<>();
    private ArrayList<Account> blockedUsers = new ArrayList<>();
    private ArrayList<Account> views = new ArrayList<>();
    private ArrayList<Date> dateviewed = new ArrayList<>();
    private Date dateview;

    protected Account(String username, String password) {
        this.username = username.toLowerCase();
        this.password = password;
        followers = new HashSet<>();
        followings = new HashSet<>();
        posts = new ArrayList<>();
    }

    public static Account createAccount(String username, String password) {
        Account account = new Account(username, password);
        accounts.put(username, account);
        return account;
    }

    public static Account getAccount(String username) {
        username = username.toLowerCase();
        return accounts.get(username);
    }

    public static Account checkLogin(String username, String password) {
        username = username.toLowerCase();
        Account account = accounts.get(username);
        if (account == null) {
            return null;
        }
        if (account.password.equals(password)) {
            return account;
        }
        return null;
    }

    public static boolean usernameExists(String username) {
        username = username.toLowerCase();
        return accounts.containsKey(username);
    }

    public void createPost(String content, File file) {
        Post post = new Post(content, this, file);

        posts.add(post);
    }

    public void createGroup(Account account, String name) {
        new GroupChat(account, name);
    }

    public boolean isBusinessAccount() {
        return this instanceof BusinessAccount;
    }

    public String getUsername() {
        return username;
    }

    public Integer getNumberOfFollowers() {
        return followers.size();
    }

    public Integer getNumberOfFollowings() {
        return followings.size();
    }

    public Integer getNumbersOfGroups() {
        return GroupChat.getUserGroups(this).size();
    }

    public ArrayList<Post> getPosts() {
        return new ArrayList<>(posts);
    }

    public void follow(Account account2) {
        this.followings.add(account2);
        account2.followers.add(this);
    }

    public HashSet<Account> getFollowers() {
        return new HashSet<>(followers);
    }

    public HashSet<Account> getFollowings() {
        return new HashSet<>(followings);
    }

    public static HashMap<String, Account> getAccounts() {
        return accounts;
    }

    public ArrayList<Account> getBlockedUsers() {
        return blockedUsers;
    }

    public void blockUser(Account account) {
        blockedUsers.add(account);
    }

    public static void setAccounts(HashMap<String, Account> accounts) {
        Account.accounts = accounts;
    }


    public void addview(Account account) {
        if (!views.contains(account)) {
            dateview = new Date();
            dateviewed.add(dateview);
            views.add(account);
        }
    }

    public void showviewSize() {
        System.out.println("number of views : " + views.size());
        for (int i = 0; i < views.size(); i++) {
            System.out.println(dateviewed.get(i) + " = viewed by username (" + views.get(i).getUsername() + ")");
        }
    }

    public ArrayList<PrivateChat> getPrivateChats() {
        ArrayList<PrivateChat> res = new ArrayList<>();
        for (PrivateChat privateChat : PrivateChat.getPrivateChats()) {
            if (privateChat.getAccount1() == this || privateChat.getAccount2() == this)
                res.add(privateChat);
        }
        return res;
    }
}
