package edu.ntnu.g14.frontend;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import edu.ntnu.g14.Account;
import edu.ntnu.g14.TransactionWithProperty;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import edu.ntnu.g14.AccountWithProperty;
import javafx.util.Callback;
import javafx.util.StringConverter;


public class ApplicationObjects {

    public static String borderColor = "#071E22";
    public static String backgroundColor = "#1D7874";
    public static Color sceneColor = Color.valueOf("#F4C095");
    

    static Stage stage = ApplicationFront.getStage();

    public static final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public static Color getSceneColor(){
        return sceneColor;
    }

    
    public static Group userMenu() {
        Rectangle rectangle = newRectangle(553, 10, 145, 110);
        
        Button logOut = newButton("overview", 563, 15, 130, 40, 15);
        logOut.setOnAction(e -> {
            try {
                stage.setScene(LoginChooseUserScene.scene());
            } catch (IOException e1) {
                
                e1.printStackTrace();
            }
        });
        Button manageUser = newButton("manage user", 563, 70, 130, 40, 15);
        manageUser.setOnAction(e -> {
            try {
                stage.setScene(UserManagementScene.scene());
            } catch (FileNotFoundException e1) {
                
                e1.printStackTrace();
            }
        });
        return new Group(rectangle, logOut, manageUser);
    }

    public static Group dropDownMenu() {
        Rectangle rectangle = newRectangle(553, 10, 145, 340);
        Button invoice = newButton("invoice", 563, 15, 130, 40, 20);
        invoice.setOnAction(e -> {
            try {
                stage.setScene(InvoiceScene.scene());
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        Button transfer = newButton("transfer", 563, 70, 130, 40, 20);
        transfer.setOnAction(e -> {
            try {
                stage.setScene(TransferScene.scene());
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        Button payment = newButton("payment", 563, 125, 130, 40, 20);
        payment.setOnAction(e -> {
            try {
                stage.setScene(PaymentScene.scene());
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        Button accounts = newButton("accounts", 563, 180, 130, 40, 20);
        accounts.setOnAction(e -> {
            try {
                stage.setScene(AccountOverviewScene.scene());
            } catch (FileNotFoundException e1) {
                
                e1.printStackTrace();
            }
        });
        Button overview = newButton("overview", 563, 235, 130, 40, 20);
        overview.setOnAction(e -> {
            try {
                stage.setScene(GeneralOverviewScene.scene());
            } catch (FileNotFoundException e1) {
                
                e1.printStackTrace();
            }
        });
        Button budgetting = newButton("budgetting", 563, 290, 130, 40, 20);
        budgetting.setOnAction(e -> {
            try {
                stage.setScene(BudgetingScene.scene());
            } catch (FileNotFoundException e1) {
                
                e1.printStackTrace();
            }
        });
        return new Group(rectangle, invoice, transfer, payment, accounts, overview, budgetting);
    }

    public static Button newButton(String text, int x, int y, int width, int height, int fontSize) {
        Button button = new Button(text);
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setStyle(setStyleString(borderColor, backgroundColor, width, height, fontSize));
        button.setOnMouseEntered(
            e -> button.setStyle(setStyleString(borderColor, "grey", width, height, fontSize)));
        button.setOnMouseExited(e -> button.setStyle(
            setStyleString(borderColor, backgroundColor, width, height, fontSize)));
        return button;
    }
    
    public static ToggleButton newToggleButton(String text, int x, int y, int width, int height, int fontSize) {
        ToggleButton toggleButton = new ToggleButton(text);
        toggleButton.setLayoutX(x);
        toggleButton.setLayoutY(y);
        toggleButton.setStyle(setStyleString(borderColor, backgroundColor, width, height, fontSize));
        toggleButton.setOnMouseEntered(e -> toggleButton.setStyle(setStyleString(borderColor, "grey", width, height, fontSize)));
        toggleButton.setOnMouseExited(e -> toggleButton.setStyle(setStyleString(borderColor, backgroundColor, width, height, fontSize)));
        return toggleButton;
    }

    public static TextField newTextField(String promptText, int x, int y, int width, int height, int fontSize) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setLayoutX(x);
        textField.setLayoutY(y);
        textField.setStyle(setStyleString(borderColor, backgroundColor, width, height, fontSize));
        return textField;
    }

    public static ImageView newImage(String imagename,
     int x, int y, int width, int height) throws FileNotFoundException{
        ImageView imageview = new ImageView();
        Image image = new Image(new FileInputStream("src/main/resources/images/" + imagename));
        imageview.setImage(image);
        imageview.setX(x);
        imageview.setY(y);
        imageview.setFitHeight(width);
        imageview.setFitWidth(height);
        imageview.setPreserveRatio(true);
        return imageview;
    }

    public static Text newText(String title, int size, boolean underline, int x, int y) {
        Text text = new Text(title);
        text.setFont(Font.font("Times New Roman", FontWeight.BOLD, size));
        text.setUnderline(underline);
        text.setX(x);
        text.setY(y);
        return text;
    }

    public static TableView<ObservableList<Object>> newTableView(String[] columnTitles, double x, double y,
        double width, double height) {
        TableView<ObservableList<Object>> tableView = new TableView<>();
        tableView.setLayoutX(x);
        tableView.setLayoutY(y);
        tableView.setPrefWidth(width);
        tableView.setPrefHeight(height);

        int colum = columnTitles.length;

        // create columns
         for (String title : columnTitles) {
         TableColumn<ObservableList<Object>, Object> column = new TableColumn<>(title);
         column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
         param.getValue().get(tableView.getColumns().indexOf(column))));
         tableView.getColumns().add(column);
         }

        return tableView;
    }
    
    public static ListView<String> newListView(String[] elements, double x, double y, double width, double height) {
        ListView<String> listView = new ListView<>();
        listView.setLayoutX(x);
        listView.setLayoutY(y);
        listView.setPrefWidth(width);
        listView.setPrefHeight(height);

        for (String element: elements) {
            listView.getItems().add(element);
        }
        return listView;
    }
    
    public static Rectangle newRectangle(int x, int y, int width, int height){
        Rectangle rectangle = new Rectangle();
        rectangle.setX(x);
        rectangle.setY(y);
        rectangle.setWidth(width);
        rectangle.setHeight(height);
        rectangle.setStroke(Color.BLACK);
        rectangle.setFill(Color.WHITE);
        return rectangle;
    }

    public static ChoiceBox<String> newChoiceBox(String[] choices, int width, int height, int fontSize, int x, int y) {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(choices);
        choiceBox.setStyle(setStyleString(borderColor, backgroundColor, width, height, fontSize));
        choiceBox.setLayoutX(x);
        choiceBox.setLayoutY(y);
        return choiceBox;
    }

    public static void alertBox(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static String setStyleString(String borderColor,
        String backgroundColor, int width, int height, int fontSize) {
        return "-fx-border-color: " + borderColor + ";" +
            "-fx-background-color: " + backgroundColor + ";" +
            "-fx-pref-width: " + width + ";" +
            "-fx-pref-height: " + height + ";" +
            "-fx-font-size: " + fontSize + "px;";
    }
    
    public static class AccountWithPropertyDialog extends Dialog<AccountWithProperty> {
        private final AccountWithProperty account;
        private ChoiceBox<String> accountTypeField;
        private TextField amountField;
        private TextField accountNumberField;
        private TextField accountNameField;

        public AccountWithPropertyDialog(AccountWithProperty account) {
            super();
            this.setTitle("Add Account");
            this.account = account;
            buildUI();
            setPropertyBindings();
            setResultConverter();
        }

        private void setPropertyBindings() {
            amountField.textProperty().bindBidirectional(account.amountProperty());
            accountNumberField.textProperty().bindBidirectional(account.accountNumberProperty());
            accountNameField.textProperty().bindBidirectional(account.accountNameProperty());
            accountTypeField.valueProperty().bindBidirectional(account.accountTypeProperty());
            // TODO: Implement String Properties to Account class
        }

        private void setResultConverter() {
            javafx.util.Callback<ButtonType, AccountWithProperty> accountResultConverter = param -> {
                if (param == ButtonType.APPLY) {
                    return account;
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
                private boolean validateDialog() {
                    String regexAccountNumber = "[0-9]{4}+\\.[0-9]{2}+\\.[0-9]{5}";
                    BigDecimal amountBigDecimal;
                    try {
                        amountBigDecimal = new BigDecimal(amountField.getText());
                    } catch (NumberFormatException | NullPointerException e) {
                        return false;
                    }
                    return !accountNumberField.getText().isBlank()
                            && accountNumberField.getText() != null
                            && !accountNameField.getText().isBlank()
                            && !accountNameField.getText().isBlank()
                            && accountNameField.getText() != null
                            && !accountTypeField.getValue().isBlank()
                            && !(amountBigDecimal.floatValue() < 0)
                            && Pattern.matches(regexAccountNumber, accountNumberField.getText());
                }
            });
        }

        public Pane createGridPane() {
            VBox content = new VBox(10);
            Label accountTypeLabel = new Label("Choose the type of account:");
            Label accountNumberLabel = new Label("Enter the account number:");
            Label amountLabel = new Label("Enter the balance of your account:");
            Label accountNameLabel = new Label("Choose a name for your account:");
            this.accountTypeField = new ChoiceBox<>();
            this.accountNumberField = new TextField();
            this.amountField = new TextField();
            this.accountNameField = new TextField();
            this.accountTypeField.getItems().addAll("Spendings Account", "Savings Account", "Pensions Account", "Other");
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
    
    static class TransactionWithPropertyDialog extends Dialog<TransactionWithProperty> {
        private final TransactionWithProperty transaction;
        private TextField fromAccountNumberField;
        private TextField toAccountNumberField;
        private TextField amountField;
        private TextField descriptionField;
        private DatePicker dateOfTransactionField;
        public TransactionWithPropertyDialog(TransactionWithProperty transaction) {
            super();
            this.setTitle("Add Transaction");
            this.transaction = transaction;
            buildUI();
            setPropertyBindings();
            setResultConverter();
        }
        private void setPropertyBindings() {
            fromAccountNumberField.textProperty().bindBidirectional(transaction.getFromAccountIdProperty());
            toAccountNumberField.textProperty().bindBidirectional(transaction.getToAccountIdProperty());
            amountField.textProperty().bindBidirectional(transaction.getAmountProperty());
            descriptionField.textProperty().bindBidirectional(transaction.getDescriptionProperty());
            dateOfTransactionField.valueProperty().bindBidirectional(transaction.getDateOfTransactionProperty());
        }
        private void setResultConverter() {
            javafx.util.Callback<ButtonType, TransactionWithProperty> transactionResultConverter = param -> {
                if (param == ButtonType.APPLY) {
                    return transaction;
                } else {
                    return null;
                }
            };
            setResultConverter(transactionResultConverter);
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
                private boolean validateDialog() {
                    String regexAccountNumber = "[0-9]{4}+\\.[0-9]{2}+\\.[0-9]{5}";

                    return !toAccountNumberField.getText().isBlank()
                            && !amountField.getText().isBlank()
                            && !descriptionField.getText().isBlank()
                            && !toAccountNumberField.getText().isBlank()
                            && (ApplicationFront.loggedInUser.getAccountsAsList().stream().map(Account::getAccountNumber).anyMatch(accountNumber -> accountNumber.equals(toAccountNumberField.getText())) || ApplicationFront.loggedInUser.getAccountsAsList().stream().map(Account::getAccountNumber).anyMatch(accountNumber -> accountNumber.equals(fromAccountNumberField.getText()))
                            && Pattern.matches(regexAccountNumber, toAccountNumberField.getText()));
                }
            });
        }
        public Pane createGridPane() {
            VBox content = new VBox(10);
            Label fromAccountLabel = new Label("Enter the sender's account:");
            Label toAccountLabel = new Label("Enter the recipient's account number:");
            Label amountLabel = new Label("Enter the amount you want to send:");
            Label descriptionLabel = new Label("Enter the description of the transaction:");
            Label dateOfTransactionLabel = new Label("Choose the date of the transaction:")   ;

            this.fromAccountNumberField = new TextField();
            this.toAccountNumberField = new TextField();
            this.amountField = new TextField();
            this.descriptionField = new TextField();
            this.dateOfTransactionField = new DatePicker();
            dateOfTransactionField.setConverter(new StringConverter<LocalDate>() {
                @Override
                public String toString(LocalDate localDate) {
                    if (localDate != null) {
                        return dateFormatter.format(localDate);
                    }
                    return null;
                }

                @Override
                public LocalDate fromString(String string) {
                    if (string != null && !string.isBlank())
                        return LocalDate.parse(string, dateFormatter);
                    return null;
                }
            });
            restrictDatePicker(dateOfTransactionField, LocalDate.now());
            //TODO: Make it retrieve a users accounts which are not savings or pensions
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(5);
            grid.add(fromAccountLabel,0,0);
            grid.add(toAccountLabel,0,1);
            grid.add(amountLabel,0,2);
            grid.add(descriptionLabel,0,3);
            grid.add(dateOfTransactionLabel,0,4);
            GridPane.setHgrow(this.fromAccountNumberField, Priority.ALWAYS);
            grid.add(fromAccountNumberField, 1,0);
            GridPane.setHgrow(this.toAccountNumberField,Priority.ALWAYS);
            grid.add(toAccountNumberField,1,1);
            grid.add(amountField,1,2);
            GridPane.setHgrow(this.amountField,Priority.ALWAYS);
            grid.add(descriptionField,1,3);
            GridPane.setHgrow(this.descriptionField,Priority.ALWAYS);
            grid.add(dateOfTransactionField,1,4);
            GridPane.setHgrow(this.dateOfTransactionField,Priority.ALWAYS);
            content.getChildren().add(grid);
            return content;
        }
        public void restrictDatePicker(DatePicker datePicker, LocalDate minDate) {
            final Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item.isBefore(minDate)) {
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

}
