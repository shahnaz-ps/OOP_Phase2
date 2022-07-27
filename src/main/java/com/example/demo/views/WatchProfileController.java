package com.example.demo.views;

import com.example.demo.model.Account;
import com.example.demo.model.LoggedInAccount;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class WatchProfileController {


    public TextField idTextbox;
    public TextField followersNum;
    public TextField followingsNum;
    public ListView PostsList;
    public ImageView imageView2;


    public void initialize() {
        idTextbox.setText(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getUsername());
        followersNum.setText(String.valueOf(LoginAccountPageController.getAccount2().getNumberOfFollowers()));
        followingsNum.setText(String.valueOf(LoginAccountPageController.getAccount2().getNumberOfFollowings()));

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
