package edu.ntnu.g14.frontend;

import edu.ntnu.g14.*;

import java.io.IOException;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginUserScene {
    static Stage stage = BankApplication.getStage();

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
            String key = EmailVerification.sendVerificationKey(BankApplication.loggedInUser.getEmail());
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
            if (key == KeyCode.ENTER && passwordField.getText().equals(BankApplication.loggedInUser.getLoginInfo().getPassword())) {
                try {
                    stage.setScene(MainPageScene.scene());
                } catch (IOException e1) {
                    
                    e1.printStackTrace();
                }
            } else if(key == KeyCode.ENTER && !passwordField.getText().equals(BankApplication.loggedInUser.getLoginInfo().getPassword())) {
                ApplicationObjects.alertBox("ERROR", "Wrong password", "Please insert the right password");
            }
        });
        loginButton.setOnAction(e -> {
            if (passwordField.getText().equals(BankApplication.loggedInUser.getLoginInfo().getPassword())) {
                try {
                    stage.setScene(MainPageScene.scene());
                } catch (IOException e1) {
                   
                    e1.printStackTrace();
                }
            } else {
                ApplicationObjects.alertBox("ERROR", "Wrong password", "Please insert the right password");
            }
        });
        Text welcomeText = ApplicationObjects.newText("Welcome back, " + BankApplication.loggedInUser.getLoginInfo().getUserName(), 25, false, 0, 40);
        

        Group root = new Group(welcomeText,
                notYou, forgotPassword, passwordField, loginButton);
        root.getStylesheets().add("StyleSheet.css"); 
        Scene scene = new Scene(root, 500, 300, ApplicationObjects.getSceneColor());
        Platform.runLater(() -> {
            passwordField.setLayoutX(250 - passwordField.getLayoutBounds().getWidth()/2);
            loginButton.setLayoutX(250 - loginButton.getLayoutBounds().getWidth()/2);
            welcomeText.setLayoutX(250 - welcomeText.getLayoutBounds().getWidth()/2);
        });
        return scene;
    }

}
