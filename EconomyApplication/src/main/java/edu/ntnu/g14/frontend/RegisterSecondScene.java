package edu.ntnu.g14.frontend;

import java.io.IOException;

import edu.ntnu.g14.FileManagement;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegisterSecondScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene(String key, String email, String usersID) throws IOException{
        TextField keyInput = ApplicationObjects.newTextField("", 120, 195, 100, 20, 15);
        Button next = ApplicationObjects.newButton("Next", 240, 195, 100, 20, 15);
        next.setOnAction(e -> {
            if (keyInput.getText().replace(" ", "").equals(key.replace(" ", ""))) {
                try {
                    ApplicationFront.setLoggedInUser(FileManagement.readUser(usersID));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    stage.setScene(RegisterThirdScene.scene());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else {
                ApplicationObjects.alertBox("ERROR", "Wrong key", "The wrong key has been input");
            }
        });

        Group root = new Group(keyInput, next,
        ApplicationObjects.newText("An email with a confirmation code", 25, false, 60, 25),
                ApplicationObjects.newText("has been sent to", 25, false, 140, 50),
                ApplicationObjects.newText(email, 25, false, 140, 75),
                ApplicationObjects.newText("Code:", 30, false, 20, 225));
        root.getStylesheets().add("StyleSheet.css");         
        Scene scene = new Scene(root, 500, 300, ApplicationObjects.getSceneColor());
        
        return scene;
    }

}
