package com.example.demo.views;

import com.example.demo.model.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.File;

public class PostInfoController {
    public ListView PostsList;
    public ImageView imageView2;
    public ListView AccountLikePost;
    public ListView AccountComentList;
    public TextField idTextbox;
    public Image image;
    public Circle ProCircle;

    public void initialize() {
        idTextbox.setText(LoggedInPost.getInstance().getLoggedIn().getOwner().getUsername());
        if(LoggedInPost.getInstance().getLoggedIn().getOwner().getAccountsFile().get(LoggedInPost.getInstance().getLoggedIn().getOwner())!=null){
            image = new Image(LoggedInPost.getInstance().getLoggedIn().getOwner().getAccountsFile().get(LoggedInPost.getInstance().getLoggedIn().getOwner()).toURI().toString(), 100, 150, true, true);
            ProCircle.setFill(new ImagePattern(image));
        }
        Button btnNumber = new Button();
        Button Showlikebtn = new Button();
        Button Showcommentbtn = new Button();
        Showlikebtn.setText("Show Likes");
        btnNumber.setText("Like");
        Showcommentbtn.setText("show comment(write a comment)");

        btnNumber.setOnAction((ActionEvent) -> {
            likepost(LoggedInPost.getInstance().getLoggedIn().getFile());
            clearTextandImg();
        });

        Showlikebtn.setOnAction((ActionEvent) -> {
            clearTextandImg();
            Post post = Post.getPostByFile(LoggedInPost.getInstance().getLoggedIn().getFile());
            if (post.getLike().size() == 0) {
                AccountLikePost.getItems().add("no likes for this post :(");
            } else {
                AccountLikePost.getItems().add("likes : " + post.getLike().size());
                for (int j = 0; j < post.getLike().size(); j++) {
                    if (!AccountLikePost.getItems().contains(post.getLike().get(j).getUsername())) {
                        AccountLikePost.getItems().add(post.getLike().get(j).getUsername());
                    }
                }
            }
        });

        Showcommentbtn.setOnAction((ActionEvent) -> {
            clearTextandImg();
            Post post = Post.getPostByFile(LoggedInPost.getInstance().getLoggedIn().getFile());
            TextField textField = new TextField();
            textField.setPromptText("write down your comment");
            Button commentbtn = new Button();
            commentbtn.setText("comment");
            commentbtn.setOnAction((ActionEvent2) -> {
                commentpost(LoggedInPost.getInstance().getLoggedIn().getFile(), textField.getText());
                clearTextandImg();
            });

            if (post.getComments().size() == 0) {
                AccountComentList.getItems().add("no comments for this post :(");
                for (int k = 0; k < 2; k++) {
                    AccountComentList.getItems().add("");
                }
            } else {
                AccountComentList.getItems().add("comments : " + post.getComments().size());
                for (int j = 0; j < post.getComments().size(); j++) {
                    if (!AccountComentList.getItems().contains(post.getComments().get(j))) {
                        AccountComentList.getItems().add(post.getComments().get(j));
                        Button likeCommentbtn = new Button();
                        Button replyCommentbtn = new Button();
                        likeCommentbtn.setText("like comment");
                        replyCommentbtn.setText("reply on this comment!");
                        AccountComentList.getItems().add(likeCommentbtn);
                        AccountComentList.getItems().add(replyCommentbtn);
                        int finalJ = j;
                        likeCommentbtn.setOnAction((ActionEvent3) -> {
                            clearTextandImg();
                            Comment comment = Comment.getCommentById(post.getComments().get(finalJ).getId());
                            boolean hasLiked = false;
                            for (int k = 0; k < comment.getLikes().size(); k++) {
                                if (comment.getLikes().get(k).getAccount().equals(LoggedInAccount.getInstance().getLoggedIn())) {
                                    System.out.println("hasliked");
                                    new PopupMessage(Alert.AlertType.ERROR, "you have already liked this comment!");
                                    hasLiked = true;
                                }
                            }
                            if (!hasLiked) {
                                likecomment(comment);
                            }
                        });

                        replyCommentbtn.setOnAction((ActionEvent3) -> {
                            //TextField replytextfield = new TextField();
                            //replytextfield.setPromptText("reply on this comment");
                            textField.setPromptText("reply on this comment");
                            //AccountComentList.getItems().add(replytextfield);
                            Button submitbtn = new Button();
                            submitbtn.setText("submit");
                            AccountComentList.getItems().remove(commentbtn);//////
                            AccountComentList.getItems().add(submitbtn);
                            submitbtn.setOnAction((ActionEvent4) -> {
                                Comment comment = Comment.getCommentById(post.getComments().get(finalJ).getId());
                                //comment.writeReplyComment(replytextfield.getText(), LoggedInAccount.getInstance().getLoggedIn());
                                comment.writeReplyComment(textField.getText(), LoggedInAccount.getInstance().getLoggedIn());
                                System.out.println("Replied comment wrote!");
                                new PopupMessage(Alert.AlertType.ERROR, "you replied to this comment!");
                               // replytextfield.setText("");
                                textField.setText("");
                                clearTextandImg();
                            });
                        });

                        AccountComentList.getItems().add("_____________________________");
                    }
                }
                for (int k = 0; k < 2; k++) {
                    AccountComentList.getItems().add("");
                }
            }

            AccountComentList.getItems().add(textField);
            AccountComentList.getItems().add(commentbtn);

        });

        image = new Image(LoggedInPost.getInstance().getLoggedIn().getFile().toURI().toString(), 100, 150, true, true);
        imageView2 = new ImageView();imageView2.setImage(image);imageView2.setFitWidth(100);imageView2.setFitHeight(150);imageView2.setPreserveRatio(true);imageView2.setSmooth(true);imageView2.setCache(true);
        LoggedInPost.getInstance().getLoggedIn().addview(LoggedInAccount.getInstance().getLoggedIn());
        PostsList.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getUsername());
        PostsList.getItems().add(imageView2);
        PostsList.getItems().add(btnNumber);
        PostsList.getItems().add(Showlikebtn);
        PostsList.getItems().add(Showcommentbtn);
        PostsList.getItems().add(LoggedInPost.getInstance().getLoggedIn());
        PostsList.getItems().add("_____________________________");
    }

    public void clearTextandImg(){
        AccountLikePost.getItems().clear();
        AccountComentList.getItems().clear();
    }

    private void likepost(File file) {
        Post post = Post.getPostByFile(file);
        boolean success=true;
        success = post.like(LoggedInAccount.getInstance().getLoggedIn());
        if(post.getLike().contains(LoggedInAccount.getInstance().getLoggedIn())){
            System.out.println("ehem");
            new PopupMessage(Alert.AlertType.ERROR, "you have already liked this post!");
        }
        else {
            post.addlikestate(LoggedInAccount.getInstance().getLoggedIn());
            new PopupMessage(Alert.AlertType.ERROR, "you liked this post!");
        }

    }

    private void commentpost(File file, String text) {
        Post post = Post.getPostByFile(file);
        post.writeComment(text,LoggedInAccount.getInstance().getLoggedIn());
        new PopupMessage(Alert.AlertType.ERROR, "comment is written!");
    }

    private void likecomment(Comment comment) {
        boolean success=true;
        new PopupMessage(Alert.AlertType.ERROR, "you  liked this comment!");
        success = comment.like(LoggedInAccount.getInstance().getLoggedIn());
    }

    public void gotoMainPage(ActionEvent actionEvent) {
        MenuChanger.changeMenu("MainPaneforLoginAccount");
    }
}
