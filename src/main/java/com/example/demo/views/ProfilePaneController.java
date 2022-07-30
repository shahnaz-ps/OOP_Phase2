package com.example.demo.views;

import com.example.demo.model.LoggedInAccount;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class ProfilePaneController {
    public TextField idTextbox;
    public TextField followersNum;
    public TextField followingsNum;
    public TextField FilePath;
    public ImageView imageView;
    public ImageView imageView2;
    public Image image;
    public FileChooser fileChooser;
    public File file;
    public static File finalFile2;
    public ImageView imageView3;
    public Circle ProCircle;

    public static File getFinalFile() {
        return finalFile2;
    }

    public static void setFinalFile(File finalFile) {
        ProfilePaneController.finalFile2= finalFile;
    }

    public void initialize() {
        idTextbox.setText(LoggedInAccount.getInstance().getLoggedIn().getUsername());
        followersNum.setText(String.valueOf(LoggedInAccount.getInstance().getLoggedIn().getNumberOfFollowers()));
        followingsNum.setText(String.valueOf(LoggedInAccount.getInstance().getLoggedIn().getNumberOfFollowings()));
        if(LoggedInAccount.getInstance().getLoggedIn().getAccountsFile().get(LoggedInAccount.getInstance().getLoggedIn())!=null){
            image = new Image(LoggedInAccount.getInstance().getLoggedIn().getAccountsFile().get(LoggedInAccount.getInstance().getLoggedIn()).toURI().toString(), 100, 150, true, true);
            ProCircle.setFill(new ImagePattern(image));
        }
    }


    public void ChooseFile(ActionEvent actionEvent) throws IOException {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files","*txt"),
                new FileChooser.ExtensionFilter("Image Files","*.png","*.jpj","*.gif","*.jpeg"),
                new FileChooser.ExtensionFilter("Audio Files","*wav","*.mp3","*.aac"),
                new FileChooser.ExtensionFilter("All Files","*.*"));

        file =fileChooser.showOpenDialog(MenuChanger.getStage());
        ProfilePaneController.setFinalFile(file);
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

    public void SubmitProPic(ActionEvent actionEvent) {
        System.out.println(finalFile2);
        LoggedInAccount.getInstance().getLoggedIn().getAccountsFile().replace(LoggedInAccount.getInstance().getLoggedIn(),finalFile2);
        System.out.println(LoggedInAccount.getInstance().getLoggedIn().getAccountsFile().containsValue(finalFile2));
        System.out.println(LoggedInAccount.getInstance().getLoggedIn().getAccountsFile().get(LoggedInAccount.getInstance().getLoggedIn()));
        new PopupMessage(Alert.AlertType.ERROR, "you have picked your profile pic!");
        if(finalFile2 != null){
            System.out.println("profile");
            image = new Image(LoggedInAccount.getInstance().getLoggedIn().getAccountsFile().get(LoggedInAccount.getInstance().getLoggedIn()).toURI().toString(), 100, 150, true, true);
            imageView3 = new ImageView();imageView3.setImage(image);imageView3.setFitWidth(100);imageView3.setFitHeight(150);imageView3.setPreserveRatio(true);imageView3.setSmooth(true);imageView3.setCache(true);
            ProCircle.setFill(new ImagePattern(image));
        }
        clearTextandImg();

    }


    public void clearTextandImg(){
        FilePath.setText("");
        imageView.setImage(null);
    }


    public void gotoMainPage(ActionEvent actionEvent) {
        MenuChanger.changeMenu("MainPaneforLoginAccount");
    }
}
