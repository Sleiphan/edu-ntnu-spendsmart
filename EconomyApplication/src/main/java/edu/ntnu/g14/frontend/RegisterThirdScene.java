package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegisterThirdScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene()throws IOException {
        Button yes = ApplicationObjects.newButton("Yes", 200, 120, 50, 20, 15);
        yes.setOnAction(e -> {
            try {
                stage.setScene(AccountOverviewScene.scene(null));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        
        Group root = new Group(yes,
        ApplicationObjects.newText("Would you like to add", 25, false, 130, 40),
        ApplicationObjects.newText("account information", 25, false, 130, 80));
        root.getStylesheets().add("StyleSheet.css"); 
        Scene scene = new Scene(root, 500, 300, ApplicationObjects.getSceneColor());
        
        return scene;
    }

}
