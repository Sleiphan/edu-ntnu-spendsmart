package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import edu.ntnu.g14.Budget;
import edu.ntnu.g14.BudgetItem;
import edu.ntnu.g14.User;
import edu.ntnu.g14.dao.BudgetDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BudgetingScene {
    static Stage stage = ApplicationFront.getStage();
    private static User loggedInUser = ApplicationFront.loggedInUser;
    static BudgetDAO budgetDAO;

    static public Scene scene() throws IOException {
        budgetDAO = getBudgetDAO();

        TableView<ObservableList<Object>> revenues = createRevenuesTable();
        TableView<ObservableList<Object>> expenditures = createExpendituresTable();

        Label backgroundLabel = createBackgroundLabel();
        Text MonthlyBudget = createMonthlyBudgetText();
        Text savings = createSavingsText();

        Button createNewBudget = createCreateNewBudgetButton();
        Button budgetSuggestions = createBudgetSuggestionsButton();

        ImageView homeButton = createHomeButton();
        Button dropDownButton = createDropDownButton();
        Group dropDown = ApplicationObjects.dropDownMenu();
        ImageView manageUserButton = createManageUserButton();

        Group root = new Group(backgroundLabel, revenues, expenditures, createNewBudget,
                budgetSuggestions, MonthlyBudget, savings, dropDownButton, homeButton, manageUserButton);
        dropDownButton.setOnAction(e -> root.getChildren().add(dropDown));

        Scene scene = createScene(root);

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

    private static TableView<ObservableList<Object>> createRevenuesTable() throws IOException {
        TableView<ObservableList<Object>> revenues = ApplicationObjects.newTableView(new String[]{"Revenues", "Amount"}, 40, 70, 380, 120);
        revenues.setItems(initializeRevenuesData());
        revenues.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        revenues.setFixedCellSize(50);
        return revenues;
    }

    private static TableView<ObservableList<Object>> createExpendituresTable() {
        TableView<ObservableList<Object>> expenditures = ApplicationObjects.newTableView(new String[]{"Expenditures", "Budget"}, 40, 210, 380, 250);
        expenditures.setItems(initializeExpenditureData());
        expenditures.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        expenditures.setFixedCellSize(50);
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
        Text savings = ApplicationObjects.newText("Savings: " + loggedInUser.getBudget().getSavings(), 30, false, 40, 480);
        savings.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        return savings;
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

    private static Scene createScene(Group root) {
        Scene scene = new Scene(root, 728, 567, ApplicationObjects.getSceneColor());
        scene.setFill(ApplicationObjects.getSceneColor());
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
                    revenuesData.add(FXCollections.observableArrayList(entry.getCategory(), entry.getFinancialValue()));
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
                    expenditureData.add(FXCollections.observableArrayList(entry.getCategory(), entry.getFinancialValue()));
                }
            }
        }
        return expenditureData;
    }

}

