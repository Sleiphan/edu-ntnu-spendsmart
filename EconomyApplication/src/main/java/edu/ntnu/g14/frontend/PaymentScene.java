package edu.ntnu.g14.frontend;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PaymentScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene() {
        int x = 300;
        int y = 35;
        int n = 85;
        int m = n - 5;

        TextField fromAccount = ApplicationObjects.newTextField("12345678910", x, y, "black", "white", 200, 20, 15);
        TextField amount = ApplicationObjects.newTextField("50kr", x, y + n, "black", "white", 100, 20, 15);
        TextField dueDate = ApplicationObjects.newTextField("dd.mm.yy", x, y + 2 * n, "black", "white", 100, 20, 15);
        TextField toAccount = ApplicationObjects.newTextField("10987654321", x, y + 3 * n, "black", "white", 100, 20, 15);
        TextField cid = ApplicationObjects.newTextField("0123456789", x, y + 4 * n, "black", "white", 100, 20, 15);

        Button pay = ApplicationObjects.newButton("Pay", 200, 450, "black", "white", 100, 20, 15);
        Button cancel = ApplicationObjects.newButton("Cancel", 350, 450, "black", "white", 100, 20, 15);
        pay.setOnAction(e -> {
            stage.setScene(PaymentConfirmationScene.scene());
        });
        cancel.setOnAction(e -> {
            stage.setScene(MainPageScene.scene());
        });
        Group root = new Group(fromAccount, amount, dueDate, toAccount, cid,
        ApplicationObjects.newText("From account", 30, false, x, y - 5),
        ApplicationObjects.newText("Amount:", 30, false, x, y + m),
        ApplicationObjects.newText("Due date:", 30, false, x, y + 2 * m),
        ApplicationObjects.newText("To account:", 30, false, x, y + 3 * m),
        ApplicationObjects.newText("CID:", 30, false, x, y + 4 * m),
            pay, cancel);
        Scene scene = new Scene(root, 800, 500, Color.WHITE);
        return scene;
    }

}
