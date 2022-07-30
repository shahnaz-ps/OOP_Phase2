package com.example.demo.views;

import com.example.demo.model.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class WatchProfileController {


    public TextField idTextbox;
    public TextField followersNum;
    public TextField followingsNum;
    public ListView PostsList;
    public ImageView imageView2;
    public VBox vboxForButtons = new VBox();
    public Image image;
    public ListView AccountLikePost;
    public ListView AccountComentList;
    public Circle ProCircle;


    public void initialize() {
        idTextbox.setText(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getUsername());
        followersNum.setText(String.valueOf(LoginAccountPageController.getAccount2().getNumberOfFollowers()));
        followingsNum.setText(String.valueOf(LoginAccountPageController.getAccount2().getNumberOfFollowings()));
        if(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getAccountsFile().get(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()))!=null){
            image = new Image(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getAccountsFile().get(Account.getAccount(LoginAccountPageController.getAccount2().getUsername())).toURI().toString(), 100, 150, true, true);
            ProCircle.setFill(new ImagePattern(image));
        }

        int size=Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().size();
        if(size<=2){
            for (int i = size - 1; i >= 0; i--) {
                Button btnNumber = new Button();
                Button Showlikebtn = new Button();
                Button Showcommentbtn = new Button();
                Showcommentbtn.setText("show comment(write a comment)");
                Showlikebtn.setText("Show Likes");
                btnNumber.setText("Like");
                int finalI = i;
                int finalI1 = i;
                btnNumber.setOnAction((ActionEvent) -> {
                    likepost(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(finalI1).getFile());
                    clearTextandImg();
                });

                Showlikebtn.setOnAction((ActionEvent) -> {
                    clearTextandImg();
                    Post post = Post.getPostByFile(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(finalI1).getFile());
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
                    Post post = Post.getPostByFile(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(finalI1).getFile());
                    TextField textField = new TextField();
                    textField.setPromptText("write down your comment");
                    Button commentbtn = new Button();
                    commentbtn.setText("comment");
                    commentbtn.setOnAction((ActionEvent2) -> {
                        commentpost(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(finalI1).getFile(), textField.getText());
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

                clearTextandImg();

                vboxForButtons.getChildren().add(btnNumber);
                image = new Image(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(finalI1).getFile().toURI().toString(), 100, 150, true, true);
                imageView2 = new ImageView();
                imageView2.setImage(image);
                imageView2.setFitWidth(100);
                imageView2.setFitHeight(150);
                imageView2.setPreserveRatio(true);
                imageView2.setSmooth(true);
                imageView2.setCache(true);
                Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(i).addview(LoggedInAccount.getInstance().getLoggedIn());
                PostsList.getItems().add(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getUsername());
                PostsList.getItems().add(imageView2);
                PostsList.getItems().add(btnNumber);
                PostsList.getItems().add(Showlikebtn);
                PostsList.getItems().add(Showcommentbtn);
                PostsList.getItems().add(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(i));
                PostsList.getItems().add("_____________________________");
            }
        }
        else {
            for (int i = size - 1; i >= size - 2; i--) {
                Button btnNumber = new Button();
                Button Showlikebtn = new Button();
                Button Showcommentbtn = new Button();
                Showcommentbtn.setText("show comment(write a comment)");
                Showlikebtn.setText("Show Likes");
                btnNumber.setText("Like");
                int finalI = i;
                int finalI1 = i;
                btnNumber.setOnAction((ActionEvent) -> {
                    likepost(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(finalI1).getFile());
                    clearTextandImg();
                });

                Showlikebtn.setOnAction((ActionEvent) -> {
                    clearTextandImg();
                    Post post = Post.getPostByFile(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(finalI1).getFile());
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
                    Post post = Post.getPostByFile(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(finalI1).getFile());
                    TextField textField = new TextField();
                    textField.setPromptText("write down your comment");
                    Button commentbtn = new Button();
                    commentbtn.setText("comment");
                    commentbtn.setOnAction((ActionEvent2) -> {
                        commentpost(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(finalI1).getFile(), textField.getText());
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
                                            new PopupMessage(Alert.AlertType.ERROR, "you have already liked this comment!");
                                            hasLiked = true;
                                        }
                                    }
                                    if (!hasLiked) {
                                        likecomment(comment);
                                    }
                                });
                                replyCommentbtn.setOnAction((ActionEvent3) -> {
                                    TextField replytextfield = new TextField();
                                    replytextfield.setPromptText("reply on this comment");
                                    AccountComentList.getItems().add(replytextfield);
                                    Button submitbtn = new Button();
                                    submitbtn.setText("submit");
                                    AccountComentList.getItems().add(submitbtn);
                                    submitbtn.setOnAction((ActionEvent4) -> {
                                        Comment comment = Comment.getCommentById(post.getComments().get(finalJ).getId());
                                        comment.writeReplyComment(replytextfield.getText(), LoggedInAccount.getInstance().getLoggedIn());
                                        System.out.println("Replied comment wrote!");
                                        new PopupMessage(Alert.AlertType.ERROR, "you replied to this comment!");
                                        replytextfield.setText("");

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

                clearTextandImg();

                vboxForButtons.getChildren().add(btnNumber);
                image = new Image(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(finalI1).getFile().toURI().toString(), 100, 150, true, true);
                imageView2 = new ImageView();
                imageView2.setImage(image);
                imageView2.setFitWidth(100);
                imageView2.setFitHeight(150);
                imageView2.setPreserveRatio(true);
                imageView2.setSmooth(true);
                imageView2.setCache(true);
                Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(i).addview(LoggedInAccount.getInstance().getLoggedIn());
                PostsList.getItems().add(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getUsername());
                PostsList.getItems().add(imageView2);
                PostsList.getItems().add(btnNumber);
                PostsList.getItems().add(Showlikebtn);
                PostsList.getItems().add(Showcommentbtn);
                PostsList.getItems().add(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(i));
                PostsList.getItems().add("_____________________________");
            }
        }

        clearTextandImg();
    }

    private void commentpost(File file, String text) {
        Post post = Post.getPostByFile(file);
        post.writeComment(text,LoggedInAccount.getInstance().getLoggedIn());
        new PopupMessage(Alert.AlertType.ERROR, "comment is written!");
        System.out.println("Comment wrote!");

    }

    private void likepost(File file) {
        Post post = Post.getPostByFile(file);
        boolean success=true;
        success = post.like(LoggedInAccount.getInstance().getLoggedIn());
        if(post.getLike().contains(LoggedInAccount.getInstance().getLoggedIn())){
            System.out.println("you have liked");
            new PopupMessage(Alert.AlertType.ERROR, "you have already liked this post!");
        }
        else {
            post.addlikestate(LoggedInAccount.getInstance().getLoggedIn());
            new PopupMessage(Alert.AlertType.ERROR, "you liked this post!");
        }
        //System.out.println(post);
    }

    private void likecomment(Comment comment) {
        boolean success;
        new PopupMessage(Alert.AlertType.ERROR, "you  liked this comment!");
        success = comment.like(LoggedInAccount.getInstance().getLoggedIn());
    }

    public void clearTextandImg(){
        AccountLikePost.getItems().clear();
        AccountComentList.getItems().clear();
    }

    public void Follow(ActionEvent actionEvent) {
            if (Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getFollowers().contains(LoggedInAccount.getInstance().getLoggedIn())) {
                new PopupMessage(Alert.AlertType.ERROR, "You have already followed this "+"\n account!");
            }
            else {
                LoggedInAccount.getInstance().getLoggedIn().follow(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()));
                new  PopupMessage(Alert.AlertType.INFORMATION, "you followed "  + Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getUsername());
            }
    }

    public void BacktoYourAccount(ActionEvent actionEvent) {
        MenuChanger.changeMenu("LoginAccountPage");
    }

    public void showAllPosts(ActionEvent actionEvent) {
        int size=Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().size();
        PostsList.getItems().clear();
        for (int i = size - 1; i >= 0; i--) {
            Button btnNumber = new Button();
            Button Showlikebtn = new Button();
            Button Showcommentbtn = new Button();
            Showcommentbtn.setText("show comment(write a comment)");
            Showlikebtn.setText("Show Likes");
            btnNumber.setText("Like");
            int finalI = i;
            int finalI1 = i;
            btnNumber.setOnAction((ActionEvent) -> {
                likepost(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(finalI1).getFile());
                clearTextandImg();
            });

            Showlikebtn.setOnAction((ActionEvent) -> {
                clearTextandImg();
                Post post = Post.getPostByFile(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(finalI1).getFile());
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
                Post post = Post.getPostByFile(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(finalI1).getFile());
                TextField textField = new TextField();
                textField.setPromptText("write down your comment");
                Button commentbtn = new Button();
                commentbtn.setText("comment");
                commentbtn.setOnAction((ActionEvent2) -> {
                    commentpost(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(finalI1).getFile(), textField.getText());
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
                                        new PopupMessage(Alert.AlertType.ERROR, "you have already liked this comment!");
                                        hasLiked = true;
                                    }
                                }
                                if (!hasLiked) {
                                    likecomment(comment);
                                }
                            });
                            replyCommentbtn.setOnAction((ActionEvent3) -> {
                                TextField replytextfield = new TextField();
                                replytextfield.setPromptText("reply on this comment");
                                AccountComentList.getItems().add(replytextfield);
                                Button submitbtn = new Button();
                                submitbtn.setText("submit");
                                AccountComentList.getItems().add(submitbtn);
                                submitbtn.setOnAction((ActionEvent4) -> {
                                    Comment comment = Comment.getCommentById(post.getComments().get(finalJ).getId());
                                    comment.writeReplyComment(replytextfield.getText(), LoggedInAccount.getInstance().getLoggedIn());
                                    System.out.println("Replied comment wrote!");
                                    new PopupMessage(Alert.AlertType.ERROR, "you replied to this comment!");
                                    replytextfield.setText("");

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

            clearTextandImg();

            vboxForButtons.getChildren().add(btnNumber);
            image = new Image(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(finalI1).getFile().toURI().toString(), 100, 150, true, true);
            imageView2 = new ImageView();
            imageView2.setImage(image);
            imageView2.setFitWidth(100);
            imageView2.setFitHeight(150);
            imageView2.setPreserveRatio(true);
            imageView2.setSmooth(true);
            imageView2.setCache(true);
            Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(i).addview(LoggedInAccount.getInstance().getLoggedIn());
            PostsList.getItems().add(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getUsername());
            PostsList.getItems().add(imageView2);
            PostsList.getItems().add(btnNumber);
            PostsList.getItems().add(Showlikebtn);
            PostsList.getItems().add(Showcommentbtn);
            PostsList.getItems().add(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(i));
            PostsList.getItems().add("_____________________________");
        }
    }

    public void gotoMainPage(ActionEvent actionEvent) {
        MenuChanger.changeMenu("MainPaneforLoginAccount");
    }

    public void PostListPressed(MouseEvent mouseEvent) throws MalformedURLException {
        if(PostsList.getSelectionModel().getSelectedItem().getClass().equals(ImageView.class)){
            ImageView imageView3= (ImageView) PostsList.getSelectionModel().getSelectedItem();
            URL url = new URL(imageView3.getImage().getUrl());
            File f = new File(url.getFile());
            System.out.println(f);
            Post post = Post.getPostByFile(f);
            gotoPostinfoPage(post);
        }
    }

    private void gotoPostinfoPage(Post post) {
        LoggedInPost.getInstance().setLoggedIn(post);
        MenuChanger.changeMenu("PostInfo");
    }

}
