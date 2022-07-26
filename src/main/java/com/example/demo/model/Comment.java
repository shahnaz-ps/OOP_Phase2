package com.example.demo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Comment {
    private static int idCounter = 1;
    private static HashMap<Integer, Comment> comments = new HashMap<>();
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

    @Override
    public String toString() {
        ArrayList<String>  likeuser = new ArrayList<>();
        for (Like like:likes) {
            likeuser.add(like.getAccount().getUsername());
        }
        return "comment ID : "+" | "+id +"\n"+account.getUsername() + ": " + content
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
