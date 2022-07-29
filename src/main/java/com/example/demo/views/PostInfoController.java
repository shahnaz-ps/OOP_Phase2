package com.example.demo.views;

import com.example.demo.model.LoggedInAccount;
import com.example.demo.model.LoggedInPost;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PostInfoController {
    public ListView PostsList;
    public ImageView imageView2;
    public ListView AccountLikePost;
    public ListView AccountComentList;
    public TextField idTextbox;
    public Image image;

    public void initialize() {
        idTextbox.setText(LoggedInAccount.getInstance().getLoggedIn().getUsername());
        image = new Image(LoggedInPost.getInstance().getLoggedIn().getFile().toURI().toString(), 100, 150, true, true);
        imageView2 = new ImageView();
        imageView2.setImage(image);
        imageView2.setFitWidth(100);
        imageView2.setFitHeight(150);
        imageView2.setPreserveRatio(true);
        imageView2.setSmooth(true);
        imageView2.setCache(true);
        LoggedInPost.getInstance().getLoggedIn().addview(LoggedInAccount.getInstance().getLoggedIn());
        PostsList.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getUsername());
        PostsList.getItems().add(imageView2);
//        PostsList.getItems().add(btnNumber);
//        PostsList.getItems().add(Showlikebtn);
//        PostsList.getItems().add(Showcommentbtn);
        PostsList.getItems().add(LoggedInPost.getInstance().getLoggedIn());
        PostsList.getItems().add("_____________________________");
    }

    public void gotoMainPage(ActionEvent actionEvent) {
        MenuChanger.changeMenu("MainPaneforLoginAccount");
    }
}
