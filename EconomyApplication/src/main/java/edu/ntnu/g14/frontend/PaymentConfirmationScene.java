package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import edu.ntnu.g14.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import edu.ntnu.g14.Payment;

import static edu.ntnu.g14.FileManagement.writeNewTransaction;

public class PaymentConfirmationScene {
    static Stage stage = ApplicationFront.getStage();
    private static ArrayList<String> paymentInfo;

    static public Scene scene() throws FileNotFoundException {
        TableView<ObservableList<Object>> payment = ApplicationObjects.newTableView(new String[]{"Payment", "Information"}, 100, 150, 602, 150);
        ObservableList<ObservableList<Object>> paymentData = initializePaymentData(getPaymentInfo());
        payment.setItems(paymentData);
        payment.getColumns().forEach(colum -> colum.setMinWidth(300));
        payment.getColumns().forEach(column -> column.setSortable(false));


        Button confirm = ApplicationObjects.newButton("Confirm", 250, 350, "black", "white", 100, 20, 15);
        Button cancel = ApplicationObjects.newButton("Cancel", 400, 350, "black", "white", 100, 20, 15);


        confirm.setOnAction(e -> {
            try {
                User loggedInUser = ApplicationFront.getLoggedInUser();
                LocalDate dueDate = LocalDate.parse(getPaymentInfo().get(3));
                LocalDate dateOfTransaction = LocalDate.now();
                Payment paymentObject = new Payment(getPaymentInfo().get(0), new BigDecimal(getPaymentInfo().get(1)), getPaymentInfo().get(2), getPaymentInfo().get(3), dueDate, getPaymentInfo().get(5), dateOfTransaction);

                try {
                    writeNewTransaction(loggedInUser.getLoginInfo().getUserId(), paymentObject);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                stage.setScene(MainPageScene.scene());
            } catch (FileNotFoundException e1) {
                
                e1.printStackTrace();
            }
        });
        cancel.setOnAction(e -> {
            try {
                stage.setScene(PaymentScene.scene());
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
        Group root = new Group(payment, confirm, cancel, dropDownButton, homeButton, manageUserButton);
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

    public static ObservableList<ObservableList<Object>> initializePaymentData(ArrayList<String> paymentInfo) {
        String fromAccount = paymentInfo.get(0);
        String amount = paymentInfo.get(1);
        String description = paymentInfo.get(2);
        String dueDate = paymentInfo.get(3);
        String toAccount = paymentInfo.get(4);
        String cid = paymentInfo.get(5);

        ObservableList<ObservableList<Object>> paymentData = FXCollections.observableArrayList();
        paymentData.add(FXCollections.observableArrayList("From account:", new BigDecimal(fromAccount)));
        paymentData.add(FXCollections.observableArrayList("Amount:", new BigDecimal(amount) + "kr"));
        paymentData.add(FXCollections.observableArrayList("Description:", description));
        paymentData.add(FXCollections.observableArrayList("Due date:", dueDate));
        paymentData.add(FXCollections.observableArrayList("To account:", new BigDecimal(toAccount)));
        paymentData.add(FXCollections.observableArrayList("CID:", new BigDecimal(cid)));
        return paymentData;
    }

    public static ArrayList<String> getPaymentInfo() {
        return paymentInfo;
    }

    public static void setPaymentInfo(ArrayList<String> paymentInfo) {
        PaymentConfirmationScene.paymentInfo = paymentInfo;
    }
}
