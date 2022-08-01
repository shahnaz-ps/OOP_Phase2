package com.example.demo.views;

import com.example.demo.model.Account;
import com.example.demo.model.LoggedInAccount;
import com.example.demo.model.LoggedInPost;
import com.example.demo.model.Post;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

public class MainPageforAccountController {
    public TextField idTextbox;
    public TextField followersNum;
    public TextField followingsNum;
    public ListView FollowingsList;
    public ListView FollowingsRecentPost;
    public ListView showYourRecentPosts;
    public Image image;
    public ImageView imageView2;
    public Circle ProCircle;

    public void initialize() {
        idTextbox.setText(LoggedInAccount.getInstance().getLoggedIn().getUsername());
        followersNum.setText(String.valueOf(LoggedInAccount.getInstance().getLoggedIn().getNumberOfFollowers()));
        followingsNum.setText(String.valueOf(LoggedInAccount.getInstance().getLoggedIn().getNumberOfFollowings()));
        if(LoggedInAccount.getInstance().getLoggedIn().getAccountsFile().get(LoggedInAccount.getInstance().getLoggedIn())!=null){
            image = new Image(LoggedInAccount.getInstance().getLoggedIn().getAccountsFile().get(LoggedInAccount.getInstance().getLoggedIn()).toURI().toString(), 100, 150, true, true);
            ProCircle.setFill(new ImagePattern(image));
        }
        Iterator<Account> it = LoggedInAccount.getInstance().getLoggedIn().getFollowings().iterator();
        while (it.hasNext()) {
            FollowingsList.getItems().add(it.next().getUsername());
        }

        int size=LoggedInAccount.getInstance().getLoggedIn().getPosts().size();
        if(size<=2) {
            for (int i = size - 1; i >= 0; i--) {
                int finalI = i;
                Button btnNumber = new Button();
                Button Showlikebtn = new Button();
                Button Showcommentbtn = new Button();
                Showlikebtn.setText("Show Likes");
                btnNumber.setText("go to post info");
                Showcommentbtn.setText("show comment(write a comment)");

                if(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getFile()!=null) {
                    image = new Image(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getFile().toURI().toString(), 100, 150, true, true);
                    imageView2 = new ImageView();imageView2.setImage(image);imageView2.setFitWidth(100);imageView2.setFitHeight(150);imageView2.setPreserveRatio(true);imageView2.setSmooth(true);imageView2.setCache(true);
                }

                LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).addview(LoggedInAccount.getInstance().getLoggedIn());
                showYourRecentPosts.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getUsername());
                if(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getFile()!=null) {
                    showYourRecentPosts.getItems().add(imageView2);
                }

                showYourRecentPosts.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i));
                showYourRecentPosts.getItems().add("_____________________________");
            }
        }else{
            for (int i = size - 1; i >= size-2; i--) {
                clearTextandImg();
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
                showYourRecentPosts.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getUsername());
                if(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getFile()!=null) {
                    showYourRecentPosts.getItems().add(imageView2);
                }
                showYourRecentPosts.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i));
                if(LoggedInAccount.getInstance().getLoggedIn().isBusinessAccount()){
                    showYourRecentPosts.getItems().add("views: "+post.getViews().size());
                    for(int k=0;k<post.getViews().size();k++){
                        showYourRecentPosts.getItems().add(post.getDateviewed().get(k) + " = username (" + post.getViews().get(k).getUsername()+")");
                    }
                    showYourRecentPosts.getItems().add("likes : "+post.getLike().size());
                    for (int k=0;k<post.getLike().size();k++){
                        showYourRecentPosts.getItems().add(post.getDateliked().get(k)+" = username (" + post.getLike().get(k).getUsername()+")");
                    }


                }
                showYourRecentPosts.getItems().add("_____________________________");
            }
        }

    }

    public void createPostsinAnotherPage(ActionEvent actionEvent) {
        MenuChanger.changeMenu("CreatePost");
    }

    public void logout(ActionEvent actionEvent) {
        MenuChanger.changeMenu("LoginMenu");
    }

    public void gotoLoginPage(ActionEvent actionEvent) {
        MenuChanger.changeMenu("LoginAccountPage");
    }

    public void followingListPressed(MouseEvent mouseEvent) {
        Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString());
        int size = Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().size();
        System.out.println(size);
        if (size == 0) {
            FollowingsRecentPost.getItems().add("no posts yet :(");
        }
        else{
        if (size <= 2) {
            clearTextandImg();
            for (int i = size - 1; i >= 0; i--) {
                Button ShowviewChart = new Button();
                ShowviewChart.setText("show view state chart");
                Button ShowlikeChart = new Button();
                ShowlikeChart.setText("show like state chart");
                if(Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i).getFile()!=null) {
                    image = new Image(Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i).getFile().toURI().toString(), 100, 150, true, true);
                    imageView2 = new ImageView();
                    imageView2.setImage(image);
                    imageView2.setFitWidth(100);
                    imageView2.setFitHeight(150);
                    imageView2.setPreserveRatio(true);
                    imageView2.setSmooth(true);
                    imageView2.setCache(true);
                }
                Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i).addview(LoggedInAccount.getInstance().getLoggedIn());
                Post post =Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i);
                FollowingsRecentPost.getItems().add(Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getUsername());
                if(Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i).getFile()!=null) {
                    FollowingsRecentPost.getItems().add(imageView2);
                }
                FollowingsRecentPost.getItems().add(Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i));
                FollowingsRecentPost.getItems().add("_____________________________");
            }
        } else {
            clearTextandImg();
            for (int i = size - 1; i >= size - 2; i--) {
                Button ShowviewChart = new Button();
                ShowviewChart.setText("show view state chart");
                Button ShowlikeChart = new Button();
                ShowlikeChart.setText("show like state chart");
                if(Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i).getFile()!=null) {
                    image = new Image(Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i).getFile().toURI().toString(), 100, 150, true, true);
                    imageView2 = new ImageView();
                    imageView2.setImage(image);
                    imageView2.setFitWidth(100);
                    imageView2.setFitHeight(150);
                    imageView2.setPreserveRatio(true);
                    imageView2.setSmooth(true);
                    imageView2.setCache(true);
                }
                Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i).addview(LoggedInAccount.getInstance().getLoggedIn());
                Post post =Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i);
                FollowingsRecentPost.getItems().add(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getUsername());
                if(Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i).getFile()!=null) {
                    FollowingsRecentPost.getItems().add(imageView2);
                }
                FollowingsRecentPost.getItems().add(Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i));

                FollowingsRecentPost.getItems().add("_____________________________");
            }
        }
    }

    }

    private void clearTextandImg() {
        FollowingsRecentPost.getItems().clear();
    }

    public void FollowinPostPressed(MouseEvent mouseEvent) throws MalformedURLException {
        if(FollowingsRecentPost.getSelectionModel().getSelectedItem().getClass().equals(ImageView.class)){
            ImageView imageView3= (ImageView) FollowingsRecentPost.getSelectionModel().getSelectedItem();
            URL url = new URL(imageView3.getImage().getUrl());
            File f = new File(url.getFile());
            System.out.println(f);
            Post post = Post.getPostByFile(f);
            gotoPostinfoPage(post);
        }
        if(FollowingsRecentPost.getSelectionModel().getSelectedItem().getClass().equals(Post.class)){
            Post post = (Post) FollowingsRecentPost.getSelectionModel().getSelectedItem();
            gotoPostinfoPage(post);
            System.out.println(post);
        }

    }

    private void gotoPostinfoPage(Post post) {
        System.out.println("im in");
        LoggedInPost.getInstance().setLoggedIn(post);
          MenuChanger.changeMenu("PostInfo");
    }

    public void ProPane(ActionEvent actionEvent) {
        MenuChanger.changeMenu("Profile");
    }

    public void YourPostPressed(MouseEvent mouseEvent) throws MalformedURLException {
        if(showYourRecentPosts.getSelectionModel().getSelectedItem().getClass().equals(ImageView.class)){
            ImageView imageView3= (ImageView) showYourRecentPosts.getSelectionModel().getSelectedItem();
            URL url = new URL(imageView3.getImage().getUrl());
            File f = new File(url.getFile());
            System.out.println(f);
            Post post = Post.getPostByFile(f);
            gotoPostinfoPage(post);
        }
        if(showYourRecentPosts.getSelectionModel().getSelectedItem().getClass().equals(Post.class)){
            Post post = (Post) showYourRecentPosts.getSelectionModel().getSelectedItem();
            gotoPostinfoPage(post);
            System.out.println(post);
        }

    }

    public void showAllYourPosts(ActionEvent actionEvent) {
        showYourRecentPosts.getItems().clear();
        int size=LoggedInAccount.getInstance().getLoggedIn().getPosts().size();
        if(size==0){
            showYourRecentPosts.getItems().add("no posts yet:(");
        }
        if(size<=2) {
            for (int i = size - 1; i >= 0; i--) {
                int finalI = i;
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
                showYourRecentPosts.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getUsername());
                if(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getFile()!=null) {
                    showYourRecentPosts.getItems().add(imageView2);
                }
                showYourRecentPosts.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i));
                showYourRecentPosts.getItems().add("_____________________________");
            }
        }else{
            for (int i = size - 1; i >= 0; i--) {
                clearTextandImg();
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
                showYourRecentPosts.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getUsername());
                if(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i).getFile()!=null) {
                    showYourRecentPosts.getItems().add(imageView2);
                }
                showYourRecentPosts.getItems().add(LoggedInAccount.getInstance().getLoggedIn().getPosts().get(i));
                showYourRecentPosts.getItems().add("_____________________________");
            }
        }

    }

    public void showAllFollowingPost(ActionEvent actionEvent) {
        Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString());
        FollowingsRecentPost.getItems().clear();
        int size=Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().size();
        if(size<=2){
            clearTextandImg();
            for (int i = size - 1; i >= 0; i--) {
                Button ShowviewChart = new Button();
                ShowviewChart.setText("show view state chart");
                Button ShowlikeChart = new Button();
                ShowlikeChart.setText("show like state chart");
                if(Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i).getFile()!=null) {
                    image = new Image(Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i).getFile().toURI().toString(), 100, 150, true, true);
                    imageView2 = new ImageView();
                    imageView2.setImage(image);
                    imageView2.setFitWidth(100);
                    imageView2.setFitHeight(150);
                    imageView2.setPreserveRatio(true);
                    imageView2.setSmooth(true);
                    imageView2.setCache(true);
                }
                Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i).addview(LoggedInAccount.getInstance().getLoggedIn());
                FollowingsRecentPost.getItems().add(Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getUsername());
                Post post =Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i);
                if(Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i).getFile()!=null) {
                    FollowingsRecentPost.getItems().add(imageView2);
                }
                FollowingsRecentPost.getItems().add(Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i));
                if(LoggedInAccount.getInstance().getLoggedIn().isBusinessAccount()){
                    FollowingsRecentPost.getItems().add("views: "+post.getViews().size());
                    for(int k=0;k<post.getViews().size();k++){
                        FollowingsRecentPost.getItems().add(post.getDateviewed().get(k) + " = username (" + post.getViews().get(k).getUsername()+")");
                    }
                    FollowingsRecentPost.getItems().add("likes : "+post.getLike().size());
                    for (int k=0;k<post.getLike().size();k++){
                        FollowingsRecentPost.getItems().add(post.getDateliked().get(k)+" = username (" + post.getLike().get(k).getUsername()+")");
                    }
                    FollowingsRecentPost.getItems().add(ShowviewChart);
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
                    FollowingsRecentPost.getItems().add(ShowlikeChart);
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
                FollowingsRecentPost.getItems().add("_____________________________");
            }
        } else{
            clearTextandImg();
            for (int i = size - 1; i >= 0; i--) {
                Button ShowviewChart = new Button();
                ShowviewChart.setText("show view state chart");
                Button ShowlikeChart = new Button();
                ShowlikeChart.setText("show like state chart");
                if(Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i).getFile()!=null) {
                    image = new Image(Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i).getFile().toURI().toString(), 100, 150, true, true);
                    imageView2 = new ImageView();
                    imageView2.setImage(image);
                    imageView2.setFitWidth(100);
                    imageView2.setFitHeight(150);
                    imageView2.setPreserveRatio(true);
                    imageView2.setSmooth(true);
                    imageView2.setCache(true);
                }
                Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i).addview(LoggedInAccount.getInstance().getLoggedIn());
                Post post =Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i);
                FollowingsRecentPost.getItems().add(Account.getAccount(LoginAccountPageController.getAccount2().getUsername()).getUsername());
                if(Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i).getFile()!=null) {
                    FollowingsRecentPost.getItems().add(imageView2);
                }
                FollowingsRecentPost.getItems().add(Account.getAccount(FollowingsList.getSelectionModel().getSelectedItem().toString()).getPosts().get(i));
                if(LoggedInAccount.getInstance().getLoggedIn().isBusinessAccount()){
                    FollowingsRecentPost.getItems().add("views: "+post.getViews().size());
                    for(int k=0;k<post.getViews().size();k++){
                        FollowingsRecentPost.getItems().add(post.getDateviewed().get(k) + " = username (" + post.getViews().get(k).getUsername()+")");
                    }
                    FollowingsRecentPost.getItems().add("likes : "+post.getLike().size());
                    for (int k=0;k<post.getLike().size();k++){
                        FollowingsRecentPost.getItems().add(post.getDateliked().get(k)+" = username (" + post.getLike().get(k).getUsername()+")");
                    }
                    FollowingsRecentPost.getItems().add(ShowviewChart);
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
                    FollowingsRecentPost.getItems().add(ShowlikeChart);
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
                FollowingsRecentPost.getItems().add("_____________________________");
            }
        }
    }

}
