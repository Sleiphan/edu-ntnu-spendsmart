package edu.ntnu.g14.frontend;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TransferScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene() {
        int x = 300;
        int y = 35;
        int n = 85;
        int m = n - 5;

        TextField fromAccount = ApplicationObjects.newTextField("12345678910", x, y, "black", "white", 200, 20, 15);
        TextField amount = ApplicationObjects.newTextField("50kr", x, y + n, "black", "white", 100, 20, 15);
        TextField toAccount = ApplicationObjects.newTextField("10987654321", x, y + 2 * n, "black", "white", 100, 20, 15);

        Button transfer = ApplicationObjects.newButton("Pay", 200, 255, "black", "white", 100, 20, 15);
        Button cancel = ApplicationObjects.newButton("Cancel", 350, 255, "black", "white", 100, 20, 15);
        transfer.setOnAction(e -> {
            stage.setScene(MainPageScene.scene());
        });
        cancel.setOnAction(e -> {
            stage.setScene(MainPageScene.scene());
        });
        Group root = new Group(fromAccount, amount, toAccount,
        ApplicationObjects.newText("From account", 30, false, x, y - 5),
        ApplicationObjects.newText("Amount:", 30, false, x, y + m),
        ApplicationObjects.newText("To account:", 30, false, x, y + 2 * m),
            transfer, cancel);
        Scene scene = new Scene(root, 800, 500, Color.WHITE);
        return scene;
    }
    
}
