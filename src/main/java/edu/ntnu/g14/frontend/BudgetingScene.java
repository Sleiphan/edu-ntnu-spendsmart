package edu.ntnu.g14.frontend;

import edu.ntnu.g14.BankApplication;
import edu.ntnu.g14.model.Budget;
import edu.ntnu.g14.model.BudgetCategory;
import edu.ntnu.g14.model.BudgetItem;
import edu.ntnu.g14.model.User;
import edu.ntnu.g14.dao.BudgetDAO;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * The {@code BudgetingScene} class represents the budgeting scene in the Bank Application, allowing
 * users to view and edit their budget items, such as revenues and expenditures.
 * <p>This class provides methods for creating, editing, and deleting budget items,
 * as well as updating the savings value based on the user's budget.
 */
public class BudgetingScene {

  static Stage stage = BankApplication.getStage();
  private static User loggedInUser = BankApplication.loggedInUser;
  static BudgetDAO budgetDAO;
  static TableView<ObservableList<Object>> revenues;
  static TableView<ObservableList<Object>> expenditures;

  static ContextMenu contextMenu;

  static ContextMenu revenuesContextMenu;

  static ContextMenu expendituresContextMenu;

  static Button confirmEditButton;

  static Text savings;
  private static boolean skipListener = false;

  private static Tooltip tooltip;



  /**
   * Generates the main budgeting scene.
   *
   * @return the budgeting {@link Scene}
   * @throws IOException if there is an error accessing the budget file
   */
  static public Scene scene() throws IOException {
    
    budgetDAO = getBudgetDAO();

    revenues = createRevenuesTable();
    expenditures = createExpendituresTable();
    revenuesContextMenu = createContextMenu(revenues);
    expendituresContextMenu = createContextMenu(expenditures);

    revenues.setOnContextMenuRequested(
        event -> revenuesContextMenu.show(revenues, event.getScreenX(), event.getScreenY()));
    expenditures.setOnContextMenuRequested(
        event -> expendituresContextMenu.show(expenditures, event.getScreenX(),
            event.getScreenY()));

    Label backgroundLabel = createBackgroundLabel();
    Text MonthlyBudget = createMonthlyBudgetText();
    savings = createSavingsText();
    updateSavings();
    Text infoText = createInfoText();

    Button createNewBudget = createCreateNewBudgetButton();
    Button budgetSuggestions = createBudgetSuggestionsButton();

    ImageView homeButton = createHomeButton();
    Button dropDownButton = createDropDownButton();
    Group dropDown = ApplicationObjects.dropDownMenu();
    ImageView manageUserButton = createManageUserButton();

    confirmEditButton = confirmEditButton();

    Group root = new Group(backgroundLabel, revenues, expenditures, confirmEditButton,
        createNewBudget,
        budgetSuggestions, MonthlyBudget, savings, infoText, dropDownButton, homeButton,
        manageUserButton);
    dropDownButton.setOnAction(e -> root.getChildren().add(dropDown));

    Scene scene = createScene(root, event -> {
      if (!revenues.contains(event.getX(), event.getY()) && !expenditures.contains(event.getX(),
          event.getY())) {
          saveModifiedData();
      }
    });
    scene.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
      if (!revenues.contains(event.getX(), event.getY()) && !expenditures.contains(event.getX(),
          event.getY())) {
        saveModifiedData();
        revenues.getSelectionModel().clearSelection();
        //expenditures.getSelectionModel().clearSelection(); did not work for some reason:(
      }
    });

    Group userButtons = ApplicationObjects.userMenu();
    addUserButtonsEventHandler(manageUserButton, root, userButtons);
    scene.setOnMouseClicked(e -> {
      root.getChildren().remove(userButtons);
      root.getChildren().remove(dropDown);
    });

    return scene;
  }

  /**
   * Creates and returns a new {@link BudgetDAO} instance with the specified file path.
   *
   * @return a new {@link BudgetDAO} instance
   * @throws IOException if there is an error accessing the file at the specified path
   */
  private static BudgetDAO getBudgetDAO() throws IOException {
    try {
      return new BudgetDAO("saves/budgets.txt");
    } catch (IOException e) {
      e.printStackTrace();
      throw e;
    }
  }

  /**
   * Creates and returns a new {@link Scene} with the specified root {@link Group} and mouse click
   * event handler.
   *
   * @param root              the root {@link Group} for the scene
   * @param mouseClickHandler the {@link EventHandler} for handling mouse click events on the scene
   * @return a new {@link Scene} instance
   */
  private static Scene createScene(Group root, EventHandler<MouseEvent> mouseClickHandler) {
    Scene scene = new Scene(root, 728, 567, ApplicationObjects.getSceneColor());
    scene.setFill(ApplicationObjects.getSceneColor());
    scene.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseClickHandler);
    return scene;
  }

  /**
   * Adds an event handler to the specified {@link ImageView} that displays the user buttons group
   * when clicked.
   *
   * @param manageUserButton the {@link ImageView} to add the event handler to
   * @param root             the root {@link Group} of the scene
   * @param userButtons      the {@link Group} containing the user buttons
   */
  private static void addUserButtonsEventHandler(ImageView manageUserButton, Group root,
      Group userButtons) {
    manageUserButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
      root.getChildren().add(userButtons);
      event.consume();
    });
  }

  /**
   * Initializes revenues data from the budget file.
   *
   * @return an {@link ObservableList} containing the revenues data
   * @throws IOException if there is an error accessing the budget file
   */
  public static ObservableList<ObservableList<Object>> initializeRevenuesData() throws IOException {
    ObservableList<ObservableList<Object>> revenuesData = FXCollections.observableArrayList();
    Budget budget = budgetDAO.getBudget(loggedInUser.getLoginInfo().getUserId());
    if (budget == null) {
      budget = new Budget();
      loggedInUser.setBudget(budget);
    }
    // Fetch revenues from budget and add them to revenuesData
    for (BudgetItem entry : budget.getEntries()) {
      // need to update budgetitem to contain getRevenues category
      if (entry.getCategory().getType().equals("r")) {
        revenuesData.add(FXCollections.observableArrayList(entry.getCategory().
            toString().toLowerCase().replaceAll("_", " "), entry.getFinancialValue()));
      }
    }

    return revenuesData;
  }

  /**
   * Initializes expenditures data from the budget file.
   *
   * @return an {@link ObservableList} containing the expenditure data
   */
  public static ObservableList<ObservableList<Object>> initializeExpenditureData() {
    ObservableList<ObservableList<Object>> expenditureData = FXCollections.observableArrayList();
    Budget budget = loggedInUser.getBudget();
    if (budget == null) {
      budget = new Budget();
      loggedInUser.setBudget(budget);
    }
    // Fetch expenditures from budget and add them to expenditureData
    for (BudgetItem entry : budget.getEntries()) {
      if (entry.getCategory().getType().equals("e")) {
        expenditureData.add(FXCollections.observableArrayList(entry.getCategory().
            toString().toLowerCase().replaceAll("_", " "), entry.getFinancialValue()));
      }
    }
    return expenditureData;
  }

  /**
   * Creates a context menu for the provided table view.
   *
   * @param tableView the table view for which the context menu is created
   * @return the created context menu
   */
  private static ContextMenu createContextMenu(TableView<ObservableList<Object>> tableView) {
    MenuItem addItem = new MenuItem("Add");
    addItem.setOnAction(e -> addBudgetItem(tableView));

    MenuItem deleteItem = new MenuItem("Delete");
    deleteItem.setOnAction(e -> deleteBudgetItem(tableView));

    ContextMenu contextMenu = new ContextMenu();
    contextMenu.getItems().addAll(addItem, deleteItem);
    return contextMenu;
  }

  /**
   * Creates a custom cell factory for the provided table view.
   *
   * @param tableView the table view for which the cell factory is created
   * @param <T>       the type of the items contained in the table
   * @return the created cell factory
   */
  private static <T> Callback<TableColumn<ObservableList<Object>, T>, TableCell<ObservableList<Object>, T>> createCustomCellFactory(
      TableView<ObservableList<Object>> tableView) {
    return param -> new TableCell<>() {
      private TextField textField;

      @Override
      protected void updateItem(Object item, boolean empty) {
        super.updateItem((T) item, empty);
        if (empty || item == null) {
          setGraphic(null);
        } else {
          int columnIndex = getTableView().getColumns().indexOf(getTableColumn());

          if (columnIndex == 0) {
            ChoiceBox<String> choiceBox;
            if (tableView == expenditures) {
              List<String> expenditureChoiceList = new ArrayList<>();
              for (BudgetCategory category : BudgetCategory.values()) {
                if (category.getType().equals("e")) {
                  expenditureChoiceList.add(category.toString().toLowerCase().replaceAll("_", " "));
                }
              }
              choiceBox = new ChoiceBox<>(FXCollections.observableArrayList(expenditureChoiceList));
            } else {
              List<String> revenueChoiceList = new ArrayList<>();
              for (BudgetCategory category : BudgetCategory.values()) {
                if (category.getType().equals("r")) {
                  revenueChoiceList.add(category.toString().toLowerCase().replaceAll("_", " "));
                }
              }
              choiceBox = new ChoiceBox<>(FXCollections.observableArrayList(revenueChoiceList));
            }

            choiceBox.setValue(item.toString());
            choiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
              if (skipListener) {
                return;
              }

              if (!oldValue.equals(newValue) && validateAndSetNewChoice(oldValue, newValue, tableView)) {
                int index = getIndex();
                tableView.getItems().get(index).set(0, newValue);
                confirmEditButton.setVisible(true);
              } else {
                skipListener = true;
                choiceBox.setValue(oldValue);
                skipListener = false;
              }
            });


            setGraphic(choiceBox);
          } else {
            textField = new TextField(item.toString());
            textField.setOnAction(e -> {
              int index = getIndex();
              tableView.getItems().get(index).set(1, textField.getText());
              confirmEditButton.setVisible(
                  true);// displays the confirm edit button if you press enter
            });
            textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
              if (!newValue) {
                tableView.getItems().get(getIndex()).set(1, textField.getText());
                confirmEditButton.setVisible(
                    true); // displays confirm button if you press outside off the box
              }
            });
            textField.addEventFilter(KeyEvent.KEY_TYPED, KeyEvent -> {
              String input = KeyEvent.getCharacter();
              String currentText = textField.getText();

              if (!input.matches("[\\d.]") || (input.equals(".") && currentText.contains("."))) {
                if (input.matches("[,]")) {
                  if (tooltip == null) {
                    tooltip = new Tooltip("Please use '.', to indicate decimal point");
                  }
                  tooltip.show(getScene().getWindow());

                  // Set the duration after which the tooltip will be hidden (e.g., 5 seconds)
                  PauseTransition pause = new PauseTransition(Duration.seconds(5));
                  pause.setOnFinished(event -> tooltip.hide());
                  pause.play();
                }
                KeyEvent.consume(); // If it's not a valid character or already contains a decimal point, consume the event
              }
            });
            setGraphic(textField);

          }
        }
      }
    };
  }

  /**
   * Adds a new budget item to the provided table view.
   *
   * @param tableView the table view to add the new budget item
   */
  private static void addBudgetItem(TableView<ObservableList<Object>> tableView) {
    List<String> choiceList = new ArrayList<>();
    String type = tableView == revenues ? "r" : "e";
    for (BudgetCategory category : BudgetCategory.values()) {
      if (category.getType().equals(type)) {
        choiceList.add(category.toString().toLowerCase().replaceAll("_", " "));
      }
    }

    // Find the first available budget category that is not already in the table
    String availableCategory = null;
    for (String category : choiceList) {
      boolean alreadyExists = false;
      for (ObservableList<Object> row : tableView.getItems()) {
        if (row.get(0).toString().equals(category)) {
          alreadyExists = true;
          break;
        }
      }
      if (!alreadyExists) {
        availableCategory = category;
        break;
      }
    }

    // If an available category is found, add a new row with that category
    if (availableCategory != null) {
      double defaultAmount = 0.0;
      ObservableList<Object> newRow = FXCollections.observableArrayList(availableCategory, defaultAmount);
      tableView.getItems().add(newRow);
    }
  }


  /**
   * Deletes the selected budget item from the provided table view.
   *
   * @param tableView the table view from which the selected budget item will be deleted
   */
  private static void deleteBudgetItem(TableView<ObservableList<Object>> tableView) {
    ObservableList<Object> selectedItem = tableView.getSelectionModel().getSelectedItem();
    if (selectedItem != null) {
      tableView.getItems().remove(selectedItem);
      saveModifiedData(); // Save the changes to the file
      updateSavings();
    }
  }

  /**
   * Refreshes the data in the revenues and expenditures tables.
   */
  public static void refreshData() {
    try {
      revenues.setItems(initializeRevenuesData());
      expenditures.setItems(initializeExpenditureData());
      updateSavings();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Saves the modified budget data to the budget file.
   */
  public static void saveModifiedData() {
    try {
      Budget budget = loggedInUser.getBudget();
      if (budget!=null) {
        budget.clearEntries();
      }
      for (ObservableList<Object> row : revenues.getItems()) {
        String categoryString =
            row.get(0) instanceof ChoiceBox ? ((ChoiceBox<String>) row.get(0)).getValue()
                : row.get(0).toString();
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(row.get(1).toString()));

        BudgetCategory category = BudgetCategory.valueOf(
            categoryString.toUpperCase().replaceAll(" ", "_"));
        assert budget != null;
        budget.addBudgetItem(new BudgetItem(category, amount));
      }

      for (ObservableList<Object> row : expenditures.getItems()) {
        String categoryString =
            row.get(0) instanceof ChoiceBox ? ((ChoiceBox<String>) row.get(0)).getValue()
                : row.get(0).toString();
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(row.get(1).toString()));

        BudgetCategory category = BudgetCategory.valueOf(
            categoryString.toUpperCase().replaceAll(" ", "_"));
        assert budget != null;
        budget.addBudgetItem(new BudgetItem(category, amount));
      }

      assert budget != null;
      budgetDAO.setBudget(loggedInUser.getLoginInfo().getUserId(), budget);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Updates the savings value by calculating the difference between the total revenues and total
   * expenditures. Then sets the savings value to the loggedInUser's budget and updates the savings
   * text on the screen.
   */
  private static void updateSavings() {
    BigDecimal revenuesTotal = revenues.getItems().stream()
        .map(row -> new BigDecimal(row.get(1).toString()))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    BigDecimal expendituresTotal = expenditures.getItems().stream()
        .map(row -> new BigDecimal(row.get(1).toString()))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    BigDecimal savingsValue = revenuesTotal.subtract(expendituresTotal);
    if (loggedInUser.getBudget() != null) {
      loggedInUser.getBudget().setSavings(savingsValue);
      savings.setText("Savings: " + loggedInUser.getBudget().getSavings());
    }
  }

  /**
   * Sets the loggedInUser for the budgeting scene.
   *
   * @param user the {@link User} to be set as the loggedInUser
   */
  public static void setLoggedInUser(User user) {
    loggedInUser = user;
  }
  private static boolean validateAndSetNewChoice(String oldValue, String newValue, TableView<ObservableList<Object>> tableView) {
    boolean alreadyExists = false;
    for (ObservableList<Object> row : tableView.getItems()) {
      if (row.get(0).toString().equals(newValue)) {
        alreadyExists = true;
        break;
      }
    }

    if (alreadyExists) {
      // Show a warning message, e.g., using an Alert
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Duplicate Category");
      alert.setHeaderText("This category is already in the table.");
      alert.setContentText("Please select a different category.");
      alert.showAndWait();
    }

    return !alreadyExists;
  }



  /**
   * Creates a new TableView for displaying revenues and configures its properties.
   *
   * @return the configured TableView for displaying revenues
   * @throws IOException if an I/O error occurs
   */
  private static TableView<ObservableList<Object>> createRevenuesTable() throws IOException {
    revenues = ApplicationObjects.newTableView(new String[]{"Revenues", "Amount"}, 40, 70, 380,
        120);
    revenues.setItems(initializeRevenuesData());
    revenues.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    revenues.setFixedCellSize(50);

    revenues.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> {
          if (newValue != null) {
            revenues.setEditable(true);
          }
        });

    revenues.getColumns()
        .forEach(column -> column.setCellFactory(createCustomCellFactory(revenues)));

    return revenues;
  }

  /**
   * Creates a new TableView for displaying expenditures and configures its properties.
   *
   * @return the configured TableView for displaying expenditures
   */
  private static TableView<ObservableList<Object>> createExpendituresTable() {
    expenditures = ApplicationObjects.newTableView(new String[]{"Expenditures", "Budget"}, 40, 210,
        380, 250);
    expenditures.setItems(initializeExpenditureData());
    expenditures.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    expenditures.setFixedCellSize(50);

    expenditures.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> {
          if (newValue != null) {
            expenditures.setEditable(true);
          }
        });

    expenditures.getColumns()
        .forEach(column -> column.setCellFactory(createCustomCellFactory(expenditures)));

    return expenditures;
  }

  /**
   * Creates a new background label with a specific style and dimensions.
   *
   * @return the configured Label for the background
   */
  private static Label createBackgroundLabel() {
    Label backgroundLabel = new Label();
    backgroundLabel.setPrefWidth(400);
    backgroundLabel.setPrefHeight(500);
    backgroundLabel.setLayoutX(30);
    backgroundLabel.setLayoutY(40);
    backgroundLabel.setStyle("-fx-background-color: #f0f0f0;");
    return backgroundLabel;
  }

  /**
   * Creates a new Text object for displaying the "Monthly Budget" title.
   *
   * @return the configured Text object for the "Monthly Budget" title
   */
  private static Text createMonthlyBudgetText() {
    return ApplicationObjects.newText("Monthly Budget", 30, false, 40, 60);
  }

  /**
   * Creates a new Text object for displaying the savings value.
   *
   * @return the configured Text object for the savings value
   */
  private static Text createSavingsText() {
    BigDecimal savingsNum =
        loggedInUser.getBudget() != null ? loggedInUser.getBudget().getSavings() : BigDecimal.ZERO;
    savings = ApplicationObjects.newText("Savings: " + savingsNum, 30, false, 40, 480);
    savings.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
    return savings;
  }

  /**
   * Creates a new Text object for displaying informational text.
   *
   * @return the configured Text object for the informational text
   */
  private static Text createInfoText() {
    return ApplicationObjects.newText("Right click on row, to add or delete budget item", 15, true,
        100, 530);
  }

  /**
   * Creates a new Button for creating a new budget and configures its properties.
   *
   * @return the configured Button for creating a new budget
   */
  private static Button createCreateNewBudgetButton() {
    Button createNewBudget = ApplicationObjects.newButton("Create new budget", 470, 180, 157, 25,
        16);
    createNewBudget.setOnAction(e -> {
      try {
        stage.setScene(CreateNewBudgetScene.scene());
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });
    return createNewBudget;
  }

  /**
   * Creates a new Button for accessing budget suggestions and configures its properties.
   *
   * @return the configured Button for accessing budget suggestions
   */
  private static Button createBudgetSuggestionsButton() {
    Button budgetSuggestions = ApplicationObjects.newButton("Budget suggestion", 470, 270, 157, 25,
        16);
    budgetSuggestions.setOnAction(e -> {
      try {
        stage.setScene(BudgetSuggestionsScene.scene());
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });
    return budgetSuggestions;
  }

  /**
   * Creates a new Button for confirming edits made to budget items and configures its properties.
   *
   * @return the configured Button for confirming budget item edits
   */
  private static Button confirmEditButton() {
    confirmEditButton = ApplicationObjects.newButton("Confirm Edit", 270, 470, 157, 25, 16);
    confirmEditButton.setVisible(false);
    confirmEditButton.setOnAction(event -> {
      saveModifiedData();
      refreshData();
      confirmEditButton.setVisible(false);
    });
    return confirmEditButton;
  }

  /**
   * Creates a new ImageView as a home button and configures its properties.
   *
   * @return the configured ImageView for the home button
   * @throws FileNotFoundException if the image file is not found
   */
  private static ImageView createHomeButton() throws FileNotFoundException {
    ImageView homeButton = ApplicationObjects.newImage("home.png", 10, 10, 20, 20);
    homeButton.setOnMouseClicked(e -> {
      MediaPlayer textToSpeachnew;
      try {
        textToSpeachnew = ApplicationObjects.newSound("mainPageScene");
        if (ApplicationObjects.soundOn()) {
          ApplicationObjects.getPlaying().stop();
          textToSpeachnew.play();
          ApplicationObjects.setPlaying(textToSpeachnew);
        }
      } catch (MalformedURLException e1) {
        e1.printStackTrace();
      }
      try {
        stage.setScene(MainPageScene.scene());
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });
    return homeButton;
  }

  /**
   * Creates a new Button for a drop-down menu and configures its properties.
   *
   * @return the configured Button for the drop-down menu
   */
  private static Button createDropDownButton() {
    return ApplicationObjects.newButton("test", 676, 10, 10, 10, 10);
  }

  /**
   * Creates a new ImageView as a user management button and configures its properties.
   *
   * @return the configured ImageView for the user management button
   * @throws FileNotFoundException if the image file is not found
   */
  private static ImageView createManageUserButton() throws FileNotFoundException {
    return ApplicationObjects.newImage("user.png", 646, 10, 20, 20);
  }

}

