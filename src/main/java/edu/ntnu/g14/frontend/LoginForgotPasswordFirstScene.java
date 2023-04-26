package edu.ntnu.g14.frontend;

import java.io.IOException;

import edu.ntnu.g14.BankApplication;
import javafx.application.Platform;
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
        Text infoText = ApplicationObjects.newText("An email with a key has been sent to:", 15, false, 0, 40);
        Text blurredEmail = ApplicationObjects.newText(BankApplication.loggedInUser.getBlurredEmail(), 15, false, 0, 60);
        Group root = new Group(infoText, blurredEmail,
                goBack, keyField, loginButton);
        root.getStylesheets().add("StyleSheet.css"); 
        Scene scene = new Scene(root, 500, 300, ApplicationObjects.getSceneColor());
        Platform.runLater(() -> {
            infoText.setLayoutX(250 - infoText.getLayoutBounds().getWidth()/2);
            keyField.setLayoutX(250 - keyField.getLayoutBounds().getWidth()/2);
            loginButton.setLayoutX(250 - loginButton.getLayoutBounds().getWidth()/2);
            blurredEmail.setLayoutX(250 - blurredEmail.getLayoutBounds().getWidth()/2);
        });
        return scene;
    }

}