package edu.ntnu.g14.frontend;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CreateNewBudgetScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene() {

        String[] revenues = {"Revenues", "Salary", "Income"};
        ChoiceBox<String> revenue = ApplicationObjects.newChoiceBox(revenues, "black", "white", 200, 50, 15, 150, 200);
        revenue.setValue("Revenues");
        TextField revenueInput = ApplicationObjects.newTextField("", 360, 205, "black", "white", 150, 40, 15);

        String[] expenditures = {"Expenditure", "Food", "Clothes", "Other"};
        ChoiceBox<String> expenditure = ApplicationObjects.newChoiceBox(expenditures, "black", "white", 200, 50, 15, 1000, 200);
        expenditure.setValue("Expenditure");
        TextField expenditureInput = ApplicationObjects.newTextField("", 1210,205 , "black", "white", 150, 40, 15);

        String[] personals = {"Personal", "Age", "Gender", "Household"};
        ChoiceBox<String> personal = ApplicationObjects.newChoiceBox(personals, "black", "white", 200, 50, 15, 150, 550);
        personal.setValue("Personal");
        TextField personalInput = ApplicationObjects.newTextField("", 360, 555, "black", "white", 150, 40, 15);

        Button cancelBtn= ApplicationObjects.newButton("Cancel", 800, 690, "black", "white", 80, 30, 18);
        cancelBtn.setOnAction(e -> stage.setScene(EmptyBudgetScene.scene()));

        Button createBtn= ApplicationObjects.newButton("Create", 650, 690, "black", "white", 80, 30, 18);
        createBtn.setOnAction(e -> stage.setScene(BudgetingScene.scene()));

        Group root = new Group(revenue, revenueInput, expenditure, expenditureInput,
                personal, personalInput, cancelBtn, createBtn);
        Scene scene = new Scene(root, 1920, 1080, Color.WHITE);
        return scene;
    }

}
