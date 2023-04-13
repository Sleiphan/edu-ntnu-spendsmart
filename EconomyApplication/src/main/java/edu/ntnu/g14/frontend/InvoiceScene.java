package edu.ntnu.g14.frontend;

import edu.ntnu.g14.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.BigDecimalValidator;

public class InvoiceScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene() throws FileNotFoundException {
        Invoice[] invoicesUser = ApplicationFront.loggedInUser.getAllInvoices();
        List<Invoice> invoices = new ArrayList<Invoice>();
        for(int i = 0; i < ApplicationFront.loggedInUser.getAllInvoices().length; i++){
                invoices.add(invoicesUser[i]);
        }
        

        Text amount_t = ApplicationObjects.newText("Amount (kr):", 17, false, 11, 345 - 186);
        Text accountNum_t = ApplicationObjects.newText("Account number:", 17, false, 11, 376 - 186);
        Text cidComment_t = ApplicationObjects.newText("CID / Comment:", 17, false, 11, 407 - 186);
        Text dueDate_t = ApplicationObjects.newText("Due date:", 17, false, 11, 438 - 186);
        TextField amount_tf = ApplicationObjects.newTextField("Amount (kr)", 198, 322 - 186, 206, 20,
                14);
        TextField accountNum_tf = ApplicationObjects.newTextField("Account number", 198, 353 - 186, 206,
                20, 14);
        TextField cidComment_tf = ApplicationObjects.newTextField("CID / Comment", 198, 384 - 186, 206,
                20, 14);

        DatePicker due_dp = new DatePicker();
        due_dp.setLayoutX(527 - 329);
        due_dp.setLayoutY(422 - 186);

        ListView<Invoice> invoices_lv = new ListView<>();
        invoices_lv.setLayoutX(782 - 329);
        invoices_lv.setLayoutY(326 - 186);
        invoices_lv.getItems().setAll(invoices);
        invoices_lv.setEditable(false);

        Button clear_bt = ApplicationObjects.newButton("Clear", 358 - 329, 570 - 186, 159, 61, 16);
        Button register_bt = ApplicationObjects.newButton("Register", 549 - 329, 570 - 186, 159, 61, 16);
        Button back_bt = ApplicationObjects.newButton("Back", 358 - 329, 637 - 186, 159, 35, 16);
        Button payNow_bt = ApplicationObjects.newButton("Pay now", 817 - 329, 493 - 186, 159, 61, 16);
        Button delete_bt = ApplicationObjects.newButton("Delete", 817 - 329, 570 - 186, 159, 61, 16);

        clear_bt.setOnAction(e -> {
            amount_tf.clear();
            accountNum_tf.clear();
            cidComment_tf.clear();
        });
        register_bt.setOnAction(e -> {
            if (amount_tf.getText().isBlank()) {
                ApplicationObjects.alertBox("Missing input", "Missing information",
                        "Please enter the financial amount in the new invoice first.");
                return;
            }

            if (accountNum_tf.getText().isBlank()) {
                ApplicationObjects.alertBox("Missing input", "Missing information",
                        "Please enter the account number this invoice .");
                return;
            }

            BigDecimal amount = BigDecimalValidator.getInstance().validate(amount_tf.getText());
            if (amount == null) {
                ApplicationObjects.alertBox("Invalid input", "Invalid amount",
                        "Could not understand the amount specified. Make sure to only enter numbers and use the period-sign instead of comma.");
                return;
            }

            // TODO: Check validity of account number.

            // TODO: Reconsider use of Date class.
            LocalDate due = due_dp.getValue();
            Invoice newInvoice = new Invoice(
                    due.atStartOfDay(ZoneId.systemDefault()).toLocalDate(), 
                    amount,
                    accountNum_tf.getText());
            invoices.add(newInvoice);
            try {
                FileManagement.writeNewInvoice(ApplicationFront.loggedInUser.getLoginInfo().getUserId(), newInvoice);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            clear_bt.getOnAction().handle(null);
            throw new RuntimeException("Action not connected to backend.");
        });
        back_bt.setOnAction(e -> {
            try {
                stage.setScene(MainPageScene.scene());
            } catch (FileNotFoundException e1) {
               
                e1.printStackTrace();
            }
        });
        payNow_bt.setOnAction(e -> {
            invoices.remove(invoices_lv.getSelectionModel().getSelectedItem());
            try {
                FileManagement.deleteInvoice(invoices_lv.getSelectionModel().getSelectedItem(), ApplicationFront.loggedInUser.getLoginInfo().getUserId());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        delete_bt.setOnAction(e -> {
            invoices.remove(invoices_lv.getSelectionModel().getSelectedItem());
            try {
                FileManagement.deleteInvoice(invoices_lv.getSelectionModel().getSelectedItem(), ApplicationFront.loggedInUser.getLoginInfo().getUserId());
            } catch (IOException e1) {
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
        Group root = new Group(amount_t, accountNum_t, cidComment_t, dueDate_t,
        amount_tf, accountNum_tf, cidComment_tf,
        invoices_lv, due_dp,
        clear_bt, register_bt, back_bt, payNow_bt, delete_bt, dropDownButton, homeButton, manageUserButton);
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
