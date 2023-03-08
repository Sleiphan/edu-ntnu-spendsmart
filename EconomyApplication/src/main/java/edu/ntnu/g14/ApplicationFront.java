//TODO: make sure the mouse does not start in the textfield, this makes the prompt text unreadable
//TODO: make it so the user can input the email key with spaces or without. take input and remove spaces

package edu.ntnu.g14;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.BigDecimalValidator;

public class ApplicationFront extends Application {

    private Stage stage;
    User loggedInUser;

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        this.stage = stage;
        stage.setScene(paymentConfirmation());
        stage.show();
    }

    
    //TODO: REMOVE dropdown and add to all other scenes
    public Scene loginChooseUser(){
        Text chooseUser = newText("Choose user", 30, false, 170, 40);
        Text registerNew = newText("Register new account", 10, true, 400, 280);
        registerNew.setOnMouseClicked(e -> {
            stage.setScene(registerFirst());
        });

        String[] users = {"Barack Obama", "Donald Trump"}; //TODO: make variable
        ChoiceBox<String> user = newChoiceBox(users, "black", "white", 250, 20, 15, 125, 70);

        Button confirm = newButton("Confirm", 175, 110, "black", "white", 150, 20, 15);
        confirm.setOnAction(e -> {
            stage.setScene(loginUser(user.getValue()));
        });
        
        Button testDropDown = newButton("test", 0, 0, "black", "white", 10, 10, 10);
        Group dropDown = dropDownMenu();
        Group root = new Group(chooseUser, registerNew, user, confirm, testDropDown);
        testDropDown.setOnAction(e -> {
            root.getChildren().add(dropDown);
        });
        
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        scene.setOnMouseClicked(e -> {
            root.getChildren().remove(dropDown);
        });
        return scene;
    }

    //TODO: MISSING BUTTONS
    public Group dropDownMenu(){
        Rectangle rectangle = newRectangle(0, 0, 150, 200);
        Button invoice = newButton("invoice", 15, 15, "black", "white", 130, 40, 25);
        Button transfer = newButton("transfer", 15, 70, "black", "white", 130, 40, 25);
        Button payment = newButton("payment", 15, 125, "black", "white", 130, 40, 25);

        Group group = new Group(rectangle, invoice, transfer, payment);
        return group;
    }

    public Scene loginUser(String user) { //TODO: add User user as parameter
        Text notYou = newText("Is this not you?", 10, true, 400, 280);
        notYou.setOnMouseClicked(e -> {
            stage.setScene(loginChooseUser());
        });

        Text forgotPassword = newText("Forgot password", 10, true, 400, 260);
        forgotPassword.setOnMouseClicked(e -> {
            stage.setScene(loginForgotPasswordFirst(user));
        });

        TextField textField = newTextField("Password", 120, 60, "black", "white", 250, 40, 25);
        Button loginButton = newButton("Login", 185, 130, "black", "white", 100, 30, 25);
        loginButton.setOnAction(e -> {
            if (textField.getText() == "Passord123") { //add password based on user
                //stage.setScene(overview(user));
            } else {
                //add what happens if the password is wrong
            }
        });

        Group root = new Group(newText("Welcome back " + user, 25, false, 120, 40),
            notYou, forgotPassword, textField, loginButton);
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

    public Scene loginForgotPasswordFirst(String user) {
        Text goBack = newText("Go back?", 10, true, 400, 260);
        goBack.setOnMouseClicked(e -> {
            stage.setScene(loginUser(user));
        });

        TextField keyField = newTextField("Code-key", 120, 70, "black", "white", 250, 30, 25);
        Button loginButton = newButton("Make new password", 120, 140, "black", "white", 250, 20, 15);

        Group root = new Group(
            newText("An email with a code-key has been sent to:", 15, false, 130, 40),
            newText("Oboooma@gmail.com", 15, false, 170, 60),
            goBack, keyField, loginButton);
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

    public Scene loginForgotpasswordSecond() {
        TextField newPassword = newTextField("New password", 125, 70, "black", "white", 250, 20, 15);
        TextField retypeNewPassword = newTextField("Retype new password", 125, 110, "black", "white",
            250, 20, 15);
        Button confirmButton = newButton("Confirm", 175, 150, "black", "white", 150, 20, 15);
        Text goBack = newText("Go back", 10, true, 400, 260);

        Group root = new Group(goBack, newPassword, retypeNewPassword, confirmButton);
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

    public Scene registerFirst() {
        TextField firstName = newTextField("", 10, 35, "black", "white", 100, 20, 15);
        TextField lastName = newTextField("", 10, 135, "black", "white", 100, 20, 15);
        TextField email = newTextField("", 10, 235, "black", "white", 100, 20, 15);
        TextField password = newTextField("", 260, 35, "black", "white", 100, 20, 15);
        TextField confirmPassword = newTextField("", 260, 135, "black", "white", 100, 20, 15);
        Button next = newButton("Next", 260, 235, "black", "white", 100, 20, 15);
        next.setOnAction(e -> {
            if (firstName.getText() != "" && lastName.getText() != ""
                && email.getText() != "" && password.getText() != ""
                && confirmPassword.getText() != "") {
                //TODO: add register user
                String key = EmailVertification.sendVertificationKey(email.getText().replace(" ", ""));
                stage.setScene(registerSecond(key, email.getText()));
            } else {
                alertBox("ERROR", "Missing information", "Pleace fill out all required information");
            }
        });

        Group root = new Group(firstName, lastName,
            email, password, confirmPassword, next,
            newText("First name", 30, false, 10, 25),
            newText("Last name", 30, false, 10, 125),
            newText("Email", 30, false, 10, 225),
            newText("Password", 30, false, 260, 25),
            newText("Confirm password", 30, false, 260, 125));
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

    public Scene registerSecond(String key, String email) {
        TextField keyInput = newTextField("", 120, 195, "black", "white", 100, 20, 15);
        Button next = newButton("Next", 240, 195, "black", "white", 100, 20, 15);
        next.setOnAction(e -> {
            if (keyInput.getText().replace(" ", "").equals(key.replace(" ", ""))) {
                stage.setScene(registerThird());
            } else {
                alertBox("ERROR", "Wrong key", "The wrong key has been input");
            }
        });

        Group root = new Group(keyInput, next,
            newText("An email with a confirmation code", 25, false, 60, 25),
            newText("has been sent to", 25, false, 140, 50),
            newText(email, 25, false, 140, 75),
            newText("Code:", 30, false, 20, 225));
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

    public Scene registerThird() {
        Button yes = newButton("Yes", 200, 120, "black", "white", 50, 20, 15);
        Button skip = newButton("Skip for now", 170, 170, "black", "white", 100, 20, 15);
        Button addDefault = newButton("Add default accounts", 150, 220, "black", "white", 150, 20, 15);

        Group root = new Group(yes, skip, addDefault,
            newText("Would you like to add", 25, false, 130, 40),
            newText("account information", 25, false, 130, 80));
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

    public Scene overview() {
        Group root = new Group();
        Scene scene = new Scene(root, 800, 500, Color.WHITE);
        return scene;
    }

    public Scene invoice() {
        List<Invoice> invoices = new ArrayList<>(); // TODO: Fill with actal data.
        invoices.add(
            new Invoice(new Date(System.currentTimeMillis() + 24 * 3600), BigDecimal.valueOf(769.43),
                "17535.83.78287"));
        invoices.add(
            new Invoice(new Date(System.currentTimeMillis() + 24 * 3600), BigDecimal.valueOf(769.43),
                "17535.83.78287"));
        invoices.add(
            new Invoice(new Date(System.currentTimeMillis() + 24 * 3600), BigDecimal.valueOf(769.43),
                "17535.83.78287"));

        Text amount_t = newText("Amount (kr):", 17, false, 11, 345 - 186);
        Text accountNum_t = newText("Account number:", 17, false, 11, 376 - 186);
        Text cidComment_t = newText("CID / Comment:", 17, false, 11, 407 - 186);
        Text dueDate_t = newText("Due date:", 17, false, 11, 438 - 186);
        TextField amount_tf = newTextField("Amount (kr)", 198, 322 - 186, "black", "white", 206, 20,
            14);
        TextField accountNum_tf = newTextField("Account number", 198, 353 - 186, "black", "white", 206,
            20, 14);
        TextField cidComment_tf = newTextField("CID / Comment", 198, 384 - 186, "black", "white", 206,
            20, 14);

        DatePicker due_dp = new DatePicker();
        due_dp.setLayoutX(527 - 329);
        due_dp.setLayoutY(422 - 186);

        ListView<Invoice> invoices_lv = new ListView<>();
        invoices_lv.setLayoutX(782 - 329);
        invoices_lv.setLayoutY(326 - 186);
        invoices_lv.getItems().setAll(invoices);
        invoices_lv.setEditable(false);

        Button clear_bt = newButton("Clear", 358 - 329, 570 - 186, "white", "grey", 159, 61, 16);
        Button register_bt = newButton("Register", 549 - 329, 570 - 186, "white", "grey", 159, 61, 16);
        Button back_bt = newButton("Back", 358 - 329, 637 - 186, "white", "grey", 159, 35, 16);
        Button payNow_bt = newButton("Pay now", 817 - 329, 493 - 186, "white", "grey", 159, 61, 16);
        Button delete_bt = newButton("Delete", 817 - 329, 570 - 186, "white", "grey", 159, 61, 16);

        clear_bt.setOnAction(e -> {
            amount_tf.clear();
            accountNum_tf.clear();
            cidComment_tf.clear();
        });
        register_bt.setOnAction(e -> {
            if (amount_tf.getText().isBlank()) {
                alertBox("Missing input", "Missing information",
                    "Please enter the financial amount in the new invoice first.");
                return;
            }

            if (accountNum_tf.getText().isBlank()) {
                alertBox("Missing input", "Missing information",
                    "Please enter the account number this invoice .");
                return;
            }

            BigDecimal amount = BigDecimalValidator.getInstance().validate(amount_tf.getText());
            if (amount == null) {
                alertBox("Invalid input", "Invalid amount",
                    "Could not understand the amount specified. Make sure to only enter numbers and use the period-sign instead of comma.");
                return;
            }

            // TODO: Check validity of account number.

            // TODO: Reconsider use of Date class.
            LocalDate due = due_dp.getValue();
            Invoice newInvoice = new Invoice(
                new Date(due.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()), amount,
                accountNum_tf.getText());
            invoices.add(newInvoice);

            clear_bt.getOnAction().handle(null);
            throw new RuntimeException("Action not connected to backend.");
        });
        back_bt.setOnAction(e -> {
            stage.setScene(overview());
        });
        payNow_bt.setOnAction(e -> {
            invoices.remove(invoices_lv.getSelectionModel().getSelectedItem());
            throw new RuntimeException("Action not connected to backend.");
        });
        delete_bt.setOnAction(e -> {
            invoices.remove(invoices_lv.getSelectionModel().getSelectedItem());
            throw new RuntimeException("Action not connected to backend.");
        });

        Group root = new Group(
            amount_t, accountNum_t, cidComment_t, dueDate_t,
            amount_tf, accountNum_tf, cidComment_tf,
            invoices_lv,
            due_dp,
            clear_bt, register_bt, back_bt, payNow_bt, delete_bt);
        return new Scene(root, 728, 567, Color.WHITE);
    }

    public Scene budgeting() {
        TableView<ObservableList<Object>> revenues = newTableView(new String[]{"Revenues", "Amount"},
            20, 200, 400, 600);
        ObservableList<ObservableList<Object>> revenuesData = initializeRevenuesData();
        revenues.setItems(revenuesData);

        TableView<ObservableList<Object>> expenditure = newTableView(
            new String[]{"Expenditures", "Budget"}, 20, 900, 400, 600);
        ObservableList<ObservableList<Object>> expenditureData = initializeExpenditureData();
        revenues.setItems(expenditureData);

        Button CreateNewBudget = newButton("Create new budget", 240, 195, "black", "white", 100, 20,
            15);
        Button budgetSuggestions = newButton("Budget suggestion", 400, 400, "black", "white", 100, 20,
            15);

        CreateNewBudget.setOnAction(e -> {
            stage.setScene(createNewBudgetScene());
        });
        budgetSuggestions.setOnAction(e -> {
            stage.setScene(budgetSuggestionsScene());
        });

        Group root = new Group(revenues, expenditure, CreateNewBudget, budgetSuggestions);
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

    public Scene createNewBudgetScene() {

        String[] revenues = {"Revenues", "Salary", "Income"};
        ChoiceBox<String> revenue = newChoiceBox(revenues, "black", "white", 250, 20, 15, 125, 70);
        TextField revenueInput = newTextField("", 120, 195, "black", "white", 100, 20, 15);

        String[] expenditures = {"Expenditure", "Food", "Clothes", "Other"};
        ChoiceBox<String> expenditure = newChoiceBox(expenditures, "black", "white", 250, 20, 15, 125,
            70);
        TextField expenditureInput = newTextField("", 400, 195, "black", "white", 100, 20, 15);

        String[] personals = {"Personal", "Age", "Gender", "Household"};
        ChoiceBox<String> personal = newChoiceBox(personals, "black", "white", 250, 20, 15, 125, 70);
        TextField personalInput = newTextField("", 120, 600, "black", "white", 100, 20, 15);
        Group root = new Group(revenue, revenueInput, expenditure, expenditureInput, personal,
            personalInput);
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

    public Scene budgetSuggestionsScene() {
        // TODO: create a scene for budgetSuggestions
        return null;
    }

    public Scene payment() {
        int x = 300;
        int y = 35;
        int n = 85;
        int m = n - 5;

        TextField fromAccount = newTextField("12345678910", x, y, "black", "white", 200, 20, 15);
        TextField amount = newTextField("50kr", x, y + n, "black", "white", 100, 20, 15);
        TextField dueDate = newTextField("dd.mm.yy", x, y + 2 * n, "black", "white", 100, 20, 15);
        TextField toAccount = newTextField("10987654321", x, y + 3 * n, "black", "white", 100, 20, 15);
        TextField cid = newTextField("0123456789", x, y + 4 * n, "black", "white", 100, 20, 15);

        Button pay = newButton("Pay", 200, 450, "black", "white", 100, 20, 15);
        Button cancel = newButton("Cancel", 350, 450, "black", "white", 100, 20, 15);
        pay.setOnAction(e -> {
            stage.setScene(paymentConfirmation());
        });
        cancel.setOnAction(e -> {
            stage.setScene(overview());
        });
        Group root = new Group(fromAccount, amount, dueDate, toAccount, cid,
            newText("From account", 30, false, x, y - 5),
            newText("Amount:", 30, false, x, y + m),
            newText("Due date:", 30, false, x, y + 2 * m),
            newText("To account:", 30, false, x, y + 3 * m),
            newText("CID:", 30, false, x, y + 4 * m),
            pay, cancel);
        Scene scene = new Scene(root, 800, 500, Color.WHITE);
        return scene;
    }

    public Scene paymentConfirmation() {
        TableView<ObservableList<Object>> payment = newTableView(new String[]{"Payment", "Information"}, 100, 150, 602, 150);
        ObservableList<ObservableList<Object>> paymentData = initializePaymentData();
        payment.setItems(paymentData);
        payment.getColumns().forEach(colum -> colum.setMinWidth(300));
        payment.getColumns().forEach(column -> column.setSortable(false));


        Button confirm = newButton("Confirm", 250, 350, "black", "white", 100, 20, 15);
        Button cancel = newButton("Cancel", 400, 350, "black", "white", 100, 20, 15);


        confirm.setOnAction(e -> {
            stage.setScene(overview());
        });
        cancel.setOnAction(e -> {
            stage.setScene(payment());
        });


        Group root = new Group(payment, confirm, cancel);
        Scene scene = new Scene(root, 800, 500, Color.WHITE);
        return scene;
    }



    public Scene transfer() {
        int x = 300;
        int y = 35;
        int n = 85;
        int m = n - 5;

        TextField fromAccount = newTextField("12345678910", x, y, "black", "white", 200, 20, 15);
        TextField amount = newTextField("50kr", x, y + n, "black", "white", 100, 20, 15);
        TextField toAccount = newTextField("10987654321", x, y + 2 * n, "black", "white", 100, 20, 15);

        Button transfer = newButton("Pay", 200, 255, "black", "white", 100, 20, 15);
        Button cancel = newButton("Cancel", 350, 255, "black", "white", 100, 20, 15);
        transfer.setOnAction(e -> {
            stage.setScene(overview());
        });
        cancel.setOnAction(e -> {
            stage.setScene(overview());
        });
        Group root = new Group(fromAccount, amount, toAccount,
            newText("From account", 30, false, x, y - 5),
            newText("Amount:", 30, false, x, y + m),
            newText("To account:", 30, false, x, y + 2 * m),
            transfer, cancel);
        Scene scene = new Scene(root, 800, 500, Color.WHITE);
        return scene;
    }

    public ObservableList<ObservableList<Object>> initializeRevenuesData() {
        ObservableList<ObservableList<Object>> revenuesData = FXCollections.observableArrayList();
        revenuesData.add(FXCollections.observableArrayList("Sales", new BigDecimal("1000.00")));
        revenuesData.add(FXCollections.observableArrayList("Rent", new BigDecimal("500.00")));
        revenuesData.add(FXCollections.observableArrayList("Salaries", new BigDecimal("2000.00")));
        return revenuesData;
    }

    public ObservableList<ObservableList<Object>> initializeExpenditureData() {
        ObservableList<ObservableList<Object>> expenditureData = FXCollections.observableArrayList();
        expenditureData.add(
            FXCollections.observableArrayList("Food and Drink", new BigDecimal("200.00")));
        expenditureData.add(FXCollections.observableArrayList("Travel", new BigDecimal("300.00")));
        expenditureData.add(FXCollections.observableArrayList("Other", new BigDecimal("2000.00")));
        return expenditureData;
    }

    public ObservableList<ObservableList<Object>> initializePaymentData() {
        ObservableList<ObservableList<Object>> paymentData = FXCollections.observableArrayList();
        paymentData.add(FXCollections.observableArrayList("From account:", new BigDecimal("12345678910")));
        paymentData.add(FXCollections.observableArrayList("Amount:", new BigDecimal("50.00") + "kr"));
        paymentData.add(FXCollections.observableArrayList("Due date:", "01.01.2019"));
        paymentData.add(FXCollections.observableArrayList("To account:", new BigDecimal("10987654321")));
        paymentData.add(FXCollections.observableArrayList("CID:", new BigDecimal("0123456789")));
        return paymentData;
    }

    public Button newButton(String text, int x, int y, String borderColor,
        String backgroundColor, int width, int height, int fontSize) {
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

    public TextField newTextField(String promptText, int x, int y, String borderColor,
        String backgroundColor, int width, int height, int fontSize) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setLayoutX(x);
        textField.setLayoutY(y);
        textField.setStyle(setStyleString(borderColor, backgroundColor, width, height, fontSize));
        return textField;
    }

    public Text newText(String title, int size, boolean underline, int x, int y) {
        Text text = new Text(title);
        text.setFont(Font.font("Times New Roman", FontWeight.BOLD, size));
        text.setUnderline(underline);
        text.setX(x);
        text.setY(y);
        return text;
    }

    public TableView<ObservableList<Object>> newTableView(String[] columnTitles, double x, double y,
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
    
    public Rectangle newRectangle(int x, int y, int width, int height){
        Rectangle rectangle = new Rectangle();
        rectangle.setX(x);
        rectangle.setY(y);
        rectangle.setWidth(width);
        rectangle.setHeight(height);
        rectangle.setStroke(Color.BLACK);
        rectangle.setFill(Color.WHITE);
        return rectangle;
    }

    public ChoiceBox<String> newChoiceBox(String[] choices, String borderColor,
        String backgroundColor, int width, int height, int fontSize, int x, int y) {
        ChoiceBox<String> choiceBox = new ChoiceBox();
        choiceBox.getItems().addAll(choices);
        choiceBox.setStyle(setStyleString(borderColor, backgroundColor, width, height, fontSize));
        choiceBox.setLayoutX(x);
        choiceBox.setLayoutY(y);
        return choiceBox;
    }

    public void alertBox(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public String setStyleString(String borderColor,
        String backgroundColor, int width, int height, int fontSize) {
        return "-fx-border-color: " + borderColor + ";" +
            "-fx-background-color: " + backgroundColor + ";" +
            "-fx-pref-width: " + width + ";" +
            "-fx-pref-height: " + height + ";" +
            "-fx-font-size: " + fontSize + "px;";
    }

    public static void main(String[] args) {
        launch();
    }
}
