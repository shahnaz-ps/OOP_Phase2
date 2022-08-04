package com.example.demo.views;

import com.example.demo.model.Account;
import com.example.demo.model.BusinessAccount;
import com.example.demo.model.LoggedInAccount;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.net.MalformedURLException;

public class LoginPage {

    @FXML
    private TextField usernameFieldSignUp;
    @FXML
    private PasswordField passwordFieldSignUp;
    @FXML
    private TextField usernameFieldLogin;
    @FXML
    private PasswordField passwordFieldLogin;
    @FXML
    private CheckBox isBusiness;
    private Account account;


    @FXML
    public void initialize() {
        usernameFieldSignUp.setOnKeyPressed(k -> {
            if (k.getCode().equals(KeyCode.ENTER)) passwordFieldSignUp.requestFocus();
        });
        passwordFieldSignUp.setOnKeyPressed(k -> {
            if (k.getCode().equals(KeyCode.ENTER)) {
                try {
                    registerUser();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
        usernameFieldLogin.setOnKeyPressed(k -> {
            if (k.getCode().equals(KeyCode.ENTER)) {
                passwordFieldLogin.requestFocus();
            }
        });
        passwordFieldLogin.setOnKeyPressed(k -> {
            if (k.getCode().equals(KeyCode.ENTER)) {
                try {
                    loginUser();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void registerUser() throws MalformedURLException {
        String username = usernameFieldSignUp.getText();
        String password = passwordFieldSignUp.getText();
        boolean business = isBusiness.isSelected();
        while (true) {
            if (Account.usernameExists(username)) {
                new PopupMessage(Alert.AlertType.ERROR, "Username is not available, Please try again!");
                usernameFieldSignUp.clear();
                passwordFieldSignUp.clear();
                return;
            } else
                break;
        }
        while (true) {
            if (!passwordIsStrong(password)) {
                new PopupMessage(Alert.AlertType.ERROR, "Password is weak, Please try again!");
                usernameFieldSignUp.clear();
                passwordFieldSignUp.clear();
                return;
            } else
                break;
        }
        if (business) {
            account = BusinessAccount.createAccount(username, password);
            System.out.println(account);
            BusinessAccount.createAccount(username, password);
            System.out.println("b");
        } else {
            account = Account.createAccount(username, password);
            Account.createAccount(username, password);
        }
        new PopupMessage(Alert.AlertType.INFORMATION, "registered user!");
        usernameFieldSignUp.clear();
        passwordFieldSignUp.clear();
        isBusiness.setSelected(false);
    }

    private boolean passwordIsStrong(String password) {
        return true || password.length() >= 8 &&
                !password.toLowerCase().equals(password) &&
                !password.toUpperCase().equals(password) &&
                password.matches(".*[0-9]+.*");
    }

    public void loginUser() {
        String username = usernameFieldLogin.getText();
        String password = passwordFieldLogin.getText();
        Account account = Account.checkLogin(username, password);
        if (account == null) {
            new PopupMessage(Alert.AlertType.ERROR, "Wrong username or password or username!" + "\n" + "Try again!");
            usernameFieldLogin.clear();
            passwordFieldLogin.clear();
            return;
        }
        this.account = account;
        if(account.isBusinessAccount()){
            System.out.println(account);
        }
        else
        {
            System.out.println("not bus");
        }

        LoggedInAccount.getInstance().setLoggedIn(account);
        MenuChanger.changeMenu("MainPaneforLoginAccount");
    }

    public void exit(MouseEvent mouseEvent) {
        Platform.exit();
    }

    public void playPauseMusic(MouseEvent mouseEvent) {

    }

    public void muteUnmuteMusic(MouseEvent mouseEvent) {

    }

    public void nextTrack(MouseEvent mouseEvent) {

    }
}
