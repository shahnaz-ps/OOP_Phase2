package com.example.demo.model;

import java.util.Date;
import java.util.UUID;

public class Message {
    private Account account;
    private Date date;
    private String content;
    private UUID uuid;
    private UUID repliedUUID = null;
    private String forwardedUsername = null;
    private boolean edited = false;

    public Message(Account account, String content) {
        this.account = account;
        this.content = content;
        this.date = new Date();
        this.uuid = UUID.randomUUID();
    }

    public Message(Account account, String content, String repliedUUID) {
        this.account = account;
        this.content = content;
        this.date = new Date();
        this.uuid = UUID.randomUUID();
        this.repliedUUID = UUID.fromString(repliedUUID);
    }

    public Message(Account account, String content, Account forwardedAccount) {
        this.account = account;
        this.content = content;
        this.date = new Date();
        this.uuid = UUID.randomUUID();
        this.forwardedUsername = forwardedAccount.getUsername();
    }


    public Account getAccount() {
        return account;
    }

    public String getContent() {
        return content;
    }

    public UUID getUuid() {
        return uuid;
    }

    public UUID getRepliedUUID() {
        return repliedUUID;
    }

    public String getForwardedUsername() {
        return forwardedUsername;
    }

    public void setRepliedUUID(UUID repliedUUID) {
        this.repliedUUID = repliedUUID;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public static Message getMessageById(String id){
        for (Message message : Chat.getWholeMessages()) {
            if (message.getUuid().toString().equals(id)){
                return message;
            }
        }
        return null;
    }

    public void deleteMessage(){
        Chat.getWholeMessages().remove(this);
        for (GroupChat groupChat : GroupChat.getGroupChats()) {
            for (Message message : groupChat.getMessages()) {
                if (message==this){
                    groupChat.getMessages().remove(this);
                    return;
                }
            }
        }
        for (PrivateChat privateChat : PrivateChat.getPrivateChats()) {
            for (Message message : privateChat.getMessages()) {
                if (message==this){
                    privateChat.getMessages().remove(this);
                    return;
                }
            }
        }
    }

    public Date getDate() {
        return date;
    }
}
