package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Map;

import edu.ntnu.g14.Budget;
import edu.ntnu.g14.BudgetItem;
import edu.ntnu.g14.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BudgetingScene {
    static Stage stage = ApplicationFront.getStage();
    private static User loggedInUser = ApplicationFront.getLoggedInUser();

    static public Scene scene() throws FileNotFoundException {
        TableView<ObservableList<Object>> revenues = ApplicationObjects.newTableView(new String[]{"Revenues", "Amount"}, 40, 70, 380, 120);
        ObservableList<ObservableList<Object>> revenuesData = initializeRevenuesData();
        revenues.setItems(revenuesData);
        revenues.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        revenues.setFixedCellSize(50);

        TableView<ObservableList<Object>> expenditures = ApplicationObjects.newTableView(new String[]{"Expenditures", "Budget"}, 40, 210, 380, 250);
        ObservableList<ObservableList<Object>> expenditureData = initializeExpenditureData();
        expenditures.setItems(expenditureData);
        expenditures.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        expenditures.setFixedCellSize(50);

        Label backgroundLabel = new Label();
        backgroundLabel.setPrefWidth(400);
        backgroundLabel.setPrefHeight(500);
        backgroundLabel.setLayoutX(30);
        backgroundLabel.setLayoutY(40);
        backgroundLabel.setStyle("-fx-background-color: #f0f0f0;");

        Text MonthlyBudget = ApplicationObjects.newText("Monthly Budget", 30, false, 40, 60);
        MonthlyBudget.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Text savings = ApplicationObjects.newText(loggedInUser.getBudget().getSavings().toString(), 30, false, 40, 480);
        savings.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Button createNewBudget = ApplicationObjects.newButton("Create new budget", 470, 180, "black", "white", 157, 25, 16);
        Button budgetSuggestions = ApplicationObjects.newButton("Budget suggestion", 470, 270, "black", "white", 157, 25, 16);

        createNewBudget.setOnAction(e -> {
            try {
                stage.setScene(CreateNewBudgetScene.scene());
            } catch (FileNotFoundException e1) {
                
                e1.printStackTrace();
            }
        });
        budgetSuggestions.setOnAction(e -> {
            try {
                stage.setScene(BudgetSuggestionsScene.scene());
            } catch (FileNotFoundException e1) {
                
                e1.printStackTrace();
            }
        });

        

        ImageView homeButton = ApplicationObjects.newImage("home.png", 10, 10, 20, 20);
        homeButton.setOnMouseClicked(e -> {
            try {
                stage.setScene(MainPageScene.scene());
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        Button dropDownButton = ApplicationObjects.newButton("test", 676, 10, "black", "white", 10, 10, 10);
        Group dropDown = ApplicationObjects.dropDownMenu();
        ImageView manageUserButton = ApplicationObjects.newImage("user.png", 646, 10, 20, 20);
        Group root = new Group(backgroundLabel, revenues, expenditures, createNewBudget,
                budgetSuggestions, MonthlyBudget, savings, dropDownButton, homeButton, manageUserButton);
        dropDownButton.setOnAction(e -> {
            root.getChildren().add(dropDown);
        });

        Scene scene = new Scene(root, 728, 567, Color.WHITE);
        
    
        Group userButtons = ApplicationObjects.userMenu();
        manageUserButton.setOnMouseEntered(e -> {
            root.getChildren().add(userButtons);
        });
        scene.setOnMouseClicked(e -> {
            root.getChildren().remove(userButtons);
            root.getChildren().remove(dropDown);
        });
        return scene;
    }

    public static ObservableList<ObservableList<Object>> initializeRevenuesData() {
        ObservableList<ObservableList<Object>> revenuesData = FXCollections.observableArrayList();
        Budget budget = loggedInUser.getBudget();
        // Fetch revenues from budget and add them to revenuesData
        if (budget != null) {
            for (BudgetItem entry : budget.getEntries()) {
                // need to update budgetitem to contain getRevenues category
                revenuesData.add(FXCollections.observableArrayList(entry.getCategory() , entry.getFinancialValue()));
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
                expenditureData.add(FXCollections.observableArrayList(entry.getCategory(), entry.getFinancialValue()));
            }
        }

        return expenditureData;
    }



}
