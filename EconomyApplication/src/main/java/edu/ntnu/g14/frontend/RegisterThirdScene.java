package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;


import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class RegisterThirdScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene() {
        Button yes = ApplicationObjects.newButton("Yes", 200, 120, "black", "white", 50, 20, 15);
        yes.setOnAction(e -> {
            try {
                stage.setScene(AccountOverviewScene.scene());
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        Button skip = ApplicationObjects.newButton("Skip for now", 170, 170, "black", "white", 100, 20, 15);
        skip.setOnAction(e -> {
            try {
                stage.setScene(MainPageScene.scene());
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        
        Group root = new Group(yes, skip,
        ApplicationObjects.newText("Would you like to add", 25, false, 130, 40),
        ApplicationObjects.newText("account information", 25, false, 130, 80));
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

}
