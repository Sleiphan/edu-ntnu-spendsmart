package edu.ntnu.g14.frontend;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
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
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AccountOverviewScene {

  static Stage stage = BankApplication.getStage();
  private static Account currentAccount;
  private static TableView<ObservableList<Object>> lastTransactionsTable;
  private static ObservableList<ObservableList<Object>> lastTransactionsData;
  private static Text amountText;
  private static Text accountNumberText;

  static public Scene scene(Optional<Account> account) throws IOException {
    MediaPlayer textToSpeach = ApplicationObjects.newSound("accountOverviewScene");
    if (ApplicationObjects.soundOn()) {
      textToSpeach.play();
    }
    List<Account> accounts = BankApplication.loggedInUser.getAccountsAsList();
    ObservableList<String> accountNames = FXCollections.observableArrayList(getAccountsNames());

    String[] columnTitlesTransactionsTable = {"Transaction", "Date", "Amount"};

    ComboBox<String> accountComboBox = ApplicationObjects.newComboBox(columnTitlesTransactionsTable,
        364, 30, 30, 364 - (364 / 2), 50);
    accountComboBox.setItems(accountNames);
    accountComboBox.setValue(accounts.get(0).getAccountName());
    accountNumberText = ApplicationObjects.newText(
        "Account Number: " + accounts.get(0).getAccountNumber(), 14, false, 0, 130);
    amountText = ApplicationObjects.newText(
        "Balance: " + ApplicationObjects.formatCurrency(accounts.get(0).getAmount()), 20, false, 0,
        160);
    Button addExpense = ApplicationObjects.newButton("Add Expense", 30, 80, 100, 20, 14);
    Button addIncome = ApplicationObjects.newButton("Add Income", 30, 50, 100, 20, 14);
    Button addAccount = ApplicationObjects.newButton("Add Account", 728 - 130, 50, 100, 20, 14);
    Button editAccount = ApplicationObjects.newButton("Edit Account", 728 - 130, 80, 100, 20, 14);
    Text lastTransactionsText = ApplicationObjects.newText("Last Transactions:", 24, false, 20,
        200);
    if (account != null && account.isPresent()) {
      accountComboBox.setValue(account.get().getAccountName());
      setAccountNumberAndAmountText();

    }
    amountText.setLayoutX((double) 728 / 2 - amountText.getLayoutBounds().getWidth() / 2);
    accountNumberText.setLayoutX(
        (double) 728 / 2 - accountNumberText.getLayoutBounds().getWidth() / 2);
    lastTransactionsTable = ApplicationObjects.newTableView1(columnTitlesTransactionsTable, 20, 230,
        688, 300,
        BankApplication.loggedInUser.getAccountsAsList().stream().map(Account::getAccountNumber)
            .collect(Collectors.toList()));
    lastTransactionsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    if (accountComboBox.getValue() != null) {
      setCurrentAccount(accountComboBox.getValue());
    }
    ObservableList<ObservableList<Object>> lastTransactionsData = initializeLastTransactionsData(
        currentAccount);
    lastTransactionsTable.setItems(lastTransactionsData);

    accountComboBox.setOnAction(actionEvent -> {
      if (accountComboBox.getValue() != null) {
        setCurrentAccount(accountComboBox.getValue());

        accountNumberText.setText("Account Number: " + currentAccount.getAccountNumber());
        amountText.setText(
            "Balance: " + ApplicationObjects.formatCurrency(currentAccount.getAmount()));

      }
    });

    addAccount.setOnAction(actionEvent -> {
      Dialog<Account.AccountBuilder> accountBuilderDialog = new AccountDialog(
          new Account.AccountBuilder());
      Optional<Account.AccountBuilder> result = accountBuilderDialog.showAndWait();
      result.ifPresent(accountBuilder -> addAndWriteAccount(accountNames, accountBuilder));
    });
    editAccount.setOnAction(actionEvent -> {
      Dialog<Account> accountDialog = new EditAccountDialog(currentAccount);
      Optional<Account> result = accountDialog.showAndWait();

      if (result.isPresent()) {

        accountComboBox.setItems(FXCollections.observableArrayList(getAccountsNames()));

        if (!BankApplication.loggedInUser.getAccountsAsList().contains(currentAccount)) {
          getAccountsNames().remove(currentAccount.getAccountName());
          accountComboBox.setItems(FXCollections.observableArrayList(getAccountsNames()));
          if (accountNames.size() != 0) {
            accountComboBox.setValue(getAccountsNames().get(0));
          }
        } else {
          accountComboBox.setValue(currentAccount.getAccountName());
        }
        FileManagement.editAccount(BankApplication.loggedInUser.getLoginInfo().getUserId(),
            currentAccount);
      }
    });

    addExpense.setOnAction(actionEvent -> showTransactionDialog(false));

    addIncome.setOnAction(actionEvent -> showTransactionDialog(true));

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
    Group root = new Group(accountComboBox, accountNumberText, amountText, addIncome, addExpense,
        addAccount, editAccount, lastTransactionsText, lastTransactionsTable, dropDownButton,
        homeButton, manageUserButton);
    dropDownButton.setOnAction(e -> root.getChildren().add(dropDown));

    root.getStylesheets().add("StyleSheet.css");
    Scene scene = new Scene(root, 728, 567, ApplicationObjects.getSceneColor());

    Group userButtons = ApplicationObjects.userMenu();
    manageUserButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        root.getChildren().add(userButtons);
        event.consume();
      }
    });

    return scene;
  }

  private static void addAndWriteAccount(ObservableList<String> accountNames,
      Account.AccountBuilder result) {
    Account account = result.build();
    FileManagement.writeAccount(BankApplication.loggedInUser.getLoginInfo().getUserId(), account);
    accountNames.add(account.getAccountName());
    BankApplication.loggedInUser.addAccount(account);
  }

  private static void setCurrentAccount(String accountName) {
    try {
      currentAccount = BankApplication.loggedInUser.getAccountWithAccountName(accountName);
    } catch (NoSuchElementException e) {
      Account accountWithOldAccountName = currentAccount;
      currentAccount = BankApplication.loggedInUser.getAccountWithAccountNumber(
          accountWithOldAccountName
              .getAccountNumber());
    }
    initializeLastTransactionsData(currentAccount);
    lastTransactionsTable.setItems(lastTransactionsData);

  }

  private static List<String> getAccountsNames() {

    return BankApplication.loggedInUser.getAccountsAsList()
        .stream().map(Account::getAccountName).collect(Collectors.toList());
  }

  private static ObservableList<ObservableList<Object>> initializeLastTransactionsData(
      Account account) {

    List<Transaction> transactionsOfAccount = BankApplication.loggedInUser.getTransactionsAsList()
        .stream()
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
                ApplicationObjects.formatCurrency(transaction.getAmount())));
      } else {
        lastTransactionsData.add(
            FXCollections.observableArrayList(transaction.getFromAccountNumber(),
                transaction.getDateOfTransaction().format(ApplicationObjects.dateFormatter),
                ApplicationObjects.formatCurrency(transaction.getAmount())));
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

  private static void showTransactionDialog(boolean incomeOrExpense) {
    Dialog<Transaction.TransactionBuilder> transactionBuilderDialog =
        new TransactionDialog(new Transaction.TransactionBuilder(), incomeOrExpense);
    Optional<Transaction.TransactionBuilder> result = transactionBuilderDialog.showAndWait();
    if (result.isPresent()) {
      Transaction transaction = result.get().build();

      FileManagement.writeTransaction(BankApplication.loggedInUser.getLoginInfo().getUserId(),
          transaction);
      BankApplication.loggedInUser.addTransaction(transaction);
      addTransaction(transaction);
    }
  }

  private static void setAccountNumberAndAmountText() {
    accountNumberText.setText("Account Number: " + currentAccount.getAccountNumber());
    amountText.setText("Balance: " + ApplicationObjects.formatCurrency(currentAccount.getAmount()));
  }
}
