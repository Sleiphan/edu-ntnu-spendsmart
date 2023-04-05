package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UserManagementScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene() throws FileNotFoundException {
        Text loggedInUser = ApplicationObjects.newText("Walter Banks", 60, false, 600, 150);
        Text loggedInUserEmail = ApplicationObjects.newText("walBa76@gmail.com", 20, false, 685, 200);

        Button password = ApplicationObjects.newButton("Password", 700, 400 ,"white", "grey", 157,60,16);
       // password.setOnAction(event -> stage.setScene(createNewPasswordScene()));

        Button switchUser = ApplicationObjects.newButton("Switch User", 700, 470,"white", "grey", 157,60,16);
        Button logOut = ApplicationObjects.newButton("Log out", 870, 470,"white", "grey", 157,50,16);
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
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        Button edit = ApplicationObjects.newButton("Edit", 700, 520,"white", "grey", 157,60,16);
        Button confirm = ApplicationObjects.newButton("confirm", 870, 520,"white", "grey", 157,50,16);
        confirm.setVisible(false);
        edit.setOnAction(event -> {
            if (edit.getText().equals("Edit")) {
                edit.setText("Cancel");
                confirm.setVisible(true);
                loggedInUser.setText("Edited User");
                loggedInUserEmail.setText("Edited User Email");

            } else {
                edit.setText("Edit");
                confirm.setVisible(false);
                loggedInUser.setText("Walter Banks");
                loggedInUserEmail.setText("walBa76@gmail.com");
            }
        });
        confirm.setOnAction(event -> {loggedInUser.setText("Edited User");
            loggedInUserEmail.setText("Edited User Email"); confirm.setVisible(false);
            edit.setText("Edit");});


        Button delete = ApplicationObjects.newButton("Delete", 700, 570,"white", "grey", 157,60,16);
        delete.setOnAction(event -> {
            try {
                stage.setScene(DeleteUserScene.scene());
            } catch (FileNotFoundException e) {
                
                e.printStackTrace();
            }
        });
        
        ImageView homeButton = ApplicationObjects.newImage("home.png", 10, 10, 20, 20);
        homeButton.setOnMouseClicked(e -> {
            try {
                stage.setScene(MainPageScene.scene());
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        Button dropDownButton = ApplicationObjects.newButton("test", 676, 10, "black", "white", 10, 10, 10);
        Group dropDown = ApplicationObjects.dropDownMenu();
        ImageView manageUserButton = ApplicationObjects.newImage("user.png", 646, 10, 20, 20);
        Group root = new Group(loggedInUser, loggedInUserEmail,password, switchUser,
         edit, delete, logOut, confirm, dropDownButton, homeButton, manageUserButton);
        dropDownButton.setOnAction(e -> {
            root.getChildren().add(dropDown);
        });

        Scene scene = new Scene(root, 728, 567, Color.WHITE);
        
    
        Group userButtons = ApplicationObjects.userMenu();
        manageUserButton.setOnMouseEntered(e -> {
            root.getChildren().add(userButtons);
        });
        scene.setOnMouseClicked(e -> {
            root.getChildren().remove(userButtons);
            root.getChildren().remove(dropDown);
        });
        return scene;
    }
    
}
