package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;
import java.io.IOException;

import edu.ntnu.g14.FileManagement;
import edu.ntnu.g14.Login;
import edu.ntnu.g14.User;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginForgotPasswordSecondScene {
    static Stage stage = ApplicationFront.getStage();


    
    static public Scene scene() throws IOException{
        TextField newPassword = ApplicationObjects.newTextField("New password", 125, 70, 250, 20, 15);
        TextField retypeNewPassword = ApplicationObjects.newTextField("Retype new password", 125, 110,
                250, 20, 15);
       
        Button confirmButton = ApplicationObjects.newButton("Confirm", 175, 150, 150, 20, 15);
        confirmButton.setOnAction(e -> {
            if(newPassword.getText().equals(retypeNewPassword.getText()) && !newPassword.getText().equals("")){
                Login newLogin = new Login(ApplicationFront.loggedInUser.getLoginInfo().getUserName(), newPassword.getText(), ApplicationFront.loggedInUser.getLoginInfo().getUserId());
                User newPasswordUser = new User(ApplicationFront.loggedInUser.getAccounts(), ApplicationFront.loggedInUser.getAllInvoices(),
                newLogin, ApplicationFront.loggedInUser.getEmail(), ApplicationFront.loggedInUser.getLastName(),
                ApplicationFront.loggedInUser.getFirstName(), ApplicationFront.loggedInUser.getTransactions(), ApplicationFront.loggedInUser.getBudget());

                try {
                    FileManagement.editUser(newPasswordUser, ApplicationFront.loggedInUser.getLoginInfo().getUserId());
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
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
        root.getStylesheets().add("StyleSheet.css"); 
        Scene scene = new Scene(root, 500, 300, ApplicationObjects.getSceneColor());
        
        return scene;
    }
    
}
