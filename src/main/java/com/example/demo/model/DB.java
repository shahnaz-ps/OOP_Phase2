package com.example.demo.model;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class DB {
    private HashMap<String, Account> accounts;
    private ArrayList<Message> messages;
    private int commentCounter;
    private HashMap<Integer, Comment> comments;
    private int postCounter;
    private HashMap<Integer, Post> posts;
    private ArrayList<GroupChat> groupChats;
    private ArrayList<PrivateChat> privateChats;

    private DB() {
    }

    public static DB getInstance() {
        DB data = new DB();

        data.accounts = Account.getAccounts();
        data.messages = Chat.getWholeMessages();
        data.commentCounter = Comment.getIdCounter();
        data.comments = Comment.getComments();
        data.postCounter = Post.getIdCounter();
        data.posts = Post.getPosts();
        data.groupChats = GroupChat.getGroupChats();
        data.privateChats = PrivateChat.getPrivateChats();

        return data;

    }

    public void setToGameDataBase() {
        Account.setAccounts(accounts);
        Chat.setMessages(messages);
        Comment.setComments(comments);
        Comment.setIdCounter(commentCounter);
        Post.setPosts(posts);
        Post.setIdCounter(postCounter);
        GroupChat.setGroupChats(groupChats);
        PrivateChat.setPrivateChats(privateChats);
    }


    public static void saveData() {
        try {
            FileWriter fileWriter;
            if (Files.exists(Paths.get("data/projectInformation.xml")))
                fileWriter = new FileWriter("data/projectInformation.xml", false);
            else {
                new File("data").mkdir();
                fileWriter = new FileWriter("data/projectInformation.xml", false);
            }
            XStream xStream = new XStream();
            fileWriter.write(xStream.toXML(DB.getInstance()));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadData() {
        try {
            String xml = new String(Files.readAllBytes(Paths.get("data/projectInformation.xml")));
            XStream xStream = new XStream();
            xStream.addPermission(AnyTypePermission.ANY);
            if (xml.length() != 0) {
                DB game = (DB) xStream.fromXML(xml);
                game.setToGameDataBase();
            }
        } catch (IOException ignored) {
        }
    }
}