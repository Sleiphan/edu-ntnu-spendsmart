package edu.ntnu.g14.frontend;

import edu.ntnu.g14.Account;
import edu.ntnu.g14.AccountCategory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.util.regex.Pattern;

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
        accountBuilder.accountCategory(AccountCategory.valueOf(accountTypeField.getValue().replaceAll(" ", "_").toUpperCase()));
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
        String regexAccountNumber = "[0-9]{4}+\\.[0-9]{2}+\\.[0-9]{5}";
        BigDecimal amountBigDecimal;
        try {
            amountBigDecimal = new BigDecimal(amountField.getText());
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return accountNumberField.getText() != null
                && accountNameField.getText() != null
                && !accountNameField.getText().isBlank()
                && accountTypeField.getValue() != null
                && !(amountBigDecimal.floatValue() < 0)
                && Pattern.matches(regexAccountNumber, accountNumberField.getText())
                && !ApplicationFront.loggedInUser.checkIfAccountNameIsOccupied(accountNameField.getText())
                && !ApplicationFront.loggedInUser.checkIfAccountNumberIsOccupied(accountNumberField.getText());
    }

    private Pane createGridPane() {
        VBox content             = new VBox(10);
        Label accountTypeLabel   = new Label("Choose the type of account:");
        Label accountNumberLabel = new Label("Enter the account number:");
        Label amountLabel        = new Label("Enter the balance of your account:");
        Label accountNameLabel   = new Label("Choose a name for your account:");
        this.accountTypeField    = new ComboBox<>();
        this.accountNumberField  = new TextField();
        this.amountField         = new TextField();
        this.accountNameField    = new TextField();

        this.accountTypeField.getItems().addAll(ApplicationObjects.getAccountCategories());
        this.accountTypeField.setPromptText("Account Type");

        GridPane grid = new GridPane();

        grid.setHgap(10);
        grid.setVgap(5);
        grid.add(accountTypeLabel,0,0);
        grid.add(accountNumberLabel,0,1);
        grid.add(amountLabel,0,2);
        grid.add(accountNameLabel,0,3);
        grid.add(accountTypeField,1,0);
        GridPane.setHgrow(this.accountTypeField, Priority.ALWAYS);
        grid.add(accountNumberField, 1,1);
        GridPane.setHgrow(this.accountNumberField,Priority.ALWAYS);
        grid.add(amountField,1,2);
        GridPane.setHgrow(this.amountField,Priority.ALWAYS);
        grid.add(accountNameField,1,3);
        GridPane.setHgrow(this.accountNameField,Priority.ALWAYS);
        accountTypeField.setMaxWidth(160);
        content.getChildren().add(grid);
        return content;
    }
}