package com.example.demo.views;

import com.example.demo.model.Account;
import com.example.demo.model.LoggedInAccount;
import com.example.demo.model.LoggedInPost;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

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
