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
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AccountOverviewScene {
    static Stage stage = ApplicationFront.getStage();
    private static Account currentAccount;
    private static TableView<ObservableList<Object>> lastTransactionsTable;
    private static ObservableList<ObservableList<Object>> lastTransactionsData;
    static public Scene scene() throws IOException {
        List<Account> accounts = ApplicationFront.loggedInUser.getAccountsAsList();
        ObservableList<String> accountNames = FXCollections.observableArrayList(getAccountsNames());

        if (accounts.isEmpty()) {
            Dialog<AccountWithProperty> accountWithPropertyDialog = new ApplicationObjects.AccountWithPropertyDialog(new AccountWithProperty(null, null, null, null));
            Optional<AccountWithProperty> result = accountWithPropertyDialog.showAndWait();
            if (result.isPresent()) {

                createAccountDialog(accountNames, result);
            }
            else {
                return MainPageScene.scene();
            }
        }

        String[] columnTitlesTransactionsTable = {"Transaction", "Date", "Amount"};


        ComboBox<String> accountComboBox = ApplicationObjects.newComboBox(columnTitlesTransactionsTable, 364, 30, 30, 364-(364/2), 50);
        accountComboBox.setItems(accountNames);
        accountComboBox.setValue(accounts.get(0).getAccountName());
        Text accountNumberText    = ApplicationObjects.newText("Account Number: " + accounts.get(0).getAccountNumber(), 14, false, 0, 130);
        Text amountText           = ApplicationObjects.newText("Amount: " + accounts.get(0).getAmount() + "kr", 20, false, 0, 160);
        Button addExpense         = ApplicationObjects.newButton("Add Expense", 30,80, 100, 20,14);
        Button addIncome          = ApplicationObjects.newButton("Add Income", 30,50, 100, 20,14);
        Button addAccount         = ApplicationObjects.newButton("Add Account", 728 - 130, 50, 100, 20, 14);
        Button editAccount        = ApplicationObjects.newButton("Edit Account", 728 - 130, 80, 100, 20, 14);
        Text lastTransactionsText = ApplicationObjects.newText("Last Transactions:", 24, false, 20, 200);
        amountText.setLayoutX((double) 728/2 - amountText.getLayoutBounds().getWidth()/2);
        accountNumberText.setLayoutX((double) 728/2 - accountNumberText.getLayoutBounds().getWidth()/2);
        lastTransactionsTable = ApplicationObjects.newTableView(columnTitlesTransactionsTable, 20, 230, 688, 300);
        lastTransactionsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        setCurrentAccount(accountComboBox.getValue());
        ObservableList<ObservableList<Object>> lastTransactionsData = initializeLastTransactionsData(currentAccount);
        lastTransactionsTable.setItems(lastTransactionsData);

        accountComboBox.setOnAction(actionEvent -> {
            setCurrentAccount(accountComboBox.getValue());


            accountNumberText.setText("Account Number: " + currentAccount.getAccountNumber());
            amountText.setText("Amount: " + currentAccount.getAmount().toString() + "kr");
        });
        addAccount.setOnAction(actionEvent -> {
            Dialog<AccountWithProperty> accountWithPropertyDialog = new ApplicationObjects.AccountWithPropertyDialog(new AccountWithProperty(null, null, null, null));
            Optional<AccountWithProperty> result = accountWithPropertyDialog.showAndWait();
            if (result.isPresent()) {

                createAccountDialog(accountNames, result);
            }
        });
        editAccount.setOnAction(actionEvent -> {
            Dialog<Account> accountDialog = new ApplicationObjects.EditAccountDialog(currentAccount);
            Optional<Account> result = accountDialog.showAndWait();
            if (result.isPresent()) {
                Account account = result.get();
                accountComboBox.setItems(FXCollections.observableArrayList(getAccountsNames()));

            }
        });

        addExpense.setOnAction(actionEvent -> {
            showDialog(false);
        });

        addIncome.setOnAction(actionEvent -> {
            showDialog(true);
        });



        ImageView homeButton = ApplicationObjects.newImage("home.png", 10, 10, 20, 20);
        homeButton.setOnMouseClicked(e -> {
            try {
                stage.setScene(MainPageScene.scene());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        
        Button dropDownButton = ApplicationObjects.newButton("test", 676, 10, 10, 10, 10);
        Group dropDown = ApplicationObjects.dropDownMenu();
        ImageView manageUserButton = ApplicationObjects.newImage("user.png", 646, 10, 20, 20);
        Group root = new Group(accountComboBox, accountNumberText, amountText, addIncome, addExpense, addAccount, editAccount, lastTransactionsText, lastTransactionsTable, dropDownButton, homeButton, manageUserButton);
        dropDownButton.setOnAction(e -> root.getChildren().add(dropDown));

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
        

        return scene;
    }

    private static void createAccountDialog(ObservableList<String> accountNames, Optional<AccountWithProperty> result) {
        AccountWithProperty accountWithProperty = result.get();
        Account account = new Account(AccountCategory.valueOf(accountWithProperty.getAccountType()),
                new BigDecimal(accountWithProperty.getAmount()),
                accountWithProperty.getAccountNumber(), accountWithProperty.getAccountName());
        FileManagement.writeAccount(ApplicationFront.loggedInUser.getLoginInfo().getUserId(), account);
        accountNames.add(account.getAccountName());
        ApplicationFront.loggedInUser.addAccount(account);
    }

    private static void setCurrentAccount(String accountName) {
        currentAccount = ApplicationFront.loggedInUser.getAccountWithAccountName(accountName);
        initializeLastTransactionsData(currentAccount);
        lastTransactionsTable.setItems(lastTransactionsData);

    }
    private static List<String> getAccountsNames() {

        return ApplicationFront.loggedInUser.getAccountsAsList()
                .stream().map(Account::getAccountName).collect(Collectors.toList());
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
    private static void showDialog(Boolean incomeOrExpense) {
        Dialog<TransactionWithProperty> transactionWithPropertyDialog =
                new ApplicationObjects.TransactionWithPropertyDialog(
                        new TransactionWithProperty(null,
                                null, null,
                                null, null, null), incomeOrExpense);
        Optional<TransactionWithProperty> result = transactionWithPropertyDialog.showAndWait();
        if (result.isPresent()) {
            TransactionWithProperty transactionWithProperty = result.get();
            Transaction transaction;
            if (incomeOrExpense) {
                transaction = new Transaction(transactionWithProperty.getFromAccountId(),
                        ApplicationFront.loggedInUser.getAccountWithAccountName(transactionWithProperty.getToAccountId()).getAccountNumber(),
                        new BigDecimal(transactionWithProperty.getAmount()),
                        transactionWithProperty.getDescription(), transactionWithProperty.getDateOfTransaction(),
                        BudgetCategory.valueOf(transactionWithProperty.getCategory()));
            } else {
                transaction = new Transaction(
                        ApplicationFront.loggedInUser
                        .getAccountWithAccountName(transactionWithProperty.getFromAccountId()).getAccountNumber(),
                        transactionWithProperty.getToAccountId(), new BigDecimal(transactionWithProperty.getAmount()),
                        transactionWithProperty.getDescription(), transactionWithProperty.getDateOfTransaction(),
                        BudgetCategory.valueOf(transactionWithProperty.getCategory()));
            }

            FileManagement.writeTransaction(ApplicationFront.loggedInUser.getLoginInfo().getUserId(), transaction);
            ApplicationFront.loggedInUser.getTransactionsAsList().add(transaction);
            addTransaction(transaction);
        }
    }
}
