package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;

import edu.ntnu.g14.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CreateNewBudgetScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene() throws FileNotFoundException {
        User loggedInUser = ApplicationFront.getLoggedInUser();
        String[] revenues = {"Revenues", "Salary", "Income"};
        ChoiceBox<String> revenue = ApplicationObjects.newChoiceBox(revenues, "black", "white", 150, 34, 15, 50, 100);
        revenue.setValue("Revenues");
        TextField revenueInput = ApplicationObjects.newTextField("", 210, 100, "black", "white", 130, 30, 15);
        revenueInput.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String selectedItem = revenue.getValue();
                if (!selectedItem.equals("Expenditure")) {
                    String inputText = revenueInput.getText();
                    try {
                        double inputAmount = Double.parseDouble(inputText);

                        // Create the BudgetItem object
                        RevenueCategory selectedCategory = RevenueCategory.valueOf(selectedItem.replace(' ', '_')); //TODO: add RevenuCategory enum
                        BudgetItem budgetItem = new BudgetItem(selectedCategory, inputAmount); //TODO: need new budgetItem constructor

                        // Process the created BudgetItem object as needed
                        loggedInUser.getBudget().addBudgetItem(budgetItem);

                        // Clear the TextField after processing
                        revenueInput.clear();
                    } catch (NumberFormatException e) {
                        // Handle invalid input, e.g., show an error message or a dialog
                    }
                }
            }
        });

        ChoiceBox<String> expenditure = new ChoiceBox<>();
        expenditure.setStyle("-fx-base: white; -fx-font-size: 15px;");
        expenditure.setLayoutX(400);
        expenditure.setLayoutY(100);
        expenditure.setPrefWidth(150);
        expenditure.setPrefHeight(34);
        expenditure.getItems().add("Expenditure");

        for (ExpenditureCategory category : ExpenditureCategory.values()) {
            expenditure.getItems().add(category.name().replace('_', ' '));
        }
        expenditure.setValue("Expenditure");

        TextField expenditureInput = ApplicationObjects.newTextField("", 560, 100, "black", "white", 130, 30, 15);

// Event handler for TextField
        expenditureInput.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String selectedItem = expenditure.getValue();
                if (!selectedItem.equals("Expenditure")) {
                    String inputText = expenditureInput.getText();
                    try {
                        double inputAmount = Double.parseDouble(inputText);

                        // Create the BudgetItem object
                        ExpenditureCategory selectedCategory = ExpenditureCategory.valueOf(selectedItem.replace(' ', '_'));
                        BudgetItem budgetItem = new BudgetItem(selectedCategory, inputAmount); //TODO: need new budgetItem constructor

                        // Process the created BudgetItem object as needed
                        loggedInUser.getBudget().addBudgetItem(budgetItem);

                        // Clear the TextField after processing
                        expenditureInput.clear();
                    } catch (NumberFormatException e) {
                        // Handle invalid input, e.g., show an error message or a dialog
                    }
                }
            }
        });


        String[] personals = {"Personal", "Age", "Gender", "Household"};
        ChoiceBox<String> personal = ApplicationObjects.newChoiceBox(personals, "black", "white", 150, 34, 15, 50, 300);
        personal.setValue("Personal");
        TextField personalInput = ApplicationObjects.newTextField("", 210, 300, "black", "white", 130, 30, 15);

// Event handler for TextField
        personalInput.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String selectedItem = personal.getValue();
                if (!selectedItem.equals("Personal")) {
                    try {
                        String inputText = personalInput.getText();

                        // Create the BudgetItem object
                        BudgetItem budgetItem = null;
                        switch (selectedItem) {
                            case "Age":
                                int inputAge = Integer.parseInt(inputText);
                                budgetItem = new BudgetItem("Age", inputAge);
                                break;
                            case "Gender":
                                budgetItem = new BudgetItem("Gender", inputText);
                                break;
                            case "Household":
                                budgetItem = new BudgetItem("Household", inputText);
                                break;
                        }

                        if (budgetItem != null) {
                            // Process the created BudgetItem object as needed
                            // loggedInUser.getBudget().addBudgetItem(budgetItem);
                        }

                        // Clear the TextField after processing
                        personalInput.clear();
                    } catch (NumberFormatException e) {
                        // Handle invalid input, e.g., show an error message or a dialog
                    }
                }
            }
        });


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
