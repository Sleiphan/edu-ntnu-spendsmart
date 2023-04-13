package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EmptyBudgetScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene() throws FileNotFoundException {
        TableView<ObservableList<Object>> revenues = ApplicationObjects.newTableView(new String[]{"Revenues", "Amount"}, 40, 70, 380, 120);
        revenues.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        revenues.setFixedCellSize(50);

        TableView<ObservableList<Object>> expenditures = ApplicationObjects.newTableView(new String[]{"Expenditures", "Budget"}, 40, 210, 380, 250);
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

        Text savings = ApplicationObjects.newText("Savings: ", 30, false, 40, 480);
        savings.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Button createNewBudget = ApplicationObjects.newButton("Create new budget", 470, 180, 157, 25, 16);
        Button budgetSuggestions = ApplicationObjects.newButton("Budget suggestion", 470, 270, 157, 25, 16);

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
        Button dropDownButton = ApplicationObjects.newButton("test", 676, 10, 10, 10, 10);
        Group dropDown = ApplicationObjects.dropDownMenu();
        ImageView manageUserButton = ApplicationObjects.newImage("user.png", 646, 10, 20, 20);
        Group root = new Group(backgroundLabel, revenues, expenditures, createNewBudget,
                budgetSuggestions, MonthlyBudget, savings, dropDownButton, homeButton, manageUserButton);
        dropDownButton.setOnAction(e -> {
            root.getChildren().add(dropDown);
        });
        root.getStylesheets().add("StyleSheet.css"); 
        Scene scene = new Scene(root, 728, 567, ApplicationObjects.getSceneColor());


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
