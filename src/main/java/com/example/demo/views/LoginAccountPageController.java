package com.example.demo.views;

import com.example.demo.model.Account;
import com.example.demo.model.LoggedInAccount;
import com.example.demo.model.Post;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
            //Post.fis = new FileInputStream(file);
            //FileInputStream fis =new FileInputStream(file);
            //image = new Image(file.toURI().toString(),100,150,true,true);
            String content1 = "ad : ";
            String FinalContent = content1 + "hi";
            LoggedInAccount.getInstance().getLoggedIn().createPost(FinalContent,finalFile);
        } else {
            //FileInputStream fis =new FileInputStream(file);
            // image = new Image(file.toURI().toString(),100,150,true,true);
            String content = "hi ";
            LoggedInAccount.getInstance().getLoggedIn().createPost(content,finalFile);
        }
        System.out.println("Post created!");


        for (Post post : LoggedInAccount.getInstance().getLoggedIn().getPosts()) {
            if (!PostsList.getItems().contains(post)) {
                image = new Image(finalFile.toURI().toString(), 100, 150, true, true);
                imageView2 = new ImageView();
                imageView2.setImage(image);
                imageView2.setFitWidth(100);
                imageView2.setFitHeight(150);
                imageView2.setPreserveRatio(true);
                imageView2.setSmooth(true);
                imageView2.setCache(true);

                System.out.println(post);
                System.out.println("_____________________________");
                post.addview(LoggedInAccount.getInstance().getLoggedIn());
                PostsList.getItems().add(post);

            }
        }
        clearTextandImg();
    }

    public void clearTextandImg(){
        FilePath.setText("");
        imageView.setPreserveRatio(false);
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
