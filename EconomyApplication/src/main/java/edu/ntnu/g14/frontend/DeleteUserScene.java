package edu.ntnu.g14.frontend;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DeleteUserScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene() {
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
        cancel.setOnAction(event -> stage.setScene(UserManagementScene.scene()));

        Group root = new Group(loggedInUser, loggedInUserEmail, cancel, confirm, warningText);
        return new Scene(root, 500, 300, Color.WHITE);
    }

}
