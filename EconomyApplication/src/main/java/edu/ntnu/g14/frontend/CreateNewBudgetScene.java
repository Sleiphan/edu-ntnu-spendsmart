package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CreateNewBudgetScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene() throws FileNotFoundException {

        String[] revenues = {"Revenues", "Salary", "Income"};
        ChoiceBox<String> revenue = ApplicationObjects.newChoiceBox(revenues, "black", "white", 150, 34, 15, 50, 100);
        revenue.setValue("Revenues");
        TextField revenueInput = ApplicationObjects.newTextField("", 210, 100, "black", "white", 130, 30, 15);


        String[] expenditures = {"Expenditure", "Food", "Clothes", "Other"};
        ChoiceBox<String> expenditure = ApplicationObjects.newChoiceBox(expenditures, "black", "white", 150, 34, 15, 400, 100);
        expenditure.setValue("Expenditure");
        TextField expenditureInput = ApplicationObjects.newTextField("", 560, 100, "black", "white", 130, 30, 15);


        String[] personals = {"Personal", "Age", "Gender", "Household"};
        ChoiceBox<String> personal = ApplicationObjects.newChoiceBox(personals, "black", "white", 150, 34, 15, 50, 300);
        personal.setValue("Personal");
        TextField personalInput = ApplicationObjects.newTextField("", 210, 300, "black", "white", 130, 30, 15);

        Button cancelBtn= ApplicationObjects.newButton("Cancel", 200, 480, "black", "white", 157, 25, 16);
        cancelBtn.setOnAction(e -> {
            try {
                stage.setScene(EmptyBudgetScene.scene());
            } catch (FileNotFoundException e1) {
                
                e1.printStackTrace();
            }
        });

        Button createBtn= ApplicationObjects.newButton("Create", 380, 480, "black", "white", 157, 25, 16);
        createBtn.setOnAction(e -> {
            try {
                stage.setScene(BudgetingScene.scene());
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
        Group root = new Group(revenue, revenueInput, expenditure, expenditureInput,
        personal, personalInput, cancelBtn, createBtn, dropDownButton, homeButton, manageUserButton);
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
