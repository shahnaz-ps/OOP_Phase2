package com.example.demo.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class GroupChat extends Chat {
    private Account owner;
    private String name;
    private UUID uuid;
    private HashSet<Account> accounts = new HashSet<>();
    private ArrayList<Message> messages = new ArrayList<>();
    private ArrayList<Account> bannedUsers = new ArrayList<>();
    private File file;
    private static ArrayList<GroupChat> groupChats = new ArrayList<>();

    public GroupChat(Account owner, String name) {
        this.owner = owner;
        this.name = name;
        groupChats.add(this);
        accounts.add(owner);
        uuid = UUID.randomUUID();
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


//    public GroupChat createGroup(Account account, String name){
//        GroupChat groupChat = new GroupChat(account,name);
//        groupChats.add(groupChat);
//        accounts.add(account);
//        return groupChat;
//    }

    public void addUser(Account account) {
        accounts.add(account);
    }

    public void removeUser(Account account) {
        accounts.remove(account);
    }

    public static boolean isOwner(Account account, String name) {
        return account.getUsername().equals(getGroupChatByName(name).getOwner().getUsername());
    }

    public static ArrayList<GroupChat> getUserGroups(Account account) {
        ArrayList<GroupChat> userChats = new ArrayList<>();
        for (GroupChat groupChat : groupChats) {
            if (groupChat.accounts.contains(account)) {
                userChats.add(groupChat);
            }
        }
        return userChats;
    }

    public HashSet<Account> getAccounts() {
        return accounts;
    }

    public static ArrayList<GroupChat> getGroupChats() {
        return groupChats;
    }

    public static GroupChat getGroupChatByName(String name) {
        for (GroupChat groupChat : groupChats) {
            if (groupChat.getName().equals(name)) {
                return groupChat;
            }
        }
        return null;
    }

    public static boolean isExist(String name) {
        for (GroupChat groupChat : groupChats) {
            if (groupChat.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMember(Account account, String name) {
        GroupChat groupChat = getGroupChatByName(name);
        for (Account groupChatAccount : groupChat.getAccounts()) {
            if (groupChatAccount.getUsername().equals(account.getUsername())) {
                return true;
            }
        }
        return false;
    }

    public Account getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public static void setGroupChats(ArrayList<GroupChat> groupChats) {
        GroupChat.groupChats = groupChats;
    }

    public ArrayList<Account> getBannedUsers() {
        return bannedUsers;
    }

    public void banUser(Account account) {
        bannedUsers.add(account);
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}