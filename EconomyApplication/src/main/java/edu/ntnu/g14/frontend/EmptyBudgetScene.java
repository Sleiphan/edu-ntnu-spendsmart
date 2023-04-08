package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EmptyBudgetScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene() throws FileNotFoundException {
        TableView<ObservableList<Object>> revenues = ApplicationObjects.newTableView(new String[]{"Revenues", "Amount"}, 80, 160, 660, 150);
        revenues.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        revenues.setFixedCellSize(50);

        TableView<ObservableList<Object>> expenditures = ApplicationObjects.newTableView(new String[]{"Expenditures", "Budget"}, 80, 320, 660, 310);
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
        Group root = new Group(backgroundLabel, revenues, expenditures,createNewBudget,
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

}