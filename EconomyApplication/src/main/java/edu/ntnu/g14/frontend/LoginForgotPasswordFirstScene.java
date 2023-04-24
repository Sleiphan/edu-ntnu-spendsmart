package edu.ntnu.g14.frontend;

import java.io.IOException;

import edu.ntnu.g14.BankApplication;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginForgotPasswordFirstScene {
    static Stage stage = BankApplication.getStage();


    static public Scene scene(String key) throws IOException{
        Text goBack = ApplicationObjects.newText("Go back?", 10, true, 400, 260);
        goBack.setOnMouseClicked(e -> {
            try {
                stage.setScene(LoginUserScene.scene());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        TextField keyField = ApplicationObjects.newTextField("Code-key", 120, 70, 250, 30, 25);
        Button loginButton = ApplicationObjects.newButton("Make new password", 120, 140, 250, 20, 15);
        loginButton.setOnAction(e -> {
            if (keyField.getText().replace(" ", "").equals(key.replace(" ", ""))) {
                try {
                    stage.setScene(LoginForgotPasswordSecondScene.scene());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else {
                ApplicationObjects.alertBox("ERROR", "Wrong key", "The wrong key has been input");
            }
        });

        Group root = new Group(
            ApplicationObjects.newText("An email with a code-key has been sent to:", 15, false, 130, 40),
                ApplicationObjects.newText(BankApplication.loggedInUser.getEmail(), 15, false, 170, 60),
                goBack, keyField, loginButton);
        root.getStylesheets().add("StyleSheet.css"); 
        Scene scene = new Scene(root, 500, 300, ApplicationObjects.getSceneColor());
        
        return scene;
    }

}
