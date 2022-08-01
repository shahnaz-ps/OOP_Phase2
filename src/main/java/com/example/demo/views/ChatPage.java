package com.example.demo.views;

import com.example.demo.ConsoleApplication;
import com.example.demo.model.*;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatPage {

    private static ChatPage instance;

    public static void setNull() {
        instance = null;
    }

    public static ChatPage getInstance() {
        if (instance == null) instance = new ChatPage();
        return instance;
    }

    private int chatMode = 0;// 1: private chat, 2: groupChat todo : change --> 0 nothing
    private PrivateChat privateChat;
    private GroupChat groupChat;

    @FXML
    private HBox beforeScrollPaneHBox;
    @FXML
    private ScrollPane suggestionScrollPane;
    @FXML
    private TextField searchTextField;
    @FXML
    private VBox suggestionVBox;
    @FXML
    private ImageView photoOfChat;
    @FXML
    public Label labelOfDataChat;
    @FXML
    private HBox dataOfChatHBox;
    @FXML
    private ImageView searchButton;
    @FXML
    private ImageView sendMessageIcon;
    @FXML
    private HBox searchPanel;
    @FXML
    private VBox mainVBox;
    @FXML
    private Button changeToRoomChatButton;
    @FXML
    private Button changeToPrivateChatButton;
    @FXML
    private VBox rightBarVBox;
    private boolean isRightBarVBoxOpen = false;
    @FXML
    private Button changeMessageButton;
    @FXML
    private TextField editTextField;
    @FXML
    private VBox editMessage;
    @FXML
    private Button deleteForMe;
    @FXML
    private Button deleteForEveryone;
    @FXML
    private VBox deleteMessage;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox allMessages;
    @FXML
    private TextField textToSend;

    public void initialize() throws MalformedURLException {

    }

    private void setSelectedRightBoxButtonStyle(Button button) {
        button.setStyle("-fx-background-color: #aca9a9;" +
                "-fx-border-radius: 30 30 30 30;" +
                "-fx-background-radius: 30 30 30 30;");
    }

    private void setNotSelectedRightBoxButtonStyle(Button button) {
        button.setStyle("-fx-background-color: #efeaea;" +
                "-fx-border-radius: 30 30 30 30;" +
                "-fx-background-radius: 30 30 30 30;");
    }


    public void showPrivateChats() throws MalformedURLException {
        mainVBox.getChildren().clear();
        mainVBox.getChildren().add(searchPanel);
        searchTextField.setVisible(false);
        mainVBox.getChildren().add(scrollPane);
        allMessages.getChildren().clear();
        textToSend.setVisible(false);
        sendMessageIcon.setVisible(false);
        showUserAllPrivateChats();
        searchButton.setOnMouseClicked(mouseEvent1 -> {
            searchTextField.setVisible(true);
            searchTextField.requestFocus();
            startForSearchingPrivateChat();
        });
    }


    private void goToAPrivateChat() throws MalformedURLException {
        mainVBox.getChildren().clear();
        mainVBox.getChildren().add(dataOfChatHBox);
        dataOfChatHBox.setPrefHeight(50);
        mainVBox.getChildren().add(scrollPane);
        scrollPane.setPrefHeight(570);
        allMessages.getChildren().clear();
        setDataOfChatHBoxPrivateChat(privateChat.getOtherUser(LoggedInAccount.getInstance().getLoggedIn()));
        textToSend.setVisible(true);
        sendMessageIcon.setVisible(true);
        showMessagesOfPrivateChat();
    }

    private void goToARoomChat() throws MalformedURLException {
        mainVBox.getChildren().clear();
        mainVBox.getChildren().add(dataOfChatHBox);
        dataOfChatHBox.setPrefHeight(50);
        beforeScrollPaneHBox.getChildren().clear();
        mainVBox.getChildren().add(scrollPane);
        scrollPane.setPrefHeight(570);
        allMessages.getChildren().clear();
        setDataOfChatHBoxRoomChat();
        textToSend.setVisible(true);
        sendMessageIcon.setVisible(true);
        showMessagesOfRoomChat();
    }

    private void showMessagesOfRoomChat() throws MalformedURLException {
        for (int i = 0; i < groupChat.getMessages().size(); i++) {
            Message message = groupChat.getMessages().get(i);
            // todo -->
//            if (!(message.getAccount() == LoggedInAccount.getInstance().getLoggedIn() && message.isDeletedForUser()))
                sendNewMessage(message);
//            if (message.getAccount() != LoggedInAccount.getInstance().getLoggedIn())
//                message.setSeen(true);
        }
    }

    private void setDataOfChatHBoxRoomChat() throws MalformedURLException {
        labelOfDataChat.setText(groupChat.getName());
        photoOfChat.setImage(new Image(String.valueOf(
                new URL(ConsoleApplication.class.getResource("/Image/Menu/groupChat.png").toString()))));
        photoOfChat.setCursor(Cursor.HAND);
        AtomicBoolean isUsersShowed = new AtomicBoolean(false);
        photoOfChat.setOnMouseClicked(mouseEvent -> {
            try {
                if (isUsersShowed.get()) {
                    isUsersShowed.set(false);
                    goToARoomChat();
                } else {
                    isUsersShowed.set(true);
                    showUsersInRoom();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
    }

    private void showUsersInRoom() throws MalformedURLException {
        textToSend.setVisible(false);
        sendMessageIcon.setVisible(false);
        allMessages.getChildren().clear();
        for (Account account : groupChat.getAccounts())
            addParticipant(account);
    }

    private void addParticipant(Account account) throws MalformedURLException {
        Pane pane = new Pane();
        pane.setPrefHeight(50);
        pane.setStyle("-fx-background-color: #dfc10c;" +
                "-fx-border-radius: 30 30 30 30;" +
                "-fx-background-radius: 30 30 30 30;");
        addLabelToSuggestionPane(pane, account.getUsername());
//        addAvatarToSuggestionPane(pane, account);
        allMessages.getChildren().add(pane);
        if (account != LoggedInAccount.getInstance().getLoggedIn()) {
            pane.setCursor(Cursor.HAND);
            pane.setOnMouseClicked(mouseEvent -> {
                try {
                    this.privateChat = ChatController.getInstance().getAccountPrivateChat(account);
                    chatMode = 1;
                    setSelectedRightBoxButtonStyle(changeToPrivateChatButton);
                    setNotSelectedRightBoxButtonStyle(changeToRoomChatButton);
                    goToAPrivateChat();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void setDataOfChatHBoxPrivateChat(Account account) throws MalformedURLException {
        labelOfDataChat.setText(privateChat.getOtherUser(LoggedInAccount.getInstance().getLoggedIn()).getUsername());
        photoOfChat.setVisible(true);
        photoOfChat.setImage(new Image(String.valueOf(
                new URL(ConsoleApplication.class.getResource("/Image/Menu/room.png").toString()))));
        photoOfChat.setCursor(Cursor.DEFAULT);
        photoOfChat.setOnMouseClicked(null);
    }

    private void showMessagesOfPrivateChat() throws MalformedURLException {
        for (int i = 0; i < privateChat.getMessages().size(); i++) {
            Message message = privateChat.getMessages().get(i);
            // todo -->
//            if (!(message.getAccount() == LoggedInAccount.getInstance().getLoggedIn() && message.isDeletedForUser()))
                sendNewMessage(message);
//            if (message.getAccount() != LoggedInAccount.getInstance().getLoggedIn())
//                message.setSeen(true);
        }
    }

    private void showUserAllPrivateChats() throws MalformedURLException {
        for (int i = 0; i < LoggedInAccount.getInstance().getLoggedIn().getPrivateChats().size(); i++)
            addAPrivateChat(LoggedInAccount.getInstance().getLoggedIn().getPrivateChats().get(i));
    }

    public void showRoomChats() throws MalformedURLException {
        mainVBox.getChildren().clear();
        mainVBox.getChildren().add(searchPanel);
        searchTextField.setVisible(false);
        mainVBox.getChildren().add(scrollPane);
        allMessages.getChildren().clear();
        textToSend.setVisible(false);
        sendMessageIcon.setVisible(false);
        showUserAllRooms();
//        searchButton.setOnMouseClicked(mouseEvent1 -> {
//            searchTextField.setVisible(true);
//            searchTextField.requestFocus();
//
//        });
    }

    private void showUserAllRooms() throws MalformedURLException {
        for (int i = 0; i < GroupChat.getUserGroups(LoggedInAccount.getInstance().getLoggedIn()).size(); i++)
            addARoomChat(GroupChat.getUserGroups(LoggedInAccount.getInstance().getLoggedIn()).get(i));
    }

    private void addARoomChat(GroupChat groupChat) throws MalformedURLException {
        Pane pane = new Pane();
        setMessagePaneSize(pane);
        setPrivateAndRoomChatPaneStyle(pane);
        addLabelPrivateOrRoomChat(pane, groupChat.getName());
        if (groupChat.getMessages().size() > 0)
            showLastMessage(pane, groupChat.getMessages().get(groupChat.getMessages().size() - 1).getContent());
        pane.setCursor(Cursor.HAND);
        pane.setOnMouseClicked(mouseEvent -> {
            try {
                this.groupChat = groupChat;
                goToARoomChat();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
        allMessages.getChildren().add(pane);
    }


    public void enter(KeyEvent keyEvent) throws MalformedURLException {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            keyEvent.consume();
            send(null);
        }
    }

    public void back(MouseEvent mouseEvent) {
        // todo -->
//        MenuChanger.changeMenu("");
    }

    public void send(MouseEvent mouseEvent) throws MalformedURLException {
        if (!ChatController.getInstance().isValidShortMessage(textToSend.getText())) {
            new PopupMessage(Alert.AlertType.ERROR, "empty text!");
        } else if (!ChatController.getInstance().isValidLongMessage(textToSend.getText()))
            new PopupMessage(Alert.AlertType.ERROR, "long message");
        else {
            Message message = new Message(LoggedInAccount.getInstance().getLoggedIn(), textToSend.getText());
//            if (chatMode == 0)
//                PublicChat.getInstance().addMessage(message);
            // else
            if (chatMode == 1)
                this.privateChat.getMessages().add(message);
            else if (chatMode == 2)
                this.groupChat.getMessages().add(message);
            sendNewMessage(message);
            textToSend.clear();
        }
    }

    private void sendNewMessage(Message message) throws MalformedURLException {
        Pane pane = getMessageBox(message);
        allMessages.getChildren().add(pane);
    }

    private Pane getMessageBox(Message message) throws MalformedURLException {
        Pane pane = new Pane();
        setMessagePaneSize(pane);
//        addUserAvatar(pane, message.getAccount());
        addText(pane, message.getContent());
        addUserUsername(pane, message.getAccount());
        addClock(pane, message.getDate().toString());
//        if (message.getAccount() == LoggedInAccount.getInstance().getLoggedIn())
//            addSeenUnSeen(pane, message.isSeen());
        if (message.getAccount() == LoggedInAccount.getInstance().getLoggedIn())
            addButtonToDelete(pane, message);
        if (message.getAccount() == LoggedInAccount.getInstance().getLoggedIn())
            addButtonToEdit(pane, message);
        return pane;
    }

    private void addButtonToEdit(Pane pane, Message message) throws MalformedURLException {
        ImageView imageView = new ImageView(new Image(String.valueOf(
                new URL(ConsoleApplication.class.getResource("/Image/Menu/edit.png").toString()))));
        imageView.setFitHeight(16);
        imageView.setFitWidth(16);
        imageView.setLayoutX(288);
        imageView.setLayoutY(14);
        imageView.setCursor(Cursor.HAND);
        AtomicBoolean isSelectedForEdit = new AtomicBoolean(true);
        imageView.setOnMouseClicked(mouseEvent -> {
            if (isSelectedForEdit.get()) {
                isSelectedForEdit.set(false);
                pane.getChildren().add(editMessage);
                editMessage.setLayoutX(330);
                editMessage.setLayoutY(0);
                editTextField.setText(message.getContent());
                editMessageClicked(pane, isSelectedForEdit, message);
                editMessage.setVisible(true);
                editTextField.requestFocus();
            } else {
                isSelectedForEdit.set(true);
                pane.getChildren().remove(editMessage);
            }
        });
        pane.getChildren().add(imageView);
    }

    private void editMessageClicked(Pane pane, AtomicBoolean isSelectedForEdit, Message message) {
        editTextField.setOnKeyPressed(k -> {
            if (k.getCode().equals(KeyCode.ENTER)) {
                if (!ChatController.getInstance().isValidLongMessage(editTextField.getText()))
                    new PopupMessage(Alert.AlertType.ERROR, "long message");
                else if (!ChatController.getInstance().isValidShortMessage(editTextField.getText()))
                    new PopupMessage(Alert.AlertType.ERROR, "empty text!");
                else {
                    isSelectedForEdit.set(true);
                    if (pane.getChildren().get(1) instanceof Label) {
                        ((Label) pane.getChildren().get(1)).setText(editTextField.getText());
                    }
                    message.setContent(editTextField.getText());
                    pane.getChildren().remove(editMessage);
                }
            }
        });
        changeMessageButton.setOnMouseClicked(mouseEvent -> {
            if (editTextField.getText().length() > 20)
                new PopupMessage(Alert.AlertType.ERROR, "long message!");
            else {
                isSelectedForEdit.set(true);
                if (pane.getChildren().get(1) instanceof Label) {
                    ((Label) pane.getChildren().get(1)).setText(editTextField.getText());
                }
                message.setContent(editTextField.getText());
                pane.getChildren().remove(editMessage);
            }
        });
    }

    private void addButtonToDelete(Pane pane, Message message) throws MalformedURLException {
        ImageView imageView = new ImageView(new Image(String.valueOf(
                new URL(ConsoleApplication.class.getResource("/Image/Menu/blackCross.png").toString()))));
        imageView.setFitHeight(15);
        imageView.setFitWidth(15);
        imageView.setLayoutX(310);
        imageView.setLayoutY(15);
        imageView.setCursor(Cursor.HAND);
        AtomicBoolean isSelectedForDelete = new AtomicBoolean(true);
        imageView.setOnMouseClicked(mouseEvent -> {
            if (isSelectedForDelete.get()) {
                isSelectedForDelete.set(false);
                pane.getChildren().add(deleteMessage);
                deleteMessage.setLayoutX(330);
                deleteMessage.setLayoutY(0);
                deleteMessageClicked(pane, isSelectedForDelete, message);
                deleteMessage.setVisible(true);
            } else {
                isSelectedForDelete.set(true);
                pane.getChildren().remove(deleteMessage);
            }
        });
        pane.getChildren().add(imageView);
    }

    private void deleteMessageClicked(Pane pane, AtomicBoolean isSelectedForDelete, Message message) {
        deleteForEveryone.setOnMouseClicked(mouseEvent -> {
            isSelectedForDelete.set(true);
            pane.getChildren().remove(deleteMessage);
            allMessages.getChildren().remove(pane);
            if (chatMode == 1)
                privateChat.getMessages().remove(message);
            else if (chatMode == 2)
                groupChat.getMessages().remove(message);
        });
        deleteForMe.setOnMouseClicked(mouseEvent -> {
            isSelectedForDelete.set(true);
            pane.getChildren().remove(deleteMessage);
            allMessages.getChildren().remove(pane);
//            message.setDeletedForUser(true);
        });
    }

    private void addSeenUnSeen(Pane pane, boolean isSeen) throws MalformedURLException {
        ImageView imageView;
        if (isSeen) {
            imageView = new ImageView(new Image(String.valueOf(
                    new URL(ConsoleApplication.class.getResource("/Image/Menu/seen.png").toString()))));
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            imageView.setLayoutX(305);
            imageView.setLayoutY(45);
        } else {
            imageView = new ImageView(new Image(String.valueOf(
                    new URL(ConsoleApplication.class.getResource("/Image/Menu/unseen.png").toString()))));
            imageView.setFitHeight(10);
            imageView.setFitWidth(10);
            imageView.setLayoutX(300);
            imageView.setLayoutY(50);
        }
        pane.getChildren().add(imageView);
    }

    private void addClock(Pane pane, String stringClock) {
        Label clock = new Label(stringClock);
        clock.setPrefHeight(60);
        clock.setPrefWidth(60);
        clock.setLayoutX(260);
        clock.setLayoutY(25);
        pane.getChildren().add(clock);
    }

    private void addText(Pane pane, String message) {
        Label text = new Label(message);
        text.setPrefHeight(60);
        text.setPrefWidth(230);
        text.setLayoutX(40);
        text.setLayoutY(15);
        text.setFont(Font.font(18));
        pane.getChildren().add(text);
    }

    private void addUserUsername(Pane pane, Account account) {
        Label label = new Label("name : " + account.getUsername());
        label.setPrefHeight(20);
        label.setLayoutX(40);
        label.setLayoutY(10);
        label.setFont(Font.font(12));
        label.setStyle("-fx-font-family: \"High Tower Text\"");
        pane.getChildren().add(label);
    }

    // todo -->
//    private void addUserAvatar(Pane pane, Account account) throws MalformedURLException {
//        ImageView imageView = new ImageView(new Image(String.valueOf(
//                account.getAvatarURL())));
//        imageView.setFitHeight(30);
//        imageView.setFitWidth(30);
//        imageView.setX(5);
//        imageView.setY(25);
//        pane.getChildren().add(imageView);
//    }

    private void setMessagePaneSize(Pane pane) {
        pane.setPrefWidth(100);
        pane.setPrefHeight(80);
        pane.setStyle("-fx-border-radius: 30 30 30 30;" +
                "-fx-background-radius: 30 30 30 30;" +
                "-fx-background-color: #927819;");
    }

    // chatMode
    public void changeToPrivate(MouseEvent mouseEvent) throws MalformedURLException {
        if (chatMode != 1) {
            chatMode = 1;
//            setNotSelectedRightBoxButtonStyle(changeToPublicChatButton);
            setSelectedRightBoxButtonStyle(changeToPrivateChatButton);
            setNotSelectedRightBoxButtonStyle(changeToRoomChatButton);
            showPrivateChats();
        }
    }

    public void startForSearchingPrivateChat() {
        mainVBox.getChildren().clear();
        mainVBox.getChildren().add(searchPanel);
        mainVBox.getChildren().add(suggestionScrollPane);
        searchTextField.setOnKeyReleased(keyEvent -> {
            try {
                showSuggestionPrivateChats();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
        searchButton.setOnMouseClicked(mouseEvent -> {
            try {
                showPrivateChats();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
    }

    public void changeToRoom(MouseEvent mouseEvent) throws MalformedURLException {
        if (chatMode != 2) {
            chatMode = 2;
            setNotSelectedRightBoxButtonStyle(changeToPrivateChatButton);
            setSelectedRightBoxButtonStyle(changeToRoomChatButton);
            showRoomChats();
        }
    }

    //private chat
    private void addAPrivateChat(PrivateChat privateChat) throws MalformedURLException {
        Pane pane = new Pane();
        setMessagePaneSize(pane);
        setPrivateAndRoomChatPaneStyle(pane);
//        addUserAvatar(pane, privateChat.getOtherUser(LoggedInAccount.getInstance().getLoggedIn()));
        addLabelPrivateOrRoomChat(pane, privateChat.getOtherUser(LoggedInAccount.getInstance().getLoggedIn()).getUsername());
        if (privateChat.getMessages().size() > 0)
            showLastMessage(pane, privateChat.getMessages().get(privateChat.getMessages().size() - 1).getContent());
        pane.setCursor(Cursor.HAND);
        pane.setOnMouseClicked(mouseEvent -> {
            try {
                this.privateChat = privateChat;
                goToAPrivateChat();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
        allMessages.getChildren().add(pane);
    }

    private void setPrivateAndRoomChatPaneStyle(Pane pane) {
        pane.setStyle("-fx-border-radius: 30 30 30 30;" +
                "-fx-background-radius: 30 30 30 30;" +
                "-fx-background-color: #48d816;");
    }

    private void showLastMessage(Pane pane, String message) {
        Label label = new Label(message);
        label.setPrefHeight(20);
        label.setLayoutX(50);
        label.setLayoutY(40);
        label.setStyle("-fx-font-family: \"High Tower Text\";" +
                "       -fx-font-size: 18");
        pane.getChildren().add(label);
    }

    private void addLabelPrivateOrRoomChat(Pane pane, String string) {
        Label label = new Label(string);
        label.setPrefHeight(20);
        label.setLayoutX(50);
        label.setLayoutY(0);
        label.setStyle("-fx-font-family: \"High Tower Text\";" +
                "       -fx-font-size: 24");
        pane.getChildren().add(label);
    }

    public void backFromChat(MouseEvent mouseEvent) throws MalformedURLException {
        if (chatMode == 1)
            showPrivateChats();
        else if (chatMode == 2) {
            showRoomChats();
        }
    }

    private void showSuggestionPrivateChats() throws MalformedURLException {
        suggestionVBox.getChildren().clear();
        if (searchTextField.getText().equals("")) {
            return;
        }
        ArrayList<Account> suggestionUsers = ChatController.getInstance().showUsernamesStartsWithString(searchTextField.getText());
        for (Account account : suggestionUsers)
            addSuggestionPane(account);
    }

    private void addSuggestionPane(Account account) throws MalformedURLException {
        Pane pane = new Pane();
        pane.setPrefHeight(50);
        pane.setStyle("-fx-background-color: #dfc10c;" +
                "-fx-border-radius: 30 30 30 30;" +
                "-fx-background-radius: 30 30 30 30;");
        addLabelToSuggestionPane(pane, account.getUsername());
        //todo -->
//        addAvatarToSuggestionPane(pane, account);
        pane.setCursor(Cursor.HAND);
        suggestionVBox.getChildren().add(pane);
        pane.setOnMouseClicked(mouseEvent -> {
            try {
                this.privateChat = ChatController.getInstance().getAccountPrivateChat(account);
                goToAPrivateChat();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
    }

    private void addLabelToSuggestionPane(Pane pane, String username) {
        Label label = new Label(username);
        label.setPrefHeight(20);
        label.setLayoutX(70);
        label.setLayoutY(10);
        label.setStyle("-fx-font-family: \"High Tower Text\";" +
                "       -fx-font-size: 18");
        pane.getChildren().add(label);
    }

    //todo -->
//    private void addAvatarToSuggestionPane(Pane pane, Account account) throws MalformedURLException {
//        ImageView imageView = new ImageView(new Image(String.valueOf(
//                account.getAvatarURL())));
//        imageView.setFitHeight(30);
//        imageView.setFitWidth(30);
//        imageView.setLayoutX(20);
//        imageView.setLayoutY(8);
//        pane.getChildren().add(imageView);
//    }

    public void openRightVBox(MouseEvent mouseEvent) {
        if (isRightBarVBoxOpen) {
            isRightBarVBoxOpen = false;
            rightBarVBox.setVisible(false);
        } else {
            isRightBarVBoxOpen = true;
            rightBarVBox.setVisible(true);
        }
    }
}