package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import edu.ntnu.g14.Account;
import edu.ntnu.g14.FileManagement;
import edu.ntnu.g14.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainPageScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene() throws IOException {
        String [] columnTitlesLatestActivitiesTable = {"Transaction", "Amount"};
        String [] columnTitlesDuePaymentsTable = {"Date", "Recipient", "Amount"};

        String [] accountsList = new String [ApplicationFront.loggedInUser.getAccounts().length];
        Account[] accounts = ApplicationFront.loggedInUser.getAccounts();
        for (int i = 0; i < ApplicationFront.loggedInUser.getAccounts().length; i++){
            accountsList[i] = accounts[i].getAccountName();
        }

        Text actionsText = ApplicationObjects.newText("Actions", 20, false, 160, 30);
        Group transfer = ApplicationObjects.newButtonWithIcon("Transfer", 30, 50, 157, 25, 16, "budget.png", TransferScene.scene());
        
        Group invoice = ApplicationObjects.newButtonWithIcon("Invoice", 192, 50, 157,25,16, "invoice.png", InvoiceScene.scene());
        
        Group payment = ApplicationObjects.newButtonWithIcon("Payment", 30, 90, 157, 25, 16, "payment.png", PaymentScene.scene());
        
        Group overview = ApplicationObjects.newButtonWithIcon("Overview", 192,90, 157,25,16, "overview.png", GeneralOverviewScene.scene());
        
        Group accountsButton = ApplicationObjects.newButtonWithIcon("Accounts", 30,130, 157, 25, 16, "account.png", AccountOverviewScene.scene(Optional.empty()));
        
        Group budgeting = ApplicationObjects.newButtonWithIcon("Budgeting", 192, 130, 157, 25,16, "budget.png", BudgetingScene.scene());
        
        

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

        accountsListView.setOnMouseClicked(mouseEvent -> {
            try {
                stage.setScene(AccountOverviewScene.scene(Optional.ofNullable(ApplicationFront.loggedInUser.getAccountWithAccountName(accountsListView.getSelectionModel().getSelectedItem().toString()))));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Button dropDownButton = ApplicationObjects.newButton("test", 676, 10, 10, 10, 10);
        Group dropDown = ApplicationObjects.dropDownMenu();
        ImageView manageUserButton = ApplicationObjects.newImage("user.png", 646, 10, 20, 20);
        Group root = new Group(actionsText, transfer, invoice, payment, 
        overview, accountsButton, budgeting, latestActivitiesText, latestActivitiesTable, 
        duePaymentsTable, duePaymentsText, accountsListView, accountsText, dropDownButton, manageUserButton);
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

    static public ObservableList<ObservableList<Object>> initializeLatestActivitiesData() throws IOException {
        int length = ApplicationFront.loggedInUser.getTransactions().length;
        if (length > 10){
            length = 10;
        }
        Transaction[] transactions = FileManagement.readLatestTransactions(ApplicationFront.loggedInUser.getLoginInfo().getUserId(), length);
        ObservableList<ObservableList<Object>> latestActivitiesData = FXCollections.observableArrayList();
        for(int i = 0; i < length; i++){
            latestActivitiesData.add(FXCollections.observableArrayList(transactions[i].getToAccountNumber(), transactions[i].getAmount()));
        }
        
        return  latestActivitiesData;
    }
  
}
