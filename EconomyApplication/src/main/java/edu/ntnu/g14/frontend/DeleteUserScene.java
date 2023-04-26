package edu.ntnu.g14.frontend;

import edu.ntnu.g14.BankApplication;
import edu.ntnu.g14.User;
import edu.ntnu.g14.dao.UserDAO;
import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The DeleteUserScene class is responsible for displaying to delete user scene to the user. The
 * scene displays a warning message that asks the user if they want to delete the currently
 * loggedInUser account. The user has 10 seconds to confirm the deletion, during which a countdown
 * timer is displayed. If the user does not confirm the deletion, the delete action is cancelled. If
 * the user confirms the deletion, the user account is deleted from the database and a success
 * message is displayed.
 */
public class DeleteUserScene {

  static Stage stage = BankApplication.getStage();

  static User loggedInUser = BankApplication.loggedInUser;

  static UserDAO userDAO;

  private static boolean cancelPressed = false;

  /**
   * The scene() method creates to delete user scene, which displays a warning message and buttons
   * that allow the user to either confirm or cancel the deletion of the currently loggedInUser
   * account. A countdown timer is displayed that counts down from 10 seconds, and if the user does
   * not confirm the deletion within that time frame, the deletion action is cancelled.
   *
   * @return Returns to delete user scene.
   * @throws IOException if an I/O error occurs
   */
  static public Scene scene() throws IOException {
    Label loggedInUserLabel = new Label(loggedInUser.getLoginInfo().getUserName());
    loggedInUserLabel.setStyle("-fx-font-size: 40;");
    Label loggedInUserEmail = new Label(loggedInUser.getEmail());
    loggedInUserEmail.setStyle("-fx-font-size: 20;");

    // Create a VBox to hold loggedInUser and loggedInUserEmail
    VBox userInfoBox = new VBox(5, loggedInUserLabel, loggedInUserEmail);
    userInfoBox.setAlignment(Pos.CENTER);
    userInfoBox.setLayoutX(220); // Set the VBox's layoutX and layoutY to position it on the screen
    userInfoBox.setLayoutY(75);

    Text warningText = ApplicationObjects.newText("Are you sure you want to delete this user?\n" +
        "This action cannot be undone\n" +
        "10 seconds left", 20, true, 0, 0);
    warningText.setTextAlignment(TextAlignment.CENTER);
    warningText.setVisible(false);
    // Create a countdown timer using a Timeline
    final int[] countdown = {10};
    Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
      if (countdown[0] > 0) {
        countdown[0]--;
        warningText.setText("Are you sure you want to delete this user?\n" +
            "This action cannot be undone\n" +
            countdown[0] + " seconds left");
      }
    }));
    timer.setCycleCount(10);
    Button confirm = ApplicationObjects.newButton("Delete", 0, 0, 157, 25, 16);
    confirm.setTextFill(Paint.valueOf("WHITE"));
    confirm.setOnMousePressed(event -> {
      warningText.setVisible(true);
      timer.play();

      timer.setOnFinished(e -> {
        if (!cancelPressed) {
          try {
            userDAO.deleteUser(loggedInUser.getLoginInfo().getUserId());
          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
          ApplicationObjects.alertBox("Deleted user", "You have deleted user",
              loggedInUser.getLoginInfo().getUserName() + " R.I.P");
          confirm.setStyle("-fx-background-color: red");
          confirm.setTextFill(Paint.valueOf("WHITE"));
        }
      });
    });

    Button cancel = ApplicationObjects.newButton("Cancel", 0, 0, 157, 25, 16);
    cancel.setOnAction(event -> {
      cancelPressed = true;
      timer.stop();
      try {
        stage.setScene(UserManagementScene.scene());
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });

    // Create an VBox to hold warningText, confirm and cancel
    VBox deleteUserBox = new VBox(5, warningText, confirm, cancel);
    deleteUserBox.setAlignment(Pos.CENTER);
    deleteUserBox.setLayoutX(
        145); // Set the VBox's layoutX and layoutY to position it on the screen
    deleteUserBox.setLayoutY(350);

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
    Group root = new Group(userInfoBox, deleteUserBox, dropDownButton, homeButton,
        manageUserButton);
    dropDownButton.setOnAction(e -> root.getChildren().add(dropDown));
    root.getStylesheets().add("StyleSheet.css");
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

    return scene;
  }

}
