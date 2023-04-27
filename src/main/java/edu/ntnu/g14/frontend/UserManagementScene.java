package edu.ntnu.g14.frontend;

import edu.ntnu.g14.BankApplication;
import edu.ntnu.g14.model.User;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * UserManagementScene class represents the user management page in the frontend of the bank
 * application. It allows the logged-in user to view and manage their account information, including
 * changing their password, switching user, logging out, editing their user profile, and deleting
 * their account.
 */
public class UserManagementScene {

  static Stage stage = BankApplication.getStage();
  private static User loggedInUser = BankApplication.loggedInUser;

  /**
   * Creates and returns the user management scene.
   *
   * @return the user management scene
   * @throws IOException if an I/O error occurs
   */
  static public Scene scene() throws IOException {
    Label loggedInUserLabel = new Label(loggedInUser.getLoginInfo().getUserName());
    loggedInUserLabel.setStyle("-fx-font-size: 40;");
    Label loggedInUserEmail = new Label(loggedInUser.getEmail());
    loggedInUserEmail.setStyle("-fx-font-size: 20;");

    Button password = ApplicationObjects.newButton("Password", 0, 0, 157, 60, 16);
    password.setOnAction(event -> {
      try {
        stage.setScene(ManageUserChangePasswordScene.scene());
      } catch (IOException e1) {
        throw new RuntimeException();
      }
    });
    Button switchUser = ApplicationObjects.newButton("Switch User", 0, 0, 157, 60, 16);
    Button logOut = ApplicationObjects.newButton("Log out", 0, 0, 157, 50, 16);
    logOut.setVisible(false);

    switchUser.setOnAction(event -> {
      if (switchUser.getText().equals("Switch User")) {
        switchUser.setText("Cancel");
        logOut.setVisible(true);

      } else {
        switchUser.setText("Switch User");
        logOut.setVisible(false);
      }
    });

    logOut.setOnAction(event -> {
      try {
        stage.setScene(LoginChooseUserScene.scene());
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

    VBox userInfoBox = new VBox(5, loggedInUserLabel, loggedInUserEmail);
    userInfoBox.setAlignment(Pos.CENTER);
    userInfoBox.setLayoutX(220);
    userInfoBox.setLayoutY(75);

    Button edit = ApplicationObjects.newButton("Edit", 0, 0, 157, 60, 16);
    edit.setOnAction(event -> {
      try {
        stage.setScene(UserManagementEditUserScene.scene());
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      }
    });

    Button delete = ApplicationObjects.newButton("Delete", 0, 0, 157, 60, 16);
    delete.setOnAction(event -> {
      try {
        stage.setScene(DeleteUserScene.scene());
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });

    // Create a GridPane to hold the buttons
    GridPane buttonGrid = new GridPane();
    buttonGrid.setVgap(5);
    buttonGrid.setHgap(5);
    buttonGrid.add(password, 0, 0);
    buttonGrid.add(switchUser, 0, 1);
    buttonGrid.add(logOut, 1, 1);
    buttonGrid.add(edit, 0, 2);
    buttonGrid.add(delete, 0, 3);

    // Set column constraints to make sure buttons are aligned
    ColumnConstraints column1 = new ColumnConstraints();
    column1.setMinWidth(157);
    ColumnConstraints column2 = new ColumnConstraints();
    column2.setMinWidth(157);
    buttonGrid.getColumnConstraints().addAll(column1, column2);

    buttonGrid.setAlignment(Pos.CENTER);
    buttonGrid.setLayoutX(
        260); // Set the GridPane's layoutX and layoutY to position it on the screen
    buttonGrid.setLayoutY(300);

    ImageView homeButton = ApplicationObjects.newImage("home.png", 10, 10, 20, 20);
    homeButton.setOnMouseClicked(e -> {
      try {
        stage.setScene(MainPageScene.scene());
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });

    Button soundButton = ApplicationObjects.newButton("Sound on?", 10, 537, 150, 20, 15);
    soundButton.setOnAction(e -> {
      if (soundButton.getText().equals("Sound on?") || soundButton.getText().equals("OFF")) {
        soundButton.setText("ON");
        ApplicationObjects.setSound(true);
      } else {
        soundButton.setText("OFF");
        ApplicationObjects.setSound(false);
      }
    });

    Button dropDownButton = ApplicationObjects.newButton("test", 676, 10, 10, 10, 10);
    Group dropDown = ApplicationObjects.dropDownMenu();
    ImageView manageUserButton = ApplicationObjects.newImage("user.png", 646, 10, 20, 20);
    Group root = new Group(userInfoBox, buttonGrid, dropDownButton, homeButton, manageUserButton,
        soundButton);

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
