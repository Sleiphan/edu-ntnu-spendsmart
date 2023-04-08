package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PaymentConfirmationScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene() throws FileNotFoundException {
        TableView<ObservableList<Object>> payment = ApplicationObjects.newTableView(new String[]{"Payment", "Information"}, 100, 150, 602, 150);
        ObservableList<ObservableList<Object>> paymentData = initializePaymentData();
        payment.setItems(paymentData);
        payment.getColumns().forEach(colum -> colum.setMinWidth(300));
        payment.getColumns().forEach(column -> column.setSortable(false));


        Button confirm = ApplicationObjects.newButton("Confirm", 250, 350, "black", "white", 100, 20, 15);
        Button cancel = ApplicationObjects.newButton("Cancel", 400, 350, "black", "white", 100, 20, 15);


        confirm.setOnAction(e -> {
            try {
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

    public static ObservableList<ObservableList<Object>> initializePaymentData() {
        ObservableList<ObservableList<Object>> paymentData = FXCollections.observableArrayList();
        paymentData.add(FXCollections.observableArrayList("From account:", new BigDecimal("12345678910")));
        paymentData.add(FXCollections.observableArrayList("Amount:", new BigDecimal("50.00") + "kr"));
        paymentData.add(FXCollections.observableArrayList("Due date:", "01.01.2019"));
        paymentData.add(FXCollections.observableArrayList("To account:", new BigDecimal("10987654321")));
        paymentData.add(FXCollections.observableArrayList("CID:", new BigDecimal("0123456789")));
        return paymentData;
    }


}