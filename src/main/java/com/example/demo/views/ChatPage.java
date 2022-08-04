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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private boolean forward = false;

    private SimpleDateFormat ft =
            new SimpleDateFormat("hh:mm 'on' MM.dd");

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
    @FXML
    private ImageView picAPic;


    private File picMessage;
    private Message repliedMessage = null;
    private Message forwardedMessage = null;

    public void initialize() throws MalformedURLException {
        mainVBox.getChildren().remove(searchPanel);
        rightBarVBox.setVisible(false);
        scrollPane.vvalueProperty().bind(allMessages.heightProperty());
        allMessages.setSpacing(5);

        ArrayList<PrivateChat> privateChats = LoggedInAccount.getInstance().getLoggedIn().getPrivateChats();
        ArrayList<GroupChat> groupChats = GroupChat.getUserGroups(LoggedInAccount.getInstance().getLoggedIn());
        ArrayList<Chat> allChats = new ArrayList<>();
        PCustomSort(privateChats);
        GCustomSort(groupChats);
        fillAllChats(allChats, privateChats, groupChats);

        for (Chat chat : allChats) {
            if (chat instanceof PrivateChat) {
                PrivateChat temp = (PrivateChat) chat;
                if (temp.getAccount1() == LoggedInAccount.getInstance().getLoggedIn()) {
                    if (hasNotification(temp.getAccount2())) {
                        listOfChats.getItems().add(temp.getAccount2().getUsername() + "*");
                    } else {
                        listOfChats.getItems().add(temp.getAccount2().getUsername());
                    }
                } else {
                    if (hasNotification(temp.getAccount1())) {
                        listOfChats.getItems().add(temp.getAccount1().getUsername() + "*");
                    } else {
                        listOfChats.getItems().add(temp.getAccount1().getUsername());
                    }
                }
            } else if (chat instanceof GroupChat) {
                GroupChat temp = (GroupChat) chat;
                if (hasNotification(temp)) {
                    listOfChats.getItems().add(temp.getName() + "*");
                } else {
                    listOfChats.getItems().add(temp.getName());
                }
            }
        }

//        for (PrivateChat privateChat : privateChats) {
//            if (privateChat.getAccount1() == LoggedInAccount.getInstance().getLoggedIn()) {
//                listOfChats.getItems().add(privateChat.getAccount2().getUsername());
//            } else {
//                listOfChats.getItems().add(privateChat.getAccount1().getUsername());
//            }
//        }
//        for (GroupChat groupChat : groupChats) {
//            listOfChats.getItems().add(groupChat.getName());
//        }
    }

    private void GCustomSort(ArrayList<GroupChat> groupChats) {
        Message message1, message2;
        groupChats.sort(new Comparator<GroupChat>() {
            @Override
            public int compare(GroupChat o1, GroupChat o2) {
                if (o1.getMessages().size() > o2.getMessages().size()) return -1;
                if (o1.getMessages().size() < o2.getMessages().size()) return 1;
                return 0;
            }
        });
        for (int i = 0; i < groupChats.size(); i++) {
            if (groupChats.get(i).getMessages().size() == 0) continue;
            for (int j = i + 1; j < groupChats.size(); j++) {
                if (groupChats.get(j).getMessages().size() == 0) continue;
                message1 = groupChats.get(i).getMessages().get(groupChats.get(i).getMessages().size() - 1);
                message2 = groupChats.get(j).getMessages().get(groupChats.get(j).getMessages().size() - 1);
                if (message1.getDate().compareTo(message2.getDate()) < 0)
                    Collections.swap(groupChats, i, j);
            }
        }
    }

    private void PCustomSort(ArrayList<PrivateChat> privateChats) {
        Message message1, message2;
        privateChats.sort(new Comparator<PrivateChat>() {
            @Override
            public int compare(PrivateChat o1, PrivateChat o2) {
                if (o1.getMessages().size() > o2.getMessages().size()) return -1;
                if (o1.getMessages().size() < o2.getMessages().size()) return 1;
                return 0;
            }
        });
        for (int i = 0; i < privateChats.size(); i++) {
            if (privateChats.get(i).getMessages().size() == 0) continue;
            for (int j = i + 1; j < privateChats.size(); j++) {
                if (privateChats.get(j).getMessages().size() == 0) continue;
                message1 = privateChats.get(i).getMessages().get(privateChats.get(i).getMessages().size() - 1);
                message2 = privateChats.get(j).getMessages().get(privateChats.get(j).getMessages().size() - 1);
                if (message1.getDate().compareTo(message2.getDate()) < 0)
                    Collections.swap(privateChats, i, j);
            }
        }
    }

    private void fillAllChats(ArrayList<Chat> allChats, ArrayList<PrivateChat> privateChats, ArrayList<GroupChat> groupChats) {
        allChats.clear();
        int p = 0, g = 0;
        Message pMessage, gMessage;
        while (p < privateChats.size() && g < groupChats.size()) {
            if (privateChats.get(p).getMessages().size() == 0) {
                allChats.add(groupChats.get(g));
                g++;
                continue;
            }
            if (groupChats.get(g).getMessages().size() == 0) {
                allChats.add(privateChats.get(p));
                p++;
                continue;
            }
            pMessage = privateChats.get(p).getMessages().get(privateChats.get(p).getMessages().size() - 1);
            gMessage = groupChats.get(g).getMessages().get(groupChats.get(g).getMessages().size() - 1);
            if (pMessage.getDate().compareTo(gMessage.getDate()) > 0) {
                allChats.add(privateChats.get(p));
                p++;
            } else {
                allChats.add(groupChats.get(g));
                g++;
            }
        }
        while (p < privateChats.size()) {
            allChats.add(privateChats.get(p));
            p++;
        }
        while (g < groupChats.size()) {
            allChats.add(groupChats.get(g));
            g++;
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
        picAPic.setVisible(true);
        sendMessageMode.setVisible(true);
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
        picAPic.setVisible(true);
        sendMessageMode.setVisible(true);
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
        if (groupChat.getFile() == null) {
            photoOfChat.setImage(new Image(String.valueOf(
                    new URL(ConsoleApplication.class.getResource("/Image/Menu/groupChat.png").toString()))));
        } else {
            photoOfChat.setImage(new Image(groupChat.getFile().toURI().toString(), 30, 30, true, true));
        }
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
        picAPic.setVisible(false);
        sendMessageMode.setVisible(false);
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
                new PopupMessage(Alert.AlertType.INFORMATION, "banned successfully!");
            });


            hBox.getChildren().get(3).setCursor(Cursor.HAND);
            hBox.getChildren().get(3).setOnMouseClicked(mouseEvent -> {
                this.groupChat.removeUser(account);
                new PopupMessage(Alert.AlertType.INFORMATION, "removed successfully!");
            });
        }
    }

    private void setDataOfChatHBoxPrivateChat(Account account) throws MalformedURLException {
        Account other = privateChat.getOtherUser(LoggedInAccount.getInstance().getLoggedIn());
        labelOfDataChat.setText(other.getUsername());
        photoOfChat.setVisible(true);
        if (other.getFile() == null) {
            photoOfChat.setImage(new Image(String.valueOf(
                    new URL(ConsoleApplication.class.getResource("/Image/Menu/user.png").toString()))));
        } else {
            photoOfChat.setImage(new Image(other.getFile().toURI().toString(), 30, 30, true, true));
        }
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
        picAPic.setVisible(false);
        sendMessageMode.setVisible(false);
        sendMessageIcon.setVisible(false);
        searchButton.setOnMouseClicked(mouseEvent1 -> {
            searchTextField.setVisible(true);
            searchTextField.requestFocus();
            startForSearchingPrivateChat();
        });
        listOfChats.getItems().clear();
        ArrayList<PrivateChat> privateChats = LoggedInAccount.getInstance().getLoggedIn().getPrivateChats();
        PCustomSort(privateChats);
        for (PrivateChat privateChat : privateChats) {
            if (privateChat.getAccount1() == LoggedInAccount.getInstance().getLoggedIn()) {
                if (hasNotification(privateChat.getAccount2())) {
                    listOfChats.getItems().add(privateChat.getAccount2().getUsername() + "*");
                } else {
                    listOfChats.getItems().add(privateChat.getAccount2().getUsername());
                }
            } else {
                if (hasNotification(privateChat.getAccount1())) {
                    listOfChats.getItems().add(privateChat.getAccount1().getUsername() + "*");
                } else {
                    listOfChats.getItems().add(privateChat.getAccount1().getUsername());
                }
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
        picAPic.setVisible(false);
        sendMessageMode.setVisible(false);
        sendMessageIcon.setVisible(false);
        searchButton.setOnMouseClicked(mouseEvent1 -> {
            searchTextField.setVisible(true);
            searchTextField.requestFocus();
            startForSearchingGroupChat();
        });
        listOfChats.getItems().clear();
        ArrayList<GroupChat> groupChats = GroupChat.getUserGroups(LoggedInAccount.getInstance().getLoggedIn());
        GCustomSort(groupChats);
        for (GroupChat groupChat : groupChats) {
            if (hasNotification(groupChat)) {
                listOfChats.getItems().add(groupChat.getName() + "*");
            } else {
                listOfChats.getItems().add(groupChat.getName());
            }
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
        MenuChanger.changeMenu("MainPaneforLoginAccount");
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
        message.getSeenBy().add(LoggedInAccount.getInstance().getLoggedIn());
        Pane pane = getMessageBox(message);
        allMessages.getChildren().add(pane);
    }


    private Pane getMessageBox(Message message) throws MalformedURLException {
        Pane pane = new Pane();
        setMessagePaneSize(pane);

        if (message.getForwardedMessage() != null && message.getForwardedUsername() != null) {
            addForwarded(pane, message);
            addText(pane, message.getForwardedMessage().getContent());
            if (message.getForwardedMessage().getFile() != null)
                addImage(pane, message.getForwardedMessage().getFile());
            if (message.getAccount() == LoggedInAccount.getInstance().getLoggedIn()) {
                addButtonToDelete(pane, message);
            }
        } else {
            addText(pane, message.getContent());
            if (message.getFile() != null)
                addImage(pane, message.getFile());
            if (message.getRepliedMessage() != null)
                addReplied(pane, message);
            addButtonToForward(pane, message);
            if (message.getAccount() == LoggedInAccount.getInstance().getLoggedIn()) {
                addButtonToDelete(pane, message);
                addButtonToEdit(pane, message);
            }
            if (message.isEdited()) {
                addEdited(pane);
            }
        }
        addUserUsername(pane, message.getAccount());
        addClock(pane, ft.format(message.getDate()));
        addButtonToReply(pane, message);
        return pane;
    }


    private void addForwarded(Pane pane, Message message) {
        Label label = new Label("forwarded from " + message.getForwardedUsername());
        label.setPrefHeight(30);
        label.setPrefWidth(180);
        label.setLayoutX(40);
        label.setLayoutY(18);
        label.setFont(Font.font(13));
        label.setTextFill(Color.DARKGREEN);
        label.setStyle("-fx-font-family: \"High Tower Text\"");
        pane.getChildren().add(label);
    }

    private void addReplied(Pane pane, Message message) {
        if (message.getRepliedMessage().getContent().equals("")) {
            Label label = new Label("replied to a message from " + message.getRepliedMessage().getAccount().getUsername());
            label.setPrefHeight(30);
            label.setPrefWidth(180);
            label.setLayoutX(40);
            label.setLayoutY(18);
            label.setFont(Font.font(13));
            label.setTextFill(Color.DARKGREEN);
            label.setStyle("-fx-font-family: \"High Tower Text\"");
            pane.getChildren().add(label);
        } else {
            Label label = new Label("replied to : " + message.getRepliedMessage().getContent());
            label.setPrefHeight(30);
            label.setPrefWidth(180);
            label.setLayoutX(40);
            label.setLayoutY(18);
            label.setFont(Font.font(13));
            label.setTextFill(Color.DARKGREEN);
            label.setStyle("-fx-font-family: \"High Tower Text\"");
            pane.getChildren().add(label);
        }

    }


    private void addImage(Pane pane, File file) {
        ImageView imageView = new ImageView(new Image(file.toURI().toString(), 80, 80, true, true));
        imageView.setLayoutX(140);
        imageView.setLayoutY(0);
        pane.getChildren().add(imageView);
    }

    private void addButtonToForward(Pane pane, Message message) throws MalformedURLException {
        ImageView imageView = new ImageView(new Image(String.valueOf(
                new URL(ConsoleApplication.class.getResource("/Image/Menu/close.png").toString()))));
        imageView.setFitHeight(15);
        imageView.setFitWidth(15);
        imageView.setLayoutX(244);
        imageView.setLayoutY(15);
        imageView.setCursor(Cursor.HAND);
        imageView.setOnMouseClicked(mouseEvent -> {
            forwardedMessage = message;
            showForwardOptions();
        });
        pane.getChildren().add(imageView);
    }

    public void showForwardOptions() {
        listOfChats.getItems().clear();
        for (Account account : Account.getAccounts().values()) {
            if (account != LoggedInAccount.getInstance().getLoggedIn()) {
                listOfChats.getItems().add(account.getUsername());
            }
        }
        for (GroupChat userGroup : GroupChat.getUserGroups(LoggedInAccount.getInstance().getLoggedIn())) {
            listOfChats.getItems().add(userGroup.getName());
        }
        forward = true;
    }

    public void gotoChat(MouseEvent mouseEvent) {
        String name = listOfChats.getSelectionModel().getSelectedItem().toString();
        int lastIndex = name.length() - 1;
        if (name.charAt(lastIndex) == '*') {
            name = name.substring(0, lastIndex);
        }
        if (GroupChat.isExist(name)) {
            if (forward) {
                GroupChat groupChat = GroupChat.getGroupChatByName(name);
                forwardMessage(groupChat, forwardedMessage);
                forward = false;
            } else {
                this.groupChat = GroupChat.getGroupChatByName(name);
                chatMode = 2;
                try {
                    goToARoomChat();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (forward) {
                PrivateChat privateChat = PrivateChat.getPrivateChat(LoggedInAccount.getInstance().getLoggedIn(), Account.getAccount(name));
                forwardMessage(privateChat, forwardedMessage);
                forward = false;
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
    }

    public void forwardMessage(PrivateChat chat, Message message) {
        if (message == null) {
            new PopupMessage(Alert.AlertType.ERROR, "no message to forward");
            return;
        }
        if (chat.getOtherUser(LoggedInAccount.getInstance().getLoggedIn()).getBlockedUsers().contains(LoggedInAccount.getInstance().getLoggedIn())) {
            forwardedMessage = null;
            new PopupMessage(Alert.AlertType.ERROR, "you are blocked by the other user");
            listOfChats.getItems().clear();
            return;
        }
        Message temp = new Message(LoggedInAccount.getInstance().getLoggedIn(), "");
        temp.getSeenBy().add(LoggedInAccount.getInstance().getLoggedIn());
        temp.setForwardedMessage(message);
        temp.setForwardedUsername(message.getAccount().getUsername());
        chat.getMessages().add(temp);
        forwardedMessage = null;
        if (chatMode == 1 && privateChat == chat) {
            try {
                sendNewMessage(message);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        new PopupMessage(Alert.AlertType.INFORMATION, "success");
        listOfChats.getItems().clear();
        try {
            if (chatMode == 1) {
                goToAPrivateChat();
            }
            if (chatMode == 2) {
                goToARoomChat();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public void forwardMessage(GroupChat chat, Message message) {
        if (message == null) {
            new PopupMessage(Alert.AlertType.ERROR, "no message to forward");
            return;
        }
        if (chat.getBannedUsers().contains(LoggedInAccount.getInstance().getLoggedIn())) {
            forwardedMessage = null;
            new PopupMessage(Alert.AlertType.ERROR, "you are banned from this group!");
            listOfChats.getItems().clear();
            return;
        }
        Message temp = new Message(LoggedInAccount.getInstance().getLoggedIn(), "");
        temp.getSeenBy().add(LoggedInAccount.getInstance().getLoggedIn());
        temp.setForwardedMessage(message);
        temp.setForwardedUsername(message.getAccount().getUsername());
        chat.getMessages().add(temp);
        forwardedMessage = null;
        if (chatMode == 2 && groupChat == chat) {
            try {
                sendNewMessage(message);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        new PopupMessage(Alert.AlertType.INFORMATION, "success");
        listOfChats.getItems().clear();
        try {
            if (chatMode == 1) {
                goToAPrivateChat();
            }
            if (chatMode == 2) {
                goToARoomChat();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
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
                    try {
                        if (chatMode == 1) goToAPrivateChat();
                        if (chatMode == 2) goToARoomChat();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
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
        text.setLayoutY(22);
        text.setFont(Font.font(16));
        pane.getChildren().add(text);
    }

    private void addEdited(Pane pane) {
        Label text = new Label("edited!");
        text.setPrefHeight(60);
        text.setPrefWidth(60);
        text.setLayoutX(40);
        text.setLayoutY(35);
        text.setFont(Font.font(10));
        pane.getChildren().add(text);
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
        if (hasNotification(suggestionGroup)) {
            pane.setStyle("-fx-background-color: #0cdfc6;" +
                    "-fx-border-radius: 30 30 30 30;" +
                    "-fx-background-radius: 30 30 30 30;");
        }
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
            new PopupMessage(Alert.AlertType.INFORMATION, "you left the group!");
        });
    }

    private boolean hasNotification(GroupChat groupChat) {
        if (groupChat == null) return false;
        if (groupChat.getMessages().size() == 0) return false;
        return !groupChat.getMessages().get(groupChat.getMessages().size() - 1).getSeenBy().contains(LoggedInAccount.getInstance().getLoggedIn());
    }

    private boolean hasNotification(Account account) {
        PrivateChat chat = PrivateChat.getPrivateChat(account, LoggedInAccount.getInstance().getLoggedIn());
        if (chat == null) return false;
        if (chat.getMessages().size() == 0) return false;
        return !chat.getMessages().get(chat.getMessages().size() - 1).getSeenBy().contains(LoggedInAccount.getInstance().getLoggedIn());
    }

    private void addSuggestionPane(Account account) throws MalformedURLException {
        Pane pane = new Pane();
        pane.setPrefHeight(50);
        pane.setStyle("-fx-background-color: #dfc10c;" +
                "-fx-border-radius: 30 30 30 30;" +
                "-fx-background-radius: 30 30 30 30;");
        if (hasNotification(account)) {
            pane.setStyle("-fx-background-color: #0cdfc6;" +
                    "-fx-border-radius: 30 30 30 30;" +
                    "-fx-background-radius: 30 30 30 30;");
        }
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
            new PopupMessage(Alert.AlertType.INFORMATION, "you blocked this account!");
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
                imageView.setFitHeight(35);
                imageView.setFitWidth(35);
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
                imageView.setFitHeight(35);
                imageView.setFitWidth(35);
                hBox.getChildren().add(imageView);
            } else if (groupChat != null && groupChat.getFile() != null) {
                ImageView imageView = new ImageView(new Image(groupChat.getFile().toURI().toString()));
                imageView.setFitHeight(35);
                imageView.setFitWidth(35);
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


    public void showALlChats(MouseEvent mouseEvent) {
        if (chatMode != 0) {
            chatMode = 0;
            setNotSelectedRightBoxButtonStyle(changeToPrivateChatButton);
            setSelectedRightBoxButtonStyle(allChatsButton);
            setNotSelectedRightBoxButtonStyle(changeToRoomChatButton);
        }
        listOfChats.getItems().clear();
        ArrayList<PrivateChat> privateChats = LoggedInAccount.getInstance().getLoggedIn().getPrivateChats();
        ArrayList<GroupChat> groupChats = GroupChat.getUserGroups(LoggedInAccount.getInstance().getLoggedIn());
        ArrayList<Chat> allChats = new ArrayList<>();
        PCustomSort(privateChats);
        GCustomSort(groupChats);
        fillAllChats(allChats, privateChats, groupChats);

        for (Chat chat : allChats) {
            if (chat instanceof PrivateChat) {
                PrivateChat temp = (PrivateChat) chat;
                if (temp.getAccount1() == LoggedInAccount.getInstance().getLoggedIn()) {
                    if (hasNotification(temp.getAccount2())) {
                        listOfChats.getItems().add(temp.getAccount2().getUsername() + "*");
                    } else {
                        listOfChats.getItems().add(temp.getAccount2().getUsername());
                    }
                } else {
                    if (hasNotification(temp.getAccount1())) {
                        listOfChats.getItems().add(temp.getAccount1().getUsername() + "*");
                    } else {
                        listOfChats.getItems().add(temp.getAccount1().getUsername());
                    }
                }
            } else if (chat instanceof GroupChat) {
                GroupChat temp = (GroupChat) chat;
                if (hasNotification(temp)) {
                    listOfChats.getItems().add(temp.getName() + "*");
                } else {
                    listOfChats.getItems().add(temp.getName());
                }
            }
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