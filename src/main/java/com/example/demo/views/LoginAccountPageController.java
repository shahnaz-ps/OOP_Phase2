package com.example.demo.views;

import com.example.demo.ConsoleApplication;
import com.example.demo.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

//import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class LoginAccountPageController {
    public TextField idTextbox;
    public TextField followingsNum;
    public TextField followersNum;
    public ImageView imageView;
    public ImageView imageView2;
    public ImageView imageView4;
    public Image image;
    public Image image2;
    public TextField FilePath;
    public FileChooser fileChooser;
    public File file;
    public static File finalFile;
    public static Account account2;
    //public Desktop desktop =Desktop.getDesktop();
    public ListView PostsList;
    public VBox postsVbox;
    public ListView ShowAccounts;
    public TextField postContent;
    public GridPane buttonGrid = new GridPane();
    public VBox vboxForButtons = new VBox();
    public VBox vboxForShowLikeButtons = new VBox();
    public ListView AccountLikePost;
    public ListView AccountComentList;
    public Circle ProCircle;

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
        if(LoggedInAccount.getInstance().getLoggedIn().getAccountsFile().get(LoggedInAccount.getInstance().getLoggedIn())!=null){
            image = new Image(LoggedInAccount.getInstance().getLoggedIn().getAccountsFile().get(LoggedInAccount.getInstance().getLoggedIn()).toURI().toString(), 100, 150, true, true);
            ProCircle.setFill(new ImagePattern(image));
        }
        for (Map.Entry<String, Account> set : Account.getAccounts().entrySet()) {
            if(!set.getValue().equals(LoggedInAccount.getInstance().getLoggedIn())) {
                ShowAccounts.getItems().add(set.getValue().getUsername());
            }
        }
        int size=LoggedInAccount.getInstance().getLoggedIn().getPosts().size();

        if(size<=2){
            for (int i = LoggedInAccount.getInstance().getLoggedIn().getPosts().size() - 1; i >= 0; i--) {
                if (!PostsList.getItems().contains(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i))) {
                    Button btnNumber = new Button();
                    Button Showlikebtn = new Button();
                    Button Showcommentbtn = new Button();
                    Button ShowviewChart = new Button();
                    ShowviewChart.setText("show view state chart");
                    Button ShowlikeChart = new Button();
                    ShowlikeChart.setText("show like state chart");
                    Showlikebtn.setText("Show Likes");
                    btnNumber.setText("Like");
                    Showcommentbtn.setText("show comment(write a comment)");
                    int finalI = i;
//                    File file2 = new File("/Users/kimia/Desktop/java/like and comment icon/like.png");
//                    image2 = new Image(file2.toURI().toString(),100,150,true,true);
//                    imageView4=new ImageView();
//                    imageView4.setImage(image2);
//                    imageView4.setFitWidth(100);
//                    imageView4.setFitHeight(150);
//                    imageView4.setPreserveRatio(true);
//                    imageView4.setSmooth(true);
//                    imageView4.setCache(true);

                    btnNumber.setOnAction((ActionEvent) -> {
                        likepost(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile());
                        clearTextandImg();
                    });
//                    imageView4.setOnMouseClicked((ActionEvent) -> {
//                        likepost(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile());
//                        clearTextandImg();
//                    });

                    Showlikebtn.setOnAction((ActionEvent) -> {
                        clearTextandImg();
                        Post post = Post.getPostByFile(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile());
                        if (post.getLike().size() == 0) {
                            AccountLikePost.getItems().add("no likes for this post :(");
                        } else {
                            AccountLikePost.getItems().add("likes : " + post.getLike().size());
                            for (int j = 0; j < post.getLike().size(); j++) {
                                if (!AccountLikePost.getItems().contains(post.getLike().get(j).getUsername())) {
                                    AccountLikePost.getItems().add(post.getLike().get(j).getUsername());
                                }
                            }
                        }
                    });

                    Showcommentbtn.setOnAction((ActionEvent) -> {
                        clearTextandImg();
                        Post post = Post.getPostByFile(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile());
                        TextField textField = new TextField();
                        textField.setPromptText("write down your comment");
                        Button commentbtn = new Button();
                        commentbtn.setText("comment");
                        commentbtn.setOnAction((ActionEvent2) -> {
                            commentpost(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile(), textField.getText());
                            clearTextandImg();
                        });

                        if (post.getComments().size() == 0) {
                            AccountComentList.getItems().add("no comments for this post :(");
                            for (int k = 0; k < 2; k++) {
                                AccountComentList.getItems().add("");
                            }
                        } else {
                            AccountComentList.getItems().add("comments : " + post.getComments().size());
                            for (int j = 0; j < post.getComments().size(); j++) {
                                if (!AccountComentList.getItems().contains(post.getComments().get(j))) {
                                    AccountComentList.getItems().add(post.getComments().get(j));
                                    Button likeCommentbtn = new Button();
                                    Button replyCommentbtn = new Button();
                                    likeCommentbtn.setText("like comment");
                                    replyCommentbtn.setText("reply on this comment!");
                                    AccountComentList.getItems().add(likeCommentbtn);
                                    AccountComentList.getItems().add(replyCommentbtn);
                                    int finalJ = j;
                                    likeCommentbtn.setOnAction((ActionEvent3) -> {
                                        clearTextandImg();
                                        Comment comment = Comment.getCommentById(post.getComments().get(finalJ).getId());
                                        boolean hasLiked = false;
                                        for (int k = 0; k < comment.getLikes().size(); k++) {
                                            if (comment.getLikes().get(k).getAccount().equals(LoggedInAccount.getInstance().getLoggedIn())) {
                                                System.out.println("hasliked");
                                                new PopupMessage(Alert.AlertType.ERROR, "you have already liked this comment!");
                                                hasLiked = true;
                                            }
                                        }
                                        if (!hasLiked) {
                                            likecomment(comment);
                                        }
                                    });

                                    replyCommentbtn.setOnAction((ActionEvent3) -> {
                                        //TextField replytextfield = new TextField();
                                        //replytextfield.setPromptText("reply on this comment");
                                        textField.setPromptText("reply on this comment");
                                        //AccountComentList.getItems().add(replytextfield);
                                        Button submitbtn = new Button();
                                        submitbtn.setText("submit");
                                        AccountComentList.getItems().remove(commentbtn);//////
                                        AccountComentList.getItems().add(submitbtn);
                                        submitbtn.setOnAction((ActionEvent4) -> {
                                            Comment comment = Comment.getCommentById(post.getComments().get(finalJ).getId());
                                            //comment.writeReplyComment(replytextfield.getText(), LoggedInAccount.getInstance().getLoggedIn());
                                            comment.writeReplyComment(textField.getText(), LoggedInAccount.getInstance().getLoggedIn());
                                            System.out.println("Replied comment wrote!");
                                            new PopupMessage(Alert.AlertType.ERROR, "you replied to this comment!");
                                            // replytextfield.setText("");
                                            textField.setText("");
                                            clearTextandImg();
                                        });
                                    });

                                    AccountComentList.getItems().add("_____________________________");
                                }
                            }
                            for (int k = 0; k < 2; k++) {
                                AccountComentList.getItems().add("");
                            }
                        }

                        AccountComentList.getItems().add(textField);
                        AccountComentList.getItems().add(commentbtn);

                    });

                    clearTextandImg();

                    vboxForButtons.getChildren().add(btnNumber);
                    if(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getFile()!=null) {
                        image = new Image(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getFile().toURI().toString(), 100, 150, true, true);
                        imageView2 = new ImageView();
                        imageView2.setImage(image);
                        imageView2.setFitWidth(100);
                        imageView2.setFitHeight(150);
                        imageView2.setPreserveRatio(true);
                        imageView2.setSmooth(true);
                        imageView2.setCache(true);
                    }
                    LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).addview(LoggedInAccount.getInstance().getLoggedIn());
                    Post post=LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i);
                    PostsList.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getUsername());
                    if(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getFile()!=null) {
                        PostsList.getItems().add(imageView2);
                        PostsList.getItems().add(btnNumber);
                       // PostsList.getItems().add(imageView4);
                        PostsList.getItems().add(Showlikebtn);
                        PostsList.getItems().add(Showcommentbtn);
                        PostsList.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i));
                        if(LoggedInAccount.getInstance().getLoggedIn().isBusinessAccount()){
                            PostsList.getItems().add("views: "+LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getViews().size());
                            for(int k=0;k<post.getViews().size();k++){
                                PostsList.getItems().add(post.getDateviewed().get(k) + " = username (" + post.getViews().get(k).getUsername()+")");
                            }
                            PostsList.getItems().add("likes : "+post.getLike().size());
                            for (int k=0;k<post.getLike().size();k++){
                                PostsList.getItems().add(post.getDateliked().get(k)+" = username (" + post.getLike().get(k).getUsername()+")");
                            }
                            PostsList.getItems().add(ShowviewChart);
                            ShowviewChart.setOnAction(ActionEvent -> {
                                CategoryAxis xAxis = new CategoryAxis();
                                xAxis.setLabel("day");
                                NumberAxis yAxis = new NumberAxis();
                                yAxis.setLabel("number of accounts");
                                BarChart barChart = new BarChart<>(xAxis, yAxis);
                                XYChart.Series<String, Integer> data = new XYChart.Series<>();
                                Calendar calendar = Calendar.getInstance();
                                for (int k = 0; k < post.getViews().size(); k++) {
                                    String dayWeekText1 = new SimpleDateFormat("EEEE").format(post.getDateviewed().get(k));
                                    Integer ViewNum=1;
                                    for(int l=1;l<post.getViews().size();l++){
                                        String dayWeekText2 = new SimpleDateFormat("EEEE").format(post.getDateviewed().get(k));
                                        if(dayWeekText1.equals(dayWeekText2)){
                                            ViewNum++;
                                        }
                                    }
                                    data.getData().add(new XYChart.Data<>(dayWeekText1,ViewNum));
                                }
                                barChart.getData().add(data);

                                StackPane secondaryLayout = new StackPane();
                                secondaryLayout.getChildren().add(barChart);
                                Scene secondScene = new Scene(secondaryLayout, 230, 100);
                                // New window (Stage)
                                Stage newWindow = new Stage();
                                newWindow.setTitle("State Chart");
                                newWindow.setScene(secondScene);
                                // Set position of second window, related to primary window.
                                newWindow.setX(MenuChanger.getStage().getX() - 200);
                                newWindow.setY(MenuChanger.getStage().getY() - 200);
                                newWindow.show();
                            });
                            PostsList.getItems().add(ShowlikeChart);
                            ShowlikeChart.setOnAction(ActionEvent -> {
                                CategoryAxis xAxis = new CategoryAxis();
                                xAxis.setLabel("day");
                                NumberAxis yAxis = new NumberAxis();
                                yAxis.setLabel("number of accounts");
                                BarChart barChart = new BarChart<>(xAxis, yAxis);
                                XYChart.Series<String, Integer> data = new XYChart.Series<>();
                                Calendar calendar = Calendar.getInstance();
                                for (int k = 0; k < post.getLike().size(); k++) {
                                    String dayWeekText1 = new SimpleDateFormat("EEEE").format(post.getDateliked().get(k));
                                    Integer ViewNum=1;
                                    for(int l=1;l<post.getLike().size();l++){
                                        String dayWeekText2 = new SimpleDateFormat("EEEE").format(post.getDateliked().get(k));
                                        if(dayWeekText1.equals(dayWeekText2)){
                                            ViewNum++;
                                        }
                                    }
                                    data.getData().add(new XYChart.Data<>(dayWeekText1,ViewNum));
                                }
                                barChart.getData().add(data);
                                StackPane secondaryLayout = new StackPane();
                                secondaryLayout.getChildren().add(barChart);
                                Scene secondScene = new Scene(secondaryLayout, 230, 100);
                                // New window (Stage)
                                Stage newWindow = new Stage();
                                newWindow.setTitle("State Chart");
                                newWindow.setScene(secondScene);
                                // Set position of second window, related to primary window.
                                newWindow.setX(MenuChanger.getStage().getX() - 200);
                                newWindow.setY(MenuChanger.getStage().getY() - 200);
                                newWindow.show();
                            });

                        }

                    }
                    else {
                        PostsList.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i));
                        PostsList.getItems().add(imageView2);
                        PostsList.getItems().add(btnNumber);
                        PostsList.getItems().add(Showlikebtn);
                        PostsList.getItems().add(Showcommentbtn);
                        if(LoggedInAccount.getInstance().getLoggedIn().isBusinessAccount()){
                            PostsList.getItems().add("views: "+LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getViews().size());
                            for(int k=0;k<post.getViews().size();k++){
                                PostsList.getItems().add(post.getDateviewed().get(k) + " = username (" + post.getViews().get(k).getUsername()+")");
                            }
                            PostsList.getItems().add("likes : "+post.getLike().size());
                            for (int k=0;k<post.getLike().size();k++){
                                PostsList.getItems().add(post.getDateliked().get(k)+" = username (" + post.getLike().get(k).getUsername()+")");
                            }
                            PostsList.getItems().add(ShowviewChart);
                            ShowviewChart.setOnAction(ActionEvent -> {
                                CategoryAxis xAxis = new CategoryAxis();
                                xAxis.setLabel("day");
                                NumberAxis yAxis = new NumberAxis();
                                yAxis.setLabel("number of accounts");
                                BarChart barChart = new BarChart<>(xAxis, yAxis);
                                XYChart.Series<String, Integer> data = new XYChart.Series<>();
                                Calendar calendar = Calendar.getInstance();
                                for (int k = 0; k < post.getViews().size(); k++) {
                                    String dayWeekText1 = new SimpleDateFormat("EEEE").format(post.getDateviewed().get(k));
                                    Integer ViewNum=1;
                                    for(int l=1;l<post.getViews().size();l++){
                                        String dayWeekText2 = new SimpleDateFormat("EEEE").format(post.getDateviewed().get(k));
                                        if(dayWeekText1.equals(dayWeekText2)){
                                            ViewNum++;
                                        }
                                    }
                                    data.getData().add(new XYChart.Data<>(dayWeekText1,ViewNum));
                                }
                                barChart.getData().add(data);

                                StackPane secondaryLayout = new StackPane();
                                secondaryLayout.getChildren().add(barChart);
                                Scene secondScene = new Scene(secondaryLayout, 230, 100);
                                // New window (Stage)
                                Stage newWindow = new Stage();
                                newWindow.setTitle("State Chart");
                                newWindow.setScene(secondScene);
                                // Set position of second window, related to primary window.
                                newWindow.setX(MenuChanger.getStage().getX() - 200);
                                newWindow.setY(MenuChanger.getStage().getY() - 200);
                                newWindow.show();
                            });
                            PostsList.getItems().add(ShowlikeChart);
                            ShowlikeChart.setOnAction(ActionEvent -> {
                                CategoryAxis xAxis = new CategoryAxis();
                                xAxis.setLabel("day");
                                NumberAxis yAxis = new NumberAxis();
                                yAxis.setLabel("number of accounts");
                                BarChart barChart = new BarChart<>(xAxis, yAxis);
                                XYChart.Series<String, Integer> data = new XYChart.Series<>();
                                Calendar calendar = Calendar.getInstance();
                                for (int k = 0; k < post.getLike().size(); k++) {
                                    String dayWeekText1 = new SimpleDateFormat("EEEE").format(post.getDateliked().get(k));
                                    Integer ViewNum=1;
                                    for(int l=1;l<post.getLike().size();l++){
                                        String dayWeekText2 = new SimpleDateFormat("EEEE").format(post.getDateliked().get(k));
                                        if(dayWeekText1.equals(dayWeekText2)){
                                            ViewNum++;
                                        }
                                    }
                                    data.getData().add(new XYChart.Data<>(dayWeekText1,ViewNum));
                                }
                                barChart.getData().add(data);
                                StackPane secondaryLayout = new StackPane();
                                secondaryLayout.getChildren().add(barChart);
                                Scene secondScene = new Scene(secondaryLayout, 230, 100);
                                // New window (Stage)
                                Stage newWindow = new Stage();
                                newWindow.setTitle("State Chart");
                                newWindow.setScene(secondScene);
                                // Set position of second window, related to primary window.
                                newWindow.setX(MenuChanger.getStage().getX() - 200);
                                newWindow.setY(MenuChanger.getStage().getY() - 200);
                                newWindow.show();
                            });

                        }

                    }
                    PostsList.getItems().add("_____________________________");

                }
            }
        }
        else {
            for (int i = size-1; i >=size-2; i--) {
                if (!PostsList.getItems().contains(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i))) {
                    Button btnNumber = new Button();
                    Button Showlikebtn = new Button();
                    Button Showcommentbtn = new Button();
                    Button ShowviewChart = new Button();
                    ShowviewChart.setText("show view state chart");
                    Button ShowlikeChart = new Button();
                    ShowlikeChart.setText("show like state chart");
                    Showlikebtn.setText("Show Likes");
                    btnNumber.setText("Like");
                    Showcommentbtn.setText("show comment(write a comment)");
                    int finalI = i;

                    btnNumber.setOnAction((ActionEvent) -> {
                        likepost(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile());
                        clearTextandImg();
                    });

                    Showlikebtn.setOnAction((ActionEvent) -> {
                        clearTextandImg();
                        Post post = Post.getPostByFile(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile());
                        if (post.getLike().size() == 0) {
                            AccountLikePost.getItems().add("no likes for this post :(");
                        } else {
                            AccountLikePost.getItems().add("likes : " + post.getLike().size());
                            for (int j = 0; j < post.getLike().size(); j++) {
                                if (!AccountLikePost.getItems().contains(post.getLike().get(j).getUsername())) {
                                    AccountLikePost.getItems().add(post.getLike().get(j).getUsername());
                                }
                            }
                        }
                    });

                    Showcommentbtn.setOnAction((ActionEvent) -> {
                        clearTextandImg();
                        Post post = Post.getPostByFile(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile());
                        TextField textField = new TextField();
                        textField.setPromptText("write down your comment");
                        Button commentbtn = new Button();
                        commentbtn.setText("comment");
                        commentbtn.setOnAction((ActionEvent2) -> {
                            commentpost(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile(), textField.getText());
                            clearTextandImg();
                        });

                        if (post.getComments().size() == 0) {
                            AccountComentList.getItems().add("no comments for this post :(");
                            for (int k = 0; k < 2; k++) {
                                AccountComentList.getItems().add("");
                            }
                        } else {
                            AccountComentList.getItems().add("comments : " + post.getComments().size());
                            for (int j = 0; j < post.getComments().size(); j++) {
                                if (!AccountComentList.getItems().contains(post.getComments().get(j))) {
                                    AccountComentList.getItems().add(post.getComments().get(j));
                                    Button likeCommentbtn = new Button();
                                    Button replyCommentbtn = new Button();
                                    likeCommentbtn.setText("like comment");
                                    replyCommentbtn.setText("reply on this comment!");
                                    AccountComentList.getItems().add(likeCommentbtn);
                                    AccountComentList.getItems().add(replyCommentbtn);
                                    int finalJ = j;
                                    likeCommentbtn.setOnAction((ActionEvent3) -> {
                                        clearTextandImg();
                                        Comment comment = Comment.getCommentById(post.getComments().get(finalJ).getId());
                                        boolean hasLiked = false;
                                        for (int k = 0; k < comment.getLikes().size(); k++) {
                                            if (comment.getLikes().get(k).getAccount().equals(LoggedInAccount.getInstance().getLoggedIn())) {
                                                System.out.println("hasliked");
                                                new PopupMessage(Alert.AlertType.ERROR, "you have already liked this comment!");
                                                hasLiked = true;
                                            }
                                        }
                                        if (!hasLiked) {
                                            likecomment(comment);
                                        }
                                    });

                                    replyCommentbtn.setOnAction((ActionEvent3) -> {
                                        TextField replytextfield = new TextField();
                                        replytextfield.setPromptText("reply on this comment");
                                        AccountComentList.getItems().add(replytextfield);
                                        Button submitbtn = new Button();
                                        submitbtn.setText("submit");
                                        AccountComentList.getItems().add(submitbtn);
                                        submitbtn.setOnAction((ActionEvent4) -> {
                                            Comment comment = Comment.getCommentById(post.getComments().get(finalJ).getId());
                                            comment.writeReplyComment(replytextfield.getText(), LoggedInAccount.getInstance().getLoggedIn());
                                            System.out.println("Replied comment wrote!");
                                            new PopupMessage(Alert.AlertType.ERROR, "you replied to this comment!");
                                            replytextfield.setText("");

                                        });
                                    });

                                    AccountComentList.getItems().add("_____________________________");
                                }
                            }
                            for (int k = 0; k < 2; k++) {
                                AccountComentList.getItems().add("");
                            }
                        }

                        AccountComentList.getItems().add(textField);
                        AccountComentList.getItems().add(commentbtn);

                    });

                    clearTextandImg();

                    vboxForButtons.getChildren().add(btnNumber);
                    if(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getFile()!=null) {
                        image = new Image(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getFile().toURI().toString(), 100, 150, true, true);
                        imageView2 = new ImageView();
                        imageView2.setImage(image);
                        imageView2.setFitWidth(100);
                        imageView2.setFitHeight(150);
                        imageView2.setPreserveRatio(true);
                        imageView2.setSmooth(true);
                        imageView2.setCache(true);
                    }
                    LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).addview(LoggedInAccount.getInstance().getLoggedIn());
                    PostsList.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getUsername());
                    Post post=LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i);
                    if(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getFile()!=null) {
                        PostsList.getItems().add(imageView2);
                        PostsList.getItems().add(btnNumber);
                        PostsList.getItems().add(Showlikebtn);
                        PostsList.getItems().add(Showcommentbtn);
                        PostsList.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i));
                        if(LoggedInAccount.getInstance().getLoggedIn().isBusinessAccount()){
                            PostsList.getItems().add("views: "+LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getViews().size());
                            for(int k=0;k<post.getViews().size();k++){
                                PostsList.getItems().add(post.getDateviewed().get(k) + " = username (" + post.getViews().get(k).getUsername()+")");
                            }
                            PostsList.getItems().add("likes : "+post.getLike().size());
                            for (int k=0;k<post.getLike().size();k++){
                                PostsList.getItems().add(post.getDateliked().get(k)+" = username (" + post.getLike().get(k).getUsername()+")");
                            }
                            PostsList.getItems().add(ShowviewChart);
                            ShowviewChart.setOnAction(ActionEvent -> {
                                CategoryAxis xAxis = new CategoryAxis();
                                xAxis.setLabel("day");
                                NumberAxis yAxis = new NumberAxis();
                                yAxis.setLabel("number of accounts");
                                BarChart barChart = new BarChart<>(xAxis, yAxis);
                                XYChart.Series<String, Integer> data = new XYChart.Series<>();
                                Calendar calendar = Calendar.getInstance();
                                for (int k = 0; k < post.getViews().size(); k++) {
                                    String dayWeekText1 = new SimpleDateFormat("EEEE").format(post.getDateviewed().get(k));
                                    Integer ViewNum=1;
                                    for(int l=1;l<post.getViews().size();l++){
                                        String dayWeekText2 = new SimpleDateFormat("EEEE").format(post.getDateviewed().get(k));
                                        if(dayWeekText1.equals(dayWeekText2)){
                                            ViewNum++;
                                        }
                                    }
                                    data.getData().add(new XYChart.Data<>(dayWeekText1,ViewNum));
                                }
                                barChart.getData().add(data);

                                StackPane secondaryLayout = new StackPane();
                                secondaryLayout.getChildren().add(barChart);
                                Scene secondScene = new Scene(secondaryLayout, 230, 100);
                                // New window (Stage)
                                Stage newWindow = new Stage();
                                newWindow.setTitle("State Chart");
                                newWindow.setScene(secondScene);
                                // Set position of second window, related to primary window.
                                newWindow.setX(MenuChanger.getStage().getX() - 200);
                                newWindow.setY(MenuChanger.getStage().getY() - 200);
                                newWindow.show();
                            });
                            PostsList.getItems().add(ShowlikeChart);
                            ShowlikeChart.setOnAction(ActionEvent -> {
                                CategoryAxis xAxis = new CategoryAxis();
                                xAxis.setLabel("day");
                                NumberAxis yAxis = new NumberAxis();
                                yAxis.setLabel("number of accounts");
                                BarChart barChart = new BarChart<>(xAxis, yAxis);
                                XYChart.Series<String, Integer> data = new XYChart.Series<>();
                                Calendar calendar = Calendar.getInstance();
                                for (int k = 0; k < post.getLike().size(); k++) {
                                    String dayWeekText1 = new SimpleDateFormat("EEEE").format(post.getDateliked().get(k));
                                    Integer ViewNum=1;
                                    for(int l=1;l<post.getLike().size();l++){
                                        String dayWeekText2 = new SimpleDateFormat("EEEE").format(post.getDateliked().get(k));
                                        if(dayWeekText1.equals(dayWeekText2)){
                                            ViewNum++;
                                        }
                                    }
                                    data.getData().add(new XYChart.Data<>(dayWeekText1,ViewNum));
                                }
                                barChart.getData().add(data);
                                StackPane secondaryLayout = new StackPane();
                                secondaryLayout.getChildren().add(barChart);
                                Scene secondScene = new Scene(secondaryLayout, 230, 100);
                                // New window (Stage)
                                Stage newWindow = new Stage();
                                newWindow.setTitle("State Chart");
                                newWindow.setScene(secondScene);
                                // Set position of second window, related to primary window.
                                newWindow.setX(MenuChanger.getStage().getX() - 200);
                                newWindow.setY(MenuChanger.getStage().getY() - 200);
                                newWindow.show();
                            });

                        }
                    }
                    else {
                        PostsList.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i));
                        PostsList.getItems().add(btnNumber);
                        PostsList.getItems().add(Showlikebtn);
                        PostsList.getItems().add(Showcommentbtn);
                        if(LoggedInAccount.getInstance().getLoggedIn().isBusinessAccount()){
                            PostsList.getItems().add("views: "+LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getViews().size());
                            for(int k=0;k<post.getViews().size();k++){
                                PostsList.getItems().add(post.getDateviewed().get(k) + " = username (" + post.getViews().get(k).getUsername()+")");
                            }
                            PostsList.getItems().add("likes : "+post.getLike().size());
                            for (int k=0;k<post.getLike().size();k++){
                                PostsList.getItems().add(post.getDateliked().get(k)+" = username (" + post.getLike().get(k).getUsername()+")");
                            }

                            PostsList.getItems().add(ShowviewChart);
                            ShowviewChart.setOnAction(ActionEvent -> {
                                CategoryAxis xAxis = new CategoryAxis();
                                xAxis.setLabel("day");
                                NumberAxis yAxis = new NumberAxis();
                                yAxis.setLabel("number of accounts");
                                BarChart barChart = new BarChart<>(xAxis, yAxis);
                                XYChart.Series<String, Integer> data = new XYChart.Series<>();
                                Calendar calendar = Calendar.getInstance();
                                for (int k = 0; k < post.getViews().size(); k++) {
                                    String dayWeekText1 = new SimpleDateFormat("EEEE").format(post.getDateviewed().get(k));
                                    Integer ViewNum=1;
                                    for(int l=1;l<post.getViews().size();l++){
                                        String dayWeekText2 = new SimpleDateFormat("EEEE").format(post.getDateviewed().get(k));
                                        if(dayWeekText1.equals(dayWeekText2)){
                                            ViewNum++;
                                        }
                                    }
                                    data.getData().add(new XYChart.Data<>(dayWeekText1,ViewNum));
                                }
                                barChart.getData().add(data);

                                StackPane secondaryLayout = new StackPane();
                                secondaryLayout.getChildren().add(barChart);
                                Scene secondScene = new Scene(secondaryLayout, 230, 100);
                                // New window (Stage)
                                Stage newWindow = new Stage();
                                newWindow.setTitle("State Chart");
                                newWindow.setScene(secondScene);
                                // Set position of second window, related to primary window.
                                newWindow.setX(MenuChanger.getStage().getX() - 200);
                                newWindow.setY(MenuChanger.getStage().getY() - 200);
                                newWindow.show();
                            });
                            PostsList.getItems().add(ShowlikeChart);
                            ShowlikeChart.setOnAction(ActionEvent -> {
                                CategoryAxis xAxis = new CategoryAxis();
                                xAxis.setLabel("day");
                                NumberAxis yAxis = new NumberAxis();
                                yAxis.setLabel("number of accounts");
                                BarChart barChart = new BarChart<>(xAxis, yAxis);
                                XYChart.Series<String, Integer> data = new XYChart.Series<>();
                                Calendar calendar = Calendar.getInstance();
                                for (int k = 0; k < post.getLike().size(); k++) {
                                    String dayWeekText1 = new SimpleDateFormat("EEEE").format(post.getDateliked().get(k));
                                    Integer ViewNum=1;
                                    for(int l=1;l<post.getLike().size();l++){
                                        String dayWeekText2 = new SimpleDateFormat("EEEE").format(post.getDateliked().get(k));
                                        if(dayWeekText1.equals(dayWeekText2)){
                                            ViewNum++;
                                        }
                                    }
                                    data.getData().add(new XYChart.Data<>(dayWeekText1,ViewNum));
                                }
                                barChart.getData().add(data);
                                StackPane secondaryLayout = new StackPane();
                                secondaryLayout.getChildren().add(barChart);
                                Scene secondScene = new Scene(secondaryLayout, 230, 100);
                                // New window (Stage)
                                Stage newWindow = new Stage();
                                newWindow.setTitle("State Chart");
                                newWindow.setScene(secondScene);
                                // Set position of second window, related to primary window.
                                newWindow.setX(MenuChanger.getStage().getX() - 200);
                                newWindow.setY(MenuChanger.getStage().getY() - 200);
                                newWindow.show();
                            });

                        }

                    }
                    PostsList.getItems().add("_____________________________");

                }
            }
        }
            clearTextandImg();

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
            String content1 = "ad : ";
            String FinalContent = content1 + postContent.getText();
            LoggedInAccount.getInstance().getLoggedIn().createPost(FinalContent,finalFile);
        } else {
            String content = "content : "+postContent.getText();
            LoggedInAccount.getInstance().getLoggedIn().createPost(content,finalFile);
        }
        System.out.println("Post created!");

        for(int i=0;i<LoggedInAccount.getInstance().getLoggedIn().getPosts().size();i++){
            if (!PostsList.getItems().contains(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i))) {
                Button btnNumber = new Button();
                Button Showlikebtn = new Button();
                Button Showcommentbtn= new Button();
                btnNumber.setText("Like");
                Showlikebtn.setText("Show Likes");
                Showcommentbtn.setText("show comment(write a comment)");
                int finalI = i;
                btnNumber.setOnAction((ActionEvent)->{
                    likepost(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile());
                    Post post = Post.getPostByFile(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile());
                    clearTextandImg();
                });

                Showlikebtn.setOnAction((ActionEvent)->{
                    clearTextandImg();
                    Post post = Post.getPostByFile(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile());
                    if(post.getLike().size()==0){
                        AccountLikePost.getItems().add("no likes for this post :(");
                    }
                    else {
                        AccountLikePost.getItems().add("likes : "+post.getLike().size());
                        for (int j = 0; j < post.getLike().size(); j++) {
                           if (! AccountLikePost.getItems().contains(post.getLike().get(j).getUsername())) {
                                AccountLikePost.getItems().add(post.getLike().get(j).getUsername());
                                }
                        }
                    }
                });

                Showcommentbtn.setOnAction((ActionEvent)->{
                    clearTextandImg();
                    Post post = Post.getPostByFile(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile());
                    TextField textField = new TextField();
                    textField.setPromptText("write down your comment");
                    Button commentbtn = new Button();
                    commentbtn.setText("comment");
                    commentbtn.setOnAction((ActionEvent2)->{
                        commentpost(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile() , textField.getText());
                        clearTextandImg();
                    });

                    if(post.getComments().size()==0){
                        AccountComentList.getItems().add("no comments for this post :(");
                        for (int k=0;k<2;k++){
                            AccountComentList.getItems().add("");
                        }
                    }
                    else{
                        AccountComentList.getItems().add("comments : "+post.getComments().size());
                        for (int j=0;j<post.getComments().size();j++){
                            if (! AccountComentList.getItems().contains(post.getComments().get(j))) {
                                AccountComentList.getItems().add(post.getComments().get(j));
                                Button likeCommentbtn= new Button();
                                likeCommentbtn.setText("like comment");
                                AccountComentList.getItems().add(likeCommentbtn);
                                int finalJ = j;
                                likeCommentbtn.setOnAction((ActionEvent3)->{
                                    clearTextandImg();
                                    System.out.println(post.getComments().get(finalJ).getId());
                                    Comment comment = Comment.getCommentById(post.getComments().get(finalJ).getId());
                                    boolean hasLiked=false;
                                    for(int k=0;k<comment.getLikes().size();k++) {
                                        if (comment.getLikes().get(k).getAccount().equals(LoggedInAccount.getInstance().getLoggedIn())) {
                                            new PopupMessage(Alert.AlertType.ERROR, "you have already liked this comment!");
                                            hasLiked=true;
                                        }
                                    }
                                    if(!hasLiked) {
                                        likecomment(comment);
                                    }
                                });
                                AccountComentList.getItems().add("_____________________________");
                            }
                        }
                        for (int k=0;k<2;k++){
                            AccountComentList.getItems().add("");
                        }
                    }

                    AccountComentList.getItems().add(textField);
                    AccountComentList.getItems().add(commentbtn);

                });

                clearTextandImg();
                vboxForButtons.getChildren().add(btnNumber);
                image = new Image(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getFile().toURI().toString(), 100, 150, true, true);
                imageView2 = new ImageView();imageView2.setImage(image);imageView2.setFitWidth(100);imageView2.setFitHeight(150);imageView2.setPreserveRatio(true);imageView2.setSmooth(true);imageView2.setCache(true);
                LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).addview(LoggedInAccount.getInstance().getLoggedIn());
                PostsList.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getUsername());
                PostsList.getItems().add(imageView2);
                PostsList.getItems().add(btnNumber);
                PostsList.getItems().add(Showlikebtn);
                PostsList.getItems().add(Showcommentbtn);
                PostsList.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i));
                PostsList.getItems().add("_____________________________");

            }
        }
        clearTextandImg();
    }

    private void likecomment(Comment comment) {
        boolean success=true;
        new PopupMessage(Alert.AlertType.ERROR, "you  liked this comment!");
        success = comment.like(LoggedInAccount.getInstance().getLoggedIn());
    }

    private void commentpost(File file, String text) {
        Post post = Post.getPostByFile(file);
        post.writeComment(text,LoggedInAccount.getInstance().getLoggedIn());
        new PopupMessage(Alert.AlertType.ERROR, "comment is written!");
       // System.out.println("Comment wrote!");

    }

    private void likepost(File file) {
        Post post = Post.getPostByFile(file);
        boolean success=true;
        success = post.like(LoggedInAccount.getInstance().getLoggedIn());
        if(post.getLike().contains(LoggedInAccount.getInstance().getLoggedIn())){
            System.out.println("ehem");
            new PopupMessage(Alert.AlertType.ERROR, "you have already liked this post!");

        }
        else {
            post.addlikestate(LoggedInAccount.getInstance().getLoggedIn());
            new PopupMessage(Alert.AlertType.ERROR, "you liked this post!");
        }

    }

    public void clearTextandImg(){
        AccountLikePost.getItems().clear();
        AccountComentList.getItems().clear();
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

    public void createPostsinAnotherPage(ActionEvent actionEvent) {
        MenuChanger.changeMenu("CreatePost");
    }

    public void showAllPosts(ActionEvent actionEvent) {
        int size=LoggedInAccount.getInstance().getLoggedIn().getPosts().size();
        for (int i = size-1; i >=0; i--) {
            if (!PostsList.getItems().contains(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i))) {
                Button btnNumber = new Button();
                Button Showlikebtn = new Button();
                Button Showcommentbtn = new Button();
                Button ShowviewChart = new Button();
                ShowviewChart.setText("show view state chart");
                Button ShowlikeChart = new Button();
                ShowlikeChart.setText("show like state chart");
                Showlikebtn.setText("Show Likes");
                btnNumber.setText("Like");
                Showcommentbtn.setText("show comment(write a comment)");
                int finalI = i;

                btnNumber.setOnAction((ActionEvent) -> {
                    likepost(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile());
                    clearTextandImg();
                });

                Showlikebtn.setOnAction((ActionEvent) -> {
                    clearTextandImg();
                    Post post = Post.getPostByFile(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile());
                    if (post.getLike().size() == 0) {
                        AccountLikePost.getItems().add("no likes for this post :(");
                    } else {
                        AccountLikePost.getItems().add("likes : " + post.getLike().size());
                        for (int j = 0; j < post.getLike().size(); j++) {
                            if (!AccountLikePost.getItems().contains(post.getLike().get(j).getUsername())) {
                                AccountLikePost.getItems().add(post.getLike().get(j).getUsername());
                            }
                        }
                    }
                });

                Showcommentbtn.setOnAction((ActionEvent) -> {
                    clearTextandImg();
                    Post post = Post.getPostByFile(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile());
                    TextField textField = new TextField();
                    textField.setPromptText("write down your comment");
                    Button commentbtn = new Button();
                    commentbtn.setText("comment");
                    commentbtn.setOnAction((ActionEvent2) -> {
                        commentpost(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(finalI).getFile(), textField.getText());
                        clearTextandImg();
                    });

                    if (post.getComments().size() == 0) {
                        AccountComentList.getItems().add("no comments for this post :(");
                        for (int k = 0; k < 2; k++) {
                            AccountComentList.getItems().add("");
                        }
                    } else {
                        AccountComentList.getItems().add("comments : " + post.getComments().size());
                        for (int j = 0; j < post.getComments().size(); j++) {
                            if (!AccountComentList.getItems().contains(post.getComments().get(j))) {
                                AccountComentList.getItems().add(post.getComments().get(j));
                                Button likeCommentbtn = new Button();
                                Button replyCommentbtn = new Button();
                                likeCommentbtn.setText("like comment");
                                replyCommentbtn.setText("reply on this comment!");
                                AccountComentList.getItems().add(likeCommentbtn);
                                AccountComentList.getItems().add(replyCommentbtn);
                                int finalJ = j;
                                likeCommentbtn.setOnAction((ActionEvent3) -> {
                                    clearTextandImg();
                                    Comment comment = Comment.getCommentById(post.getComments().get(finalJ).getId());
                                    boolean hasLiked = false;
                                    for (int k = 0; k < comment.getLikes().size(); k++) {
                                        if (comment.getLikes().get(k).getAccount().equals(LoggedInAccount.getInstance().getLoggedIn())) {
                                            System.out.println("hasliked");
                                            new PopupMessage(Alert.AlertType.ERROR, "you have already liked this comment!");
                                            hasLiked = true;
                                        }
                                    }
                                    if (!hasLiked) {
                                        likecomment(comment);
                                    }
                                });

                                replyCommentbtn.setOnAction((ActionEvent3) -> {
                                    TextField replytextfield = new TextField();
                                    replytextfield.setPromptText("reply on this comment");
                                    AccountComentList.getItems().add(replytextfield);
                                    Button submitbtn = new Button();
                                    submitbtn.setText("submit");
                                    AccountComentList.getItems().add(submitbtn);
                                    submitbtn.setOnAction((ActionEvent4) -> {
                                        Comment comment = Comment.getCommentById(post.getComments().get(finalJ).getId());
                                        comment.writeReplyComment(replytextfield.getText(), LoggedInAccount.getInstance().getLoggedIn());
                                        System.out.println("Replied comment wrote!");
                                        new PopupMessage(Alert.AlertType.ERROR, "you replied to this comment!");
                                        replytextfield.setText("");

                                    });
                                });

                                AccountComentList.getItems().add("_____________________________");
                            }
                        }
                        for (int k = 0; k < 2; k++) {
                            AccountComentList.getItems().add("");
                        }
                    }

                    AccountComentList.getItems().add(textField);
                    AccountComentList.getItems().add(commentbtn);

                });

                clearTextandImg();

                vboxForButtons.getChildren().add(btnNumber);
                if(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getFile()!=null) {
                    image = new Image(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getFile().toURI().toString(), 100, 150, true, true);
                    imageView2 = new ImageView();
                    imageView2.setImage(image);
                    imageView2.setFitWidth(100);
                    imageView2.setFitHeight(150);
                    imageView2.setPreserveRatio(true);
                    imageView2.setSmooth(true);
                    imageView2.setCache(true);
                }
                LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).addview(LoggedInAccount.getInstance().getLoggedIn());
                Post post =LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i);
                PostsList.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getUsername());
                if(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getFile()!=null) {
                    PostsList.getItems().add(imageView2);
                }
                if(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getFile()!=null) {
                    PostsList.getItems().add(btnNumber);
                    PostsList.getItems().add(Showlikebtn);
                    PostsList.getItems().add(Showcommentbtn);
                    PostsList.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i));
                    if(LoggedInAccount.getInstance().getLoggedIn().isBusinessAccount()){
                        PostsList.getItems().add("views: "+LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getViews().size());
                        for(int k=0;k<post.getViews().size();k++){
                            PostsList.getItems().add(post.getDateviewed().get(k) + " = username (" + post.getViews().get(k).getUsername()+")");
                        }
                        PostsList.getItems().add("likes : "+post.getLike().size());
                        for (int k=0;k<post.getLike().size();k++){
                            PostsList.getItems().add(post.getDateliked().get(k)+" = username (" + post.getLike().get(k).getUsername()+")");
                        }

                        PostsList.getItems().add(ShowviewChart);
                        ShowviewChart.setOnAction(ActionEvent -> {
                            CategoryAxis xAxis = new CategoryAxis();
                            xAxis.setLabel("day");
                            NumberAxis yAxis = new NumberAxis();
                            yAxis.setLabel("number of accounts");
                            BarChart barChart = new BarChart<>(xAxis, yAxis);
                            XYChart.Series<String, Integer> data = new XYChart.Series<>();
                            Calendar calendar = Calendar.getInstance();
                            for (int k = 0; k < post.getViews().size(); k++) {
                                String dayWeekText1 = new SimpleDateFormat("EEEE").format(post.getDateviewed().get(k));
                                Integer ViewNum=1;
                                for(int l=1;l<post.getViews().size();l++){
                                    String dayWeekText2 = new SimpleDateFormat("EEEE").format(post.getDateviewed().get(k));
                                    if(dayWeekText1.equals(dayWeekText2)){
                                        ViewNum++;
                                    }
                                }
                                data.getData().add(new XYChart.Data<>(dayWeekText1,ViewNum));
                            }
                            barChart.getData().add(data);

                            StackPane secondaryLayout = new StackPane();
                            secondaryLayout.getChildren().add(barChart);
                            Scene secondScene = new Scene(secondaryLayout, 230, 100);
                            // New window (Stage)
                            Stage newWindow = new Stage();
                            newWindow.setTitle("State Chart");
                            newWindow.setScene(secondScene);
                            // Set position of second window, related to primary window.
                            newWindow.setX(MenuChanger.getStage().getX() - 200);
                            newWindow.setY(MenuChanger.getStage().getY() - 200);
                            newWindow.show();
                        });
                        PostsList.getItems().add(ShowlikeChart);
                        ShowlikeChart.setOnAction(ActionEvent -> {
                            CategoryAxis xAxis = new CategoryAxis();
                            xAxis.setLabel("day");
                            NumberAxis yAxis = new NumberAxis();
                            yAxis.setLabel("number of accounts");
                            BarChart barChart = new BarChart<>(xAxis, yAxis);
                            XYChart.Series<String, Integer> data = new XYChart.Series<>();
                            Calendar calendar = Calendar.getInstance();
                            for (int k = 0; k < post.getLike().size(); k++) {
                                String dayWeekText1 = new SimpleDateFormat("EEEE").format(post.getDateliked().get(k));
                                Integer ViewNum=1;
                                for(int l=1;l<post.getLike().size();l++){
                                    String dayWeekText2 = new SimpleDateFormat("EEEE").format(post.getDateliked().get(k));
                                    if(dayWeekText1.equals(dayWeekText2)){
                                        ViewNum++;
                                    }
                                }
                                data.getData().add(new XYChart.Data<>(dayWeekText1,ViewNum));
                            }
                            barChart.getData().add(data);
                            StackPane secondaryLayout = new StackPane();
                            secondaryLayout.getChildren().add(barChart);
                            Scene secondScene = new Scene(secondaryLayout, 230, 100);
                            // New window (Stage)
                            Stage newWindow = new Stage();
                            newWindow.setTitle("State Chart");
                            newWindow.setScene(secondScene);
                            // Set position of second window, related to primary window.
                            newWindow.setX(MenuChanger.getStage().getX() - 200);
                            newWindow.setY(MenuChanger.getStage().getY() - 200);
                            newWindow.show();
                        });

                    }

                }
                else {
                    PostsList.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i));
                    PostsList.getItems().add(btnNumber);
                    PostsList.getItems().add(Showlikebtn);
                    PostsList.getItems().add(Showcommentbtn);
                    if(LoggedInAccount.getInstance().getLoggedIn().isBusinessAccount()){
                        PostsList.getItems().add("views: "+LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getViews().size());
                        for(int k=0;k<post.getViews().size();k++){
                            PostsList.getItems().add(post.getDateviewed().get(k) + " = username (" + post.getViews().get(k).getUsername()+")");
                        }
                        PostsList.getItems().add("likes : "+post.getLike().size());
                        for (int k=0;k<post.getLike().size();k++){
                            PostsList.getItems().add(post.getDateliked().get(k)+" = username (" + post.getLike().get(k).getUsername()+")");
                        }
                        PostsList.getItems().add(ShowviewChart);
                        ShowviewChart.setOnAction(ActionEvent -> {
                            CategoryAxis xAxis = new CategoryAxis();
                            xAxis.setLabel("day");
                            NumberAxis yAxis = new NumberAxis();
                            yAxis.setLabel("number of accounts");
                            BarChart barChart = new BarChart<>(xAxis, yAxis);
                            XYChart.Series<String, Integer> data = new XYChart.Series<>();
                            Calendar calendar = Calendar.getInstance();
                            for (int k = 0; k < post.getViews().size(); k++) {
                                String dayWeekText1 = new SimpleDateFormat("EEEE").format(post.getDateviewed().get(k));
                                Integer ViewNum=1;
                                for(int l=1;l<post.getViews().size();l++){
                                    String dayWeekText2 = new SimpleDateFormat("EEEE").format(post.getDateviewed().get(k));
                                    if(dayWeekText1.equals(dayWeekText2)){
                                        ViewNum++;
                                    }
                                }
                                data.getData().add(new XYChart.Data<>(dayWeekText1,ViewNum));
                            }
                            barChart.getData().add(data);

                            StackPane secondaryLayout = new StackPane();
                            secondaryLayout.getChildren().add(barChart);
                            Scene secondScene = new Scene(secondaryLayout, 230, 100);
                            // New window (Stage)
                            Stage newWindow = new Stage();
                            newWindow.setTitle("State Chart");
                            newWindow.setScene(secondScene);
                            // Set position of second window, related to primary window.
                            newWindow.setX(MenuChanger.getStage().getX() - 200);
                            newWindow.setY(MenuChanger.getStage().getY() - 200);
                            newWindow.show();
                        });
                        PostsList.getItems().add(ShowlikeChart);
                        ShowlikeChart.setOnAction(ActionEvent -> {
                            CategoryAxis xAxis = new CategoryAxis();
                            xAxis.setLabel("day");
                            NumberAxis yAxis = new NumberAxis();
                            yAxis.setLabel("number of accounts");
                            BarChart barChart = new BarChart<>(xAxis, yAxis);
                            XYChart.Series<String, Integer> data = new XYChart.Series<>();
                            Calendar calendar = Calendar.getInstance();
                            for (int k = 0; k < post.getLike().size(); k++) {
                                String dayWeekText1 = new SimpleDateFormat("EEEE").format(post.getDateliked().get(k));
                                Integer ViewNum=1;
                                for(int l=1;l<post.getLike().size();l++){
                                    String dayWeekText2 = new SimpleDateFormat("EEEE").format(post.getDateliked().get(k));
                                    if(dayWeekText1.equals(dayWeekText2)){
                                        ViewNum++;
                                    }
                                }
                                data.getData().add(new XYChart.Data<>(dayWeekText1,ViewNum));
                            }
                            barChart.getData().add(data);
                            StackPane secondaryLayout = new StackPane();
                            secondaryLayout.getChildren().add(barChart);
                            Scene secondScene = new Scene(secondaryLayout, 230, 100);
                            // New window (Stage)
                            Stage newWindow = new Stage();
                            newWindow.setTitle("State Chart");
                            newWindow.setScene(secondScene);
                            // Set position of second window, related to primary window.
                            newWindow.setX(MenuChanger.getStage().getX() - 200);
                            newWindow.setY(MenuChanger.getStage().getY() - 200);
                            newWindow.show();
                        });
                    }

                }
                PostsList.getItems().add("_____________________________");

            }
        }

    }

    public void gotoMainPage(ActionEvent actionEvent) {
        MenuChanger.changeMenu("MainPaneforLoginAccount");
    }

    public void PostListPressed(MouseEvent mouseEvent) throws MalformedURLException {
        if(PostsList.getSelectionModel().getSelectedItem().getClass().equals(ImageView.class)){
            ImageView imageView3= (ImageView) PostsList.getSelectionModel().getSelectedItem();
            URL url = new URL(imageView3.getImage().getUrl());
            File f = new File(url.getFile());
            System.out.println(f);
            Post post = Post.getPostByFile(f);
            gotoPostinfoPage(post);
        }
        if(PostsList.getSelectionModel().getSelectedItem().getClass().equals(Post.class)){
            Post post = (Post) PostsList.getSelectionModel().getSelectedItem();
            gotoPostinfoPage(post);
            System.out.println(post);
        }

    }

    private void gotoPostinfoPage(Post post) {
        LoggedInPost.getInstance().setLoggedIn(post);
        MenuChanger.changeMenu("PostInfo");
    }

}
