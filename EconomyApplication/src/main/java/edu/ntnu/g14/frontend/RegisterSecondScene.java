package edu.ntnu.g14.frontend;

import edu.ntnu.g14.BankApplication;
import edu.ntnu.g14.FileManagement;
import java.io.IOException;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class RegisterSecondScene {

  static Stage stage = BankApplication.getStage();

  static public Scene scene(String key, String email, String usersID) throws IOException {
    MediaPlayer textToSpeach = ApplicationObjects.newSound("registerSecondScene");
    if (ApplicationObjects.soundOn()) {
      ApplicationObjects.getPlaying().stop();
      textToSpeach.play();
      ApplicationObjects.setPlaying(textToSpeach);
    }

    TextField keyInput = ApplicationObjects.newTextField("", 120, 195, 100, 20, 15);
    Button next = ApplicationObjects.newButton("Next", 240, 195, 100, 20, 15);
    next.setOnAction(e -> {
      if (keyInput.getText().replace(" ", "").equals(key.replace(" ", ""))) {
        try {
          BankApplication.setLoggedInUser(FileManagement.readUser(usersID));
        } catch (IOException e1) {
          e1.printStackTrace();
        }
        try {
          stage.setScene(RegisterThirdScene.scene());
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      } else {
        ApplicationObjects.alertBox("ERROR", "Wrong key", "The wrong key has been input");
      }
    });

    Group root = new Group(keyInput, next,
        ApplicationObjects.newText("An email with a confirmation code", 25, false, 60, 25),
        ApplicationObjects.newText("has been sent to", 25, false, 140, 50),
        ApplicationObjects.newText(email, 25, false, 140, 75),
        ApplicationObjects.newText("Code:", 30, false, 20, 225));
    root.getStylesheets().add("StyleSheet.css");
    Scene scene = new Scene(root, 500, 300, ApplicationObjects.getSceneColor());

    return scene;
  }

}
