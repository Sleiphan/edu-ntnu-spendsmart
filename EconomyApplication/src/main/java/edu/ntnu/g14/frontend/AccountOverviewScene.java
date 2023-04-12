package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;
import java.io.IOException;
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
    private static TableView<ObservableList<Object>> lastTransactionsTable;
    private static ObservableList<ObservableList<Object>> lastTransactionsData;
    static public Scene scene() throws FileNotFoundException {
        List<Account> accounts = ApplicationFront.loggedInUser.getAccountsAsList();
        ObservableList<String> accountNames = FXCollections.observableArrayList(getAccountsNames());

        String[] columnTitlesTransactionsTable = {"Transaction", "Date", "Amount"};


        ChoiceBox<String> accountChoiceBox = ApplicationObjects.newChoiceBox(columnTitlesTransactionsTable, 364, 30, 30, 364-(364/2), 30);
        accountChoiceBox.setItems(accountNames);
        accountChoiceBox.setValue(accounts.get(0).getAccountName());
        Text accountNumberText = ApplicationObjects.newText(accounts.get(0).getAccountNumber(), 14, false, 325, 130);
        Text amountText = ApplicationObjects.newText("Amount: " + accounts.get(0).getAmount() + "kr", 20, false, 290, 160);
        Button addTransaction = ApplicationObjects.newButton("Add Transaction", 20, 30, 120, 20, 14);
        Button addAccount = ApplicationObjects.newButton("Add Account", 20, 60, 120, 20, 14);
        Text lastTransactionsText = ApplicationObjects.newText("Last Transactions:", 24, false, 20, 200);
        lastTransactionsTable = ApplicationObjects.newTableView(columnTitlesTransactionsTable, 20, 230, 688, 300);
        lastTransactionsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        setCurrentAccount(accounts, accountChoiceBox);
        ObservableList<ObservableList<Object>> lastTransactionsData = initializeLastTransactionsData(currentAccount);
        lastTransactionsTable.setItems(lastTransactionsData);

        accountChoiceBox.setOnAction(actionEvent -> {
            setCurrentAccount(accounts, accountChoiceBox);

            accountNumberText.setText(currentAccount.getAccountNumber());
            amountText.setText("Amount: " + currentAccount.getAmount().toString() + "kr");
        });
        addAccount.setOnAction(actionEvent -> {
            Dialog<AccountWithProperty> accountWithPropertyDialog = new ApplicationObjects.AccountWithPropertyDialog(new AccountWithProperty(null, null, null, null));
            Optional<AccountWithProperty> result = accountWithPropertyDialog.showAndWait();
            if (result.isPresent()) {

                AccountWithProperty accountWithProperty = result.get();
                Account account = new Account(AccountCategory.valueOf(accountWithProperty.getAccountType()),
                        new BigDecimal(accountWithProperty.getAmount()),
                        accountWithProperty.getAccountNumber(), accountWithProperty.getAccountName());
                FileManagement.writeAccount(ApplicationFront.loggedInUser.getLoginInfo().getUserId(), account);
                accountNames.add(account.getAccountName());
                ApplicationFront.loggedInUser.addAccount(account);
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

                FileManagement.writeTransaction(ApplicationFront.loggedInUser.getLoginInfo().getUserId(), transaction);
                ApplicationFront.loggedInUser.getTransactionsAsList().add(transaction);
                addTransaction(transaction);
            }
        });
        Button back_bt = ApplicationObjects.newButton("Back", 20, 90, 120, 20, 14);
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
        
        Button dropDownButton = ApplicationObjects.newButton("test", 676, 10, 10, 10, 10);
        Group dropDown = ApplicationObjects.dropDownMenu();
        ImageView manageUserButton = ApplicationObjects.newImage("user.png", 646, 10, 20, 20);
        Group root = new Group(accountChoiceBox, accountNumberText, amountText, addTransaction, addAccount, lastTransactionsText, lastTransactionsTable, back_bt, dropDownButton, homeButton, manageUserButton);
        dropDownButton.setOnAction(e -> root.getChildren().add(dropDown));

        root.getStylesheets().add("StyleSheet.css"); 
        Scene scene = new Scene(root, 728, 567, ApplicationObjects.getSceneColor());
        

        
        Group userButtons = ApplicationObjects.userMenu();
        manageUserButton.setOnMouseEntered(e -> root.getChildren().add(userButtons));
        scene.setOnMouseClicked(e -> {
            root.getChildren().remove(userButtons);
            root.getChildren().remove(dropDown);
        });
        
        return scene;
    }

    private static void setCurrentAccount(List<Account> accounts, ChoiceBox<String> accountChoiceBox) {
        currentAccount = accounts.stream().filter(account -> account
                .getAccountName()
                .equals(accountChoiceBox.getValue()))
                .findFirst().orElseThrow(() -> new IllegalStateException("Account could not be found."));
        initializeLastTransactionsData(currentAccount);
        lastTransactionsTable.setItems(lastTransactionsData);

    }
    private static String[] getAccountsNames() {
        return Arrays.stream(ApplicationFront.loggedInUser.getAccounts())
                .map(Account::getAccountName).toArray(String[]::new);
    }
    private static ObservableList<ObservableList<Object>> initializeLastTransactionsData(Account account) {

        List<Transaction> transactionsOfAccount = Arrays.stream(ApplicationFront.loggedInUser.getTransactions())
                .filter(transaction -> transaction.getToAccountNumber()
                .equals(account.getAccountNumber())
                        || transaction.getFromAccountNumber().equals(account.getAccountNumber()))
                .collect(Collectors.toList());

        lastTransactionsData = FXCollections.observableArrayList();

        transactionsOfAccount.forEach(transaction -> {
            if (transaction.getFromAccountNumber().equals(account.getAccountNumber())) {
                lastTransactionsData.
                        add(FXCollections.observableArrayList(transaction.getToAccountNumber(),
                                transaction.getDateOfTransaction().format(ApplicationObjects.dateFormatter),
                                transaction.getAmount()));
            } else {
                lastTransactionsData.add(FXCollections.observableArrayList(transaction.getFromAccountNumber(),
                        transaction.getDateOfTransaction().format(ApplicationObjects.dateFormatter),
                        transaction.getAmount()));
            }
            });

        return lastTransactionsData;
    }

    private static void addTransaction(Transaction transaction) {
        if (transaction.getFromAccountNumber().equals(currentAccount.getAccountNumber())) {
            lastTransactionsData.
                    add(FXCollections.observableArrayList(transaction.getToAccountNumber(),
                            transaction.getDateOfTransaction().format(ApplicationObjects.dateFormatter),
                            transaction.getAmount()));
        } else {
            lastTransactionsData.add(FXCollections.observableArrayList(transaction.getFromAccountNumber(),
                    transaction.getDateOfTransaction().format(ApplicationObjects.dateFormatter),
                    transaction.getAmount()));
        }
    }
}
