package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import edu.ntnu.g14.AccountWithProperty;
import edu.ntnu.g14.Transaction;

public class AccountOverviewScene {
    static Stage stage = ApplicationFront.getStage();


    static public Scene scene() throws FileNotFoundException {
        //TODO: Choices should get the different accounts a user has. Example: "user.getAccounts().asArray()"
        String [] columnTitlesTransactionsTable = {"Transaction", "Date", "Amount"};
        String [] choices = {"Spendings Account", "Savings Account"};
        ChoiceBox<String> accountChoiceBox = ApplicationObjects.newChoiceBox(choices, "black", "white", 364, 30, 30, 364-(364/2), 30);
        accountChoiceBox.setValue("Spendings Account");
        //TODO: Take the first element of the Array and make it the default label of the choice box when account overview is opened

        Text accountNumberText = ApplicationObjects.newText("9293 11 39239", 14, false, 325, 130);
        Text amountText = ApplicationObjects.newText("Amount: 23 340 kr", 20, false, 290, 160);
        Button addTransaction = ApplicationObjects.newButton("Add Transaction", 20, 30, "black", "white", 120, 20, 14);
        Button addAccount = ApplicationObjects.newButton("Add Account", 20, 60, "black", "white", 120, 20, 14);
        Text lastTransactionsText = ApplicationObjects.newText("Last Transactions:", 24, false, 20, 200);
        TableView lastTransactionsTable = ApplicationObjects.newTableView(columnTitlesTransactionsTable, 20, 230, 688, 300);
        ObservableList<ObservableList<Object>> lastTransactionsData = initializeLastTransactionsData();
        lastTransactionsTable.setItems(lastTransactionsData);
        lastTransactionsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        addAccount.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Dialog<AccountWithProperty> accountWithPropertyDialog = new ApplicationObjects.AccountWithPropertyDialog(new AccountWithProperty(null, null, null, null));
                Optional<AccountWithProperty> result = accountWithPropertyDialog.showAndWait();
                if (result.isPresent()) {
                    AccountWithProperty account = result.get();
                    //TODO: Add the account to the users list of accounts
                }
            }
        });
        addTransaction.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Dialog<Transaction> transactionDialog = new ApplicationObjects.TransactionDialog(new Transaction("9293.11.39233", "9293.11.39234", (short) 32121, "Hope it is enough", LocalDate.now()));
                Optional<Transaction> result = transactionDialog.showAndWait();
                if (result.isPresent()) {
                    Transaction transaction = result.get();
                    //TODO: ---
                }
            }
        });
        Button back_bt = ApplicationObjects.newButton("Back", 20, 90, "black", "white", 120, 20, 14);
        back_bt.setOnAction(e -> {
            try {
                stage.setScene(MainPageScene.scene());
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
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
        Group root = new Group(accountChoiceBox, accountNumberText, amountText, addTransaction,
         addAccount, lastTransactionsText, lastTransactionsTable, back_bt, dropDownButton, homeButton, manageUserButton);
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


    public static ObservableList<ObservableList<Object>> initializeLastTransactionsData() {
        ObservableList<ObservableList<Object>> lastTransactionsData = FXCollections.observableArrayList();
        lastTransactionsData.add(FXCollections.observableArrayList("Elkjøp (Reserved)", "10.02.2022", new BigDecimal("10000.00")));
        lastTransactionsData.add(FXCollections.observableArrayList("Harampolet", "10.02.2022", new BigDecimal("5000.00")));
        lastTransactionsData.add(FXCollections.observableArrayList("Kiwi Groceries","09.02.2022", new BigDecimal("458.00")));
        lastTransactionsData.add(FXCollections.observableArrayList("Steampowered","03.02.2022", new BigDecimal("900.00")));
        lastTransactionsData.add(FXCollections.observableArrayList("Harambet","29.01.2022", new BigDecimal("1700.00")));
        return lastTransactionsData;
    }

}
