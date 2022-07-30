package com.example.demo.views;

import com.example.demo.model.LoggedInAccount;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;

public class CreatePostController {


    public TextField FilePath;
    public ImageView imageView;
    public TextField postContent;
    public TextField idTextbox;
    public ImageView imageView2;
    public Image image;
    public FileChooser fileChooser;
    public File file;
    public Circle ProCircle;

    public void initialize() {
        idTextbox.setText(LoggedInAccount.getInstance().getLoggedIn().getUsername());
        if(LoggedInAccount.getInstance().getLoggedIn().getAccountsFile().get(LoggedInAccount.getInstance().getLoggedIn())!=null){
            image = new Image(LoggedInAccount.getInstance().getLoggedIn().getAccountsFile().get(LoggedInAccount.getInstance().getLoggedIn()).toURI().toString(), 100, 150, true, true);
            ProCircle.setFill(new ImagePattern(image));
        }
    }

    public void ChooseFile(ActionEvent actionEvent) {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files","*txt"),
                new FileChooser.ExtensionFilter("Image Files","*.png","*.jpj","*.gif","*.jpeg"),
                new FileChooser.ExtensionFilter("Audio Files","*wav","*.mp3","*.aac"),
                new FileChooser.ExtensionFilter("All Files","*.*"));

        file =fileChooser.showOpenDialog(MenuChanger.getStage());
        LoginAccountPageController.setFinalFile(file);
        if(file != null){
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

    public void post(ActionEvent actionEvent) {
        if (LoggedInAccount.getInstance().getLoggedIn().isBusinessAccount()) {
            String content1 = "ad : ";
            String FinalContent = content1 + postContent.getText();
            LoggedInAccount.getInstance().getLoggedIn().createPost(FinalContent,LoginAccountPageController.getFinalFile());
        } else {
            String content = "content : "+postContent.getText();
            LoggedInAccount.getInstance().getLoggedIn().createPost(content,LoginAccountPageController.getFinalFile());
        }
        System.out.println("Post created!");
        new PopupMessage(Alert.AlertType.ERROR, "post created!");
        LoginAccountPageController.setFinalFile(null);

        clearTextandImg();
    }

    public void clearTextandImg(){
        postContent.setText("");
        FilePath.setText("");
        imageView.setImage(null);

    }

    public void BacktoYourAccount(ActionEvent actionEvent) {
        MenuChanger.changeMenu("LoginAccountPage");
    }

    public void gotoMainPage(ActionEvent actionEvent) {
        MenuChanger.changeMenu("MainPaneforLoginAccount");
    }
}
