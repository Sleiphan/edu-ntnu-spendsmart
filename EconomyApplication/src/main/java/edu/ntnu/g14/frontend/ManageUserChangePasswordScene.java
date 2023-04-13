package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;

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

    static public Scene scene() throws FileNotFoundException{
        String email = "xxputinxx@gmail.com";
        String name = "vladimir putin";
        Text nameText = ApplicationObjects.newText(name, 40, false, 563-329, 234-136);
        Text emailText = ApplicationObjects.newText(email, 17, false, 616-329, 283-136);

        Button cancelButton = ApplicationObjects.newButton("Cancel", 594-329, 399-136, 159, 61, 16);
        Button confirmButton = ApplicationObjects.newButton("Confirm", 761-329, 399-136, 159, 61, 16);
        Text oldPassword = ApplicationObjects.newText("Enter old password", 16, false, 446-329, 494-136);
        Text newPassword = ApplicationObjects.newText("Enter new password", 16, false, 438-329, 528-136);
        Text reNewPassword = ApplicationObjects.newText("Re-enter new password", 16, false, 413-329, 562-136);
        TextField oldPasswordField = ApplicationObjects.newTextField("", 594-329, 476-136, 326, 30, 0);
        TextField newPasswordField = ApplicationObjects.newTextField("", 594-329, 510-136, 326, 30, 0);
        TextField reNewPasswordField = ApplicationObjects.newTextField("", 594-329, 544-136, 326, 30, 0);

        ImageView homeButton = ApplicationObjects.newImage("home.png", 10, 10, 20, 20);
        homeButton.setOnMouseClicked(e -> {
            try {
                stage.setScene(MainPageScene.scene());
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        Button dropDownButton = ApplicationObjects.newButton("test", 676, 10, 10, 10, 10);
        Group dropDown = ApplicationObjects.dropDownMenu();
        ImageView manageUserButton = ApplicationObjects.newImage("user.png", 646, 10, 20, 20);
        Group root = new Group(nameText, emailText, cancelButton, confirmButton,
        oldPassword, newPassword, reNewPassword, oldPasswordField,
        newPasswordField, reNewPasswordField, dropDownButton, homeButton, manageUserButton);
        dropDownButton.setOnAction(e -> {
            root.getChildren().add(dropDown);
        });
        root.getStylesheets().add("StyleSheet.css"); 
        Scene scene = new Scene(root, 728, 567, ApplicationObjects.getSceneColor());
       
        
        Group userButtons = ApplicationObjects.userMenu();
        manageUserButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event){
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
