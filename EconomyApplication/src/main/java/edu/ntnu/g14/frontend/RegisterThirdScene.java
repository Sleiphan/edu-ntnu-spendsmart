package edu.ntnu.g14.frontend;

import edu.ntnu.g14.BankApplication;
import java.io.IOException;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class RegisterThirdScene {

  static Stage stage = BankApplication.getStage();

  static public Scene scene() throws IOException {
    MediaPlayer textToSpeach = ApplicationObjects.newSound("registerThirdScene");
    ApplicationObjects.setPlaying(textToSpeach);
    if (ApplicationObjects.soundOn()) {
      ApplicationObjects.getPlaying().stop();
      textToSpeach.play();
    }
    Button yes = ApplicationObjects.newButton("Yes", 200, 120, 50, 20, 15);
    yes.setOnAction(e -> {
      try {
        stage.setScene(AccountOverviewScene.scene(java.util.Optional.empty()));
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });

    Group root = new Group(yes,
        ApplicationObjects.newText("Would you like to add", 25, false, 130, 40),
        ApplicationObjects.newText("account information", 25, false, 130, 80));
    root.getStylesheets().add("StyleSheet.css");
    Scene scene = new Scene(root, 500, 300, ApplicationObjects.getSceneColor());

    return scene;
  }

}
