package edu.ntnu.g14.frontend;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class RegisterThirdScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene() {
        Button yes = ApplicationObjects.newButton("Yes", 200, 120, "black", "white", 50, 20, 15);
        Button skip = ApplicationObjects.newButton("Skip for now", 170, 170, "black", "white", 100, 20, 15);
        Button addDefault = ApplicationObjects.newButton("Add default accounts", 150, 220, "black", "white", 150, 20, 15);

        Group root = new Group(yes, skip, addDefault,
        ApplicationObjects.newText("Would you like to add", 25, false, 130, 40),
        ApplicationObjects.newText("account information", 25, false, 130, 80));
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

}
