package com.example.demo.views;

import com.example.demo.model.Account;
import com.example.demo.model.Comment;
import com.example.demo.model.LoggedInAccount;
import com.example.demo.model.Post;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

//import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class LoginAccountPageController {
    public TextField idTextbox;
    public TextField followingsNum;
    public TextField followersNum;
    public ImageView imageView;
    public ImageView imageView2;
    public Image image;
    public TextField FilePath;
    public FileChooser fileChooser;
    public File file;
    public static File finalFile;
    public static Account account2;
    //public Desktop desktop =Desktop.getDesktop();
    public ListView PostsList;
    public VBox postsVbox;
    public ListView ShowAccounts;
    public TextField postContent;
    public GridPane buttonGrid = new GridPane();
    public VBox vboxForButtons = new VBox();
    public VBox vboxForShowLikeButtons = new VBox();
    public ListView AccountLikePost;
    public ListView AccountComentList;

    public static File getFinalFile() {
        return finalFile;
    }

    public static void setFinalFile(File finalFile) {
        LoginAccountPageController.finalFile = finalFile;
    }

    public static Account getAccount2() {
        return account2;
    }

    public static void setAccount2(Account account2) {
        LoginAccountPageController.account2 = account2;
    }

    public void initialize() {
        idTextbox.setText(LoggedInAccount.getInstance().getLoggedIn().getUsername());
        followersNum.setText(String.valueOf(LoggedInAccount.getInstance().getLoggedIn().getNumberOfFollowers()));
        followingsNum.setText(String.valueOf(LoggedInAccount.getInstance().getLoggedIn().getNumberOfFollowings()));
        for (Map.Entry<String, Account> set : Account.getAccounts().entrySet()) {
            if(!set.getValue().equals(LoggedInAccount.getInstance().getLoggedIn())) {
                ShowAccounts.getItems().add(set.getValue().getUsername());
            }
        }

        for(int i=0;i<LoggedInAccount.getInstance().getLoggedIn().getPosts().size();i++){
            if (!PostsList.getItems().contains(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i))) {
                Button btnNumber = new Button();
                Button Showlikebtn = new Button();
                Button Showcommentbtn= new Button();
                Showlikebtn.setText("Show Likes");
                btnNumber.setText("Like");
                Showcommentbtn.setText("show comment(write a comment)");
                int finalI = i;

                btnNumber.setOnAction((ActionEvent)->{
                    likepost(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile());
                    clearTextandImg();
                });

                Showlikebtn.setOnAction((ActionEvent)->{
                    clearTextandImg();
                    Post post = Post.getPostByFile(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile());
                    if(post.getLike().size()==0){
                        AccountLikePost.getItems().add("no likes for this post :(");
                    }
                    else {
                        AccountLikePost.getItems().add("likes : "+post.getLike().size());
                        for (int j = 0; j < post.getLike().size(); j++) {
                            if (!AccountLikePost.getItems().contains(post.getLike().get(j).getUsername())) {
                                AccountLikePost.getItems().add(post.getLike().get(j).getUsername());
                            }
                        }
                    }
                });

                Showcommentbtn.setOnAction((ActionEvent)->{
                    clearTextandImg();
                    Post post = Post.getPostByFile(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile());
                    TextField textField = new TextField();
                    textField.setPromptText("write down your comment");
                    Button commentbtn = new Button();
                    commentbtn.setText("comment");
                    commentbtn.setOnAction((ActionEvent2)->{
                        commentpost(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile() , textField.getText());
                        clearTextandImg();
                    });

                    if(post.getComments().size()==0){
                        AccountComentList.getItems().add("no comments for this post :(");
                        for (int k=0;k<2;k++){
                            AccountComentList.getItems().add("");
                        }
                    }
                    else{
                        AccountComentList.getItems().add("comments : "+post.getComments().size());
                        for (int j=0;j<post.getComments().size();j++){
                            if (! AccountComentList.getItems().contains(post.getComments().get(j))) {
                                AccountComentList.getItems().add(post.getComments().get(j));
                                Button likeCommentbtn= new Button();
                                likeCommentbtn.setText("like comment");
                                AccountComentList.getItems().add(likeCommentbtn);
                                int finalJ = j;
                                likeCommentbtn.setOnAction((ActionEvent3)->{
                                    clearTextandImg();
                                    Comment comment = Comment.getCommentById(post.getComments().get(finalJ).getId());
                                    likecomment(comment);
                                });
                                AccountComentList.getItems().add("_____________________________");
                            }
                        }
                        for (int k=0;k<2;k++){
                            AccountComentList.getItems().add("");
                        }
                    }

                    AccountComentList.getItems().add(textField);
                    AccountComentList.getItems().add(commentbtn);

                });

                clearTextandImg();

                vboxForButtons.getChildren().add(btnNumber);
                image = new Image(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getFile().toURI().toString(), 100, 150, true, true);imageView2 = new ImageView();imageView2.setImage(image);imageView2.setFitWidth(100);imageView2.setFitHeight(150);imageView2.setPreserveRatio(true);imageView2.setSmooth(true);imageView2.setCache(true);
                LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).addview(LoggedInAccount.getInstance().getLoggedIn());
                PostsList.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getUsername());
                PostsList.getItems().add(imageView2);
                PostsList.getItems().add(btnNumber);
                PostsList.getItems().add(Showlikebtn);
                PostsList.getItems().add(Showcommentbtn);
                PostsList.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i));
                PostsList.getItems().add("_____________________________");

            }
        }
        clearTextandImg();

    }

    public void ChooseFile(ActionEvent actionEvent) throws IOException {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files","*txt"),
                new FileChooser.ExtensionFilter("Image Files","*.png","*.jpj","*.gif","*.jpeg"),
                new FileChooser.ExtensionFilter("Audio Files","*wav","*.mp3","*.aac"),
                new FileChooser.ExtensionFilter("All Files","*.*"));

        file =fileChooser.showOpenDialog(MenuChanger.getStage());
        LoginAccountPageController.setFinalFile(file);
        if(file != null){
            //desktop.open(file);
            FilePath.setText(file.getAbsolutePath());
            image = new Image(file.toURI().toString(),100,150,true,true);
            imageView.setImage(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);
        }

    }

    public void post(ActionEvent actionEvent) throws FileNotFoundException {
        if (LoggedInAccount.getInstance().getLoggedIn().isBusinessAccount()) {
            String content1 = "ad : ";
            String FinalContent = content1 + postContent.getText();
            LoggedInAccount.getInstance().getLoggedIn().createPost(FinalContent,finalFile);
        } else {
            String content = "content : "+postContent.getText();
            LoggedInAccount.getInstance().getLoggedIn().createPost(content,finalFile);
        }
        System.out.println("Post created!");

        for(int i=0;i<LoggedInAccount.getInstance().getLoggedIn().getPosts().size();i++){
            if (!PostsList.getItems().contains(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i))) {
                Button btnNumber = new Button();
                Button Showlikebtn = new Button();
                Button Showcommentbtn= new Button();
                btnNumber.setText("Like");
                Showlikebtn.setText("Show Likes");
                Showcommentbtn.setText("show comment(write a comment)");
                int finalI = i;
                btnNumber.setOnAction((ActionEvent)->{
                    likepost(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile());
                    Post post = Post.getPostByFile(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile());
                    clearTextandImg();
                });

                Showlikebtn.setOnAction((ActionEvent)->{
                    clearTextandImg();
                    Post post = Post.getPostByFile(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile());
                    if(post.getLike().size()==0){
                        AccountLikePost.getItems().add("no likes for this post :(");
                    }
                    else {
                        AccountLikePost.getItems().add("likes : "+post.getLike().size());
                        for (int j = 0; j < post.getLike().size(); j++) {
                           if (! AccountLikePost.getItems().contains(post.getLike().get(j).getUsername())) {
                                AccountLikePost.getItems().add(post.getLike().get(j).getUsername());
                                }
                        }
                    }
                });

                Showcommentbtn.setOnAction((ActionEvent)->{
                    clearTextandImg();
                    Post post = Post.getPostByFile(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile());
                    TextField textField = new TextField();
                    textField.setPromptText("write down your comment");
                    Button commentbtn = new Button();
                    commentbtn.setText("comment");
                    commentbtn.setOnAction((ActionEvent2)->{
                        commentpost(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile() , textField.getText());
                        clearTextandImg();
                    });

                    if(post.getComments().size()==0){
                        AccountComentList.getItems().add("no comments for this post :(");
                        for (int k=0;k<2;k++){
                            AccountComentList.getItems().add("");
                        }
                    }
                    else{
                        AccountComentList.getItems().add("comments : "+post.getComments().size());
                        for (int j=0;j<post.getComments().size();j++){
                            if (! AccountComentList.getItems().contains(post.getComments().get(j))) {
                                AccountComentList.getItems().add(post.getComments().get(j));
                                Button likeCommentbtn= new Button();
                                likeCommentbtn.setText("like comment");
                                AccountComentList.getItems().add(likeCommentbtn);
                                int finalJ = j;
                                likeCommentbtn.setOnAction((ActionEvent3)->{
                                    clearTextandImg();
                                    System.out.println(post.getComments().get(finalJ).getId());
                                    Comment comment = Comment.getCommentById(post.getComments().get(finalJ).getId());
                                    likecomment(comment);
                                });
                                AccountComentList.getItems().add("_____________________________");
                            }
                        }
                        for (int k=0;k<2;k++){
                            AccountComentList.getItems().add("");
                        }
                    }

                    AccountComentList.getItems().add(textField);
                    AccountComentList.getItems().add(commentbtn);

                });

                clearTextandImg();
                vboxForButtons.getChildren().add(btnNumber);
                image = new Image(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getFile().toURI().toString(), 100, 150, true, true);imageView2 = new ImageView();imageView2.setImage(image);imageView2.setFitWidth(100);imageView2.setFitHeight(150);imageView2.setPreserveRatio(true);imageView2.setSmooth(true);imageView2.setCache(true);

                LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).addview(LoggedInAccount.getInstance().getLoggedIn());
                PostsList.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getUsername());
                PostsList.getItems().add(imageView2);
                PostsList.getItems().add(btnNumber);
                PostsList.getItems().add(Showlikebtn);
                PostsList.getItems().add(Showcommentbtn);
                PostsList.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i));
                PostsList.getItems().add("_____________________________");

            }
        }
        clearTextandImg();
    }

    private void likecomment(Comment comment) {
        boolean success=true;
       // if(!Comment.getLikeUser().contains(LoggedInAccount.getInstance().getLoggedIn().getUsername())){
           // System.out.println("im in");
            new PopupMessage(Alert.AlertType.ERROR, "you  liked this comment!");
            success = comment.like(LoggedInAccount.getInstance().getLoggedIn());
       // }
        //else{

           // new PopupMessage(Alert.AlertType.ERROR, "you have already liked this comment!");
        //}
    }

    private void commentpost(File file, String text) {
        Post post = Post.getPostByFile(file);
        post.writeComment(text,LoggedInAccount.getInstance().getLoggedIn());
        new PopupMessage(Alert.AlertType.ERROR, "comment is written!");
       // System.out.println("Comment wrote!");

    }

    private void likepost(File file) {
        Post post = Post.getPostByFile(file);
        boolean success=true;
        success = post.like(LoggedInAccount.getInstance().getLoggedIn());
        if(post.getLike().contains(LoggedInAccount.getInstance().getLoggedIn())){
            System.out.println("ehem");
            new PopupMessage(Alert.AlertType.ERROR, "you have already liked this post!");

        }
        post.addlikestate(LoggedInAccount.getInstance().getLoggedIn());
        new PopupMessage(Alert.AlertType.ERROR, "you liked this post!");

    }

    public void clearTextandImg(){
        postContent.setText("");
        FilePath.setText("");
        imageView.setImage(null);
        AccountLikePost.getItems().clear();
        AccountComentList.getItems().clear();
    }

    public void logout(ActionEvent actionEvent) {
        MenuChanger.changeMenu("LoginMenu");
    }

    public void watchProfile(MouseEvent mouseEvent) {
        Account.getAccount(ShowAccounts.getSelectionModel().getSelectedItem().toString());
        Account account = Account.getAccount(ShowAccounts.getSelectionModel().getSelectedItem().toString());
        LoginAccountPageController.setAccount2(account);
        MenuChanger.changeMenu("watchProfilePane");
    }

}
