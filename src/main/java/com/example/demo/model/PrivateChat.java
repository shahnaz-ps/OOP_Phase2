package com.example.demo.model;

import java.util.ArrayList;

public class PrivateChat extends Chat {
    private Account account1;
    private Account account2;
    private ArrayList<Message> messages = new ArrayList<>();
    private static ArrayList<PrivateChat> privateChats = new ArrayList<>();

    public PrivateChat(Account account1, Account account2) {
        this.account1 = account1;
        this.account2 = account2;
    }


    @Override
    public void sendMessage(Account account, String content) {
        Message message = new Message(account, content);
        this.messages.add(message);
        Chat.getWholeMessages().add(message);
    }

    @Override
    public void replyMessage(Account account, String content, String repliedUUID) {
        Message message = new Message(account, content, repliedUUID);
        this.messages.add(message);
        Chat.getWholeMessages().add(message);
    }

    @Override
    public void forwardMessage(Account account, String content, Account forwardedAccount) {
        Message message = new Message(account, content, forwardedAccount);
        this.messages.add(message);
        Chat.getWholeMessages().add(message);
    }

    public ArrayList<Message> getMessages() {
        return this.messages;
    }

    public static PrivateChat getPrivateChat(Account account1, Account account2) {
        for (PrivateChat privateChat : privateChats) {
            if ((privateChat.account1 == account1 && privateChat.account2 == account2) ||
                    privateChat.account2 == account1 && privateChat.account1 == account2) {
                return privateChat;
            }
        }

        PrivateChat privateChat = new PrivateChat(account1, account2);
        privateChats.add(privateChat);
        return privateChat;
    }

    public static ArrayList<PrivateChat> getPrivateChats() {
        return privateChats;
    }

    public static void setPrivateChats(ArrayList<PrivateChat> privateChats) {
        PrivateChat.privateChats = privateChats;
    }

    public Account getOtherUser(Account acc) {
        if (acc == account1) return account2;
        if (acc == account2) return account1;
        return null;
    }

    public Account getAccount1() {
        return account1;
    }

    public Account getAccount2() {
        return account2;
    }
}

