package edu.ntnu.g14.frontend;

import java.io.IOException;

import edu.ntnu.g14.FileManagement;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginForgotPasswordSecondScene {
    static Stage stage = ApplicationFront.getStage();


    
    static public Scene scene() {
        TextField newPassword = ApplicationObjects.newTextField("New password", 125, 70, "black", "white", 250, 20, 15);
        TextField retypeNewPassword = ApplicationObjects.newTextField("Retype new password", 125, 110, "black", "white",
                250, 20, 15);
       
        Button confirmButton = ApplicationObjects.newButton("Confirm", 175, 150, "black", "white", 150, 20, 15);
        confirmButton.setOnAction(e -> {
            if(newPassword.getText().equals(retypeNewPassword.getText()) && !newPassword.getText().equals("")){
                //TODO: lagre passord under bruker
                try {
                    stage.setScene(LoginChooseUserScene.scene());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else{
                ApplicationObjects.alertBox("ERROR", "Password dont match", "Please insert matching passwords!");
            }
        });
        Text goBack = ApplicationObjects.newText("Go back", 10, true, 400, 260);
    
        Group root = new Group(goBack, newPassword, retypeNewPassword, confirmButton);
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }
    
}
