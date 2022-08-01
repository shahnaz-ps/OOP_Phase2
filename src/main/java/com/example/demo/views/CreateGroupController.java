package com.example.demo.views;

import com.example.demo.model.Account;
import com.example.demo.model.GroupChat;
import com.example.demo.model.LoggedInAccount;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class CreateGroupController {

    public TextField groupName;
    public ListView listOfFollowers;
    public ListView listOfMembers;

    public void initialize(){
//        for (Account follower : LoggedInAccount.getInstance().getLoggedIn().getFollowers()) {
//            listOfFollowers.getItems().add(follower.getUsername());
//        }
        for (Account account : Account.getAccounts().values()) {
            if (LoggedInAccount.getInstance().getLoggedIn()!=account){
                listOfFollowers.getItems().add(account.getUsername());
            }
        }
    }
    
    public void addThisAccountToMembers(MouseEvent mouseEvent) {
        Account account = Account.getAccount(listOfFollowers.getSelectionModel().getSelectedItem().toString());
        boolean added = true;
        for (Object member : listOfMembers.getItems()) {
            if (member.toString().equals(account.getUsername())){
                added = false;
            }
        }
        if (added){
            listOfMembers.getItems().add(account.getUsername());
        }
    }

    public void createGroup(ActionEvent actionEvent) {
        String name = groupName.getText();
        if (name.equals("")){
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
        listOfMembers.getItems().clear();
        groupName.clear();
    }

    public void back(MouseEvent mouseEvent) {
        MenuChanger.changeMenu("ChatMenu");
    }
}
