package edu.ntnu.g14.frontend;

import edu.ntnu.g14.BankApplication;
import edu.ntnu.g14.FileManagement;
import edu.ntnu.g14.Login;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginChooseUserScene {

  static Stage stage = BankApplication.getStage();

  static public Scene scene() throws IOException {
    MediaPlayer textToSpeach = ApplicationObjects.newSound("loginChooseUserScene");
    ApplicationObjects.setPlaying(textToSpeach);

    Text chooseUser = ApplicationObjects.newText("Choose user", 30, false, 0, 40);
    Text registerNew = ApplicationObjects.newText("Register new account", 10, true, 400, 280);
    registerNew.setOnMouseClicked(e -> {
      try {
        stage.setScene(RegisterFirstScene.scene());
      } catch (FileNotFoundException e1) {
        e1.printStackTrace();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });

    Login[] loginsTemp = FileManagement.readLogins();
    if (loginsTemp == null) {
      loginsTemp = new Login[0];
    }
    final Login[] logins = loginsTemp;

    String[] usernames = new String[logins.length];
    for (int i = 0; i < logins.length; i++) {
      usernames[i] = logins[i].getUserName();
    }

    ComboBox<String> user = ApplicationObjects.newComboBox(usernames, 250, 20, 15, 125, 70);
    user.setPromptText("Choose Your User");
    Button confirm = ApplicationObjects.newButton("Confirm", 0, 110, 150, 20, 15);
    Button soundButton = ApplicationObjects.newButton("Sound on?", 10, 260, 150, 20, 15);
    soundButton.setOnAction(e -> {
      if (soundButton.getText().equals("Sound on?") || soundButton.getText().equals("OFF")) {
        soundButton.setText("ON");
        ApplicationObjects.setSound(true);
        textToSpeach.play();
      } else {
        soundButton.setText("OFF");
        ApplicationObjects.setSound(false);
      }
    });

    confirm.setLayoutX(250 - 75);
    confirm.setOnAction(e -> {
      if (user.getValue() != null) {
        try {
          String usersID = "";
          for (int i = 0; i < logins.length; i++) {
            if (user.getValue().equals(logins[i].getUserName())) {
              usersID = logins[i].getUserId();
            }
          }
          BankApplication.setLoggedInUser(FileManagement.readUser(usersID));
          stage.setScene(LoginUserScene.scene());
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    });

    Group root = new Group(chooseUser, registerNew, user, confirm, soundButton);
    root.getStylesheets().add("StyleSheet.css");
    Scene scene = new Scene(root, 500, 300, ApplicationObjects.getSceneColor());
    Platform.runLater(
        () -> chooseUser.setLayoutX(250 - chooseUser.getLayoutBounds().getWidth() / 2));
    return scene;
  }

}
