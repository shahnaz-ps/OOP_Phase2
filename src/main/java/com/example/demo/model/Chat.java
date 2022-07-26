package com.example.demo.model;

import java.util.ArrayList;

public abstract class Chat {
    protected static ArrayList<Message> messages = new ArrayList<>();

    public abstract void sendMessage(Account account, String content);
    public abstract void replyMessage(Account account, String content, String repliedUUID);
    public abstract void forwardMessage(Account account, String content, Account forwardedAccount);

    public static ArrayList<Message> getWholeMessages() {
        return messages;
    }

    public static void setMessages(ArrayList<Message> messages) {
        Chat.messages = messages;
    }
}
