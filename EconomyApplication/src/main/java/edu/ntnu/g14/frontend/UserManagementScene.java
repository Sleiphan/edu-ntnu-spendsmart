package edu.ntnu.g14.frontend;

import java.io.IOException;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UserManagementScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene() {
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
        delete.setOnAction(event -> stage.setScene(DeleteUserScene.scene()));
        Group root = new Group(loggedInUser, loggedInUserEmail,password, switchUser, edit, delete, logOut, confirm);
        return new Scene(root, 500, 300, Color.WHITE);
    }
    
}
