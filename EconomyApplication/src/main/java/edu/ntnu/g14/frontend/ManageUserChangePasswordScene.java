package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;
import java.io.IOException;

import edu.ntnu.g14.FileManagement;
import edu.ntnu.g14.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ManageUserChangePasswordScene {
    static Stage stage = ApplicationFront.getStage();
    private static User loggedInUser = ApplicationFront.loggedInUser;

    static public Scene scene() throws FileNotFoundException, IOException{
        Label loggedInUserLabel = new Label(loggedInUser.getLoginInfo().getUserName());
        loggedInUserLabel.setStyle("-fx-font-size: 40;");
        Label loggedInUserEmail = new Label(loggedInUser.getEmail());
        loggedInUserEmail.setStyle("-fx-font-size: 20;");

        Button cancelButton = ApplicationObjects.newButton("Cancel", 594-329, 399-136, 159, 61, 16);
        Button confirmButton = ApplicationObjects.newButton("Confirm", 761-329, 399-136, 159, 61, 16);
        Text oldPassword = ApplicationObjects.newText("Enter old password", 16, false, 446-329, 494-136);
        Text newPassword = ApplicationObjects.newText("Enter new password", 16, false, 438-329, 528-136);
        Text reNewPassword = ApplicationObjects.newText("Re-enter new password", 16, false, 413-329, 562-136);
        TextField oldPasswordField = ApplicationObjects.newTextField("", 594-329, 476-136, 326, 30, 0);
        TextField newPasswordField = ApplicationObjects.newTextField("", 594-329, 510-136, 326, 30, 0);
        TextField reNewPasswordField = ApplicationObjects.newTextField("", 594-329, 544-136, 326, 30, 0);

        // Show an error message to the user, e.g., a dialog box, saying the old password is incorrect
        confirmButton.setOnAction(event -> {
            String oldPasswordInput = oldPasswordField.getText();
            String newPasswordInput = newPasswordField.getText();
            String reNewPasswordInput = reNewPasswordField.getText();

            if (verifyOldPassword(oldPasswordInput)) {
                if (newPasswordInput.equals(reNewPasswordInput)) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm Password Change");
                    alert.setHeaderText("Are you sure you want to change the Password to: " + newPasswordField.getText() + "?");
                    alert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    updatePassword(newPasswordInput);
                                }
                            });
                    // Show a success message to the user, e.g., a dialog box
                } else {

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
        Group root = new Group(loggedInUserLabel, loggedInUserEmail, cancelButton, confirmButton,
        oldPassword, newPassword, reNewPassword, oldPasswordField,
        newPasswordField, reNewPasswordField, dropDownButton, homeButton, manageUserButton);
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
    private static boolean verifyOldPassword(String oldPasswordInput) {
        String currentPassword = loggedInUser.getLoginInfo().getPassword();
        return oldPasswordInput.equals(currentPassword);
    }

    private static void updatePassword(String newPassword) {
        loggedInUser.getLoginInfo().setPassword(newPassword);
        FileManagement.newEditUser(loggedInUser);
        // Save the updated user data to the database or storage system
    }


}
