package com.example.demo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Comment {
    private static int idCounter = 1;
    private static HashMap<Integer, Comment> comments = new HashMap<>();
    private ArrayList<Comment> repliedComments= new ArrayList<>();
    private ArrayList<String> AccrepliedComments= new ArrayList<>();
    private Account account;
    private Date date;
    private String content;
    private ArrayList<Like> likes;
    private int id;

    public Comment(String text, Account account) {
        content = text;
        this.account = account;
        date = new Date();
        likes= new ArrayList<>();
        id = idCounter;
        idCounter++;
        comments.put(id, this);
    }

    public int getId() {
        return id;
    }

    public ArrayList<Like> getLikes() {
        return likes;
    }
    public static Comment getCommentById(int commentId) {
        return comments.get(commentId);
    }

    public boolean like(Account account) {
        for (Like like : likes) {
            if (like.getAccount().equals(account)) {
                return false;
            }
        }
        likes.add(new Like(account));
        return true;
    }

    public boolean dislike(Account account) {
        for (Like like : likes) {
            if (like.getAccount().equals(account)) {
                likes.remove(like);
                return true;
            }
        }
        return false;
    }

    public void writeReplyComment(String text, Account account) {
        Comment comment = new Comment(text,account);
        repliedComments.add(comment);
        AccrepliedComments.add(account.getUsername());
    }

    @Override
    public String toString() {
        ArrayList<String>  likeuser = new ArrayList<>();
        for (Like like:likes) {
            likeuser.add(like.getAccount().getUsername());
        }
        ArrayList<String>  repcomment = new ArrayList<>();
        for(int i=0;i<repliedComments.size();i++){
            repcomment.add(AccrepliedComments.get(i)+" replied : "+repliedComments.get(i).content);
        }
        return "comment ID : "+" | "+id +"\n"+account.getUsername() + ": " + content
                + "\nreplied comment : "+repcomment
                +"\nnumber of likes : "+likes.size()+"\nusers who liked this comment: "+likeuser
                +"\nDate : "+date;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static HashMap<Integer, Comment> getComments() {
        return comments;
    }

    public static void setIdCounter(int idCounter) {
        Comment.idCounter = idCounter;
    }

    public static void setComments(HashMap<Integer, Comment> comments) {
        Comment.comments = comments;
    }
}