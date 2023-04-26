package edu.ntnu.g14.frontend;

import static edu.ntnu.g14.BankApplication.loggedInUser;
import static edu.ntnu.g14.FileManagement.writeTransaction;

import edu.ntnu.g14.BankApplication;
import edu.ntnu.g14.BudgetCategory;
import edu.ntnu.g14.Transaction;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * This class represents the scene for transferring money between accounts. It contains a text field
 * for input of the account number to transfer from, the amount to transfer, a description of the
 * transaction, the account number to transfer to, and a category for the transaction.
 *
 * @author G14
 * @version 1.0
 * @since 2023-04-25
 */
public class TransferScene {

  static Stage stage = BankApplication.getStage();

  /**
   * Creates a scene for transferring money between accounts.
   *
   * @return Scene
   * @throws IOException if input is invalid
   */
  static public Scene scene() throws IOException {
    int x = 290;
    int y = 50;
    int n = 85;
    int m = n - 5;

    //Creates text fields for input
    TextField fromAccount = ApplicationObjects.newTextField("12345678910", x, y, 200, 20, 15);
    TextField amount = ApplicationObjects.newTextField("50kr", x, y + n, 100, 20, 15);
    TextField description = ApplicationObjects.newTextField("Description", x, y + 2 * n, 200, 20,
        15);
    TextField toAccount = ApplicationObjects.newTextField("10987654321", x, y + 3 * n, 100, 20, 15);

    String[] categoryChoices = {"Food and Drink", "Clothes and Shoes", "Personal Care", "Leisure",
        "Travel", "Alcohol and Tobacco", "Other", "Payment", "Business"};
    ComboBox<String> category = ApplicationObjects.newComboBox(categoryChoices, 200, 20, 15, x,
        y + 4 * n - 25);
    category.setPromptText("Category of Payment");

    //Creates buttons for transfer and cancel, and sets their actions
    Button transfer = ApplicationObjects.newButton("Transfer", x - 50, y + 3 * n + 125, 100, 20,
        15);
    Button cancel = ApplicationObjects.newButton("Cancel", x + 70, y + 3 * n + 125, 100, 20, 15);
    transfer.setOnAction(e -> {
      try {
        LocalDate dateOfTransaction = LocalDate.now();
        Transaction transaction = new Transaction(fromAccount.getText(), toAccount.getText(),
            new BigDecimal(amount.getText()), description.getText(), dateOfTransaction,
            BudgetCategory.valueOf(category.getValue().replaceAll(" ", "_").toUpperCase()));
        try {
          writeTransaction(loggedInUser.getLoginInfo().getUserId(), transaction);
        } catch (Exception e1) {
          e1.printStackTrace();
        }
        stage.setScene(MainPageScene.scene());
      } catch (IOException e1) {

        e1.printStackTrace();
      }
    });
    cancel.setOnAction(e -> {
      try {
        stage.setScene(MainPageScene.scene());
      } catch (IOException e1) {

        e1.printStackTrace();
      }
    });

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
    Group root = new Group(fromAccount, amount, description, toAccount, category,
        ApplicationObjects.newText("From account", 30, false, x, y - 5),
        ApplicationObjects.newText("Amount:", 30, false, x, y + m),
        ApplicationObjects.newText("Description:", 30, false, x, y + 2 * m),
        ApplicationObjects.newText("To account:", 30, false, x, y + 3 * m),
        transfer, cancel, dropDownButton, homeButton, manageUserButton);

    dropDownButton.setOnAction(e -> {
      root.getChildren().add(dropDown);
    });

    root.getStylesheets().add("StyleSheet.css");
    Scene scene = new Scene(root, 728, 567, ApplicationObjects.getSceneColor());

    Group userButtons = ApplicationObjects.userMenu();
    manageUserButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        root.getChildren().add(userButtons);
        event.consume();
      }
    });
    scene.setOnMouseClicked(e -> {
      root.getChildren().remove(userButtons);
      root.getChildren().remove(dropDown);
    });

    return scene;
  }

}
