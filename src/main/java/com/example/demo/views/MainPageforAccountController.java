package com.example.demo.views;

import com.example.demo.model.Account;
import com.example.demo.model.LoggedInAccount;
import com.example.demo.model.LoggedInPost;
import com.example.demo.model.Post;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.util.Iterator;

public class MainPageforAccountController {
    public TextField idTextbox;
    public TextField followersNum;
    public TextField followingsNum;
    public ListView FollowingsList;
    public ListView FollowingsRecentPost;
    public ListView showYourRecentPosts;
    public Image image;
    public ImageView imageView2;

    public void initialize() {
        idTextbox.setText(LoggedInAccount.getInstance().getLoggedIn().getUsername());
        followersNum.setText(String.valueOf(LoggedInAccount.getInstance().getLoggedIn().getNumberOfFollowers()));
        followingsNum.setText(String.valueOf(LoggedInAccount.getInstance().getLoggedIn().getNumberOfFollowings()));
        Iterator<Account> it = LoggedInAccount.getInstance().getLoggedIn().getFollowings().iterator();
        while (it.hasNext()) {
            FollowingsList.getItems().add(it.next().getUsername());
        }

        int size=LoggedInAccount.getInstance().getLoggedIn().getPosts().size();
        if(size<=2) {
            for (int i = size - 1; i >= 0; i--) {

                image = new Image(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getFile().toURI().toString(), 100, 150, true, true);
                imageView2 = new ImageView();imageView2.setImage(image);imageView2.setFitWidth(100);imageView2.setFitHeight(150);imageView2.setPreserveRatio(true);imageView2.setSmooth(true);imageView2.setCache(true);
                LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).addview(LoggedInAccount.getInstance().getLoggedIn());
                showYourRecentPosts.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getUsername());
                showYourRecentPosts.getItems().add(imageView2);
                showYourRecentPosts.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i));
                showYourRecentPosts.getItems().add("_____________________________");
            }
        }else{
            for (int i = size - 1; i >= size-2; i--) {
                clearTextandImg();
                image = new Image(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getFile().toURI().toString(), 100, 150, true, true);
                imageView2 = new ImageView();imageView2.setImage(image);imageView2.setFitWidth(100);imageView2.setFitHeight(150);imageView2.setPreserveRatio(true);imageView2.setSmooth(true);imageView2.setCache(true);
                LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).addview(LoggedInAccount.getInstance().getLoggedIn());
                showYourRecentPosts.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getUsername());
                showYourRecentPosts.getItems().add(imageView2);
                showYourRecentPosts.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i));
                showYourRecentPosts.getItems().add("_____________________________");
            }
        }

    }

    public void createPostsinAnotherPage(ActionEvent actionEvent) {
        MenuChanger.changeMenu("CreatePost");
    }

    public void logout(ActionEvent actionEvent) {
        MenuChanger.changeMenu("LoginMenu");
    }

    public void gotoLoginPage(ActionEvent actionEvent) {
        MenuChanger.changeMenu("LoginAccountPage");
    }

    public void followingListPressed(MouseEvent mouseEvent) {
        Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString());
        int size=Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().size();
        System.out.println(size);
        if(size<=2){
            clearTextandImg();
            for (int i = size - 1; i >= 0; i--) {
                image = new Image(Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i).getFile().toURI().toString(), 100, 150, true, true);
                imageView2 = new ImageView();imageView2.setImage(image);imageView2.setFitWidth(100);imageView2.setFitHeight(150);imageView2.setPreserveRatio(true);imageView2.setSmooth(true);imageView2.setCache(true);
                Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i).addview(LoggedInAccount.getInstance().getLoggedIn());
                FollowingsRecentPost.getItems().add(Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getUsername());
                FollowingsRecentPost.getItems().add(imageView2);
                FollowingsRecentPost.getItems().add(Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i));
                FollowingsRecentPost.getItems().add("_____________________________");
            }
        } else{
            clearTextandImg();
            for (int i = size - 1; i >= size-2; i--) {
                image = new Image(Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i).getFile().toURI().toString(), 100, 150, true, true);
                imageView2 = new ImageView();imageView2.setImage(image);imageView2.setFitWidth(100);imageView2.setFitHeight(150);imageView2.setPreserveRatio(true);imageView2.setSmooth(true);imageView2.setCache(true);
                Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i).addview(LoggedInAccount.getInstance().getLoggedIn());
                FollowingsRecentPost.getItems().add(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getUsername());
                FollowingsRecentPost.getItems().add(imageView2);
                FollowingsRecentPost.getItems().add(Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i));
                FollowingsRecentPost.getItems().add("_____________________________");
            }
        }

    }

    private void clearTextandImg() {
        FollowingsRecentPost.getItems().clear();
    }

    public void FollowinPostPressed(MouseEvent mouseEvent) {
    }

//    public void YourPostPressed(MouseEvent mouseEvent) {
//        System.out.println("selected");
//        if(showYourRecentPosts.getSelectionModel().getSelectedItem().equals()) {
//            System.out.println("imageview2");
//            File file= new File(imageView2.getImage().getUrl());
//            Post post = Post.getPostByFile(file);
//            System.out.println(post);
//            LoggedInPost.getInstance().setLoggedIn(post);
//            //MenuChanger.changeMenu("PostInfo");
//        }
//
//
//    }



}
