package edu.ntnu.g14.frontend;

import edu.ntnu.g14.BankApplication;
import edu.ntnu.g14.model.Account;
import edu.ntnu.g14.model.AccountCategory;
import java.math.BigDecimal;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class AccountDialog extends Dialog<Account.AccountBuilder> {

  private final Account.AccountBuilder accountBuilder;
  private ComboBox<String> accountTypeField;
  private TextField amountField;
  private TextField accountNumberField;
  private TextField accountNameField;

  public AccountDialog(Account.AccountBuilder accountBuilder) {
    super();
    this.setTitle("Add Account");
    this.accountBuilder = accountBuilder;
    buildUI();
    setResultConverter();
  }

  private void setBuilderFieldValues() {
    accountBuilder.amount(new BigDecimal(amountField.getText()));
    accountBuilder.accountNumber(accountNumberField.getText());
    accountBuilder.accountName(accountNameField.getText());
    accountBuilder.accountCategory(
        AccountCategory.valueOf(accountTypeField.getValue().replaceAll(" ", "_").toUpperCase()));
  }

  private void setResultConverter() {
    javafx.util.Callback<ButtonType, Account.AccountBuilder> accountResultConverter = param -> {
      if (param == ButtonType.APPLY) {
        setBuilderFieldValues();
        return accountBuilder;
      } else {
        return null;
      }
    };
    setResultConverter(accountResultConverter);
  }

  private void buildUI() {
    Pane pane = createGridPane();
    getDialogPane().setContent(pane);
    getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);
    Button applyButton = (Button) getDialogPane().lookupButton(ButtonType.APPLY);
    applyButton.addEventFilter(ActionEvent.ACTION, new EventHandler<>() {
      @Override
      public void handle(ActionEvent event) {
        if (!validateDialog()) {
          event.consume();

        }
      }
    });
  }

  private boolean validateDialog() {

    amountField.setStyle(null);
    amountField.setStyle(null);
    accountNumberField.setStyle(null);
    accountNameField.setStyle(null);

    String regexAccountNumber = "[0-9]{4}+\\.[0-9]{2}+\\.[0-9]{5}";
    BigDecimal amountBigDecimal;
    try {
      amountBigDecimal = new BigDecimal(amountField.getText());
    } catch (NumberFormatException | NullPointerException e) {
      amountBigDecimal = new BigDecimal(-1);
      amountField.setStyle(amountField.getStyle() + "-fx-border-color: red;" +
          "-fx-border-width: 0.5px;");
    }

    if (!(amountBigDecimal.intValue() > 0)) {
      amountField.setStyle(amountField.getStyle() + "-fx-border-color: red;" +
          "-fx-border-width: 0.5px;");
    }

    if (!Pattern.matches(regexAccountNumber, accountNumberField.getText())) {
      accountNumberField.setStyle(accountNumberField.getStyle() + "-fx-border-color: red;" +
          "-fx-border-width: 0.5px;");
    }
    if (BankApplication.loggedInUser.checkIfAccountNumberIsOccupied(accountNumberField.getText())) {
      accountNumberField.setStyle(accountNumberField.getStyle() + "-fx-border-color: red;" +
          "-fx-border-width: 0.5px;");
      Alert accountNumberOccupiedAlert = new Alert(Alert.AlertType.ERROR);
      accountNumberOccupiedAlert.setHeaderText("Account number is occupied");
      accountNumberOccupiedAlert.setContentText(
          "You already have an account with this account number!");
      accountNumberOccupiedAlert.show();
    }

    if (accountNameField.getText().isBlank()) {
      accountNameField.setStyle(accountNameField.getStyle() + "-fx-border-color: red;" +
          "-fx-border-width: 0.5px;");
    }
    if (BankApplication.loggedInUser.checkIfAccountNameIsOccupied(accountNameField.getText())) {
      accountNameField.setStyle(accountNameField.getStyle() + "-fx-border-color: red;" +
          "-fx-border-width: 0.5px;");
      Alert accountNameOccupiedAlert = new Alert(Alert.AlertType.ERROR);
      accountNameOccupiedAlert.setHeaderText("Account name is occupied");
      accountNameOccupiedAlert.setContentText(
          "You already have an account with this account name!");
      accountNameOccupiedAlert.show();
    }
    if (accountTypeField.getValue() == null) {
      accountTypeField.setStyle(accountTypeField.getStyle() + "-fx-border-color: red;" +
          "-fx-border-width: 0.5px;");
    }

    return accountNumberField.getText() != null
        && !accountNumberField.getText().isBlank()
        && Pattern.matches(regexAccountNumber, accountNumberField.getText())
        && accountNameField.getText() != null
        && !BankApplication.loggedInUser.checkIfAccountNameIsOccupied(accountNameField.getText())
        && !accountNameField.getText().isBlank()
        && accountTypeField.getValue() != null
        && !(amountBigDecimal.floatValue() < 0)
        && !BankApplication.loggedInUser.checkIfAccountNumberIsOccupied(
        accountNumberField.getText());
  }

  private Pane createGridPane() {
    VBox content = new VBox(10);
    Label accountTypeLabel = new Label("Choose the type of account:");
    Label accountNumberLabel = new Label("Enter the account number:");
    Label amountLabel = new Label("Enter the balance of your account:");
    Label accountNameLabel = new Label("Choose a name for your account:");
    this.accountTypeField = new ComboBox<>();
    this.accountNumberField = new TextField();
    this.amountField = new TextField();
    this.accountNameField = new TextField();
    this.accountTypeField.setPromptText("");
    this.accountTypeField.getItems().addAll(ApplicationObjects.getAccountCategories());
    this.accountTypeField.setPromptText("Choose an Account Type");
    this.accountNameField.setPromptText("Enter Account Name");
    this.amountField.setPromptText("Enter Amount");
    this.accountNumberField.setPromptText("11-digit Account Number");

    GridPane grid = new GridPane();

    grid.setHgap(10);
    grid.setVgap(5);
    grid.add(accountTypeLabel, 0, 0);
    grid.add(accountNumberLabel, 0, 1);
    grid.add(amountLabel, 0, 2);
    grid.add(accountNameLabel, 0, 3);
    grid.add(accountTypeField, 1, 0);
    GridPane.setHgrow(this.accountTypeField, Priority.ALWAYS);
    grid.add(accountNumberField, 1, 1);
    GridPane.setHgrow(this.accountNumberField, Priority.ALWAYS);
    grid.add(amountField, 1, 2);
    GridPane.setHgrow(this.amountField, Priority.ALWAYS);
    grid.add(accountNameField, 1, 3);
    GridPane.setHgrow(this.accountNameField, Priority.ALWAYS);
    accountTypeField.setMaxWidth(160);
    content.getChildren().add(grid);
    return content;
  }
}