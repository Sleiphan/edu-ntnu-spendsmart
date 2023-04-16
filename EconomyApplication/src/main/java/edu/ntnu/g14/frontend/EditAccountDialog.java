package edu.ntnu.g14.frontend;

import edu.ntnu.g14.Account;
import edu.ntnu.g14.AccountCategory;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class EditAccountDialog extends Dialog<Account> {
    private final Account account;
    private TextField editAccountNameField;
    private ComboBox<String> editAccountTypeBox;
    private ButtonType deleteButtonType;

    public EditAccountDialog(Account account) {
        super();
        this.setTitle("Edit Account");
        this.account = account;
        buildUI();
        setResultConverter();
    }

    private void setResultConverter() {
        Callback<ButtonType, Account> accountResultConverter = buttonType -> {
            if (buttonType == ButtonType.OK) {
                editAccount();
                return account;
            }
            if (buttonType == deleteButtonType) {
                Alert deleteAccountAlert = new Alert(Alert.AlertType.CONFIRMATION);
                deleteAccountAlert.setHeaderText("Are you sure you want to delete the account?");
                deleteAccountAlert.setContentText("Are you sure you want to delete: " + account.getAccountName() + "?");
                deleteAccountAlert.setTitle("Delete account");
                deleteAccountAlert.showAndWait();
                if (deleteAccountAlert.getResult() == ButtonType.CANCEL) {
                    return null;
                }
                ApplicationFront.loggedInUser.removeAccount(account);
                return account;
            }
            else {
                return null;
            }
        };
        setResultConverter(accountResultConverter);
    }


    private void editAccount() {
        account.setAccountType(AccountCategory.valueOf(editAccountTypeBox.getValue()
                .replaceAll(" ", "_").toUpperCase()));
        account.setAccountName(editAccountNameField.getText());
    }

    private void buildUI() {
        Pane pane = createGridPane();
        getDialogPane().setContent(pane);
        deleteButtonType = new ButtonType("Delete");
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL, deleteButtonType);

        Button button = (Button) getDialogPane().lookupButton(ButtonType.OK);
        button.addEventFilter(ActionEvent.ACTION, actionEvent -> {
            if (!validateDialog()) {
                actionEvent.consume();
            }
        });

    }

    private boolean validateDialog() {
        return editAccountNameField.getText() != null
                && editAccountTypeBox.getValue() != null
                && ApplicationFront.loggedInUser.checkIfAccountNameIsOccupied(editAccountNameField.getText());
    }

    private Pane createGridPane() {
        VBox content = new VBox(10);
        Label editAccountNameLabel = new Label("Enter new name for your account:");
        Label editAccountTypeLabel = new Label("Choose the new type of your account:");
        this.editAccountNameField  = new TextField();
        this.editAccountTypeBox    = new ComboBox<>();

        this.editAccountTypeBox.getItems().addAll(ApplicationObjects.getAccountCategories());
        this.editAccountTypeBox.setPromptText("New account type");
        this.editAccountTypeBox.setMaxWidth(200);
        this.editAccountNameField.setPromptText("New account name:");
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(5);
        pane.add(editAccountNameLabel, 0, 0);
        pane.add(editAccountNameField, 1,  0);
        GridPane.setHgrow(editAccountNameField, Priority.ALWAYS);

        pane.add(editAccountTypeLabel, 0, 1);
        pane.add(editAccountTypeBox, 1, 1);
        GridPane.setHgrow(editAccountTypeBox, Priority.ALWAYS);



        content.getChildren().addAll(pane);

        return content;
    }

}