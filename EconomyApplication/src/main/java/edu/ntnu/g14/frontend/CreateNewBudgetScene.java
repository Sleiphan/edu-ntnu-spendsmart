package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.ntnu.g14.*;
import edu.ntnu.g14.dao.BudgetDAO;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CreateNewBudgetScene {
    static Stage stage = ApplicationFront.getStage();
    static BudgetDAO budgetDAO;

    static Budget userBudget;

    static User loggedInUser = ApplicationFront.loggedInUser;

    static Group revenueComponents;
    static Group expenditureComponents;

    static Button createBudgetBtn;



    static public Scene scene() throws IOException {



        try {
            budgetDAO = new BudgetDAO("saves/budgets.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create components
        Group selectTypeComponents = createSelectTypeComponents();

        revenueComponents = createRevenueComponents();
        expenditureComponents = createExpenditureComponents();

        // Hide revenue, expenditure and createBudgetBtn components initially
        revenueComponents.setVisible(false);
        expenditureComponents.setVisible(false);
        createBudgetBtn.setVisible(false);

        ImageView homeButton = ApplicationObjects.newImage("home.png", 10, 10, 20, 20);
        homeButton.setOnMouseClicked(e -> {
            try {
                stage.setScene(MainPageScene.scene());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        Button dropDownButton = ApplicationObjects.newButton("test", 676, 10, 10, 10, 10);
        Group dropDown = ApplicationObjects.dropDownMenu();
        ImageView manageUserButton = ApplicationObjects.newImage("user.png", 646, 10, 20, 20);
        Group root = new Group(selectTypeComponents, revenueComponents, expenditureComponents, dropDownButton, homeButton, manageUserButton);
        dropDownButton.setOnAction(e -> {
            root.getChildren().add(dropDown);
        });

        Scene scene = new Scene(root, 728, 567, ApplicationObjects.getSceneColor());
        Group userButtons = ApplicationObjects.userMenu();
        manageUserButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            root.getChildren().add(userButtons);
            event.consume();
        });
        scene.setOnMouseClicked(e -> {
            root.getChildren().remove(userButtons);
            root.getChildren().remove(dropDown);
        });
        scene.setFill(ApplicationObjects.getSceneColor());

        return scene;
    }

    private static Group createSelectTypeComponents() {
        // Create components for selecting type, age, gender, and household
        // ... (Add your existing code for creating these components)

        // Add a listener to the personal choice box
        String[] personals = {"Select Type", "Age & Gender", "Household"};
        ChoiceBox<String> personal = ApplicationObjects.newChoiceBox(personals, 150, 34, 15, 50, 100);
        personal.setValue("Select Type");
        ToggleGroup genderToggleGroup = new ToggleGroup();
        RadioButton maleRadioButton = new RadioButton("Male");
        maleRadioButton.setToggleGroup(genderToggleGroup);
        maleRadioButton.setUserData("MALE");
        maleRadioButton.setLayoutX(350);
        maleRadioButton.setLayoutY(100);

        RadioButton femaleRadioButton = new RadioButton("Female");
        femaleRadioButton.setToggleGroup(genderToggleGroup);
        femaleRadioButton.setUserData("FEMALE");
        femaleRadioButton.setLayoutX(350);
        femaleRadioButton.setLayoutY(120);

// Hide gender RadioButtons by default
        maleRadioButton.setVisible(false);
        femaleRadioButton.setVisible(false);

        TextField AgeInput = ApplicationObjects.newTextField("Enter Age", 210, 100, 130, 30, 15);
        // ...

// Replace the TextField with a ChoiceBox for Household input
        List<String> householdChoiceList = new ArrayList<>();
        for (HouseholdCategory category : HouseholdCategory.values()) {
            householdChoiceList.add(category.toString().toLowerCase().replaceAll("_", " "));
        }
        String[] householdChoices = householdChoiceList.toArray(new String[0]);
        ChoiceBox<String> HouseholdInput = ApplicationObjects.newChoiceBox(householdChoices, 150, 34, 15, 210, 100);
        HouseholdInput.setValue(HouseholdCategory.LIVING_ALONE.toString().toLowerCase().replaceAll("_", " "));
        HouseholdInput.setVisible(false);

        AgeInput.setVisible(false);

        // Add a listener to the personal choice box
        personal.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedValue) -> {
            if (selectedValue.equals("Age & Gender")) {
                AgeInput.setVisible(true);
                maleRadioButton.setVisible(true);
                femaleRadioButton.setVisible(true);
                HouseholdInput.setVisible(false);
            } else if (selectedValue.equals("Household")) {
                AgeInput.setVisible(false);
                maleRadioButton.setVisible(false);
                femaleRadioButton.setVisible(false);
                HouseholdInput.setVisible(true);
            } else {
                AgeInput.setVisible(false);
                maleRadioButton.setVisible(false);
                femaleRadioButton.setVisible(false);
                HouseholdInput.setVisible(false);
            }
        });

// When the 'Create' button is clicked
        Button continueBtn = ApplicationObjects.newButton("Continue", 500, 480, 157, 25, 16);
        continueBtn.setOnAction(e -> {
            // Get the input values for Age, Gender, and Household
            String inputAge = AgeInput.getText();
            String inputGender = genderToggleGroup.getSelectedToggle() != null ? genderToggleGroup.getSelectedToggle().getUserData().toString() : "";
            String inputHousehold = HouseholdInput.getValue();

            // Get the selected value from the personal ChoiceBox
            String selectedValue = personal.getValue();

            // Process the input and create the userBudget object
            processSelectTypeInput(selectedValue, inputAge, inputGender, inputHousehold);

            // Show the revenue and expenditure components
            showRevenueComponents();
            showExpenditureComponents();
            continueBtn.setVisible(false);
            createBudgetBtn.setVisible(true);
        });
        createBudgetBtn =  ApplicationObjects.newButton("Create Budget", 500, 480, 157, 25, 16);
        createBudgetBtn.setOnAction(e -> {
            BudgetingScene.refreshData();
            try {
                budgetDAO.setBudget(loggedInUser.getLoginInfo().getUserId(), userBudget);
                // Navigate to the BudgetingScene or another desired page after saving the budget
                stage.setScene(BudgetingScene.scene());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });


        Group selectTypeGroup = new Group(personal, AgeInput, maleRadioButton, femaleRadioButton, HouseholdInput, continueBtn, createBudgetBtn);
        return selectTypeGroup;
    }

    private static Group createRevenueComponents() {
        // Create components for selecting and inputting revenues
        List<String> revenueChoiceList = new ArrayList<>();
        for (BudgetCategory category : BudgetCategory.values()) {
            if (category.getType().equals("r")) {
                revenueChoiceList.add(category.toString().toLowerCase().replaceAll("_", " "));
            }
        }
        String[] revenueChoices = revenueChoiceList.toArray(new String[0]);
        ChoiceBox<String> revenue = ApplicationObjects.newChoiceBox(revenueChoices, 150, 34, 15, 0, 0);
        revenue.setValue(BudgetCategory.SALARY.toString());


        TextField revenueInput = ApplicationObjects.newTextField("Enter Amount", 0, 0, 130, 30, 15);
        Button addRevenueItemBtn = ApplicationObjects.newButton("Add revenue to budget", 0, 0, 157, 25, 16);
        addRevenueItemBtn.setOnAction(e -> {
            BudgetCategory selectedItem = BudgetCategory.valueOf(revenue.getValue().toUpperCase().replaceAll(" ","_"));
            String inputText = revenueInput.getText();
            addRevenueToBudget(selectedItem, inputText, budgetDAO);
        });

        VBox revenueBox = new VBox();
        revenueBox.setLayoutX(50);
        revenueBox.setLayoutY(150);
        revenueBox.setSpacing(10);
        revenueBox.getChildren().addAll(revenue, revenueInput, addRevenueItemBtn);

        Group revenueGroup = new Group(revenueBox);
        return revenueGroup;
    }

    private static Group createExpenditureComponents() {
        // Create components for selecting and inputting expenditures

        List<String> expenditureChoiceList = new ArrayList<>();
        for (BudgetCategory category : BudgetCategory.values()) {
            if (category.getType().equals("e")) {
                expenditureChoiceList.add(category.toString().toLowerCase().replaceAll("_", " "));
            }
        }
        String[] expenditureChoices = expenditureChoiceList.toArray(new String[0]);
        ChoiceBox<String> expenditure = ApplicationObjects.newChoiceBox(expenditureChoices, 150, 34, 15, 0, 0);
        expenditure.setValue(BudgetCategory.FOOD_AND_DRINK.toString().toLowerCase().replaceAll("_", " "));

        TextField expenditureInput = ApplicationObjects.newTextField("Enter Amount", 0, 0, 130, 30, 15);
        Button addExpenditureItemBtn = ApplicationObjects.newButton("Add expenditure to Budget", 0, 0, 157, 25, 16);;
        addExpenditureItemBtn.setOnAction(e -> {
            BudgetCategory selectedItem = BudgetCategory.valueOf(expenditure.getValue().toUpperCase().replaceAll(" ", "_"));
            String inputText = expenditureInput.getText();
            addExpenditureToBudget(selectedItem, inputText, budgetDAO);
        });
        VBox expenditureBox = new VBox();
        expenditureBox.setLayoutX(50);
        expenditureBox.setLayoutY(300);
        expenditureBox.setSpacing(10);
        expenditureBox.getChildren().addAll(expenditure, expenditureInput, addExpenditureItemBtn);

        Group expenditureGroup = new Group(expenditureBox);
        return expenditureGroup;
    }
    private static void addRevenueToBudget(BudgetCategory selectedItem, String inputText, BudgetDAO budgetDAO) {
        BigDecimal amount = new BigDecimal(inputText);

        BudgetItem budgetItem = new BudgetItem(selectedItem, amount);
        userBudget.addBudgetItem(budgetItem);

        // Update the loggedInUser's budget
        loggedInUser.setBudget(userBudget);

        // Get the updated User object after updating the budget
        User updatedUser = loggedInUser;

        // Update the loggedInUser object in the BudgetingScene class
        BudgetingScene.setLoggedInUser(updatedUser);

        // Refresh the tables
        BudgetingScene.refreshData();

        try {
            budgetDAO.setBudget(loggedInUser.getLoginInfo().getUserId(), userBudget);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BudgetingScene.refreshData();
    }

    private static void addExpenditureToBudget(BudgetCategory selectedItem, String inputText, BudgetDAO budgetDAO) {
        BigDecimal amount = new BigDecimal(inputText);

        BudgetItem budgetItem = new BudgetItem(selectedItem, amount);
        userBudget.addBudgetItem(budgetItem);

        // Update the loggedInUser's budget
        loggedInUser.setBudget(userBudget);

        // Get the updated User object after updating the budget
        User updatedUser = loggedInUser;

        // Update the loggedInUser object in the BudgetingScene class
        BudgetingScene.setLoggedInUser(updatedUser);

        // Refresh the tables
        BudgetingScene.refreshData();

        try {
            budgetDAO.setBudget(updatedUser.getLoginInfo().getUserId(), userBudget);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BudgetingScene.refreshData();
    }

    private static void processSelectTypeInput(String selectedValue, String inputAge, String inputGender, String inputHousehold) {
        // Create a new budget object and associate it with the loggedInUser
        if (selectedValue.equals("Age & Gender")) {
            Byte age = Byte.valueOf(inputAge);
            GenderCategory Gender = GenderCategory.valueOf(inputGender.toUpperCase());
            userBudget = new Budget( age, Gender);
        } else if (selectedValue.equals("Household")) {
            // Convert the inputHousehold String to HouseholdCategory enum
            HouseholdCategory householdSize = HouseholdCategory.valueOf(inputHousehold.toUpperCase().replaceAll(" ", "_"));
            userBudget = new Budget(householdSize);
        }
    }
    private static void showRevenueComponents() {
        revenueComponents.setVisible(true);
    }

    private static void showExpenditureComponents() {
        expenditureComponents.setVisible(true);
    }
}

