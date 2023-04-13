package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.io.IOException;

import edu.ntnu.g14.*;
import edu.ntnu.g14.dao.BudgetDAO;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CreateNewBudgetScene {
    static Stage stage = ApplicationFront.getStage();
    static BudgetDAO budgetDAO;

    static Budget userBudget;

    static User loggedInUser = ApplicationFront.getLoggedInUser();

    static Group revenueComponents;
    static Group expenditureComponents;

    static public Scene scene() throws FileNotFoundException {

        try {
            budgetDAO = new BudgetDAO("budgets.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create components
        Group selectTypeComponents = createSelectTypeComponents();
        Group revenueComponents = createRevenueComponents();
        Group expenditureComponents = createExpenditureComponents();

        // Hide revenue and expenditure components initially
        revenueComponents.setVisible(false);
        expenditureComponents.setVisible(false);

        Group root = new Group(selectTypeComponents, revenueComponents, expenditureComponents);
        Scene scene = new Scene(root, 728, 567, Color.WHITE);

        return scene;
    }

    private static Group createSelectTypeComponents() {
        // Create components for selecting type, age, gender, and household
        // ... (Add your existing code for creating these components)

        // Add a listener to the personal choice box
        String[] personals = {"Select Type", "Age & Gender", "Household"};
        Text Age = ApplicationObjects.newText("Age", 20, false, 50, 300);
        ChoiceBox<String> personal = ApplicationObjects.newChoiceBox(personals, "black", "white", 150, 34, 15, 50, 300);
        personal.setValue("Select Type");
        ToggleGroup genderToggleGroup = new ToggleGroup();
        RadioButton maleRadioButton = new RadioButton("Male");
        maleRadioButton.setToggleGroup(genderToggleGroup);
        maleRadioButton.setLayoutX(350);
        maleRadioButton.setLayoutY(300);

        RadioButton femaleRadioButton = new RadioButton("Female");
        femaleRadioButton.setToggleGroup(genderToggleGroup);
        femaleRadioButton.setLayoutX(350);
        femaleRadioButton.setLayoutY(330);

// Hide gender RadioButtons by default
        maleRadioButton.setVisible(false);
        femaleRadioButton.setVisible(false);

        TextField AgeInput = ApplicationObjects.newTextField("Age", 210, 300, "black", "white", 130, 30, 15);
        TextField HouseholdInput = ApplicationObjects.newTextField("Household", 490, 300, "black", "white", 130, 30, 15);
        HouseholdInput.setVisible(false);

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
        Button createBtn = ApplicationObjects.newButton("Create", 380, 480, "black", "white", 157, 25, 16);
        createBtn.setOnAction(e -> {
            // Get the input values for Age, Gender, and Household
            String inputAge = AgeInput.getText();
            String inputGender = genderToggleGroup.getSelectedToggle() != null ? genderToggleGroup.getSelectedToggle().getUserData().toString() : "";
            String inputHousehold = HouseholdInput.getText();

            // Get the selected value from the personal ChoiceBox
            String selectedValue = personal.getValue();

            // Process the input and create the userBudget object
            processSelectTypeInput(selectedValue, inputAge, inputGender, inputHousehold, budgetDAO, loggedInUser);

            // Show the revenue and expenditure components
            revenueComponents.setVisible(true);
            expenditureComponents.setVisible(true);
        });

        Group selectTypeGroup = new Group(personal, Age, AgeInput, maleRadioButton, femaleRadioButton, HouseholdInput, createBtn);
        return selectTypeGroup;
    }

    private static Group createRevenueComponents() {
        // Create components for selecting and inputting revenues
        ChoiceBox<String> revenue = new ChoiceBox<>();
        revenue.getItems().addAll("Revenues", "Salary", "Business", "Investments");
        revenue.setValue("Revenues");
        revenue.setLayoutX(50);
        revenue.setLayoutY(150);

        TextField revenueInput = new TextField();
        revenueInput.setPromptText("Enter amount");
        revenueInput.setLayoutX(200);
        revenueInput.setLayoutY(150);

        // Add an event handler for the revenue TextField
        revenueInput.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String selectedItem = revenue.getValue();
                if (!selectedItem.equals("Revenues")) {
                    String inputText = revenueInput.getText();
                    addRevenueToBudget(selectedItem, inputText, budgetDAO);
                }
            }
        });

        Group revenueGroup = new Group(revenue, revenueInput);
        return revenueGroup;
    }

    private static Group createExpenditureComponents() {
        // Create components for selecting and inputting expenditures
        ChoiceBox<String> expenditure = new ChoiceBox<>();
        expenditure.getItems().addAll("Expenditures", "Rent", "Groceries", "Utilities");
        expenditure.setValue("Expenditures");
        expenditure.setLayoutX(50);
        expenditure.setLayoutY(200);

        TextField expenditureInput = new TextField();
        expenditureInput.setPromptText("Enter amount");
        expenditureInput.setLayoutX(200);
        expenditureInput.setLayoutY(200);

        // Add an event handler for the expenditure TextField
        expenditureInput.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String selectedItem = expenditure.getValue();
                if (!selectedItem.equals("Expenditures")) {
                    String inputText = expenditureInput.getText();
                    addExpenditureToBudget(selectedItem, inputText, budgetDAO);
                }
            }
        });

        Group expenditureGroup = new Group(expenditure, expenditureInput);
        return expenditureGroup;
    }
    private static void addRevenueToBudget(String selectedItem, String inputText, BudgetDAO budgetDAO) {
        BigDecimal amount = new BigDecimal(inputText);

        BudgetItem budgetItem = new BudgetItem();//selectedItem, amount, BudgetCategory.OTHER);
        userBudget.addBudgetItem(budgetItem);

        try {
            budgetDAO.setBudget(loggedInUser.getLoginInfo().getUserId(),userBudget);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addExpenditureToBudget(String selectedItem, String inputText, BudgetDAO budgetDAO) {
        BigDecimal amount = new BigDecimal(inputText);
        selectedItem.toUpperCase();
        BudgetItem budgetItem = new BudgetItem();//amount, selectedItem);
        userBudget.addBudgetItem(budgetItem);

        try {
            budgetDAO.setBudget(loggedInUser.getLoginInfo().getUserId(),userBudget);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void processSelectTypeInput(String selectedValue, String inputAge, String inputGender, String inputHousehold, BudgetDAO budgetDAO, User loggedInUser) {
        // Create a new budget object and associate it with the loggedInUser
        if (selectedValue.equals("Age & Gender")) {
            Byte age = Byte.valueOf(inputAge);
            GenderCategory Gender = GenderCategory.valueOf(inputGender.toUpperCase());
            userBudget = new Budget( age, Gender);
        } else if (selectedValue.equals("Household")) {
            HouseholdCategory householdSize = HouseholdCategory.valueOf(inputHousehold.toUpperCase());
            userBudget = new Budget(householdSize);
        }
    }


}

