package edu.ntnu.g14.frontend;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class PaymentScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene() throws FileNotFoundException {
        int x = 300;
        int y = 35;
        int n = 85;
        int m = n - 5;

        TextField fromAccount = ApplicationObjects.newTextField("12345678910", x, y, "black", "white", 200, 20, 15);
        TextField amount = ApplicationObjects.newTextField("50kr", x, y + n, "black", "white", 100, 20, 15);
        TextField description = ApplicationObjects.newTextField("Description", x, y + 2 * n, "black", "white", 200, 20, 15);
        TextField dueDate = ApplicationObjects.newTextField("dd.mm.yy", x, y + 3 * n, "black", "white", 100, 20, 15);
        TextField toAccount = ApplicationObjects.newTextField("10987654321", x, y + 4 * n, "black", "white", 100, 20, 15);
        TextField cid = ApplicationObjects.newTextField("0123456789", x, y + 5 * n, "black", "white", 100, 20, 15);

        Button pay = ApplicationObjects.newButton("Pay", 200, 450, "black", "white", 100, 20, 15);
        Button cancel = ApplicationObjects.newButton("Cancel", 350, 450, "black", "white", 100, 20, 15);
        pay.setOnAction(e -> {
            try {
                ArrayList<String> paymentInfo = new ArrayList<String>();
                paymentInfo.add(fromAccount.getText());
                paymentInfo.add(amount.getText());
                paymentInfo.add(description.getText());
                paymentInfo.add(dueDate.getText());
                paymentInfo.add(toAccount.getText());
                paymentInfo.add(cid.getText());
                PaymentConfirmationScene.setPaymentInfo(paymentInfo);
                stage.setScene(PaymentConfirmationScene.scene());
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
        Button dropDownButton = ApplicationObjects.newButton("test", 676, 10, "black", "white", 10, 10, 10);
        Group dropDown = ApplicationObjects.dropDownMenu();
        ImageView manageUserButton = ApplicationObjects.newImage("user.png", 646, 10, 20, 20);
        Group root = new Group(fromAccount, amount, description, dueDate, toAccount, cid,
                ApplicationObjects.newText("From account", 30, false, x, y - 5),
                ApplicationObjects.newText("Amount:", 30, false, x, y + m),
                ApplicationObjects.newText("Description:", 30, false, x, y + 2 * m),
                ApplicationObjects.newText("Due date:", 30, false, x, y + 3 * m),
                ApplicationObjects.newText("To account:", 30, false, x, y + 4 * m),
                ApplicationObjects.newText("CID:", 30, false, x, y + 5 * m),
                pay, cancel, dropDownButton, homeButton, manageUserButton);
        dropDownButton.setOnAction(e -> {
            root.getChildren().add(dropDown);
        });

        Scene scene = new Scene(root, 728, 567, Color.WHITE);


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
