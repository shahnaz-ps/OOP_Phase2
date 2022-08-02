package com.example.demo.views;

import com.example.demo.ConsoleApplication;
import com.example.demo.model.Account;
import com.example.demo.model.GroupChat;
import com.example.demo.model.LoggedInAccount;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;

public class CreateGroupController {

    public TextField groupName;
    public ListView listOfFollowers;
    public ListView listOfMembers;
    public TextField groupName_addUser;
    public ChoiceBox notJoinedList;
    public TextField groupName_rename;
    public TextField newGroupName_rename;
    public TextField groupName_profile;

    public void initialize() {
//        for (Account follower : LoggedInAccount.getInstance().getLoggedIn().getFollowers()) {
//            listOfFollowers.getItems().add(follower.getUsername());
//        }
        for (Account account : Account.getAccounts().values()) { //bayad follower ha bashe
            if (LoggedInAccount.getInstance().getLoggedIn() != account) {
                listOfFollowers.getItems().add(account.getUsername());
            }
        }
    }

    public void addThisAccountToMembers(MouseEvent mouseEvent) {
        Account account = Account.getAccount(listOfFollowers.getSelectionModel().getSelectedItem().toString());
        boolean added = true;
        for (Object member : listOfMembers.getItems()) {
            if (member.toString().equals(account.getUsername())) {
                added = false;
            }
        }
        if (added) {
            listOfMembers.getItems().add(account.getUsername());
        }
    }

    public void createGroup(ActionEvent actionEvent) {
        String name = groupName.getText();
        if (name.equals("")) {
            new PopupMessage(Alert.AlertType.ERROR, "Enter the name of the group!");
            return;
        }
        GroupChat groupChat = new GroupChat(LoggedInAccount.getInstance().getLoggedIn(), name);
        for (Object member : listOfMembers.getItems()) {
            Account account = Account.getAccount(member.toString());
            groupChat.getAccounts().add(account);
            //System.out.println("added");
        }
        //System.out.println("created");
        new PopupMessage(Alert.AlertType.INFORMATION, "Group Created!");
        listOfMembers.getItems().clear();
        groupName.clear();
    }

    public void back(MouseEvent mouseEvent) {
        MenuChanger.changeMenu("ChatMenu");
    }

    public void addUser(ActionEvent actionEvent) {
        GroupChat groupChat = GroupChat.getGroupChatByName(groupName_addUser.getText());
        Account account = Account.getAccount(notJoinedList.getValue().toString());
        groupChat.getAccounts().add(account);
        new PopupMessage(Alert.AlertType.INFORMATION, "User added!");
        groupName_addUser.clear();
        notJoinedList.getItems().clear();
    }

    public void searchGroup(ActionEvent actionEvent) {
        if (!groupName_addUser.getText().equals("")) {
            if (GroupChat.getGroupChatByName(groupName_addUser.getText()) == null) {
                new PopupMessage(Alert.AlertType.ERROR, "Not Founded!");
                groupName_addUser.clear();
            } else {
                GroupChat groupChat = GroupChat.getGroupChatByName(groupName_addUser.getText());
                if (groupChat.getOwner() != LoggedInAccount.getInstance().getLoggedIn()) {
                    new PopupMessage(Alert.AlertType.ERROR, "You can't rename this group!");
                    groupName_addUser.clear();
                } else {
                    for (Account account : Account.getAccounts().values()) { // bayad follower ha bashe
                        if (!groupChat.getAccounts().contains(account)) {
                            notJoinedList.getItems().add(account.getUsername());
                        }
                    }
                }
            }
        }
    }

    public void rename(ActionEvent actionEvent) {
        if (!groupName_rename.getText().equals("") && !newGroupName_rename.getText().equals("")) {
            if (GroupChat.getGroupChatByName(groupName_rename.getText()) == null) {
                new PopupMessage(Alert.AlertType.ERROR, "Group Not Exist!");
            } else {
                GroupChat groupChat = GroupChat.getGroupChatByName(groupName_rename.getText());
                if (groupChat.getOwner() != LoggedInAccount.getInstance().getLoggedIn()) {
                    new PopupMessage(Alert.AlertType.ERROR, "You can't rename this group!");
                } else {
                    if (GroupChat.isExist(newGroupName_rename.getText())){
                        new PopupMessage(Alert.AlertType.ERROR, "This name is not available!");
                    } else {
                        groupChat.setName(newGroupName_rename.getText());
                        new PopupMessage(Alert.AlertType.INFORMATION, "Renamed!");
                    }
                }
            }
        } else {
            new PopupMessage(Alert.AlertType.ERROR, "Error!");
        }
        groupName_rename.clear();
        newGroupName_rename.clear();
    }

    public void setProfile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG, PNG, JEPG Files", "*.jpg", "*.png", "*.jepg"));
        File selectedFile = fc.showOpenDialog(MenuChanger.getStage());
        if (selectedFile != null && GroupChat.isExist(groupName_profile.getText())) {
            GroupChat groupChat = GroupChat.getGroupChatByName(groupName_profile.getText());
            groupChat.setFile(selectedFile);
//            this.picMessage = selectedFile;
//            sendMessageMode.setImage(new Image(String.valueOf(
//                    new URL(ConsoleApplication.class.getResource("/Image/Menu/camera.png").toString()))));
        } else {
            new PopupMessage(Alert.AlertType.ERROR, "file is not valid");
        }
        groupName_profile.clear();
    }
}
