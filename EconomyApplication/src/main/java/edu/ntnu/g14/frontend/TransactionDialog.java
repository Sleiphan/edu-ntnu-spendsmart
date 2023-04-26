package edu.ntnu.g14.frontend;

import edu.ntnu.g14.*;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static edu.ntnu.g14.frontend.ApplicationObjects.dateFormatter;

public class TransactionDialog extends Dialog<Transaction.TransactionBuilder> {

  private final Transaction.TransactionBuilder transactionBuilder;
  private ComboBox<String> chooseAccountComboBox;
  private TextField accountNumberField;
  private TextField amountField;
  private TextField descriptionField;
  private DatePicker dateOfTransactionField;
  private ComboBox<String> categoryField;
  Label amountLabel;
  Label descriptionLabel;
  Label dateOfTransactionLabel;
  Label categoryLabel;
  Label accountLabel2;
  Label accountLabel1;

  public TransactionDialog(Transaction.TransactionBuilder transactionBuilder, boolean income) {
    super();
    this.transactionBuilder = transactionBuilder;
    if (income) {
      this.setTitle("Add Income");
      buildUI(true);
    } else {
      this.setTitle("Add Expense");
      buildUI(false);
    }
    setResultConverter(income);
  }

  private void setBuilderFieldValuesIncome() {
    transactionBuilder.toAccountNumber(
        BankApplication.loggedInUser.getAccountWithAccountName(chooseAccountComboBox.getValue())
            .getAccountNumber());
    transactionBuilder.fromAccountNumber(accountNumberField.getText());
    setBuilderFieldValues();
  }

  private void setBuilderFieldValuesExpense() {
    transactionBuilder.toAccountNumber(accountNumberField.getText());
    transactionBuilder.fromAccountNumber(
        BankApplication.loggedInUser.getAccountWithAccountName(chooseAccountComboBox.getValue())
            .getAccountNumber());
    setBuilderFieldValues();
  }

  private void setBuilderFieldValues() {
    transactionBuilder.amount(new BigDecimal(amountField.getText()));
    transactionBuilder.description(descriptionField.getText());
    transactionBuilder.dateOfTransaction(dateOfTransactionField.getValue());
    transactionBuilder.category(
        BudgetCategory.valueOf(categoryField.getValue().replaceAll(" ", "_").toUpperCase()));
  }

  private void setResultConverter(boolean income) {
    javafx.util.Callback<ButtonType, Transaction.TransactionBuilder> transactionResultConverter = param -> {
      if (param == ButtonType.APPLY) {
        if (income) {
          setBuilderFieldValuesIncome();
        } else {
          setBuilderFieldValuesExpense();
        }

        return transactionBuilder;
      } else {
        return null;
      }
    };
    setResultConverter(transactionResultConverter);
  }

  private void buildUI(boolean income) {
    Pane pane;
    if (income) {
      pane = createIncomeGridPane();
    } else {
      pane = createExpenseGridPane();
    }

    getDialogPane().setContent(pane);
    getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);

    Button applyButton = (Button) getDialogPane().lookupButton(ButtonType.APPLY);
    applyButton.addEventFilter(ActionEvent.ACTION, event -> {
      if (!validateDialog()) {
        event.consume();
      }
    });
  }

  private boolean validateDialog() {
    BigDecimal amountBigDecimal;

    try {
      amountBigDecimal = new BigDecimal(amountField.getText());
    } catch (NumberFormatException | NullPointerException e) {
      return false;
    }
    return accountNumberField.getText() != null
        && amountField.getText() != null
        && !(amountBigDecimal.floatValue() < 0)
        && descriptionField.getText() != null
        && accountNumberField.getText() != null
        && categoryField.getValue() != null
        && Pattern.matches(Transaction.regexAccountNumber, accountNumberField.getText());
  }

  private void defineLabelsAndFields() {
    this.amountField = new TextField();
    this.amountField.setPromptText("Enter amount");
    this.descriptionLabel = new Label("Enter the description of the transaction:");
    this.descriptionField = new TextField();
    this.descriptionField.setPromptText("Enter description");
    this.dateOfTransactionLabel = new Label("Select the date of the transaction:");
    this.dateOfTransactionField = new DatePicker();
    this.dateOfTransactionField.setPromptText("Select date");

    this.categoryLabel = new Label("Select the category of the transaction:");
    this.categoryField = new ComboBox<>();
    this.categoryField.setPromptText("Category of Transaction");
    this.categoryField.setMaxWidth(200);

    this.chooseAccountComboBox = new ComboBox<>();
    this.chooseAccountComboBox.setPromptText("Select Account");
    this.chooseAccountComboBox.setMaxWidth(200);

    this.accountNumberField = new TextField();
    this.accountNumberField.setPromptText("11-digit Account Number");
    restrictDatePicker(dateOfTransactionField);
    setDatePickerConverter(dateOfTransactionField);
  }

  private GridPane addLabelsAndFieldsToGrid() {
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(5);

    grid.add(categoryLabel, 0, 0);
    grid.add(this.categoryField, 1, 0);
    GridPane.setHgrow(this.categoryField, Priority.ALWAYS);

    grid.add(amountLabel, 0, 3);
    grid.add(amountField, 1, 3);
    GridPane.setHgrow(this.amountField, Priority.ALWAYS);

    grid.add(descriptionLabel, 0, 4);
    grid.add(descriptionField, 1, 4);
    GridPane.setHgrow(this.descriptionField, Priority.ALWAYS);

    grid.add(dateOfTransactionLabel, 0, 5);
    grid.add(dateOfTransactionField, 1, 5);
    GridPane.setHgrow(this.dateOfTransactionField, Priority.ALWAYS);

    grid.add(accountLabel1, 0, 1);
    grid.add(this.chooseAccountComboBox, 1, 1);
    GridPane.setHgrow(this.chooseAccountComboBox, Priority.ALWAYS);

    grid.add(accountLabel2, 0, 2);
    grid.add(this.accountNumberField, 1, 2);
    GridPane.setHgrow(this.accountNumberField, Priority.ALWAYS);
    return grid;
  }

  private Pane createIncomeGridPane() {
    VBox content = new VBox(10);
    this.accountLabel1 = new Label("Choose your receiving account number:");
    this.accountLabel2 = new Label("Enter the sender's account:");
    this.amountLabel = new Label("Enter the amount you received:");

    defineLabelsAndFields();

    this.chooseAccountComboBox.getItems().addAll(BankApplication.loggedInUser
        .getAccountsAsList()
        .stream()
        .map(Account::getAccountName)
        .collect(Collectors.toList()));
    this.categoryField.getItems().addAll(ApplicationObjects.getBudgetIncomeCategories());

    content.getChildren().add(addLabelsAndFieldsToGrid());
    return content;
  }

  private Pane createExpenseGridPane() {
    VBox content = new VBox(10);
    this.accountLabel1 = new Label("Choose the sending account:");
    this.accountLabel2 = new Label("Enter the recipient's account:");
    this.amountLabel = new Label("Enter the amount you sent:");

    defineLabelsAndFields();

    this.chooseAccountComboBox.getItems().addAll(BankApplication.loggedInUser
        .getAccountsAsList()
        .stream()
        .filter(account -> account.getAccountType().equals(AccountCategory.CHECKING_ACCOUNT)
            || account.getAccountType().equals(AccountCategory.OTHER))
        .map(Account::getAccountName)
        .collect(Collectors.toList()));
    this.categoryField.getItems().addAll(ApplicationObjects.getBudgetExpenditureCategories());

    content.getChildren().add(addLabelsAndFieldsToGrid());
    return content;
  }

  private void setDatePickerConverter(DatePicker dateOfTransactionField) {
    dateOfTransactionField.setConverter(new StringConverter<>() {
      @Override
      public String toString(LocalDate localDate) {
        if (localDate != null) {
          return dateFormatter.format(localDate);
        }
        return null;
      }

      @Override
      public LocalDate fromString(String string) {
        if (string != null && !string.isBlank()) {
          return LocalDate.parse(string, dateFormatter);
        }
        return null;
      }
    });
  }

  private void restrictDatePicker(DatePicker datePicker) {
    final Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
      @Override
      public DateCell call(final DatePicker datePicker) {
        return new DateCell() {
          @Override
          public void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);
            if (item.isAfter(LocalDate.now().minusDays(1))) {
              setDisable(true);
              setStyle("-fx-background-color: #ffc0cb;");
            }
          }
        };
      }
    };
    datePicker.setDayCellFactory(dayCellFactory);
  }
}