package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DeleteUserScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene() throws FileNotFoundException {
        Text loggedInUser = ApplicationObjects.newText("Walter Banks", 60, false, 600, 150);
        Text loggedInUserEmail = ApplicationObjects.newText("walBa76@gmail.com", 20, false, 685, 200);
        Text warningText = ApplicationObjects.newText("Are you sure you want to delete this user?\n" +
                "_____This action cannot be undone____\n" +
                "___________10 seconds left__________", 20, false, 600, 350);


        Button confirm = ApplicationObjects.newButton("Delete", 700, 520,"white", "red", 157,60,16);
        confirm.setTextFill(Paint.valueOf("WHITE"));
        confirm.setMinWidth(157);
        confirm.setMinHeight(50);
        confirm.setOnMousePressed(event -> {ApplicationObjects.alertBox("Deleted user", "You have deleted user", "Walter Banks R.I.P");    confirm.setStyle("-fx-background-color: red");
            confirm.setTextFill(Paint.valueOf("WHITE"));
            confirm.setMinWidth(157);
            confirm.setMinHeight(50);});

        Button cancel = ApplicationObjects.newButton("Cancel", 700, 570,"white", "grey", 157,60,16);
        cancel.setOnAction(event -> {
            try {
                stage.setScene(UserManagementScene.scene());
            } catch (FileNotFoundException e) {
                
                e.printStackTrace();
            }
        });

        
        ImageView homeButton = ApplicationObjects.newImage("home.png", 10, 10, 20, 20);
        homeButton.setOnMouseClicked(e -> {
            try {
                stage.setScene(MainPageScene.scene());
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        Button dropDownButton = ApplicationObjects.newButton("test", 676, 10, "black", "white", 10, 10, 10);
        Group dropDown = ApplicationObjects.dropDownMenu();
        ImageView manageUserButton = ApplicationObjects.newImage("user.png", 646, 10, 20, 20);
        Group root = new Group(loggedInUser, loggedInUserEmail, cancel, confirm, warningText, dropDownButton, homeButton, manageUserButton);
        dropDownButton.setOnAction(e -> {
            root.getChildren().add(dropDown);
        });

        Scene scene = new Scene(root, 728, 567, Color.WHITE);
        
      
        Group userButtons = ApplicationObjects.userMenu();
        manageUserButton.setOnMouseEntered(e -> {
            root.getChildren().add(userButtons);
        });
        scene.setOnMouseClicked(e -> {
            root.getChildren().remove(userButtons);
            root.getChildren().remove(dropDown);
        });
        return scene;
    }

}
