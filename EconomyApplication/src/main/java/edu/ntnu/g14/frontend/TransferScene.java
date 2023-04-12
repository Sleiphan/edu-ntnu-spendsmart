package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TransferScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene() throws FileNotFoundException {
        int x = 300;
        int y = 35;
        int n = 85;
        int m = n - 5;

        TextField fromAccount = ApplicationObjects.newTextField("12345678910", x, y, 200, 20, 15);
        TextField amount = ApplicationObjects.newTextField("50kr", x, y + n, 100, 20, 15);
        TextField toAccount = ApplicationObjects.newTextField("10987654321", x, y + 2 * n, 100, 20, 15);

        Button transfer = ApplicationObjects.newButton("Pay", 200, 255, 100, 20, 15);
        Button cancel = ApplicationObjects.newButton("Cancel", 350, 255, 100, 20, 15);
        transfer.setOnAction(e -> {
            try {
                stage.setScene(MainPageScene.scene());
            } catch (FileNotFoundException e1) {
                
                e1.printStackTrace();
            }
        });
        cancel.setOnAction(e -> {
            try {
                stage.setScene(MainPageScene.scene());
            } catch (FileNotFoundException e1) {
                
                e1.printStackTrace();
            }
        });
        
        ImageView homeButton = ApplicationObjects.newImage("home.png", 10, 10, 20, 20);
        homeButton.setOnMouseClicked(e -> {
            try {
                stage.setScene(MainPageScene.scene());
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        Button dropDownButton = ApplicationObjects.newButton("test", 676, 10, 10, 10, 10);
        Group dropDown = ApplicationObjects.dropDownMenu();
        ImageView manageUserButton = ApplicationObjects.newImage("user.png", 646, 10, 20, 20);
        Group root = new Group(fromAccount, amount, toAccount,
        ApplicationObjects.newText("From account", 30, false, x, y - 5),
        ApplicationObjects.newText("Amount:", 30, false, x, y + m),
        ApplicationObjects.newText("To account:", 30, false, x, y + 2 * m),
            transfer, cancel, dropDownButton, homeButton, manageUserButton);
        dropDownButton.setOnAction(e -> {
            root.getChildren().add(dropDown);
        });
        root.getStylesheets().add("StyleSheet.css"); 
        Scene scene = new Scene(root, 728, 567, ApplicationObjects.getSceneColor());
        
        
        Group userButtons = ApplicationObjects.userMenu();
        manageUserButton.setOnMouseEntered(e -> {
            root.getChildren().add(userButtons);
        });
        scene.setOnMouseClicked(e -> {
            root.getChildren().remove(userButtons);
            root.getChildren().remove(dropDown);
        });
        
        return scene;
    }
    
}
