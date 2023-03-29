package edu.ntnu.g14.frontend;

import edu.ntnu.g14.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class RegisterFirstScene {
    static Stage stage = ApplicationFront.getStage();


    static public Scene scene() {
        TextField firstName = ApplicationObjects.newTextField("", 10, 35, "black", "white", 100, 20, 15);
        TextField lastName = ApplicationObjects.newTextField("", 10, 135, "black", "white", 100, 20, 15);
        TextField email = ApplicationObjects.newTextField("", 10, 235, "black", "white", 100, 20, 15);
        TextField password = ApplicationObjects.newTextField("", 260, 35, "black", "white", 100, 20, 15);
        TextField confirmPassword = ApplicationObjects.newTextField("", 260, 135, "black", "white", 100, 20, 15);
        Button next = ApplicationObjects.newButton("Next", 260, 235, "black", "white", 100, 20, 15);
        next.setOnAction(e -> {
            if (firstName.getText() != "" && lastName.getText() != ""
                    && email.getText() != "" && password.getText() != ""
                    && confirmPassword.getText() != "") {
                //TODO: add register user
                String key = EmailVertification.sendVertificationKey(email.getText().replace(" ", ""));
                stage.setScene(RegisterSecondScene.scene(key, email.getText()));
            } else {
                ApplicationObjects.alertBox("ERROR", "Missing information", "Pleace fill out all required information");
            }
        });

        Group root = new Group(firstName, lastName,
                email, password, confirmPassword, next,
                ApplicationObjects.newText("First name", 30, false, 10, 25),
                ApplicationObjects.newText("Last name", 30, false, 10, 125),
                ApplicationObjects.newText("Email", 30, false, 10, 225),
                ApplicationObjects.newText("Password", 30, false, 260, 25),
                ApplicationObjects.newText("Confirm password", 30, false, 260, 125));
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

}
