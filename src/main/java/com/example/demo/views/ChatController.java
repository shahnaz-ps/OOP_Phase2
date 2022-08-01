package com.example.demo.views;

import com.example.demo.model.Account;
import com.example.demo.model.GroupChat;
import com.example.demo.model.LoggedInAccount;
import com.example.demo.model.PrivateChat;

import java.util.ArrayList;

public class ChatController {
    private static ChatController instance;

    public static void setNull() {
        instance = null;
    }

    public static ChatController getInstance() {
        if (instance == null) instance = new ChatController();
        return instance;
    }

    public boolean isValidLongMessage(String message) {
        return !(message.length() > 25);
    }

    public boolean isValidShortMessage(String message) {
        return !(message.length() <= 0);
    }

    public ArrayList<Account> showUsernamesStartsWithString(String username) {
        ArrayList<Account> users = new ArrayList<>();
        for (String temp : Account.getAccounts().keySet()) {
            Account account = Account.getAccount(temp);
            if (account.getUsername().startsWith(username))
                if (account != LoggedInAccount.getInstance().getLoggedIn())
                    users.add(account);
        }
        return users;
    }

    public PrivateChat getAccountPrivateChat(Account account) {
        for (PrivateChat privateChat : LoggedInAccount.getInstance().getLoggedIn().getPrivateChats())
            if (privateChat.getOtherUser(LoggedInAccount.getInstance().getLoggedIn()) == account)
                return privateChat;
        PrivateChat privateChat = new PrivateChat(LoggedInAccount.getInstance().getLoggedIn(), account);
        PrivateChat.getPrivateChats().add(privateChat);
        return privateChat;
    }

    public ArrayList<GroupChat> showGroupsStartsWithString(String username) {
        ArrayList<GroupChat> res = new ArrayList<>();
        for (GroupChat userGroup : GroupChat.getUserGroups(LoggedInAccount.getInstance().getLoggedIn())) {
            if (userGroup.getName().startsWith(username)) res.add(userGroup);
        }
        return res;
    }


    public static void setGlobalChatNull() {
        ChatController.setNull();
        ChatPage.setNull();
    }
}