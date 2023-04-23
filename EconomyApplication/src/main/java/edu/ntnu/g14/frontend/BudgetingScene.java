package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ntnu.g14.Budget;
import edu.ntnu.g14.BudgetCategory;
import edu.ntnu.g14.BudgetItem;
import edu.ntnu.g14.User;
import edu.ntnu.g14.dao.BudgetDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class BudgetingScene {
    static Stage stage = ApplicationFront.getStage();
    private static User loggedInUser = ApplicationFront.loggedInUser;
    static BudgetDAO budgetDAO;
    static TableView<ObservableList<Object>> revenues;
    static TableView<ObservableList<Object>> expenditures;

    static ContextMenu contextMenu;

    static ContextMenu revenuesContextMenu;

    static ContextMenu expendituresContextMenu;

    static Button confirmEditButton;

    static Text savings;




    static public Scene scene() throws IOException {
        budgetDAO = getBudgetDAO();

        contextMenu = createContextMenu(null); // Pass null initially
        revenues = createRevenuesTable();
        expenditures = createExpendituresTable();
        revenuesContextMenu = createContextMenu(revenues);
        expendituresContextMenu = createContextMenu(expenditures);

        revenues.setOnContextMenuRequested(event -> revenuesContextMenu.show(revenues, event.getScreenX(), event.getScreenY()));
        expenditures.setOnContextMenuRequested(event -> expendituresContextMenu.show(expenditures, event.getScreenX(), event.getScreenY()));


        Label backgroundLabel = createBackgroundLabel();
        Text MonthlyBudget = createMonthlyBudgetText();
        savings = createSavingsText();
        updateSavings();
        Text infoText = createInfoText();

        Button createNewBudget = createCreateNewBudgetButton();
        Button budgetSuggestions = createBudgetSuggestionsButton();

        ImageView homeButton = createHomeButton();
        Button dropDownButton = createDropDownButton();
        Group dropDown = ApplicationObjects.dropDownMenu();
        ImageView manageUserButton = createManageUserButton();

        confirmEditButton = confirmEditButton();

        Group root = new Group(backgroundLabel, revenues, expenditures, confirmEditButton, createNewBudget,
                budgetSuggestions, MonthlyBudget, savings, infoText, dropDownButton, homeButton, manageUserButton);
        dropDownButton.setOnAction(e -> root.getChildren().add(dropDown));

        Scene scene = createScene(root, event -> {
            if (!revenues.contains(event.getX(), event.getY()) && !expenditures.contains(event.getX(), event.getY())) {
                saveModifiedData();
            }
        });
        scene.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (!revenues.contains(event.getX(), event.getY()) && !expenditures.contains(event.getX(), event.getY())) {
                saveModifiedData();
                revenues.getSelectionModel().clearSelection();
                expenditures.getSelectionModel().clearSelection();
            }
        });



        Group userButtons = ApplicationObjects.userMenu();
        addUserButtonsEventHandler(manageUserButton, root, userButtons);
        scene.setOnMouseClicked(e -> {
            root.getChildren().remove(userButtons);
            root.getChildren().remove(dropDown);
        });

        return scene;
    }

    private static BudgetDAO getBudgetDAO() throws IOException {
        try {
            return new BudgetDAO("saves/budgets.txt");
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static Scene createScene(Group root, EventHandler<MouseEvent> mouseClickHandler) {
        Scene scene = new Scene(root, 728, 567, ApplicationObjects.getSceneColor());
        scene.setFill(ApplicationObjects.getSceneColor());
        scene.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseClickHandler);
        return scene;
    }


    private static void addUserButtonsEventHandler(ImageView manageUserButton, Group root, Group userButtons) {
        manageUserButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            root.getChildren().add(userButtons);
            event.consume();
        });
    }
    public static ObservableList<ObservableList<Object>> initializeRevenuesData() throws IOException {
        ObservableList<ObservableList<Object>> revenuesData = FXCollections.observableArrayList();
        Budget budget = budgetDAO.getBudget(loggedInUser.getLoginInfo().getUserId());
        // Fetch revenues from budget and add them to revenuesData
        if (budget != null) {
            for (BudgetItem entry : budget.getEntries()) {
                // need to update budgetitem to contain getRevenues category
                if (entry.getCategory().getType().equals("r")) {
                    revenuesData.add(FXCollections.observableArrayList(entry.getCategory().
                            toString().toLowerCase().replaceAll("_", " "), entry.getFinancialValue()));
                }
            }
        }

        return revenuesData;
    }

    public static ObservableList<ObservableList<Object>> initializeExpenditureData() {
        ObservableList<ObservableList<Object>> expenditureData = FXCollections.observableArrayList();
        Budget budget = loggedInUser.getBudget();
        // Fetch expenditures from budget and add them to expenditureData
        if (budget != null) {
            for (BudgetItem entry : budget.getEntries()) {
                if (entry.getCategory().getType().equals("e")) {
                    expenditureData.add(FXCollections.observableArrayList(entry.getCategory().
                            toString().toLowerCase().replaceAll("_"," "), entry.getFinancialValue()));
                }
            }
        }
        return expenditureData;
    }

    private static ContextMenu createContextMenu(TableView<ObservableList<Object>> tableView) {
        MenuItem addItem = new MenuItem("Add");
        addItem.setOnAction(e -> addBudgetItem(tableView));

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> deleteBudgetItem(tableView));

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(addItem, deleteItem);
        return contextMenu;
    }


    private static <T> Callback<TableColumn<ObservableList<Object>, T>, TableCell<ObservableList<Object>, T>> createCustomCellFactory(TableView<ObservableList<Object>> tableView) {
        return param -> new TableCell<>() {
            private ChoiceBox<String> choiceBox;
            private TextField textField;

            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem((T) item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    int columnIndex = getTableView().getColumns().indexOf(getTableColumn());

                    if (columnIndex == 0) {
                        if (tableView == expenditures) {
                            List<String> expenditureChoiceList = new ArrayList<>();
                            for (BudgetCategory category : BudgetCategory.values()) {
                                if (category.getType().equals("e")) {
                                    expenditureChoiceList.add(category.toString().toLowerCase().replaceAll("_", " "));
                                }
                            }
                            choiceBox = new ChoiceBox<>(FXCollections.observableArrayList(expenditureChoiceList));
                        } else {
                            List<String> revenueChoiceList = new ArrayList<>();
                            for (BudgetCategory category : BudgetCategory.values()) {
                                if (category.getType().equals("r")) {
                                    revenueChoiceList.add(category.toString().toLowerCase().replaceAll("_", " "));
                                }
                            }
                            choiceBox = new ChoiceBox<>(FXCollections.observableArrayList(revenueChoiceList));
                        }

                        choiceBox.setValue(item.toString());
                        choiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                            int index = getIndex();
                            tableView.getItems().get(index).set(0, newValue);
                            confirmEditButton.setVisible(true);
                        });
                        setGraphic(choiceBox);
                    } else {
                        textField = new TextField(item.toString());
                        textField.setOnAction(e -> {
                            int index = getIndex();
                            tableView.getItems().get(index).set(1, textField.getText());
                            confirmEditButton.setVisible(true);// displays the confirm edit button if you press enter
                        });
                        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                            if (!newValue) {
                                tableView.getItems().get(getIndex()).set(1, textField.getText());
                                confirmEditButton.setVisible(true); // displays confirm button if you press outside off the box
                            }
                        });
                        setGraphic(textField);

                    }
                }
            }
        };
    }
    private static void addBudgetItem(TableView<ObservableList<Object>> tableView) {
        List<String> choiceList = new ArrayList<>();
        String type = tableView == revenues ? "r" : "e";
        for (BudgetCategory category : BudgetCategory.values()) {
            if (category.getType().equals(type)) {
                choiceList.add(category.toString().toLowerCase().replaceAll("_", " "));
            }
        }
        ChoiceBox<String> choiceBox = new ChoiceBox<>(FXCollections.observableArrayList(choiceList));
        choiceBox.setValue(choiceList.get(0));
        double defaultAmount = 0.0;

        ObservableList<Object> newRow = FXCollections.observableArrayList(choiceBox, defaultAmount);
        tableView.getItems().add(newRow);
    }


    private static void deleteBudgetItem(TableView<ObservableList<Object>> tableView) {
        ObservableList<Object> selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            tableView.getItems().remove(selectedItem);
            saveModifiedData(); // Save the changes to the file
        }
    }


    public static void refreshData() {
        try {
            revenues.setItems(initializeRevenuesData());
            expenditures.setItems(initializeExpenditureData());
            updateSavings();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveModifiedData() {
        try {
            Budget budget = loggedInUser.getBudget();
            budget.clearEntries();
            for (ObservableList<Object> row : revenues.getItems()) {
                String categoryString = row.get(0) instanceof ChoiceBox ? ((ChoiceBox<String>) row.get(0)).getValue() : row.get(0).toString();
                BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(row.get(1).toString()));

                BudgetCategory category = BudgetCategory.valueOf(categoryString.toUpperCase().replaceAll(" ", "_"));
                budget.addBudgetItem(new BudgetItem(category, amount));
            }

            for (ObservableList<Object> row : expenditures.getItems()) {
                String categoryString = row.get(0) instanceof ChoiceBox ? ((ChoiceBox<String>) row.get(0)).getValue() : row.get(0).toString();
                BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(row.get(1).toString()));

                BudgetCategory category = BudgetCategory.valueOf(categoryString.toUpperCase().replaceAll(" ", "_"));
                budget.addBudgetItem(new BudgetItem(category, amount));
            }

            budgetDAO.setBudget(loggedInUser.getLoginInfo().getUserId(), budget);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateSavings() {
        BigDecimal revenuesTotal = revenues.getItems().stream()
                .map(row -> new BigDecimal(row.get(1).toString()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal expendituresTotal = expenditures.getItems().stream()
                .map(row -> new BigDecimal(row.get(1).toString()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal savingsValue = revenuesTotal.subtract(expendituresTotal);
        if (loggedInUser.getBudget() != null) {
            loggedInUser.getBudget().setSavings(savingsValue);
            savings.setText("Savings: " + loggedInUser.getBudget().getSavings());
        }
    }

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    private static TableView<ObservableList<Object>> createRevenuesTable() throws IOException {
        revenues = ApplicationObjects.newTableView(new String[]{"Revenues", "Amount"}, 40, 70, 380, 120);
        revenues.setItems(initializeRevenuesData());
        revenues.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        revenues.setFixedCellSize(50);

        revenues.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                revenues.setEditable(true);
            }
        });

        revenues.getColumns().forEach(column -> {
            column.setCellFactory(createCustomCellFactory(revenues));
        });

        return revenues;
    }

    private static TableView<ObservableList<Object>> createExpendituresTable() throws IOException {
        expenditures = ApplicationObjects.newTableView(new String[]{"Expenditures", "Budget"}, 40, 210, 380, 250);
        expenditures.setItems(initializeExpenditureData());
        expenditures.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        expenditures.setFixedCellSize(50);

        expenditures.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                expenditures.setEditable(true);
            }
        });

        expenditures.getColumns().forEach(column -> {
            column.setCellFactory(createCustomCellFactory(expenditures));
        });

        return expenditures;
    }


    private static Label createBackgroundLabel() {
        Label backgroundLabel = new Label();
        backgroundLabel.setPrefWidth(400);
        backgroundLabel.setPrefHeight(500);
        backgroundLabel.setLayoutX(30);
        backgroundLabel.setLayoutY(40);
        backgroundLabel.setStyle("-fx-background-color: #f0f0f0;");
        return backgroundLabel;
    }

    private static Text createMonthlyBudgetText() {
        return ApplicationObjects.newText("Monthly Budget", 30, false, 40, 60);
    }

    private static Text createSavingsText() {
        BigDecimal savingsNum = loggedInUser.getBudget() != null ? loggedInUser.getBudget().getSavings() : BigDecimal.ZERO;
        savings = ApplicationObjects.newText("Savings: " + savingsNum, 30, false, 40, 480);
        savings.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        return savings;
    }
    private static Text createInfoText(){
        return ApplicationObjects.newText("Right click on row, to add or delete budget item", 15, true, 100, 530 );
    }

    private static Button createCreateNewBudgetButton() {
        Button createNewBudget = ApplicationObjects.newButton("Create new budget", 470, 180, 157, 25, 16);
        createNewBudget.setOnAction(e -> {
            try {
                stage.setScene(CreateNewBudgetScene.scene());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        return createNewBudget;
    }

    private static Button createBudgetSuggestionsButton() {
        Button budgetSuggestions = ApplicationObjects.newButton("Budget suggestion", 470, 270, 157, 25, 16);
        budgetSuggestions.setOnAction(e -> {
            try {
                stage.setScene(BudgetSuggestionsScene.scene());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        return budgetSuggestions;
    }
    private static Button confirmEditButton() {
                confirmEditButton = ApplicationObjects.newButton("Confirm Edit", 270, 470, 157, 25, 16);
            confirmEditButton.setVisible(false);
            confirmEditButton.setOnAction(event -> {
            saveModifiedData();
            refreshData();
            confirmEditButton.setVisible(false);
        });
        return confirmEditButton;
    }

    private static ImageView createHomeButton() throws FileNotFoundException {
        ImageView homeButton = ApplicationObjects.newImage("home.png", 10, 10, 20, 20);
        homeButton.setOnMouseClicked(e -> {
            try {
                stage.setScene(MainPageScene.scene());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        return homeButton;
    }

    private static Button createDropDownButton() {
        return ApplicationObjects.newButton("test", 676, 10, 10, 10, 10);
    }

    private static ImageView createManageUserButton() throws FileNotFoundException {
        return ApplicationObjects.newImage("user.png", 646, 10, 20, 20);
    }

}

