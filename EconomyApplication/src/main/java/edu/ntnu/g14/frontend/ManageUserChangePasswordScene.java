package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;
import java.io.IOException;

import edu.ntnu.g14.BankApplication;
import edu.ntnu.g14.FileManagement;
import edu.ntnu.g14.User;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This class represents the user management change password scene, where a user can change their
 * password. It contains methods for creating and displaying the scene, and handling the user's
 * input.
 */
public class ManageUserChangePasswordScene {

  static Stage stage = BankApplication.getStage();
  private static final User loggedInUser = BankApplication.loggedInUser;

  /**
   * Creates and returns a Scene object for changing the password of a user.
   *
   * @return the Scene object for changing the user's password
   * @throws FileNotFoundException if the file is not found
   * @throws IOException           if an I/O exception occurs
   */

  static public Scene scene() throws FileNotFoundException, IOException {
    Label loggedInUserLabel = new Label(loggedInUser.getLoginInfo().getUserName());
    loggedInUserLabel.setStyle("-fx-font-size: 40;");
    Label loggedInUserEmail = new Label(loggedInUser.getEmail());
    loggedInUserEmail.setStyle("-fx-font-size: 20;");

    VBox userInfoBox = new VBox(5, loggedInUserLabel, loggedInUserEmail);
    userInfoBox.setAlignment(Pos.CENTER);
    userInfoBox.setLayoutX(220);
    userInfoBox.setLayoutY(75);

    Button cancelButton = ApplicationObjects.newButton("Cancel", 594 - 329, 399 - 136, 159, 61, 16);
    Button confirmButton = ApplicationObjects.newButton("Confirm", 761 - 329, 399 - 136, 159, 61,
        16);
    Text oldPassword = ApplicationObjects.newText("Enter old password", 16, false, 446 - 329,
        494 - 136);
    Text newPassword = ApplicationObjects.newText("Enter new password", 16, false, 438 - 329,
        528 - 136);
    Text reNewPassword = ApplicationObjects.newText("Re-enter new password", 16, false, 413 - 329,
        562 - 136);
    TextField oldPasswordField = ApplicationObjects.newTextField("", 594 - 329, 476 - 136, 326, 30,
        0);
    TextField newPasswordField = ApplicationObjects.newTextField("", 594 - 329, 510 - 136, 326, 30,
        0);
    TextField reNewPasswordField = ApplicationObjects.newTextField("", 594 - 329, 544 - 136, 326,
        30, 0);

    // Show an error message to the user, e.g., a dialog box, saying the old password is incorrect
    confirmButton.setOnAction(event -> {
      String oldPasswordInput = oldPasswordField.getText();
      String newPasswordInput = newPasswordField.getText();
      String reNewPasswordInput = reNewPasswordField.getText();

      if (verifyOldPassword(oldPasswordInput)) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        if (newPasswordInput.equals(reNewPasswordInput)) {
          alert.setTitle("Confirm Password Change");
          alert.setHeaderText(
              "Are you sure you want to change the Password to: " + newPasswordField.getText()
                  + "?");
          alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
              updatePassword(newPasswordInput);
            }
          });
          // Show a success message to the user, e.g., a dialog box
        } else {
          alert.setTitle("Try Again");
          alert.setHeaderText("The you have to enter the new password twice");
          alert.setContentText(newPasswordInput + " is not the same as: " + reNewPasswordInput);
          alert.showAndWait();
        }
        // Show an error message to the user, e.g., a dialog box, saying the new passwords do not match
      }
      try {
        stage.setScene(UserManagementScene.scene());
      } catch (IOException e) {
        throw new RuntimeException(e);
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
    Group root = new Group(userInfoBox, cancelButton, confirmButton,
        oldPassword, newPassword, reNewPassword, oldPasswordField,
        newPasswordField, reNewPasswordField, dropDownButton, homeButton, manageUserButton);
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

  /**
   * Verifies the old password by checking if the currentPassword, is equal to the oldPasswordInput
   *
   * @param oldPasswordInput the old password entered by user
   * @return true if equal, fasle if not
   */
  private static boolean verifyOldPassword(String oldPasswordInput) {
    String currentPassword = loggedInUser.getLoginInfo().getPassword();
    return oldPasswordInput.equals(currentPassword);
  }

  /**
   * Sets the newPassword to loggedInUser, and writes to file management.
   *
   * @param newPassword new password entered by User
   */
  private static void updatePassword(String newPassword) {
    loggedInUser.getLoginInfo().setPassword(newPassword);
    FileManagement.newEditUser(loggedInUser);
    // Save the updated user data to the database or storage system
  }


}
