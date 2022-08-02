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
import javafx.stage.FileChooser;

import java.io.File;
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

    private int chatMode = 0; // 1: private chat, 2: groupChat
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
    @FXML
    private ListView listOfChats;
    @FXML
    private ImageView sendMessageMode;
    @FXML
    private Button allChatsButton;


    private File picMessage;
    private Message repliedMessage = null;
    private Message forwardedMessage = null;

    public void initialize() throws MalformedURLException {
        mainVBox.getChildren().remove(searchPanel);
        rightBarVBox.setVisible(false);
        scrollPane.vvalueProperty().bind(allMessages.heightProperty());
        allMessages.setSpacing(5);
        for (PrivateChat privateChat : LoggedInAccount.getInstance().getLoggedIn().getPrivateChats()) {
            if (privateChat.getAccount1() == LoggedInAccount.getInstance().getLoggedIn()) {
                listOfChats.getItems().add(privateChat.getAccount2().getUsername());
            } else {
                listOfChats.getItems().add(privateChat.getAccount1().getUsername());
            }
        }
        for (GroupChat groupChat : GroupChat.getUserGroups(LoggedInAccount.getInstance().getLoggedIn())) {
            listOfChats.getItems().add(groupChat.getName());
        }
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
            sendNewMessage(message);
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
        addLabelToSuggestionPane(pane, account.getUsername(), true);
        allMessages.getChildren().add(pane);
        HBox hBox = (HBox) pane.getChildren().get(0);
        if (account == LoggedInAccount.getInstance().getLoggedIn()) {
            hBox.getChildren().remove(3);
            hBox.getChildren().remove(2);
            return;
        } else if (LoggedInAccount.getInstance().getLoggedIn() != this.groupChat.getOwner()) {
            hBox.getChildren().remove(3);
            hBox.getChildren().remove(2);
            hBox.getChildren().get(1).setCursor(Cursor.HAND);
            hBox.getChildren().get(1).setOnMouseClicked(mouseEvent -> {
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
            return;
        } else {
            hBox.getChildren().get(1).setCursor(Cursor.HAND);
            hBox.getChildren().get(1).setOnMouseClicked(mouseEvent -> {
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

            hBox.getChildren().get(2).setCursor(Cursor.HAND);
            hBox.getChildren().get(2).setOnMouseClicked(mouseEvent -> {
                this.groupChat.banUser(account);
            });


            hBox.getChildren().get(3).setCursor(Cursor.HAND);
            hBox.getChildren().get(3).setOnMouseClicked(mouseEvent -> {
                this.groupChat.removeUser(account);
            });
        }
    }

    private void setDataOfChatHBoxPrivateChat(Account account) throws MalformedURLException {
        labelOfDataChat.setText(privateChat.getOtherUser(LoggedInAccount.getInstance().getLoggedIn()).getUsername());
        photoOfChat.setVisible(true);
        photoOfChat.setImage(new Image(String.valueOf(
                new URL(ConsoleApplication.class.getResource("/Image/Menu/groupChat.png").toString()))));
        photoOfChat.setCursor(Cursor.DEFAULT);
        photoOfChat.setOnMouseClicked(null);
    }

    private void showMessagesOfPrivateChat() throws MalformedURLException {
        for (int i = 0; i < privateChat.getMessages().size(); i++) {
            Message message = privateChat.getMessages().get(i);
            sendNewMessage(message);
        }
    }

    public void showPrivateChats() throws MalformedURLException {
        mainVBox.getChildren().clear();
        mainVBox.getChildren().add(searchPanel);
        searchTextField.setVisible(false);
        mainVBox.getChildren().add(scrollPane);
        allMessages.getChildren().clear();
        textToSend.setVisible(false);
        sendMessageIcon.setVisible(false);
        searchButton.setOnMouseClicked(mouseEvent1 -> {
            searchTextField.setVisible(true);
            searchTextField.requestFocus();
            startForSearchingPrivateChat();
        });
        listOfChats.getItems().clear();
        for (PrivateChat privateChat : LoggedInAccount.getInstance().getLoggedIn().getPrivateChats()) {
            if (privateChat.getAccount1() == LoggedInAccount.getInstance().getLoggedIn()) {
                listOfChats.getItems().add(privateChat.getAccount2().getUsername());
            } else {
                listOfChats.getItems().add(privateChat.getAccount1().getUsername());
            }
        }
    }


    public void showRoomChats() throws MalformedURLException {
        mainVBox.getChildren().clear();
        mainVBox.getChildren().add(searchPanel);
        searchTextField.setVisible(false);
        mainVBox.getChildren().add(scrollPane);
        allMessages.getChildren().clear();
        textToSend.setVisible(false);
        sendMessageIcon.setVisible(false);
        searchButton.setOnMouseClicked(mouseEvent1 -> {
            searchTextField.setVisible(true);
            searchTextField.requestFocus();
            startForSearchingGroupChat();
        });
        listOfChats.getItems().clear();
        for (GroupChat groupChat : GroupChat.getUserGroups(LoggedInAccount.getInstance().getLoggedIn())) {
            listOfChats.getItems().add(groupChat.getName());
        }
    }

    private void startForSearchingGroupChat() {
        mainVBox.getChildren().clear();
        mainVBox.getChildren().add(searchPanel);
        mainVBox.getChildren().add(suggestionScrollPane);
        searchTextField.setOnKeyReleased(keyEvent -> {
            try {
                showSuggestionGroupChats();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
        searchButton.setOnMouseClicked(mouseEvent -> {
            try {
                showRoomChats();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
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


    private void showAllMessage(Pane pane, ArrayList<Message> messages) {
        for (Message message : messages) {
            Label label = new Label(message.getContent());
            label.setPrefHeight(20);
            label.setLayoutX(50);
            label.setLayoutY(40);
            label.setStyle("-fx-font-family: \"High Tower Text\";" +
                    "       -fx-font-size: 18");
            pane.getChildren().add(label);
        }
    }


    public void enter(KeyEvent keyEvent) throws MalformedURLException {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            keyEvent.consume();
            send(null);
        }
    }

    public void back(MouseEvent mouseEvent) {
        MenuChanger.changeMenu("LoginMenu");
    }

    public void send(MouseEvent mouseEvent) throws MalformedURLException {
        if (!ChatController.getInstance().isValidShortMessage(textToSend.getText()) && picMessage == null) {
            new PopupMessage(Alert.AlertType.ERROR, "empty text!");
        } else if (!ChatController.getInstance().isValidLongMessage(textToSend.getText()))
            new PopupMessage(Alert.AlertType.ERROR, "long message");
        else if (chatMode == 1 && privateChat.getOtherUser(LoggedInAccount.getInstance().getLoggedIn()).getBlockedUsers().contains(LoggedInAccount.getInstance().getLoggedIn()))
            new PopupMessage(Alert.AlertType.ERROR, "you are blocked by the other person!");
        else if (chatMode == 2 && this.groupChat.getBannedUsers().contains(LoggedInAccount.getInstance().getLoggedIn()))
            new PopupMessage(Alert.AlertType.ERROR, "you are banned from this group!");
        else {
            Message message = new Message(LoggedInAccount.getInstance().getLoggedIn(), textToSend.getText());
            if (picMessage != null) {
                message.setFile(picMessage);
            }
            if (repliedMessage != null) {
                message.setRepliedUUID(repliedMessage.getUuid()); // does nothing
                message.setRepliedMessage(repliedMessage);
            }
            if (chatMode == 1)
                this.privateChat.getMessages().add(message);
            else if (chatMode == 2)
                this.groupChat.getMessages().add(message);
            sendMessageMode.setImage(new Image(String.valueOf(
                    new URL(ConsoleApplication.class.getResource("/Image/Menu/seen.png").toString()))));
            sendNewMessage(message);
        }
        picMessage = null;
        repliedMessage = null;
        forwardedMessage = null;
        textToSend.clear();
    }

    private void sendNewMessage(Message message) throws MalformedURLException {
        Pane pane = getMessageBox(message);
        allMessages.getChildren().add(pane);
    }


    private Pane getMessageBox(Message message) throws MalformedURLException {
        Pane pane = new Pane();
        setMessagePaneSize(pane);
        addText(pane, message.getContent());
        addUserUsername(pane, message.getAccount());
        addClock(pane, String.valueOf(message.getDate().getTime()));
        if (message.getFile() != null)
            addImage(pane, message.getFile());
        if (message.getRepliedMessage() != null)
            addReplied(pane, message);
        if (message.getAccount() == LoggedInAccount.getInstance().getLoggedIn()) {
            addButtonToDelete(pane, message);
            addButtonToEdit(pane, message);
            addButtonToReply(pane, message);
            addButtonToForward(pane, message);
        }
        return pane;
    }

    private void addImage(Pane pane, File file) {
        ImageView imageView = new ImageView(new Image(file.toURI().toString(), 80, 80, true, true));
        imageView.setLayoutX(140);
        imageView.setLayoutY(0);  // todo fix place
        pane.getChildren().add(imageView);
    }

    //    private void addForwarded(Pane pane, Message message) {
//        Label label = new Label("forwarded from : " + message.getForwardedUsername());
//        label.setPrefHeight(30);
//        label.setPrefWidth(180);
//        label.setLayoutX(40);
//        label.setLayoutY(18);
//        label.setFont(Font.font(15));
//        label.setTextFill(Color.PURPLE);
//        label.setStyle("-fx-font-family: \"High Tower Text\"");
//        pane.getChildren().add(label);
//    }

    private void addButtonToForward(Pane pane, Message message) throws MalformedURLException {
        ImageView imageView = new ImageView(new Image(String.valueOf(
                new URL(ConsoleApplication.class.getResource("/Image/Menu/close.png").toString()))));
        imageView.setFitHeight(15);
        imageView.setFitWidth(15);
        imageView.setLayoutX(244);
        imageView.setLayoutY(15);
        imageView.setCursor(Cursor.HAND);
        imageView.setOnMouseClicked(mouseEvent -> {
            forwardMessage(message);
        });
        pane.getChildren().add(imageView);
    }

    private void forwardMessage(Message message) {
//        forwardedMessage = message;
//        repliedMessage = null;
//        try {
//            sendMessageMode.setImage(new Image(String.valueOf(
//                    new URL(ConsoleApplication.class.getResource("/Image/Menu/close.png").toString()))));
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
    }

    private void addButtonToReply(Pane pane, Message message) throws MalformedURLException {
        ImageView imageView = new ImageView(new Image(String.valueOf(
                new URL(ConsoleApplication.class.getResource("/Image/Menu/chatBack.png").toString()))));
        imageView.setFitHeight(15);
        imageView.setFitWidth(15);
        imageView.setLayoutX(266);
        imageView.setLayoutY(15);
        imageView.setCursor(Cursor.HAND);
        imageView.setOnMouseClicked(mouseEvent -> {
            replyMessage(message);
        });
        pane.getChildren().add(imageView);
    }

    private void replyMessage(Message message) {
        forwardedMessage = null;
        repliedMessage = message;
        try {
            sendMessageMode.setImage(new Image(String.valueOf(
                    new URL(ConsoleApplication.class.getResource("/Image/Menu/close.png").toString()))));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void addButtonToDelete(Pane pane, Message message) throws MalformedURLException {
        ImageView imageView = new ImageView(new Image(String.valueOf(
                new URL(ConsoleApplication.class.getResource("/Image/Menu/blackCross.png").toString()))));
        imageView.setFitHeight(15);
        imageView.setFitWidth(15);
        imageView.setLayoutX(310);
        imageView.setLayoutY(15);
        imageView.setCursor(Cursor.HAND);
        imageView.setOnMouseClicked(mouseEvent -> {
            pane.getChildren().remove(deleteMessage);
            allMessages.getChildren().remove(pane);
            message.deleteMessage();
        });
        pane.getChildren().add(imageView);
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
                    if (pane.getChildren().get(0) instanceof Label) {
                        ((Label) pane.getChildren().get(0)).setText(editTextField.getText());
                    }
                    message.setContent(editTextField.getText());
                    message.setEdited(true);
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
        text.setLayoutY(25);
        text.setFont(Font.font(18));
        pane.getChildren().add(text);
    }

    private void addReplied(Pane pane, Message message) {
        Label label = new Label("replied to : " + message.getRepliedMessage().getContent());
        label.setPrefHeight(30);
        label.setPrefWidth(180);
        label.setLayoutX(40);
        label.setLayoutY(18);
        label.setFont(Font.font(15));
        label.setTextFill(Color.PURPLE);
        label.setStyle("-fx-font-family: \"High Tower Text\"");
        pane.getChildren().add(label);
    }

    private void addUserUsername(Pane pane, Account account) {
        Label label = new Label("from : " + account.getUsername());
        label.setPrefHeight(20);
        label.setLayoutX(40);
        label.setLayoutY(10);
        label.setFont(Font.font(12));
        label.setStyle("-fx-font-family: \"High Tower Text\"");
        pane.getChildren().add(label);
    }

    private void setMessagePaneSize(Pane pane) {
        pane.setPrefWidth(100);
        pane.setPrefHeight(80);
        pane.setStyle("-fx-border-radius: 30 30 30 30;" +
                "-fx-background-radius: 30 30 30 30;" +
                "-fx-background-color: #927819;");
    }


    public void changeToPrivate(MouseEvent mouseEvent) throws MalformedURLException {
        if (chatMode != 1) {
            chatMode = 1;
            setSelectedRightBoxButtonStyle(changeToPrivateChatButton);
            setNotSelectedRightBoxButtonStyle(changeToRoomChatButton);
            setNotSelectedRightBoxButtonStyle(allChatsButton);
            showPrivateChats();
        }
    }

    public void changeToRoom(MouseEvent mouseEvent) throws MalformedURLException {
        if (chatMode != 2) {
            chatMode = 2;
            setNotSelectedRightBoxButtonStyle(changeToPrivateChatButton);
            setSelectedRightBoxButtonStyle(changeToRoomChatButton);
            setNotSelectedRightBoxButtonStyle(allChatsButton);
            showRoomChats();
        }
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


    private void showSuggestionGroupChats() throws MalformedURLException {
        suggestionVBox.getChildren().clear();
        if (searchTextField.getText().equals("")) {
            return;
        }
        ArrayList<GroupChat> suggestionGroups = ChatController.getInstance().showGroupsStartsWithString(searchTextField.getText());
        for (GroupChat suggestionGroup : suggestionGroups) {
            addSuggestionPane(suggestionGroup);
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

    private void addSuggestionPane(GroupChat suggestionGroup) {
        Pane pane = new Pane();
        pane.setPrefHeight(50);
        pane.setStyle("-fx-background-color: #dfc10c;" +
                "-fx-border-radius: 30 30 30 30;" +
                "-fx-background-radius: 30 30 30 30;");
        addLabelToSuggestionPane(pane, suggestionGroup.getName(), false);
        HBox hBox = (HBox) pane.getChildren().get(0);
        hBox.getChildren().get(1).setCursor(Cursor.HAND);
        suggestionVBox.getChildren().add(pane);
        hBox.getChildren().get(1).setOnMouseClicked(mouseEvent -> {
            try {
                this.groupChat = suggestionGroup;
                goToARoomChat();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });

        hBox.getChildren().get(2).setCursor(Cursor.HAND);
        hBox.getChildren().get(2).setOnMouseClicked(mouseEvent -> {
            suggestionGroup.removeUser(LoggedInAccount.getInstance().getLoggedIn());
        });
    }

    private void addSuggestionPane(Account account) throws MalformedURLException {
        Pane pane = new Pane();
        pane.setPrefHeight(50);
        pane.setStyle("-fx-background-color: #dfc10c;" +
                "-fx-border-radius: 30 30 30 30;" +
                "-fx-background-radius: 30 30 30 30;");
        addLabelToSuggestionPane(pane, account.getUsername(), false);
        HBox hBox = (HBox) pane.getChildren().get(0);
        hBox.getChildren().get(1).setCursor(Cursor.HAND);
        suggestionVBox.getChildren().add(pane);
        hBox.getChildren().get(1).setOnMouseClicked(mouseEvent -> {
            try {
                this.privateChat = ChatController.getInstance().getAccountPrivateChat(account);
                goToAPrivateChat();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });

        hBox.getChildren().get(2).setCursor(Cursor.HAND);
        hBox.getChildren().get(2).setOnMouseClicked(mouseEvent -> {
            LoggedInAccount.getInstance().getLoggedIn().blockUser(account);
        });
    }

    private void addLabelToSuggestionPane(Pane pane, String username, boolean isGroupMember) {
        if (isGroupMember) {
            HBox hBox = new HBox();
            hBox.setPrefHeight(20);
            hBox.setLayoutX(70);
            hBox.setLayoutY(10);
            hBox.setSpacing(10);
            Account account = Account.getAccount(username);
            if (account.getFile() != null) {
                ImageView imageView = new ImageView(new Image(account.getFile().toURI().toString()));
                hBox.getChildren().add(imageView);
            } else {
                ImageView imageView = null;
                try {
                    imageView = new ImageView(new Image(String.valueOf(new URL(ConsoleApplication.class.getResource("/Image/Menu/user.png").toString()))));
                    imageView.setFitHeight(35);
                    imageView.setFitWidth(35);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                hBox.getChildren().add(imageView);
            }

            Label label = new Label(username);
            label.setPrefHeight(20);
            label.setStyle("-fx-font-family: \"High Tower Text\";" +
                    "       -fx-font-size: 25");
            Image image = null;
            try {
                image = new Image(String.valueOf(
                        new URL(ConsoleApplication.class.getResource("/Image/Menu/menuIcon.png").toString())));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            ImageView ban = new ImageView(image);
            ban.setFitHeight(25);
            ban.setFitWidth(25);
            ImageView remove = null;
            try {
                remove = new ImageView(new Image(String.valueOf(
                        new URL(ConsoleApplication.class.getResource("/Image/Menu/blackCross.png").toString()))));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            remove.setFitHeight(25);
            remove.setFitWidth(25);
            hBox.getChildren().addAll(label, ban, remove);
            pane.getChildren().add(hBox);
        } else {
            HBox hBox = new HBox();
            hBox.setPrefHeight(20);
            hBox.setLayoutX(70);
            hBox.setLayoutY(10);
            hBox.setSpacing(10);
            Account account = Account.getAccount(username);
            GroupChat groupChat = GroupChat.getGroupChatByName(username);
            if (account != null && account.getFile() != null) {
                ImageView imageView = new ImageView(new Image(account.getFile().toURI().toString()));
                hBox.getChildren().add(imageView);
            } else if (groupChat != null && groupChat.getFile() != null) {
                ImageView imageView = new ImageView(new Image(groupChat.getFile().toURI().toString()));
                hBox.getChildren().add(imageView);
            } else {
                ImageView imageView = null;
                try {
                    imageView = new ImageView(new Image(String.valueOf(new URL(ConsoleApplication.class.getResource("/Image/Menu/user.png").toString()))));
                    imageView.setFitHeight(35);
                    imageView.setFitWidth(35);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                hBox.getChildren().add(imageView);
            }
            Label label = new Label(username);
            label.setPrefHeight(20);
            label.setStyle("-fx-font-family: \"High Tower Text\";" +
                    "       -fx-font-size: 18");
            ImageView block = null;
            try {
                block = new ImageView(new Image(String.valueOf(
                        new URL(ConsoleApplication.class.getResource("/Image/Menu/blackCross.png").toString()))));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            block.setFitHeight(25);
            block.setFitWidth(25);
            hBox.getChildren().addAll(label, block);
            pane.getChildren().add(hBox);
        }
    }

    public void openRightVBox(MouseEvent mouseEvent) {
        if (isRightBarVBoxOpen) {
            isRightBarVBoxOpen = false;
            rightBarVBox.setVisible(false);
        } else {
            isRightBarVBoxOpen = true;
            rightBarVBox.setVisible(true);
        }
    }

    public void createGroup(MouseEvent mouseEvent) {
        MenuChanger.changeMenu("CreateGroup");
    }

    public void gotoChat(MouseEvent mouseEvent) {
        String name = listOfChats.getSelectionModel().getSelectedItem().toString();
        if (GroupChat.isExist(name)) {
            this.groupChat = GroupChat.getGroupChatByName(name);
            try {
                goToARoomChat();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            this.privateChat = PrivateChat.getPrivateChat(LoggedInAccount.getInstance().getLoggedIn(), Account.getAccount(name));
            chatMode = 1;
            try {
                goToAPrivateChat();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
    }

    public void showALlChats(MouseEvent mouseEvent) {
        if (chatMode != 0) {
            chatMode = 0;
            setNotSelectedRightBoxButtonStyle(changeToPrivateChatButton);
            setSelectedRightBoxButtonStyle(allChatsButton);
            setNotSelectedRightBoxButtonStyle(changeToRoomChatButton);
        }
        listOfChats.getItems().clear();
        for (PrivateChat privateChat : LoggedInAccount.getInstance().getLoggedIn().getPrivateChats()) {
            if (privateChat.getAccount1() == LoggedInAccount.getInstance().getLoggedIn()) {
                listOfChats.getItems().add(privateChat.getAccount2().getUsername());
            } else {
                listOfChats.getItems().add(privateChat.getAccount1().getUsername());
            }
        }
        for (GroupChat groupChat : GroupChat.getUserGroups(LoggedInAccount.getInstance().getLoggedIn())) {
            listOfChats.getItems().add(groupChat.getName());
        }
    }

    public void chooseFile(MouseEvent mouseEvent) throws MalformedURLException {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG, PNG, JEPG Files", "*.jpg", "*.png", "*.jepg"));
        File selectedFile = fc.showOpenDialog(MenuChanger.getStage());
        if (selectedFile != null) {
            this.picMessage = selectedFile;
            sendMessageMode.setImage(new Image(String.valueOf(
                    new URL(ConsoleApplication.class.getResource("/Image/Menu/camera.png").toString()))));
        } else {
            new PopupMessage(Alert.AlertType.ERROR, "file is not valid");
        }
    }
}