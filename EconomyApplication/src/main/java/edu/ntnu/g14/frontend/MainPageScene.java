package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;

import edu.ntnu.g14.Account;
import edu.ntnu.g14.FileManagement;
import edu.ntnu.g14.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainPageScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene() throws FileNotFoundException {
        String [] columnTitlesLatestActivitiesTable = {"Transaction", "Amount"};
        String [] columnTitlesDuePaymentsTable = {"Date", "Recipient", "Amount"};

        String [] accountsList = new String [ApplicationFront.getLoggedInUser().getAllAccounts().length];
        Account[] accounts = ApplicationFront.getLoggedInUser().getAllAccounts();
        for (int i = 0; i < ApplicationFront.getLoggedInUser().getAllAccounts().length; i++){
            accountsList[i] = accounts[i].getAccountName();
        }

        Text actionsText = ApplicationObjects.newText("Actions", 20, false, 160, 30);
        Button transfer = ApplicationObjects.newButton("Transfer", 30, 50, "black", "white", 157, 25, 16);
        transfer.setOnMouseClicked(e -> {
            try {
                stage.setScene(TransferScene.scene());
            } catch (FileNotFoundException e1) {
                
                e1.printStackTrace();
            }
        });
        Button invoice = ApplicationObjects.newButton("Invoice", 192, 50,"black", "white", 157,25,16);
        invoice.setOnMouseClicked(e -> {
            try {
                stage.setScene(InvoiceScene.scene());
            } catch (FileNotFoundException e1) {
                
                e1.printStackTrace();
            }
        });
        Button payment = ApplicationObjects.newButton("Payment", 30, 90, "black", "white", 157, 25, 16);
        payment.setOnMouseClicked(e -> {
            try {
                stage.setScene(PaymentScene.scene());
            } catch (FileNotFoundException e1) {
                
                e1.printStackTrace();
            }
        });
        Button overview = ApplicationObjects.newButton("Overview", 192,90, "black", "white", 157,25,16);
        overview.setOnMouseClicked(e -> {
            try {
                stage.setScene(GeneralOverviewScene.scene());
            } catch (FileNotFoundException e1) {
                
                e1.printStackTrace();
            }
        });
        Button accountsButton = ApplicationObjects.newButton("Accounts", 30,130, "black", "white", 157, 25, 16);
        accountsButton.setOnMouseClicked(e -> {
            try {
                stage.setScene(AccountOverviewScene.scene());
            } catch (FileNotFoundException e1) {
                
                e1.printStackTrace();
            }
        });
        Button budgeting = ApplicationObjects.newButton("Budgeting", 192, 130, "black", "white", 157, 25,16);
        budgeting.setOnMouseClicked(e -> {
            try {
                stage.setScene(BudgetingScene.scene());
            } catch (FileNotFoundException e1) {
                
                e1.printStackTrace();
            }
        });
        Text latestActivitiesText = ApplicationObjects.newText("Latest Activities", 20, false,130, 210);
        TableView latestActivitiesTable = ApplicationObjects.newTableView(columnTitlesLatestActivitiesTable, 30, 230, 324, 300);
        ObservableList<ObservableList<Object>> latestActivitiesData;
        try {
            latestActivitiesData = initializeLatestActivitiesData();
            latestActivitiesTable.setItems(latestActivitiesData);
        } catch (IOException e1) {
            e1.printStackTrace();
        } 
        latestActivitiesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        Text duePaymentsText = ApplicationObjects.newText("Due Payments", 20, false, 473, 210);
        TableView duePaymentsTable = ApplicationObjects.newTableView(columnTitlesDuePaymentsTable, 728-30-324, 230, 324, 300);
        duePaymentsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        Text accountsText = ApplicationObjects.newText("Accounts", 20, false, 500, 30);
        ListView accountsListView = ApplicationObjects.newListView(accountsList, 728-30-324, 50, 324, 115);
        
        ImageView homeButton = ApplicationObjects.newImage("home.png", 10, 10, 20, 20);
        
        Button dropDownButton = ApplicationObjects.newButton("test", 676, 10, "black", "white", 10, 10, 10);
        Group dropDown = ApplicationObjects.dropDownMenu();
        ImageView manageUserButton = ApplicationObjects.newImage("user.png", 646, 10, 20, 20);
        Group root = new Group(actionsText, transfer, invoice, payment, 
        overview, accountsButton, budgeting, latestActivitiesText, latestActivitiesTable, 
        duePaymentsTable, duePaymentsText, accountsListView, accountsText, dropDownButton, homeButton, manageUserButton);
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

    static public ObservableList<ObservableList<Object>> initializeLatestActivitiesData() throws IOException {
        int length = ApplicationFront.getLoggedInUser().getTransactions().length;
        if (length > 10){
            length = 10;
        }
        Transaction[] transactions = FileManagement.readLatestTransactions(ApplicationFront.getLoggedInUser().getLoginInfo().getUserId(), length);
        ObservableList<ObservableList<Object>> latestActivitiesData = FXCollections.observableArrayList();
        for(int i = 0; i < length; i++){
            latestActivitiesData.add(FXCollections.observableArrayList(transactions[i].getToAccountId(), new BigDecimal(transactions[i].getAmount())));
        }
        
        return  latestActivitiesData;
    }
  
}
