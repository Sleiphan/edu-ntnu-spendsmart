package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import edu.ntnu.g14.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AccountOverviewScene {
    static Stage stage = ApplicationFront.getStage();
    private static Account currentAccount;

    static public Scene scene() throws FileNotFoundException {
        //TODO: Choices should get the different accounts a user has. Example: "user.getAccounts().asArray()"
        Account[] accounts = ApplicationFront.loggedInUser.getAllAccounts();
        String[] accountNames = Arrays.stream(ApplicationFront.loggedInUser.getAllAccounts()).map(Account::getAccountName).toArray(String[]::new);
        String[] columnTitlesTransactionsTable = {"Transaction", "Date", "Amount"};
        ChoiceBox<String> accountChoiceBox = ApplicationObjects.newChoiceBox(accountNames, "black", "white", 364, 30, 30, 364-(364/2), 30);
        accountChoiceBox.setValue(accounts[0].getAccountName());
        Text accountNumberText = ApplicationObjects.newText(accounts[0].getAccountNumber(), 14, false, 325, 130);

        Text amountText = ApplicationObjects.newText("Amount: " + accounts[0].getAmount() + "kr", 20, false, 290, 160);
        Button addTransaction = ApplicationObjects.newButton("Add Transaction", 20, 30, "black", "white", 120, 20, 14);
        Button addAccount = ApplicationObjects.newButton("Add Account", 20, 60, "black", "white", 120, 20, 14);
        Text lastTransactionsText = ApplicationObjects.newText("Last Transactions:", 24, false, 20, 200);
        TableView<ObservableList<Object>> lastTransactionsTable = ApplicationObjects.newTableView(columnTitlesTransactionsTable, 20, 230, 688, 300);
        setCurrentAccount(accounts, accountChoiceBox);
        ObservableList<ObservableList<Object>> lastTransactionsData = initializeLastTransactionsData(currentAccount);
        lastTransactionsTable.setItems(FXCollections.observableList(lastTransactionsData));
        lastTransactionsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        accountChoiceBox.setOnAction(actionEvent -> {
            setCurrentAccount(accounts, accountChoiceBox);

            accountNumberText.setText(currentAccount.getAccountNumber());
            amountText.setText("Amount: " + currentAccount.getAmount().toString() + "kr");
            lastTransactionsTable.setItems(initializeLastTransactionsData(currentAccount));
        });
        addAccount.setOnAction(actionEvent -> {
            Dialog<AccountWithProperty> accountWithPropertyDialog = new ApplicationObjects.AccountWithPropertyDialog(new AccountWithProperty(null, null, null, null));
            Optional<AccountWithProperty> result = accountWithPropertyDialog.showAndWait();
            if (result.isPresent()) {

                AccountWithProperty accountWithProperty = result.get();
                Account account = new Account(AccountCategory.valueOf(accountWithProperty.getAccountType()),
                        new BigDecimal(accountWithProperty.getAmount()),
                        accountWithProperty.getAccountNumber(), accountWithProperty.getAccountName());
                Arrays.stream(ApplicationFront.loggedInUser.getAllAccounts()).collect(Collectors.toList()).add(account);

                //TODO: Add the account to the users list of accounts
            }
        });
        addTransaction.setOnAction(actionEvent -> {
            Dialog<TransactionWithProperty> transactionWithPropertyDialog =
                    new ApplicationObjects.TransactionWithPropertyDialog(
                            new TransactionWithProperty(null,
                                    null, null,
                                    null, null, null));
            Optional<TransactionWithProperty> result = transactionWithPropertyDialog.showAndWait();
            if (result.isPresent()) {
                TransactionWithProperty transactionWithProperty = result.get();
                Transaction transaction = new Transaction(transactionWithProperty.getFromAccountId(),
                        transactionWithProperty.getToAccountId(), new BigDecimal(transactionWithProperty.getAmount()),
                        transactionWithProperty.getDescription(), transactionWithProperty.getDateOfTransaction());
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

    private static void setCurrentAccount(Account[] accounts, ChoiceBox<String> accountChoiceBox) {
        currentAccount = Arrays.stream(accounts).filter(account -> account
                .getAccountName()
                .equals(accountChoiceBox.getValue()))
                .findFirst().orElseThrow(() -> new IllegalStateException("Account could not be found."));
    }
    private static ObservableList<ObservableList<Object>> initializeLastTransactionsData(Account account) {

        List<Transaction> transactionsOfAccount = Arrays.stream(ApplicationFront.loggedInUser.getTransactions())
                .filter(transaction -> transaction.getToAccountNumber().equals(account.getAccountNumber())
                        || transaction.getFromAccountNumber().equals(account.getAccountNumber()))
                .collect(Collectors.toList());
        ObservableList<ObservableList<Object>> lastTransactionsData = FXCollections.observableArrayList();
        transactionsOfAccount.forEach(transaction -> lastTransactionsData.
                add(FXCollections.observableArrayList(transaction)));
        return lastTransactionsData;
    }

}
