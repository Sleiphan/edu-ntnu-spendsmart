package edu.ntnu.g14.frontend;

import java.io.IOException;

import edu.ntnu.g14.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginForgotPasswordSecondScene {

  static Stage stage = BankApplication.getStage();


  static public Scene scene() throws IOException {
    MediaPlayer textToSpeach = ApplicationObjects.newSound("loginForgotPasswordSecondScene");
    if (ApplicationObjects.soundOn()) {
      textToSpeach.play();
    }

    TextField newPassword = ApplicationObjects.newTextField("New password", 125, 70, 250, 20, 15);
    TextField retypeNewPassword = ApplicationObjects.newTextField("Retype new password", 125, 110,
        250, 20, 15);

    Button confirmButton = ApplicationObjects.newButton("Confirm", 175, 150, 150, 20, 15);
    confirmButton.setOnAction(e -> {
      if (newPassword.getText().equals(retypeNewPassword.getText()) && !newPassword.getText()
          .equals("")) {
        Login newLogin = new Login(BankApplication.loggedInUser.getLoginInfo().getUserName(),
            newPassword.getText(), BankApplication.loggedInUser.getLoginInfo().getUserId());
        User newPasswordUser = new User(BankApplication.loggedInUser.getAccounts(),
            BankApplication.loggedInUser.getAllInvoices().toArray(Invoice[]::new),
            newLogin, BankApplication.loggedInUser.getEmail(),
            BankApplication.loggedInUser.getLastName(),
            BankApplication.loggedInUser.getFirstName(),
            BankApplication.loggedInUser.getTransactions(),
            BankApplication.loggedInUser.getBudget());

        FileManagement.newEditUser(newPasswordUser);
        try {
          stage.setScene(LoginChooseUserScene.scene());
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      } else {
        ApplicationObjects.alertBox("ERROR", "Password dont match",
            "Please insert matching passwords!");
      }
    });
    Text goBack = ApplicationObjects.newText("Go back", 10, true, 400, 260);

    Group root = new Group(goBack, newPassword, retypeNewPassword, confirmButton);
    root.getStylesheets().add("StyleSheet.css");
    Scene scene = new Scene(root, 500, 300, ApplicationObjects.getSceneColor());

    return scene;
  }

}
