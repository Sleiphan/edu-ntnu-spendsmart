package edu.ntnu.g14.frontend;

import edu.ntnu.g14.BankApplication;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class represents the scene for making a payment. It contains a text field
 * for input of the account number to pay from, the amount to pay, a description
 * of the transaction, the account number to pay to, a category for the
 * transaction, and a due date for the payment.
 *
 * @author G14
 * @version 1.0
 * @since 2023-04-25
 */
public class PaymentScene {
    static Stage stage = BankApplication.getStage();

    static public Scene scene() throws FileNotFoundException, IOException {
        MediaPlayer textToSpeach = ApplicationObjects.newSound("paymentScene");
        if(ApplicationObjects.soundOn()){
            textToSpeach.play();
        }
        int x = 300;
        int y = 35;
        int n = 85;
        int m = n - 5;

        //Creates text fields for input
        TextField fromAccount = ApplicationObjects.newTextField("12345678910", x, y, 150, 20, 15);
        TextField amount = ApplicationObjects.newTextField("50kr", x, y + n, 100, 20, 15);
        TextField description = ApplicationObjects.newTextField("Description", x, y + 2 * n, 200, 20, 15);
        TextField dueDate = ApplicationObjects.newTextField("dd.mm.yy", x, y + 3 * n, 100, 20, 15);
        TextField toAccount = ApplicationObjects.newTextField("10987654321", x, y + 4 * n, 150, 20, 15);
        TextField cid = ApplicationObjects.newTextField("0123456789", x, y + 5 * n, 150, 20, 15);

        String[] categoryChoices = {"Food and Drink", "Clothes and Shoes", "Personal Care", "Leisure", "Travel", "Alcohol and Tobacco", "Other", "Payment", "Business"};
        ComboBox<String> category = ApplicationObjects.newComboBox(categoryChoices, 200, 20, 15,x + 130, y + 3 * n);
        category.setPromptText("Category of Payment");

        //Creates a button for making the payment and a button for cancelling the payment
        Button pay = ApplicationObjects.newButton("Pay", x - 50, y + 6 * n - 30, 100, 20, 15);
        Button cancel = ApplicationObjects.newButton("Cancel", x + 70, y + 6 * n - 30, 100, 20, 15);
        pay.setOnAction(e -> {
            try {
                ArrayList<String> paymentInfo = new ArrayList<String>();
                paymentInfo.add(fromAccount.getText());
                paymentInfo.add(amount.getText());
                paymentInfo.add(description.getText());
                paymentInfo.add(dueDate.getText());
                paymentInfo.add(toAccount.getText());
                paymentInfo.add(cid.getText());
                paymentInfo.add(category.getValue());
                PaymentConfirmationScene.setPaymentInfo(paymentInfo);
                stage.setScene(PaymentConfirmationScene.scene());
            } catch (IOException e1) {

                e1.printStackTrace();
            }
        });
        cancel.setOnAction(e -> {
            try {
                stage.setScene(MainPageScene.scene());
            } catch (IOException e1) {

                e1.printStackTrace();
            }
        });

        ImageView homeButton = ApplicationObjects.newImage("home.png", 10, 10, 20, 20);
        homeButton.setOnMouseClicked(e -> {
            try {
                stage.setScene(MainPageScene.scene());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        Button dropDownButton = ApplicationObjects.newButton("test", 676, 10, 10, 10, 10);
        Group dropDown = ApplicationObjects.dropDownMenu();
        ImageView manageUserButton = ApplicationObjects.newImage("user.png", 646, 10, 20, 20);
        Group root = new Group(fromAccount, amount, description, dueDate, toAccount, cid,
                ApplicationObjects.newText("From account", 30, false, x, y - 5),
                ApplicationObjects.newText("Amount:", 30, false, x, y + m),
                ApplicationObjects.newText("Description:", 30, false, x, y + 2 * m),
                ApplicationObjects.newText("Due date:", 30, false, x, y + 3 * m),
                ApplicationObjects.newText("To account:", 30, false, x, y + 4 * m),
                ApplicationObjects.newText("CID:", 30, false, x, y + 5 * m),
                pay,cancel, dropDownButton, homeButton, manageUserButton, category);
        dropDownButton.setOnAction(e -> {
            root.getChildren().add(dropDown);
        });
        root.getStylesheets().add("StyleSheet.css"); 
        Scene scene = new Scene(root, 728, 567, ApplicationObjects.getSceneColor());


        Group userButtons = ApplicationObjects.userMenu();
        manageUserButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event){
                root.getChildren().add(userButtons);
                event.consume();
            }
        }); 
        scene.setOnMouseClicked(e -> {
            root.getChildren().remove(userButtons);
            root.getChildren().remove(dropDown);
        });
        
        return scene;
    }



}
