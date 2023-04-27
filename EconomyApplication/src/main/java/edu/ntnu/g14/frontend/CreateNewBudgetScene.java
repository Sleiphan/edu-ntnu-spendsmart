package edu.ntnu.g14.frontend;

import edu.ntnu.g14.BankApplication;
import edu.ntnu.g14.model.Budget;
import edu.ntnu.g14.model.BudgetCategory;
import edu.ntnu.g14.model.BudgetItem;
import edu.ntnu.g14.model.GenderCategory;
import edu.ntnu.g14.model.HouseholdCategory;
import edu.ntnu.g14.model.User;
import edu.ntnu.g14.dao.BudgetDAO;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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

/**
 * The {@code CreateNewBudgetScene} class represents the graphical user interface for creating a new
 * budget in the Bank Application. It contains various UI components to enter budget revenues,
 * expenditures, and other related information.
 */
public class CreateNewBudgetScene {

  static Stage stage = BankApplication.getStage();
  static BudgetDAO budgetDAO;

  static Budget userBudget;

  static User loggedInUser = BankApplication.loggedInUser;

  static Group revenueComponents;
  static Group expenditureComponents;

  static Button createBudgetBtn;

  private static Tooltip tooltip;


  /**
   * Returns a {@code Scene} containing the UI components for creating a new budget.
   *
   * @return a new {@code Scene} instance with the new budget creation components
   * @throws IOException if there is an error accessing the budget file
   */
  static public Scene scene() throws IOException {

    try {
      budgetDAO = new BudgetDAO("saves/budgets.txt");
    } catch (IOException e) {
      e.printStackTrace();
    }
    Text title = ApplicationObjects.newText("Create New Budget", 30, false, 250, 50);

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
    Group root = new Group(title, selectTypeComponents, revenueComponents, expenditureComponents,
        dropDownButton, homeButton, manageUserButton);
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
   * Creates and returns a Group containing the components necessary for selecting the type of
   * budget (e.g., age and gender, or household size) and corresponding inputs (e.g., age, gender,
   * and household size).
   *
   * @return Group containing the components for selecting the budget type and inputs
   */
  private static Group createSelectTypeComponents() {
    // Create components for selecting type, age, gender, and household

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

    Button continueBtn = ApplicationObjects.newButton("Continue", 500, 480, 157, 25, 16);
    continueBtn.setOnAction(e -> {
      // Check if a valid personal type is selected
      String selectedValue = personal.getValue();
      if (selectedValue.equals("Select Type")) {
        if (tooltip == null) {
          tooltip = new Tooltip("Please select a type before pressing continue");
          continueBtn.setTooltip(tooltip);
        }
        tooltip.show(continueBtn.getScene().getWindow());

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
          continueBtn.setTooltip(tooltip);
        }
        tooltip.show(continueBtn.getScene().getWindow());

        // Set the duration after which the tooltip will be hidden (e.g., 5 seconds)
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> tooltip.hide());
        pause.play();

        return;
      } else {
        processSelectTypeInput(selectedValue, inputAge, inputGender, inputHousehold);
      }
      // Process the input and create the userBudget object

      // Show the revenue and expenditure components
      showRevenueComponents();
      showExpenditureComponents();
      personal.setVisible(false);
      HouseholdInput.setVisible(false);
      AgeInput.setVisible(false);
      maleRadioButton.setVisible(false);
      femaleRadioButton.setVisible(false);
      continueBtn.setVisible(false);
      selectInfo.setVisible(false);
      createBudgetBtn.setVisible(true);

    });

    createBudgetBtn = ApplicationObjects.newButton("Create Budget", 500, 480, 157, 25, 16);
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

    return new Group(selectInfo, personal, AgeInput, maleRadioButton, femaleRadioButton,
        HouseholdInput, continueBtn, createBudgetBtn);
  }

  /**
   * Creates and returns a Group containing the components necessary for inputting revenue items
   * (e.g., selecting a revenue category and entering an amount).
   *
   * @return Group containing the components for inputting revenue items
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
    ChoiceBox<String> revenue = ApplicationObjects.newChoiceBox(revenueChoices, 200, 34, 15, 0, 0);
    revenue.setValue(BudgetCategory.SALARY.toString());

    TextField revenueInput = ApplicationObjects.newTextField("Enter Amount", 0, 0, 157, 30, 16);
    // Add an event filter to the TextField to only accept numbers and a single decimal point
    revenueInput.addEventFilter(KeyEvent.KEY_TYPED, KeyEvent -> {
      String input = KeyEvent.getCharacter();
      String currentText = revenueInput.getText();

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
    Button addRevenueItemBtn = ApplicationObjects.newButton("Add revenue to budget", 0, 0, 200, 25,
        16);
    addRevenueItemBtn.setOnAction(e -> {
      if (revenueInput.getText().isEmpty()) {
        if (tooltip == null) {
          tooltip = new Tooltip("Please enter an amount, to create a revenue item");
          createBudgetBtn.setTooltip(tooltip);
        }
        tooltip.show(createBudgetBtn.getScene().getWindow());

        // Set the duration after which the tooltip will be hidden (e.g., 5 seconds)
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> tooltip.hide());
        pause.play();

        return;
      }
      String selectedValue = revenue.getValue();
      if (selectedValue == null) {
        if (tooltip == null) {
          tooltip = new Tooltip("No more categories available to add");
          createBudgetBtn.setTooltip(tooltip);
        }
        tooltip.show(createBudgetBtn.getScene().getWindow());

        // Set the duration after which the tooltip will be hidden (e.g., 5 seconds)
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> tooltip.hide());
        pause.play();

        return;
      }

      BudgetCategory selectedItem = BudgetCategory.valueOf(
          selectedValue.toUpperCase().replaceAll(" ", "_"));
      String inputText = revenueInput.getText();
      addRevenueToBudget(selectedItem, inputText, budgetDAO, revenue);
    });

    VBox revenueBox = new VBox();
    revenueBox.setLayoutX(50);
    revenueBox.setLayoutY(130);
    revenueBox.setSpacing(10);
    revenueBox.getChildren().addAll(revenue, revenueInput, addRevenueItemBtn);

    return new Group(revenueBox, infoRevenue);
  }

  /**
   * Creates and returns a Group containing the components necessary for inputting expenditure items
   * (e.g., selecting an expenditure category and entering an amount).
   *
   * @return Group containing the components for inputting expenditure items
   */
  private static Group createExpenditureComponents() {
    // Create components for selecting and inputting expenditures
    Text infoExpenditure = ApplicationObjects.newText(
        "Select expenditure type, add amount\n and add expenditure item to budget", 15, false, 50,
        300);
    List<String> expenditureChoiceList = new ArrayList<>();
    for (BudgetCategory category : BudgetCategory.values()) {
      if (category.getType().equals("e")) {
        expenditureChoiceList.add(category.toString().toLowerCase().replaceAll("_", " "));
      }
    }
    String[] expenditureChoices = expenditureChoiceList.toArray(new String[0]);
    ChoiceBox<String> expenditure = ApplicationObjects.newChoiceBox(expenditureChoices, 200, 34, 15,
        0, 0);
    expenditure.setValue(
        BudgetCategory.FOOD_AND_DRINK.toString().toLowerCase().replaceAll("_", " "));

    TextField expenditureInput = ApplicationObjects.newTextField("Enter Amount", 0, 0, 157, 30, 16);
    // Add an event filter to the TextField to only accept numbers and a single decimal point
    expenditureInput.addEventFilter(KeyEvent.KEY_TYPED, KeyEvent -> {
      String input = KeyEvent.getCharacter();
      String currentText = expenditureInput.getText();

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
    Button addExpenditureItemBtn = ApplicationObjects.newButton("Add expenditure to Budget", 0, 0,
        200, 25, 16);
    addExpenditureItemBtn.setOnAction(e -> {
      if (expenditureInput.getText().isEmpty()) {
        if (tooltip == null) {
          tooltip = new Tooltip("Please enter an amount, to create an expenditure item");
          createBudgetBtn.setTooltip(tooltip);
        }
        tooltip.show(createBudgetBtn.getScene().getWindow());

        // Set the duration after which the tooltip will be hidden (e.g., 5 seconds)
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> tooltip.hide());
        pause.play();

        return;
      }
      String selectedValue = expenditure.getValue();
      if (selectedValue == null) {
        if (tooltip == null) {
          tooltip = new Tooltip("No more categories available to add");
          createBudgetBtn.setTooltip(tooltip);
        }
        tooltip.show(createBudgetBtn.getScene().getWindow());

        // Set the duration after which the tooltip will be hidden (e.g., 5 seconds)
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> tooltip.hide());
        pause.play();

        return;
      }

      BudgetCategory selectedItem = BudgetCategory.valueOf(
          selectedValue.toUpperCase().replaceAll(" ", "_"));
      String inputText = expenditureInput.getText();
      addExpenditureToBudget(selectedItem, inputText, budgetDAO, expenditure);
    });
    VBox expenditureBox = new VBox();
    expenditureBox.setLayoutX(50);
    expenditureBox.setLayoutY(330);
    expenditureBox.setSpacing(10);
    expenditureBox.getChildren().addAll(expenditure, expenditureInput, addExpenditureItemBtn);

    return new Group(expenditureBox, infoExpenditure);
  }

  /**
   * Adds a revenue item to the user's budget, updates the loggedInUser's budget, and refreshes the
   * BudgetingScene data.
   *
   * @param selectedItem BudgetCategory of the selected revenue item
   * @param inputText    String representing the amount of the revenue item
   * @param budgetDAO    BudgetDAO instance for saving the budget
   */
  private static void addRevenueToBudget(BudgetCategory selectedItem, String inputText,
      BudgetDAO budgetDAO, ChoiceBox<String> expenditureChoiceBox) {
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
    // Remove the selected category from the choice box
    expenditureChoiceBox.getItems().remove(expenditureChoiceBox.getValue());

    // Set the first available category as the new value (if any)
    if (!expenditureChoiceBox.getItems().isEmpty()) {
      expenditureChoiceBox.setValue(expenditureChoiceBox.getItems().get(0));
    }
  }

  /**
   * Adds an expenditure item to the user's budget, updates the loggedInUser's budget, and refreshes
   * the BudgetingScene data.
   *
   * @param selectedItem BudgetCategory of the selected expenditure item
   * @param inputText    String representing the amount of the expenditure item
   * @param budgetDAO    BudgetDAO instance for saving the budget
   */
  private static void addExpenditureToBudget(BudgetCategory selectedItem, String inputText, BudgetDAO budgetDAO, ChoiceBox<String> expenditureChoiceBox) {
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

    // Remove the selected category from the choice box
    expenditureChoiceBox.getItems().remove(expenditureChoiceBox.getValue());

    // Set the first available category as the new value (if any)
    if (!expenditureChoiceBox.getItems().isEmpty()) {
      expenditureChoiceBox.setValue(expenditureChoiceBox.getItems().get(0));
    }
  }


  /**
   * Processes the input values from the budget type selection and creates a new budget object based
   * on the user's inputs (e.g., age and gender or household size).
   *
   * @param selectedValue  String representing the selected budget type
   * @param inputAge       String representing the user's age
   * @param inputGender    String representing the user's gender
   * @param inputHousehold String representing the user's household size
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

  /**
   * Sets the visibility of the revenue components to true, making them visible in the UI.
   */
  private static void showRevenueComponents() {
    revenueComponents.setVisible(true);
  }

  /**
   * Sets the visibility of the expenditure components to true, making them visible in the UI.
   */
  private static void showExpenditureComponents() {
    expenditureComponents.setVisible(true);
  }
}

