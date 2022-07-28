package com.example.demo.views;

import com.example.demo.model.Account;
import com.example.demo.model.LoggedInAccount;
import com.example.demo.model.Post;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.File;

public class WatchProfileController {


    public TextField idTextbox;
    public TextField followersNum;
    public TextField followingsNum;
    public ListView PostsList;
    public ImageView imageView2;
    public VBox vboxForButtons = new VBox();
    public Image image;
    public ListView AccountLikePost;


    public void initialize() {
        idTextbox.setText(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getUsername());
        followersNum.setText(String.valueOf(LoginAccountPageController.getAccount2().getNumberOfFollowers()));
        followingsNum.setText(String.valueOf(LoginAccountPageController.getAccount2().getNumberOfFollowings()));

        for(int i=0;i<Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().size();i++){
                Button btnNumber = new Button();
            Button Showlikebtn = new Button();
            Showlikebtn.setText("Show Likes");
                btnNumber.setText("Like");
                int finalI = i;
            int finalI1 = i;
            btnNumber.setOnAction((ActionEvent)->{
                    likepost(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(finalI1).getFile());
                   clearTextandImg();
                });

            Showlikebtn.setOnAction((ActionEvent)->{
                clearTextandImg();
                Post post = Post.getPostByFile(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(finalI1).getFile());
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
            clearTextandImg();

                vboxForButtons.getChildren().add(btnNumber);
                image = new Image(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(finalI1).getFile().toURI().toString(), 100, 150, true, true);imageView2 = new ImageView();imageView2.setImage(image);imageView2.setFitWidth(100);imageView2.setFitHeight(150);imageView2.setPreserveRatio(true);imageView2.setSmooth(true);imageView2.setCache(true);
                Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(i).addview(LoggedInAccount.getInstance().getLoggedIn());
                PostsList.getItems().add(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getUsername());
                PostsList.getItems().add(imageView2);
                PostsList.getItems().add(btnNumber);
                PostsList.getItems().add(Showlikebtn);
                PostsList.getItems().add(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getPosts().get(i));
                PostsList.getItems().add("_____________________________");
        }
        clearTextandImg();
    }

    private void likepost(File file) {
        Post post = Post.getPostByFile(file);
        boolean success=true;
        success = post.like(LoggedInAccount.getInstance().getLoggedIn());
        if(post.getLike().contains(LoggedInAccount.getInstance().getLoggedIn())){
            new PopupMessage(Alert.AlertType.WARNING, "you have already liked this post!");
        }
        post.addlikestate(LoggedInAccount.getInstance().getLoggedIn());
        System.out.println(post);
    }

    public void clearTextandImg(){
        AccountLikePost.getItems().clear();
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
}
