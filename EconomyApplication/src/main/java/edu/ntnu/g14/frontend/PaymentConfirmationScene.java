package edu.ntnu.g14.frontend;

import java.math.BigDecimal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PaymentConfirmationScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene() {
        TableView<ObservableList<Object>> payment = ApplicationObjects.newTableView(new String[]{"Payment", "Information"}, 100, 150, 602, 150);
        ObservableList<ObservableList<Object>> paymentData = initializePaymentData();
        payment.setItems(paymentData);
        payment.getColumns().forEach(colum -> colum.setMinWidth(300));
        payment.getColumns().forEach(column -> column.setSortable(false));


        Button confirm = ApplicationObjects.newButton("Confirm", 250, 350, "black", "white", 100, 20, 15);
        Button cancel = ApplicationObjects.newButton("Cancel", 400, 350, "black", "white", 100, 20, 15);


        confirm.setOnAction(e -> {
            stage.setScene(MainPageScene.scene());
        });
        cancel.setOnAction(e -> {
            stage.setScene(PaymentScene.scene());
        });


        Group root = new Group(payment, confirm, cancel);
        Scene scene = new Scene(root, 800, 500, Color.WHITE);
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
