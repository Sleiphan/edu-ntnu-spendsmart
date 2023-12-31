package edu.ntnu.g14.frontend;

import edu.ntnu.g14.BankApplication;
import edu.ntnu.g14.dao.BudgetDAO;
import edu.ntnu.g14.model.Budget;
import edu.ntnu.g14.model.BudgetCategory;
import edu.ntnu.g14.model.BudgetItem;
import edu.ntnu.g14.model.BudgetSuggestion;
import edu.ntnu.g14.model.GenderCategory;
import edu.ntnu.g14.model.HouseholdCategory;
import edu.ntnu.g14.model.User;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.animation.PauseTransition;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BudgetSuggestionsScene {

  static Stage stage = BankApplication.getStage();
  static BudgetDAO budgetDAO;

  static Budget userBudget;

  static User loggedInUser = BankApplication.loggedInUser;

  static Group revenueComponents;


  static BudgetSuggestion budgetSuggestion = new BudgetSuggestion();

  static BigDecimal totalRevenue = BigDecimal.ZERO;

  static TextField savingsInput;

  static Group selectTypeComponents;

  static Button createBudgetBtn;
  static VBox revenueBox;

  private static Tooltip tooltip;

  /**
   * The {@code BudgetSuggestionsScene} class represents the graphical user interface for generating
   * budget suggestions in the Bank Application. It contains various UI components to create and
   * view suggested budgets. This class makes use of the {@code BudgetSuggestion} class to generate
   * budget suggestions based on user input.
   */

  static public Scene scene() throws IOException {

    try {
      budgetDAO = new BudgetDAO("saves/budgets.txt");
    } catch (IOException e) {
      e.printStackTrace();
    }
    Text title = ApplicationObjects.newText("Budget Suggestion", 30, false, 250, 50);
    // Create components
    selectTypeComponents = createSelectTypeComponents();

    revenueComponents = createRevenueComponents();

    // Hide revenue, expenditure and createBudgetBtn components initially
    revenueComponents.setVisible(false);

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
    Group root = new Group(selectTypeComponents, revenueComponents, dropDownButton, homeButton,
        manageUserButton, title);
    dropDownButton.setOnAction(e -> root.getChildren().add(dropDown));

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

  /**
   * Creates the components for selecting the budget type, age, gender, and household. The method
   * creates a group of components and sets the appropriate visibility and action listeners.
   *
   * @return a Group of components for selecting the budget type, age, gender, and household
   */
  private static Group createSelectTypeComponents() {
    // Create components for selecting type, age, gender, and household

    // Add a listener to the personal choice box
    Text selectInfo = ApplicationObjects.newText("Please select a budget type", 15, false, 50, 85);
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
    AgeInput.addEventFilter(KeyEvent.KEY_TYPED, event -> {
      if (!event.getCharacter().matches("\\d")) { // Check if the character is a digit
        event.consume(); // If it's not a digit, consume the event to prevent it from being processed
      }
    });
    //ChoiceBox with Household Category elements
    List<String> householdChoiceList = new ArrayList<>();
    for (HouseholdCategory category : HouseholdCategory.values()) {
      householdChoiceList.add(category.toString().toLowerCase().replaceAll("_", " "));
    }
    String[] householdChoices = householdChoiceList.toArray(new String[0]);
    ChoiceBox<String> HouseholdInput = ApplicationObjects.newChoiceBox(householdChoices, 150, 34,
        15, 210, 100);
    HouseholdInput.setValue(
        HouseholdCategory.LIVING_ALONE.toString().toLowerCase().replaceAll("_", " "));
    HouseholdInput.setVisible(false);

    AgeInput.setVisible(false);

    // Add a listener to the personal choice box
    personal.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, selectedValue) -> {
          if (selectedValue.equals("Age & Gender")) {
            AgeInput.setVisible(true);
            maleRadioButton.setVisible(true);
            femaleRadioButton.setVisible(true);
            HouseholdInput.setVisible(false);
            selectInfo.setVisible(false);
          } else if (selectedValue.equals("Household")) {
            AgeInput.setVisible(false);
            maleRadioButton.setVisible(false);
            femaleRadioButton.setVisible(false);
            HouseholdInput.setVisible(true);
            selectInfo.setVisible(false);
          } else {
            AgeInput.setVisible(false);
            maleRadioButton.setVisible(false);
            femaleRadioButton.setVisible(false);
            HouseholdInput.setVisible(false);
          }
        });

    // When the 'Create' button is clicked
    Button continueBtnSelectType = ApplicationObjects.newButton("Continue", 500, 480, 157, 25, 16);
    continueBtnSelectType.setOnAction(e -> {
      String selectedValue = personal.getValue();
      if (selectedValue.equals("Select Type")) {
        if (tooltip == null) {
          tooltip = new Tooltip("Please select a type before pressing continue");
          continueBtnSelectType.setTooltip(tooltip);
        }
        tooltip.show(continueBtnSelectType.getScene().getWindow());

        // Set the duration after which the tooltip will be hidden (e.g., 5 seconds)
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> tooltip.hide());
        pause.play();

        return;
      }
      // Get the input values for Age, Gender, and Household

      String inputAge = AgeInput.getText();
      String inputGender =
          genderToggleGroup.getSelectedToggle() != null ? genderToggleGroup.getSelectedToggle()
              .getUserData().toString() : "";
      String inputHousehold = HouseholdInput.getValue();
      if (selectedValue.equals("Age & Gender") && (inputAge.isEmpty() || inputGender.isEmpty())) {
        if (tooltip == null) {
          if (inputAge.isEmpty() && !inputGender.isEmpty()) {
            tooltip = new Tooltip("Please enter in Age");
          } else if (!inputAge.isEmpty()) {
            tooltip = new Tooltip("Please select a gender, sorry if your gender is not an option");
          } else {
            tooltip = new Tooltip(
                "Please enter age and select a gender, sorry if your gender is not an option");
          }
          continueBtnSelectType.setTooltip(tooltip);
        }
        tooltip.show(continueBtnSelectType.getScene().getWindow());

        // Set the duration after which the tooltip will be hidden (e.g., 5 seconds)
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> tooltip.hide());
        pause.play();

        return;
      } else {
        processSelectTypeInput(selectedValue, inputAge, inputGender, inputHousehold);
      }

      selectTypeComponents.setVisible(false);
      revenueComponents.setVisible(true);

    });

    return new Group(selectInfo, personal, AgeInput, maleRadioButton, femaleRadioButton,
        HouseholdInput, continueBtnSelectType);
  }

  /**
   * Creates the components for selecting and inputting revenues. The method creates a group of
   * components and sets the appropriate visibility and action listeners.
   *
   * @return a Group of components for selecting and inputting revenues
   */
  private static Group createRevenueComponents() {
    // Create components for selecting and inputting revenues
    Text infoRevenue = ApplicationObjects.newText(
        "Select revenue type, add amount\n and add revenue item to budget", 15, false, 50, 100);
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
    revenueInput.addEventFilter(KeyEvent.KEY_TYPED, KeyEvent -> {
      String input = KeyEvent.getCharacter();
      String currentText = revenueInput.getText();

      if (!input.matches("[\\d.]") || (input.equals(".") && currentText.contains("."))) {
        if (input.matches("[,]")) {
          if (tooltip == null) {
            tooltip = new Tooltip("Please use '.', to indicate decimal point");
            revenueInput.setTooltip(tooltip);
          }
          tooltip.show(revenueInput.getScene().getWindow());

          // Set the duration after which the tooltip will be hidden (e.g., 5 seconds)
          PauseTransition pause = new PauseTransition(Duration.seconds(5));
          pause.setOnFinished(event -> tooltip.hide());
          pause.play();
        }
        KeyEvent.consume(); // If it's not a valid character or already contains a decimal point, consume the event
      }
    });
    Button addRevenueItemBtn = ApplicationObjects.newButton("Add revenue to budget", 0, 0, 157, 25,
        16);
    addRevenueItemBtn.setOnAction(e -> {
      if (revenueInput.getText().isEmpty()) {
        if (tooltip == null) {
          tooltip = new Tooltip("Please enter an amount, to create a revenue item");
          addRevenueItemBtn.setTooltip(tooltip);
        }
        tooltip.show(addRevenueItemBtn.getScene().getWindow());

        // Set the duration after which the tooltip will be hidden (e.g., 5 seconds)
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> tooltip.hide());
        pause.play();

        return;
      }
      BudgetCategory selectedItem = BudgetCategory.valueOf(
          revenue.getValue().toUpperCase().replaceAll(" ", "_"));
      String inputText = revenueInput.getText();
      addRevenueToBudget(selectedItem, inputText);
    });
    Text savingsInfo = ApplicationObjects.newText(
        "Please enter the amount you\nto save for the month", 15, false, 50, 100);
    savingsInfo.setVisible(false);
    savingsInput = ApplicationObjects.newTextField("Enter Savings Amount", 50, 130, 130, 30, 15);
    savingsInput.addEventFilter(KeyEvent.KEY_TYPED, KeyEvent -> {
      String input = KeyEvent.getCharacter();
      String currentText = savingsInput.getText();

      if (!input.matches("[\\d.]") || (input.equals(".") && currentText.contains("."))) {
        if (input.matches("[,]")) {
          if (tooltip == null) {
            tooltip = new Tooltip("Please use '.', to indicate decimal point");
            createBudgetBtn.setTooltip(tooltip);
          }
          tooltip.show(createBudgetBtn.getScene().getWindow());

          // Set the duration after which the tooltip will be hidden (e.g., 5 seconds)
          PauseTransition pause = new PauseTransition(Duration.seconds(5));
          pause.setOnFinished(event -> tooltip.hide());
          pause.play();
        }
        KeyEvent.consume(); // If it's not a valid character or already contains a decimal point, consume the event
      }
    });
    savingsInput.setVisible(false);

    Button continueBtnRevenue = ApplicationObjects.newButton("Continue", 500, 480, 157, 25, 16);
    continueBtnRevenue.setOnAction(e -> {
      if (userBudget == null || userBudget.getEntries().isEmpty()) {
        if (tooltip == null) {
          tooltip = new Tooltip("Please add a revenue item");
          continueBtnRevenue.setTooltip(tooltip);
        }
        tooltip.show(continueBtnRevenue.getScene().getWindow());

        // Set the duration after which the tooltip will be hidden (e.g., 5 seconds)
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> tooltip.hide());
        pause.play();

        return;
      }
      if (!revenueInput.getText().isEmpty()) {
        //hide and show components
        savingsInput.setVisible(true);
        createBudgetBtn.setVisible(true);
        revenueBox.setVisible(false);
        infoRevenue.setVisible(false);
        savingsInfo.setVisible(true);
      }
    });

    createBudgetBtn = ApplicationObjects.newButton("Create Budget", 500, 480, 157, 25, 16);
    createBudgetBtn.setVisible(false);
    createBudgetBtn.setOnAction(e -> {
      if (savingsInput.getText().isEmpty()) {
        if (tooltip == null) {
          tooltip = new Tooltip(
              "Please enter the amount\n you want to save, you can also enter\n 0 if you don't want to save any money");
          addRevenueItemBtn.setTooltip(tooltip);
        }
        tooltip.show(addRevenueItemBtn.getScene().getWindow());

        // Set the duration after which the tooltip will be hidden (e.g., 5 seconds)
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> tooltip.hide());
        pause.play();

        return;
      } else if (totalRevenue.compareTo(new BigDecimal(savingsInput.getText())) < 0) {
        if (tooltip == null) {
          tooltip = new Tooltip("Your savings can not be bigger than your revenue");
          addRevenueItemBtn.setTooltip(tooltip);
        }
        tooltip.show(addRevenueItemBtn.getScene().getWindow());

        // Set the duration after which the tooltip will be hidden (e.g., 5 seconds)
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> tooltip.hide());
        pause.play();

        return;
      }
      // Retrieve the input savings amount and convert it to BigDecimal
      BigDecimal savingsAmount = new BigDecimal(
          savingsInput.getText().isEmpty() ? "0" : savingsInput.getText());

      // Calculate the remaining amount after savings
      BigDecimal remainingAmount = totalRevenue.subtract(savingsAmount);

      // Generate budget suggestions based on the remaining amount
      Map<BudgetCategory, BigDecimal> suggestedBudgetItems = budgetSuggestion.suggestBudget(
          remainingAmount);

      // Add the suggested budget items and savings to userBudget
      for (Map.Entry<BudgetCategory, BigDecimal> entry : suggestedBudgetItems.entrySet()) {
        userBudget.addBudgetItem(new BudgetItem(entry.getKey(), entry.getValue()));
      }

      totalRevenue = sumOfRevenue(totalRevenue, savingsInput.getText());
      // Update the total revenue
      try {
        budgetDAO.setBudget(loggedInUser.getLoginInfo().getUserId(), userBudget);
        // Navigate to the BudgetingScene or another desired page after saving the budget
        stage.setScene(BudgetingScene.scene());
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      BudgetingScene.refreshData();
    });
    revenueBox = new VBox();
    revenueBox.setLayoutX(50);
    revenueBox.setLayoutY(130);
    revenueBox.setSpacing(10);
    revenueBox.getChildren().addAll(revenue, revenueInput, addRevenueItemBtn);

    return new Group(infoRevenue, savingsInfo, revenueBox, continueBtnRevenue, savingsInput,
        createBudgetBtn);
  }

  /**
   * Adds a revenue item to the user's budget. This method takes a BudgetCategory and a string
   * input, converts the input to a BigDecimal, and adds the new BudgetItem to the user's budget.
   *
   * @param selectedItem a BudgetCategory representing the type of revenue item
   * @param inputText    a String representing the amount of the revenue item
   */
  private static void addRevenueToBudget(BudgetCategory selectedItem, String inputText) {
    BigDecimal amount = new BigDecimal(inputText);

    BudgetItem budgetItem = new BudgetItem(selectedItem, amount);
    userBudget.addBudgetItem(budgetItem);

    // Update the totalRevenue
    totalRevenue = totalRevenue.add(amount);

    // Update the loggedInUser's budget
    loggedInUser.setBudget(userBudget);

    // Get the updated User object after updating the budget
    User updatedUser = loggedInUser;

    // Update the loggedInUser object in the BudgetingScene class
    BudgetingScene.setLoggedInUser(updatedUser);
  }

  /**
   * Calculates the sum of the total revenue and the savings amount.
   *
   * @param totalRevenue  a BigDecimal representing the current total revenue
   * @param savingsAmount a String representing the savings amount to be added
   * @return a BigDecimal representing the new total revenue after adding the savings amount
   */
  private static BigDecimal sumOfRevenue(BigDecimal totalRevenue, String savingsAmount) {
    BigDecimal savings = new BigDecimal(savingsAmount.isEmpty() ? "0" : savingsAmount);
    return totalRevenue.subtract(savings);
  }

  /**
   * Processes the input data from the select type components and initializes the user's budget.
   * This method creates a new Budget object based on the selectedValue and associates it with the
   * loggedInUser.
   *
   * @param selectedValue  a String representing the selected budget type
   * @param inputAge       a String representing the input age (if applicable)
   * @param inputGender    a String representing the input gender (if applicable)
   * @param inputHousehold a String representing the input household size (if applicable)
   */
  private static void processSelectTypeInput(String selectedValue, String inputAge,
      String inputGender, String inputHousehold) {
    // Create a new budget object and associate it with the loggedInUser
    if (selectedValue.equals("Age & Gender")) {
      byte age = Byte.parseByte(inputAge);
      GenderCategory Gender = GenderCategory.valueOf(inputGender.toUpperCase());
      userBudget = new Budget(age, Gender);
    } else if (selectedValue.equals("Household")) {
      // Convert the inputHousehold String to HouseholdCategory enum
      HouseholdCategory householdSize = HouseholdCategory.valueOf(
          inputHousehold.toUpperCase().replaceAll(" ", "_"));
      userBudget = new Budget(householdSize);
    }
  }

}
