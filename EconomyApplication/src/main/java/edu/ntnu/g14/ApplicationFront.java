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
import java.util.Optional;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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
        stage.setScene(UserManagement());
        stage.show();
    }

    //TODO: REMOVE dropdown and add to all other scenes
    public Scene loginChooseUser() {
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
    public Group dropDownMenu() {
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
            stage.setScene(mainPage());
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
        TableView<ObservableList<Object>> revenues = newTableView(new String[]{"Revenues", "Amount"}, 80, 160, 660, 150);
        ObservableList<ObservableList<Object>> revenuesData = initializeRevenuesData();
        revenues.setItems(revenuesData);
        revenues.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        revenues.setFixedCellSize(50);

        TableView<ObservableList<Object>> expenditures = newTableView(new String[]{"Expenditures", "Budget"}, 80, 320, 660, 310);
        ObservableList<ObservableList<Object>> expenditureData = initializeExpenditureData();
        expenditures.setItems(expenditureData);
        expenditures.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        expenditures.setFixedCellSize(50);

        Label backgroundLabel = new Label();
        backgroundLabel.setPrefWidth(700);
        backgroundLabel.setPrefHeight(600);
        backgroundLabel.setLayoutX(60);
        backgroundLabel.setLayoutY(100);
        backgroundLabel.setStyle("-fx-background-color: #f0f0f0;");

        Text MonthlyBudget = newText("Monthly Budget", 30, false, 70, 130 );
        MonthlyBudget.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Text savings = newText("Savings: 3000", 30, false, 70, 670 );
        savings.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");



        Button createNewBudget = newButton("Create new budget", 850, 300, "black", "white", 380, 80, 15);
        Button budgetSuggestions = newButton("Budget suggestion", 850, 500, "black", "white", 380, 80, 15);

        createNewBudget.setOnAction(e -> {
            stage.setScene(createNewBudgetScene());
        });
        budgetSuggestions.setOnAction(e -> {
            stage.setScene(budgetSuggestionsScene());
        });

        Group root = new Group(backgroundLabel, revenues, expenditures,createNewBudget,
                budgetSuggestions, MonthlyBudget, savings);
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

    public Scene emptyBudgetScene() {
        TableView<ObservableList<Object>> revenues = newTableView(new String[]{"Revenues", "Amount"}, 80, 160, 660, 150);
        revenues.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        revenues.setFixedCellSize(50);

        TableView<ObservableList<Object>> expenditures = newTableView(new String[]{"Expenditures", "Budget"}, 80, 320, 660, 310);
        expenditures.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        expenditures.setFixedCellSize(50);

        Label backgroundLabel = new Label();
        backgroundLabel.setPrefWidth(700);
        backgroundLabel.setPrefHeight(600);
        backgroundLabel.setLayoutX(60);
        backgroundLabel.setLayoutY(100);
        backgroundLabel.setStyle("-fx-background-color: #f0f0f0;");

        Text MonthlyBudget = newText("Monthly Budget", 30, false, 70, 130 );
        MonthlyBudget.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Text savings = newText("Savings: 3000", 30, false, 70, 670 );
        savings.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");



        Button createNewBudget = newButton("Create new budget", 850, 300, "black", "white", 380, 80, 15);
        Button budgetSuggestions = newButton("Budget suggestion", 850, 500, "black", "white", 380, 80, 15);

        createNewBudget.setOnAction(e -> {
            stage.setScene(createNewBudgetScene());
        });
        budgetSuggestions.setOnAction(e -> {
            stage.setScene(budgetSuggestionsScene());
        });

        Group root = new Group(backgroundLabel, revenues, expenditures,createNewBudget,
                budgetSuggestions, MonthlyBudget, savings);
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

    public Scene createNewBudgetScene() {

        String[] revenues = {"Revenues", "Salary", "Income"};
        ChoiceBox<String> revenue = newChoiceBox(revenues, "black", "white", 200, 50, 15, 150, 200);
        revenue.setValue("Revenues");
        TextField revenueInput = newTextField("", 360, 205, "black", "white", 150, 40, 15);

        String[] expenditures = {"Expenditure", "Food", "Clothes", "Other"};
        ChoiceBox<String> expenditure = newChoiceBox(expenditures, "black", "white", 200, 50, 15, 1000, 200);
        expenditure.setValue("Expenditure");
        TextField expenditureInput = newTextField("", 1210,205 , "black", "white", 150, 40, 15);

        String[] personals = {"Personal", "Age", "Gender", "Household"};
        ChoiceBox<String> personal = newChoiceBox(personals, "black", "white", 200, 50, 15, 150, 550);
        personal.setValue("Personal");
        TextField personalInput = newTextField("", 360, 555, "black", "white", 150, 40, 15);

        Button cancelBtn= newButton("Cancel", 800, 690, "black", "white", 80, 30, 18);
        cancelBtn.setOnAction(e -> stage.setScene(emptyBudgetScene()));

        Button createBtn= newButton("Create", 650, 690, "black", "white", 80, 30, 18);
        createBtn.setOnAction(e -> stage.setScene(budgeting()));

        Group root = new Group(revenue, revenueInput, expenditure, expenditureInput,
                personal, personalInput, cancelBtn, createBtn);
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
            stage.setScene(mainPage());
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
            stage.setScene(mainPage());
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
            stage.setScene(mainPage());
        });
        cancel.setOnAction(e -> {
            stage.setScene(mainPage());
        });
        Group root = new Group(fromAccount, amount, toAccount,
            newText("From account", 30, false, x, y - 5),
            newText("Amount:", 30, false, x, y + m),
            newText("To account:", 30, false, x, y + 2 * m),
            transfer, cancel);
        Scene scene = new Scene(root, 800, 500, Color.WHITE);
        return scene;
    }
    
    public Scene mainPage() {
        String [] columnTitlesLatestActivitiesTable = {"Transaction", "Amount"};
        String [] columnTitlesDuePaymentsTable = {"Date", "Recipient", "Amount"};
        String [] accountsList = {"Savings", "Spending", "Pension"};
        Text actionsText = newText("Actions", 20, false, 160, 30);
        Button transfer = newButton("Transfer", 30, 50, "white", "grey", 157, 25, 16);
        transfer.setOnMouseClicked(e -> stage.setScene(transfer()));
        Button invoice = newButton("Invoice", 192, 50,"white", "grey", 157,25,16);
        invoice.setOnMouseClicked(e -> stage.setScene(invoice()));
        Button payment = newButton("Payment", 30, 90, "white", "grey", 157, 25, 16);
        payment.setOnMouseClicked(e -> stage.setScene(payment()));
        Button overview = newButton("Overview", 192,90, "white", "grey", 157,25,16);
        overview.setOnMouseClicked(e -> stage.setScene(generalOverview()));
        Button accounts = newButton("Accounts", 30,130, "white", "grey", 157, 25, 16);
        accounts.setOnMouseClicked(e -> stage.setScene(accountOverview()));
        Button budgeting = newButton("Budgeting", 192, 130, "white", "grey", 157, 25,16);
        budgeting.setOnMouseClicked(e -> stage.setScene(budgeting()));
        Text latestActivitiesText = newText("Latest Activities", 20, false,130, 210);
        TableView latestActivitiesTable = newTableView(columnTitlesLatestActivitiesTable, 30, 230, 324, 300);
        ObservableList<ObservableList<Object>> latestActivitiesData = initializeLatestActivitiesData();
        latestActivitiesTable.setItems(latestActivitiesData);
        latestActivitiesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        Text duePaymentsText = newText("Due Payments", 20, false, 473, 210);
        TableView duePaymentsTable = newTableView(columnTitlesDuePaymentsTable, 728-30-324, 230, 324, 300);
        duePaymentsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        Text accountsText = newText("Accounts", 20, false, 500, 30);
        ListView accountsListView = newListView(accountsList, 728-30-324, 50, 324, 115);
        Group root = new Group(actionsText, transfer, invoice, payment, overview, accounts, budgeting, latestActivitiesText, latestActivitiesTable, duePaymentsTable, duePaymentsText, accountsListView, accountsText);
        return new Scene(root, 728, 567, Color.WHITE);
    }

    public Scene generalOverview() {

        String [] columnTitlesTransactionsTable = {"Date", "Transaction", "Amount", "Account"};
        Text totalOfAllAccountsCombinedText = newText("Total of all Accounts Combined (excl. Pension)", 16, false, 200, 30);
        Text bigSumText = newText("736 000 kr", 35, false,280, 90);
        Text totalIncomeText = newText("Income all accounts: 19 929 kr", 20, false, 40, 150);
        Text totalExpensesText = newText("Expenses all account: 10 320 kr", 20, false, (728/2 + 40), 150);
        ToggleGroup intervalToggles = new ToggleGroup();

        ToggleButton yearlyToggle = newToggleButton("Yearly", 40, 190, "white", "grey", 80,20, 16);
        yearlyToggle.setToggleGroup(intervalToggles);
        ToggleButton monthlyToggle = newToggleButton("Monthly", 120, 190, "white", "grey", 80,20, 16);
        monthlyToggle.setToggleGroup(intervalToggles);
        TableView<ObservableList<Object>> transactionsTables = newTableView(columnTitlesTransactionsTable, 40, 230, 658, 300);
        Group root = new Group(totalOfAllAccountsCombinedText, bigSumText, totalIncomeText, totalExpensesText, yearlyToggle, monthlyToggle, transactionsTables);
        return new Scene(root, 728, 567, Color.WHITE);
    }
    
    public Scene accountOverview() {
        //TODO: Choices should get the different accounts a user has. Example: "user.getAccounts().asArray()"
        String [] columnTitlesTransactionsTable = {"Transaction", "Date", "Amount"};
        String [] choices = {"Spendings Account", "Savings Account"};
        ChoiceBox<String> accountChoiceBox = newChoiceBox(choices, "white", "grey", 364, 30, 30, 364-(364/2), 30);

        //TODO: Take the first element of the Array and make it the default label of the choice box when account overview is opened

        Text accountNumberText = newText("9293 11 39239", 14, false, 325, 130);
        Text amountText = newText("Amount: 23 340 kr", 20, false, 290, 160);
        Button addTransaction = newButton("Add Transaction", 20, 30, "white", "grey", 120, 20, 14);
        Button addAccount = newButton("Add Account", 20, 60, "white", "grey", 120, 20, 14);
        Text lastTransactionsText = newText("Last Transactions:", 24, false, 20, 200);
        TableView lastTransactionsTable = newTableView(columnTitlesTransactionsTable, 20, 230, 688, 300);
        ObservableList<ObservableList<Object>> lastTransactionsData = initializeLastTransactionsData();
        lastTransactionsTable.setItems(lastTransactionsData);
        lastTransactionsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        addAccount.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Dialog<Account> accountDialog = new AccountDialog(new Account(AccountCategory.SAVINGS_ACCOUNT, new BigDecimal(2),"9293.11.39233","Big Buckz"));
                Optional<Account> result = accountDialog.showAndWait();
                if (result.isPresent()) {
                    Account account = result.get();
                    System.out.println(Integer.toString(account.getAmount().intValue()) + ""+ account.getAccountNumber() + " " + account.getAccountName());
                    //TODO: Add the account to the users list of accounts
                }
            }
        });
        addTransaction.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Dialog<Transaction> transactionDialog = new TransactionDialog(new Transaction("9293.11.39233", "9293.11.39234", (short) 32121, "Hope it is enough", new Date()));
                Optional<Transaction> result = transactionDialog.showAndWait();
                if (result.isPresent()) {
                    Transaction transaction = result.get();
                    //TODO: ---
                }
            }
        });
        Group root = new Group(accountChoiceBox, accountNumberText, amountText, addTransaction, addAccount, lastTransactionsText, lastTransactionsTable);
        return new Scene(root, 728, 567, Color.WHITE);
    }

    public Scene UserManagement() {
        Text loggedInUser = newText("Walter Banks", 60, false, 600, 150);
        Text loggedInUserEmail = newText("walBa76@gmail.com", 20, false, 685, 200);

        Button password = newButton("Password", 700, 400 ,"white", "grey", 157,60,16);
       // password.setOnAction(event -> stage.setScene(createNewPasswordScene()));

        Button switchUser = newButton("Switch User", 700, 470,"white", "grey", 157,60,16);
        Button logOut = newButton("Log out", 870, 470,"white", "grey", 157,50,16);
        logOut.setVisible(false);

        switchUser.setOnAction(event -> {
            if (switchUser.getText().equals("Switch User")) {
                switchUser.setText("Cancel");
                logOut.setVisible(true);

            } else {
                switchUser.setText("Switch User");
                logOut.setVisible(false);
            }
        });

        logOut.setOnAction(event -> stage.setScene(loginChooseUser()));

        Button edit = newButton("Edit", 700, 520,"white", "grey", 157,60,16);
        Button confirm = newButton("confirm", 870, 520,"white", "grey", 157,50,16);
        confirm.setVisible(false);
        edit.setOnAction(event -> {
            if (edit.getText().equals("Edit")) {
                edit.setText("Cancel");
                confirm.setVisible(true);
                loggedInUser.setText("Edited User");
                loggedInUserEmail.setText("Edited User Email");

            } else {
                edit.setText("Edit");
                confirm.setVisible(false);
                loggedInUser.setText("Walter Banks");
                loggedInUserEmail.setText("walBa76@gmail.com");
            }
        });
        confirm.setOnAction(event -> {loggedInUser.setText("Edited User");
            loggedInUserEmail.setText("Edited User Email"); confirm.setVisible(false);
            edit.setText("Edit");});


        Button delete = newButton("Delete", 700, 570,"white", "grey", 157,60,16);
        delete.setOnAction(event -> stage.setScene(deleteUserScene()));
        Group root = new Group(loggedInUser, loggedInUserEmail,password, switchUser, edit, delete, logOut, confirm);
        return new Scene(root, 500, 300, Color.WHITE);
    }
    
    public Scene deleteUserScene() {
        Text loggedInUser = newText("Walter Banks", 60, false, 600, 150);
        Text loggedInUserEmail = newText("walBa76@gmail.com", 20, false, 685, 200);
        Text warningText = newText("Are you sure you want to delete this user?\n" +
                "_____This action cannot be undone____\n" +
                "___________10 seconds left__________", 20, false, 600, 350);


        Button confirm = newButton("Edit", 700, 520,"white", "grey", 157,60,16);
        confirm.setStyle("-fx-background-color: red");
        confirm.setTextFill(Paint.valueOf("WHITE"));
        confirm.setMinWidth(157);
        confirm.setMinHeight(50);
        confirm.setOnMousePressed(event -> {alertBox("Deleted user", "You have deleted user", "Walter Banks R.I.P");    confirm.setStyle("-fx-background-color: red");
            confirm.setTextFill(Paint.valueOf("WHITE"));
            confirm.setMinWidth(157);
            confirm.setMinHeight(50);});

        Button cancel = newButton("Delete", 700, 570,"white", "grey", 157,60,16);
        cancel.setOnAction(event -> stage.setScene(UserManagement()));

        Group root = new Group(loggedInUser, loggedInUserEmail, cancel, confirm, warningText);
        return new Scene(root, 500, 300, Color.WHITE);
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
        expenditureData.add(FXCollections.observableArrayList("Clothes and Shoes", new BigDecimal("2500.00")));
        expenditureData.add(FXCollections.observableArrayList("Personal Care", new BigDecimal("1000.00")));
        expenditureData.add(FXCollections.observableArrayList("Leisure", new BigDecimal("500.00")));
        expenditureData.add(FXCollections.observableArrayList("Alcohol and tobacco", new BigDecimal("200.00")));
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

    public ObservableList<ObservableList<Object>> initializeLatestActivitiesData() {
        //TODO: Use for loop to loop for the amount and description of the latest activities of a user
        ObservableList<ObservableList<Object>> latestActivitiesData = FXCollections.observableArrayList();
        latestActivitiesData.add(FXCollections.observableArrayList("Elkjøp (Reserved)", new BigDecimal("10000.00")));
        latestActivitiesData.add(FXCollections.observableArrayList("Vinmonopolet", new BigDecimal("5000.00")));
        latestActivitiesData.add(FXCollections.observableArrayList("Kiwi Minipris", new BigDecimal("458.00")));
        latestActivitiesData.add(FXCollections.observableArrayList("Trap Star", new BigDecimal("1350.00")));
        latestActivitiesData.add(FXCollections.observableArrayList("Savings -> Spendings", new BigDecimal("2300.00")));
        latestActivitiesData.add(FXCollections.observableArrayList("Pay Check", new BigDecimal("53202.00")));
        latestActivitiesData.add(FXCollections.observableArrayList("PayPal", new BigDecimal("200.00")));
        return  latestActivitiesData;
    }
    
    public ObservableList<ObservableList<Object>> initializeLastTransactionsData() {
        ObservableList<ObservableList<Object>> lastTransactionsData = FXCollections.observableArrayList();
        lastTransactionsData.add(FXCollections.observableArrayList("Elkjøp (Reserved)", "10.02.2022", new BigDecimal("10000.00")));
        lastTransactionsData.add(FXCollections.observableArrayList("Harampolet", "10.02.2022", new BigDecimal("5000.00")));
        lastTransactionsData.add(FXCollections.observableArrayList("Kiwi Groceries","09.02.2022", new BigDecimal("458.00")));
        lastTransactionsData.add(FXCollections.observableArrayList("Steampowered","03.02.2022", new BigDecimal("900.00")));
        lastTransactionsData.add(FXCollections.observableArrayList("Harambet","29.01.2022", new BigDecimal("1700.00")));
        return lastTransactionsData;
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
    
    public ToggleButton newToggleButton(String text, int x, int y, String borderColor, String backgroundColor, int width, int height, int fontSize) {
        ToggleButton toggleButton = new ToggleButton(text);
        toggleButton.setLayoutX(x);
        toggleButton.setLayoutY(y);
        toggleButton.setStyle(setStyleString(borderColor, backgroundColor, width, height, fontSize));
        toggleButton.setOnMouseEntered(e -> toggleButton.setStyle(setStyleString(borderColor, "grey", width, height, fontSize)));
        toggleButton.setOnMouseExited(e -> toggleButton.setStyle(setStyleString(borderColor, backgroundColor, width, height, fontSize)));
        return toggleButton;
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
    
    public ListView<String> newListView(String[] elements, double x, double y, double width, double height) {
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
    
    public class AccountDialog extends Dialog<Account> {
        private Account account;
        private ChoiceBox<String> accountTypeField;
        private TextField amountField;
        private TextField accountNumberField;
        private TextField accountNameField;

        public AccountDialog(Account account) {
            super();
            this.setTitle("Add Account");
            this.account = account;
            buildUI();
            setPropertyBindings();
            setResultConverter();
        }
        private void setPropertyBindings() {
            // TODO: Implement String Properties to Account class
        }
        private void setResultConverter() {
            javafx.util.Callback<ButtonType, Account> accountResultConverter = new javafx.util.Callback<ButtonType, Account>() {
                @Override
                public Account call(ButtonType param) {
                    if (param == ButtonType.APPLY) {
                        return account;
                    } else {
                        return null;
                    }
                }
            };
            setResultConverter(accountResultConverter);
        }
        private void buildUI() {
            Pane pane = createGridPane();
            getDialogPane().setContent(pane);
            getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);
            Button applyButton = (Button) getDialogPane().lookupButton(ButtonType.APPLY);
            applyButton.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (!validateDialog()) {
                        event.consume();

                    }
                }
                private boolean validateDialog() {
                    if (accountNumberField.getText().isBlank() || amountField.getText().isBlank() || accountNameField.getText().isBlank()) {
                        return false;
                    }
                    return true;
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

            content.getChildren().add(grid);
            return content;

        }
    }
    
    class TransactionDialog extends Dialog<Transaction> {
        Transaction transaction;
        private ChoiceBox<String> fromAccountNumberField;
        private TextField toAccountNumberField;
        private TextField amountField;
        private TextField descriptionField;
        private DatePicker dateOfTransactionField;
        public TransactionDialog(Transaction transaction) {
            super();
            this.setTitle("Add Transaction");
            this.transaction = transaction;
            buildUI();
            setPropertyBindings();
            setResultConverter();
        }
        private void setPropertyBindings() {
            // TODO: Implement String Properties to Account class
        }
        private void setResultConverter() {
            javafx.util.Callback<ButtonType, Transaction> transactionResultConverter = new javafx.util.Callback<ButtonType, Transaction>() {
                @Override
                public Transaction call(ButtonType param) {
                    if (param == ButtonType.APPLY) {
                        return transaction;
                    } else {
                        return null;
                    }
                }
            };
            setResultConverter(transactionResultConverter);
        }
        private void buildUI() {
            Pane pane = createGridPane();
            getDialogPane().setContent(pane);
            getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);
            Button applyButton = (Button) getDialogPane().lookupButton(ButtonType.APPLY);
            applyButton.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (!validateDialog()) {
                        event.consume();

                    }
                }
                private boolean validateDialog() {
                    if (toAccountNumberField.getText().isBlank() || amountField.getText().isBlank()
                            || descriptionField.getText().isBlank()) {
                        return false;
                    }
                    return true;
                }
            });
        }
        public Pane createGridPane() {
            VBox content = new VBox(10);
            Label fromAccountLabel = new Label("Select the account you want to send from:");
            Label toAccountLabel = new Label("Enter the recipient account number:");
            Label amountLabel = new Label("Enter the amount you want to send:");
            Label descriptionLabel = new Label("Enter the description of the transaction:");
            Label dateOfTransactionLabel = new Label("Choose the date of the transaction:")   ;

            this.fromAccountNumberField = new ChoiceBox<>();
            this.toAccountNumberField = new TextField();
            this.amountField = new TextField();
            this.descriptionField = new TextField();
            this.dateOfTransactionField = new DatePicker();
            //TODO: Change format
            this.fromAccountNumberField.getItems().addAll("Spendings Account", "Savings Account", "Pensions Account");
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
    }

    public static void main(String[] args) {
        launch();
    }
}
