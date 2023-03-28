package edu.ntnu.g14.frontend;

import java.math.BigDecimal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BudgetingScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene() {
        TableView<ObservableList<Object>> revenues = ApplicationObjects.newTableView(new String[]{"Revenues", "Amount"}, 80, 160, 660, 150);
        ObservableList<ObservableList<Object>> revenuesData = initializeRevenuesData();
        revenues.setItems(revenuesData);
        revenues.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        revenues.setFixedCellSize(50);

        TableView<ObservableList<Object>> expenditures = ApplicationObjects.newTableView(new String[]{"Expenditures", "Budget"}, 80, 320, 660, 310);
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

        Text MonthlyBudget = ApplicationObjects.newText("Monthly Budget", 30, false, 70, 130 );
        MonthlyBudget.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Text savings = ApplicationObjects.newText("Savings: 3000", 30, false, 70, 670 );
        savings.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");



        Button createNewBudget = ApplicationObjects.newButton("Create new budget", 850, 300, "black", "white", 380, 80, 15);
        Button budgetSuggestions = ApplicationObjects.newButton("Budget suggestion", 850, 500, "black", "white", 380, 80, 15);

        createNewBudget.setOnAction(e -> {
            stage.setScene(CreateNewBudgetScene.scene());
        });
        budgetSuggestions.setOnAction(e -> {
            stage.setScene(BudgetSuggestionsScene.scene());
        });

        Group root = new Group(backgroundLabel, revenues, expenditures,createNewBudget,
                budgetSuggestions, MonthlyBudget, savings);
        Scene scene = new Scene(root, 1920, 1080, Color.WHITE);
        return scene;
    }


    public static ObservableList<ObservableList<Object>> initializeRevenuesData() {
        ObservableList<ObservableList<Object>> revenuesData = FXCollections.observableArrayList();
        revenuesData.add(FXCollections.observableArrayList("Sales", new BigDecimal("1000.00")));
        revenuesData.add(FXCollections.observableArrayList("Rent", new BigDecimal("500.00")));
        revenuesData.add(FXCollections.observableArrayList("Salaries", new BigDecimal("2000.00")));
        return revenuesData;
    }

    public static ObservableList<ObservableList<Object>> initializeExpenditureData() {
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
  

}
