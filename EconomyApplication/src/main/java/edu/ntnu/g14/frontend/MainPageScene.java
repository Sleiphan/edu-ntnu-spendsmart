package edu.ntnu.g14.frontend;

import java.math.BigDecimal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainPageScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene() {
        String [] columnTitlesLatestActivitiesTable = {"Transaction", "Amount"};
        String [] columnTitlesDuePaymentsTable = {"Date", "Recipient", "Amount"};
        String [] accountsList = {"Savings", "Spending", "Pension"};
        Text actionsText = ApplicationObjects.newText("Actions", 20, false, 160, 30);
        Button transfer = ApplicationObjects.newButton("Transfer", 30, 50, "black", "white", 157, 25, 16);
        transfer.setOnMouseClicked(e -> stage.setScene(TransferScene.scene()));
        Button invoice = ApplicationObjects.newButton("Invoice", 192, 50,"black", "white", 157,25,16);
        invoice.setOnMouseClicked(e -> stage.setScene(InvoiceScene.scene()));
        Button payment = ApplicationObjects.newButton("Payment", 30, 90, "black", "white", 157, 25, 16);
        payment.setOnMouseClicked(e -> stage.setScene(PaymentScene.scene()));
        Button overview = ApplicationObjects.newButton("Overview", 192,90, "black", "white", 157,25,16);
        overview.setOnMouseClicked(e -> stage.setScene(GeneralOverviewScene.scene()));
        Button accounts = ApplicationObjects.newButton("Accounts", 30,130, "black", "white", 157, 25, 16);
        accounts.setOnMouseClicked(e -> stage.setScene(AccountOverviewScene.scene()));
        Button budgeting = ApplicationObjects.newButton("Budgeting", 192, 130, "black", "white", 157, 25,16);
        budgeting.setOnMouseClicked(e -> stage.setScene(BudgetingScene.scene()));
        Text latestActivitiesText = ApplicationObjects.newText("Latest Activities", 20, false,130, 210);
        TableView latestActivitiesTable = ApplicationObjects.newTableView(columnTitlesLatestActivitiesTable, 30, 230, 324, 300);
        ObservableList<ObservableList<Object>> latestActivitiesData = initializeLatestActivitiesData();
        latestActivitiesTable.setItems(latestActivitiesData);
        latestActivitiesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        Text duePaymentsText = ApplicationObjects.newText("Due Payments", 20, false, 473, 210);
        TableView duePaymentsTable = ApplicationObjects.newTableView(columnTitlesDuePaymentsTable, 728-30-324, 230, 324, 300);
        duePaymentsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        Text accountsText = ApplicationObjects.newText("Accounts", 20, false, 500, 30);
        ListView accountsListView = ApplicationObjects.newListView(accountsList, 728-30-324, 50, 324, 115);
        Group root = new Group(actionsText, transfer, invoice, payment, overview, accounts, budgeting, latestActivitiesText, latestActivitiesTable, duePaymentsTable, duePaymentsText, accountsListView, accountsText);
        return new Scene(root, 728, 567, Color.WHITE);
    }

    static public ObservableList<ObservableList<Object>> initializeLatestActivitiesData() {
        //TODO: Use for loop to loop for the amount and description of the latest activities of a user
        ObservableList<ObservableList<Object>> latestActivitiesData = FXCollections.observableArrayList();
        latestActivitiesData.add(FXCollections.observableArrayList("Elkjøp (Reserved)", new BigDecimal("10000.00")));
        latestActivitiesData.add(FXCollections.observableArrayList("Vinmonopolet", new BigDecimal("5000.00")));
        latestActivitiesData.add(FXCollections.observableArrayList("Kiwi Minipris", new BigDecimal("458.00")));
        latestActivitiesData.add(FXCollections.observableArrayList("Trap Star", new BigDecimal("1350.00")));
        latestActivitiesData.add(FXCollections.observableArrayList("Savings -> Spendings", new BigDecimal("2300.00")));
        latestActivitiesData.add(FXCollections.observableArrayList("Pay Check", new BigDecimal("53202.00")));
        latestActivitiesData.add(FXCollections.observableArrayList("PayPal", new BigDecimal("200.00")));
        return  latestActivitiesData;
    }
  
}
