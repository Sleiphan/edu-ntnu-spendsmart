package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;
import java.io.IOException;

import edu.ntnu.g14.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegisterFirstScene {
    static Stage stage = ApplicationFront.getStage();


    static public Scene scene() throws FileNotFoundException, IOException{
        TextField firstName = ApplicationObjects.newTextField("", 10, 35, 100, 20, 15);
        TextField lastName = ApplicationObjects.newTextField("", 10, 135, 100, 20, 15);
        TextField email = ApplicationObjects.newTextField("", 10, 235, 100, 20, 15);
        TextField password = ApplicationObjects.newTextField("", 260, 35, 100, 20, 15);
        TextField confirmPassword = ApplicationObjects.newTextField("", 260, 135, 100, 20, 15);
        Button next = ApplicationObjects.newButton("Next", 260, 235, 100, 20, 15);
        next.setOnAction(e -> {
            if (firstName.getText() != "" && lastName.getText() != ""
                    && email.getText() != "" && password.getText() != ""
                    && confirmPassword.getText() != "" && confirmPassword.getText().equals(password.getText())) {
                Login logininfo = new Login(firstName.getText() + lastName.getText(), password.getText(), firstName.getText() + lastName.getText() + "#1");
                try {
                    FileManagement.writeNewUser(new User(null, null, logininfo, email.getText(), lastName.getText(), firstName.getText(), null, null));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                String key = EmailVerification.sendVerificationKey(email.getText().replaceAll(" ", ""));
                try {
                    stage.setScene(RegisterSecondScene.scene(key, email.getText(), firstName.getText() + lastName.getText() + "#1"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else {
                ApplicationObjects.alertBox("ERROR", "Missing information or password dont match", "Pleace fill out all required information");
            }
        });

        Group root = new Group(firstName, lastName,
                email, password, confirmPassword, next,
                ApplicationObjects.newText("First name", 30, false, 10, 25),
                ApplicationObjects.newText("Last name", 30, false, 10, 125),
                ApplicationObjects.newText("Email", 30, false, 10, 225),
                ApplicationObjects.newText("Password", 30, false, 260, 25),
                ApplicationObjects.newText("Confirm password", 30, false, 260, 125));
        root.getStylesheets().add("StyleSheet.css"); 
        Scene scene = new Scene(root, 500, 300, ApplicationObjects.getSceneColor());
        
        return scene;
    }

}
