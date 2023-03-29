package edu.ntnu.g14.frontend;

import edu.ntnu.g14.*;
import java.io.IOException;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginChooseUserScene {
    static Stage stage = ApplicationFront.getStage();

    //TODO: REMOVE dropdown and add to all other scenes
    static public Scene scene() throws IOException {
        Text chooseUser = ApplicationObjects.newText("Choose user", 30, false, 170, 40);
        Text registerNew = ApplicationObjects.newText("Register new account", 10, true, 400, 280);
        registerNew.setOnMouseClicked(e -> {
            stage.setScene(RegisterFirstScene.scene());
        });
    
        String[] usernames = new String[FileManagement.readUsers().length];
        Login[] logins = FileManagement.readUsers();
        for(int i = 0; i < FileManagement.readUsers().length; i++){
            usernames[i] = logins[i].getUserName();
        }
        
    
        ChoiceBox<String> user = ApplicationObjects.newChoiceBox(usernames, "black", "white", 250, 20, 15, 125, 70);
    
        Button confirm = ApplicationObjects.newButton("Confirm", 175, 110, "black", "white", 150, 20, 15);
        confirm.setOnAction(e -> {
            if(user.getValue() != null){
                try {
                    String usersID = "";
                    for(int i = 0; i < FileManagement.readUsers().length; i++){
                        if(user.getValue().equals(logins[i].getUserName())){
                            usersID = logins[i].getUserId();
                        }
                    }
                    ApplicationFront.setLoggedInUser(FileManagement.readUser(usersID));
                    stage.setScene(LoginUserScene.scene());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    
        Button testDropDown = ApplicationObjects.newButton("test", 0, 0, "black", "white", 10, 10, 10);
        Group dropDown = ApplicationObjects.dropDownMenu();
        Group root = new Group(chooseUser, registerNew, user, confirm, testDropDown);
        testDropDown.setOnAction(e -> {
            root.getChildren().add(dropDown);
        });

        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        scene.setOnMouseClicked(e -> {
            root.getChildren().remove(dropDown);
        });
        return scene;
    }
    
}
