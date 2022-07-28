package com.example.demo.model;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class Post {
    private static int idCounter = 1;
    private static HashMap<Integer, Post> posts = new HashMap<>();
    private static HashMap<File, Post> postsFile = new HashMap<>();
    private  ArrayList<Account> views = new ArrayList<>();
    private  ArrayList<Date> dateviewed = new ArrayList<>();
    private  ArrayList<Account> like = new ArrayList<>();
    private  ArrayList<Date> dateliked = new ArrayList<>();
    private  ArrayList<String>  likeuser = new ArrayList<>();
    private int id;
    private ArrayList<Comment> comments;
    private ArrayList<Like> likes;
    private String content;
    private Account owner;
    private Date date;
    private Date dateview;
    private Date datelike;
    private Image image;
    private FileInputStream fis;
    private File file;

    public Post(String content, Account owner,File file) {
        this.content = content;
        this.fis=fis;
        this.file=file;
        this.image=image;
        this.owner = owner;
        likes = new ArrayList<>();
        comments = new ArrayList<>();
        id = idCounter;
        idCounter++;
        posts.put(id, this);
        postsFile.put(file,this);
        date = new Date();
    }

    public static Post getPostByFile(File file) {
        return postsFile.get(file);
    }

    public static Post getPostById(int postId) {
        return posts.get(postId);
    }


    public void addview(Account account) {
        if(!views.contains(account)) {
            dateview = new Date();
            dateviewed.add(dateview);
            views.add(account);
        }
    }
    public void addlikestate(Account account) {
        if(!like.contains(account)){
            datelike=new Date();
            dateliked.add(datelike);
            like.add(account);
        }
    }
    public void showviewSize() {
        System.out.println("views : "+views.size());
        for(int i=0;i<views.size();i++){
            System.out.println(dateviewed.get(i) + " = username (" + views.get(i).getUsername()+")");
        }
        System.out.println("likes : "+like.size());
        for (int i=0;i<like.size();i++){
            System.out.println(dateliked.get(i)+" = username (" + like.get(i).getUsername()+")");
        }
    }

    public int getId() {
        return id;
    }

    public File getFile() {
        return file;
    }

    public ArrayList<Like> getLikes() {
        return likes;
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

    public ArrayList<Account> getLike() {
        return like;
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

    public ArrayList<String> getLikeUser(){
        for (Like like:likes) {
            likeuser.add(like.getAccount().getUsername());
        }
        return likeuser;
    }


    @Override
    public String toString() {
//        return owner.getUsername() + " | " + id + "\n" + content+ file
//                + "\nLikes: " + likes.size()+"\nUsers who liked this post : "+likeuser
//                + "\nComments: " + comments.size()
//                +"\nDate created : "+date;

        return  content
                //+ "\nLikes: " + likes.size()
                //+ "\nComments: " + comments.size()
                +"\nDate created : "+date;

    }

    public void writeComment(String text, Account account) {
        Comment comment = new Comment(text,account);
        this.comments.add(comment);
    }

    public ArrayList<Comment> getComments() {
        return new ArrayList<>(comments);
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        Post.idCounter = idCounter;
    }

    public static HashMap<Integer, Post> getPosts() {
        return posts;
    }

    public static void setPosts(HashMap<Integer, Post> posts) {
        Post.posts = posts;
    }

    public FileInputStream getFis() {
        return fis;
    }




}
