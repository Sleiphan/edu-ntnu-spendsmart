package edu.ntnu.g14.frontend;

// ... (other import statements)

import edu.ntnu.g14.BankApplication;
import edu.ntnu.g14.FileManagement;
import edu.ntnu.g14.User;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This class represents the user management edit user scene, where a user can edit their username
 * and email. It contains methods for creating and displaying the scene, and handling the user's
 * input.
 */

public class UserManagementEditUserScene {

  private static User loggedInUser = BankApplication.loggedInUser;
  static Stage stage = BankApplication.getStage();

  /**
   * Creates and returns the user management edit user scene.
   *
   * @return the user management edit user scene
   * @throws FileNotFoundException if the necessary files are not found
   */
  public static Scene scene() throws FileNotFoundException {
    Text title = ApplicationObjects.newText("Edit Username and Password", 30, false, 180, 60);
    TextField editUsernameField = ApplicationObjects.newTextField("Enter new Username", 0, 0, 130,
        38, 15);
    editUsernameField.setStyle(ApplicationObjects.setStyleString("black", "white", 130, 38, 15));
    TextField editEmailField = ApplicationObjects.newTextField("Enter new Email", 0, 0, 130, 38,
        15);
    editEmailField.setStyle(ApplicationObjects.setStyleString("black", "white", 130, 38, 15));

    Button confirmUsername = ApplicationObjects.newButton("Confirm username", 0, 0, 157, 25, 16);
    confirmUsername.setOnAction(event -> {
      Alert usernameAlert = new Alert(Alert.AlertType.CONFIRMATION);
      usernameAlert.setTitle("Confirm Username Change");
      usernameAlert.setHeaderText(
          "Are you sure you want to change the username to: " + editUsernameField.getText() + "?");
      usernameAlert.setContentText(
          "Changing username form: " + loggedInUser.getLoginInfo().getUserName() + " to: "
              + editUsernameField.getText());
      usernameAlert.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
          loggedInUser.getLoginInfo().setUserName(editUsernameField.getText());
          // Save the updated user data to the database or storage system
          FileManagement.newEditUser(loggedInUser);
        }
      });
      if (loggedInUser.getLoginInfo().getUserName().equals(editUsernameField.getText())) {
        usernameAlert.setTitle("Confirm Username Change");
        usernameAlert.setHeaderText(
            "This username is already in use: " + editUsernameField.getText());
        usernameAlert.setContentText("Please enter a new username");
      }
    });

    Button confirmEmail = ApplicationObjects.newButton("Confirm Email", 0, 0, 157, 25, 16);
    confirmEmail.setOnAction(event -> {
      Alert emailAlert = new Alert(Alert.AlertType.CONFIRMATION);
      emailAlert.setTitle("Confirm Email Change");
      emailAlert.setHeaderText(
          "Are you sure you want to change the Email to: " + editEmailField.getText() + "?");
      emailAlert.setContentText(
          "Changing Email form: " + loggedInUser.getEmail() + " to: " + editEmailField.getText());
      emailAlert.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
          loggedInUser.setEmail(editEmailField.getText());
          // Save the updated user data to the database or storage system
          FileManagement.newEditUser(loggedInUser);

        }
      });
      if (loggedInUser.getLoginInfo().getUserName().equals(editUsernameField.getText())) {
        emailAlert.setTitle("Confirm Email Change");
        emailAlert.setHeaderText("This user Email is already in use: " + editEmailField.getText());
        emailAlert.setContentText("Please enter a new Email");
      }
    });

    HBox usernameRow = new HBox(editUsernameField, confirmUsername);
    usernameRow.setSpacing(5);
    usernameRow.setAlignment(Pos.CENTER_RIGHT);

    HBox emailRow = new HBox(editEmailField, confirmEmail);
    emailRow.setSpacing(5);
    emailRow.setAlignment(Pos.CENTER_RIGHT);

    VBox editUserBox = new VBox(5, usernameRow, emailRow);
    editUserBox.setLayoutX(220);
    editUserBox.setLayoutY(200);
    editUserBox.setAlignment(Pos.CENTER);

    Button goBack = ApplicationObjects.newButton("Go back", 0, 0, 157, 25, 16);
    goBack.setAlignment(Pos.CENTER);
    goBack.setLayoutX(400);
    goBack.setLayoutY(400);
    goBack.setOnAction(event -> {
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
    Group root = new Group(editUserBox, goBack, title, dropDownButton, homeButton,
        manageUserButton);

    dropDownButton.setOnAction(e -> {
      root.getChildren().add(dropDown);
    });
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

