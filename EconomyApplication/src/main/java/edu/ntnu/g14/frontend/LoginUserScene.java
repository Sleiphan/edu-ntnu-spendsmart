package edu.ntnu.g14.frontend;

import edu.ntnu.g14.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginUserScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene() throws IOException{
        Text notYou = ApplicationObjects.newText("Is this not you?", 10, true, 400, 280);
        notYou.setOnMouseClicked(e -> {
            try {
                stage.setScene(LoginChooseUserScene.scene());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        Text forgotPassword = ApplicationObjects.newText("Forgot password", 10, true, 400, 260);
        forgotPassword.setOnMouseClicked(e -> {
            String key = EmailVertification.sendVertificationKey(ApplicationFront.loggedInUser.getEmail());
            try {
                stage.setScene(LoginForgotPasswordFirstScene.scene(key));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle(ApplicationObjects.setStyleString("black", "white", 250, 40, 25));
        passwordField.setLayoutX(120);
        passwordField.setLayoutY(60);
        passwordField.setFocusTraversable(false);
        Button loginButton = ApplicationObjects.newButton("Login", 185, 130, 100, 30, 25);
        passwordField.setOnKeyPressed(keyEvent -> {
            KeyCode key = keyEvent.getCode();
            if (key == KeyCode.ENTER && passwordField.getText().equals(ApplicationFront.loggedInUser.getLoginInfo().getPassword())) {
                try {
                    stage.setScene(MainPageScene.scene());
                } catch (IOException e1) {
                    
                    e1.printStackTrace();
                }
            } else if(key == KeyCode.ENTER && !passwordField.getText().equals(ApplicationFront.loggedInUser.getLoginInfo().getPassword())) {
                ApplicationObjects.alertBox("ERROR", "Wrong password", "Please insert the right password");
            }
        });
        loginButton.setOnAction(e -> {
            if (passwordField.getText().equals(ApplicationFront.loggedInUser.getLoginInfo().getPassword())) {
                try {
                    stage.setScene(MainPageScene.scene());
                } catch (IOException e1) {
                   
                    e1.printStackTrace();
                }
            } else {
                ApplicationObjects.alertBox("ERROR", "Wrong password", "Please insert the right password");
            }
        });
        
        

        Group root = new Group(ApplicationObjects.newText("Welcome back " + ApplicationFront.loggedInUser.getLoginInfo().getUserName(), 25, false, 120, 40),
                notYou, forgotPassword, passwordField, loginButton);
        root.getStylesheets().add("StyleSheet.css"); 
        Scene scene = new Scene(root, 500, 300, ApplicationObjects.getSceneColor());
       
        return scene;
    }

}
