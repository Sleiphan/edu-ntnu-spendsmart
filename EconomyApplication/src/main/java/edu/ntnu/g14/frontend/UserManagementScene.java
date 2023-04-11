package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;
import java.io.IOException;

import edu.ntnu.g14.User;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UserManagementScene {
    static Stage stage = ApplicationFront.getStage();

    static public Scene scene() throws FileNotFoundException {
        User currentUser = ApplicationFront.getLoggedInUser();
        Text loggedInUser = ApplicationObjects.newText(currentUser.getFullName(), 40, false, 0, 0);
        Text loggedInUserEmail = ApplicationObjects.newText(currentUser.getEmail(), 20, false, 0, 0);

        // Create a VBox to hold loggedInUser and loggedInUserEmail
        VBox userInfoBox = new VBox(5, loggedInUser, loggedInUserEmail);
        userInfoBox.setAlignment(Pos.CENTER);
        userInfoBox.setLayoutX(220); // Set the VBox's layoutX and layoutY to position it on the screen
        userInfoBox.setLayoutY(75);

        Button password = ApplicationObjects.newButton("Password", 0, 0 ,"white", "grey", 157,60,16);
        password.setOnAction(event -> {
           try {
               stage.setScene(ManageUserChangePasswordScene.scene());
           } catch (FileNotFoundException e) {
               throw new RuntimeException(e);
           }
        });
        Button switchUser = ApplicationObjects.newButton("Switch User", 0, 0,"white", "grey", 157,60,16);
        Button logOut = ApplicationObjects.newButton("Log out", 0, 0,"white", "grey", 157,50,16);
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
        HBox switchUserBox = new HBox(5, switchUser, logOut);

        Button edit = ApplicationObjects.newButton("Edit", 0, 0,"white", "grey", 157,60,16);
        Button confirm = ApplicationObjects.newButton("confirm", 0, 0,"white", "grey", 157,50,16);
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

        HBox editBox = new HBox(5, edit, confirm);


        Button delete = ApplicationObjects.newButton("Delete", 0, 0,"white", "grey", 157,60,16);
        delete.setOnAction(event -> {
            try {
                stage.setScene(DeleteUserScene.scene());
            } catch (FileNotFoundException e) {
                
                e.printStackTrace();
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
        buttonGrid.add(confirm, 1, 2);
        buttonGrid.add(delete, 0, 3);

        // Set column constraints to make sure buttons are aligned
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setMinWidth(157);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setMinWidth(157);
        buttonGrid.getColumnConstraints().addAll(column1, column2);

        buttonGrid.setAlignment(Pos.CENTER);
        buttonGrid.setLayoutX(260); // Set the GridPane's layoutX and layoutY to position it on the screen
        buttonGrid.setLayoutY(300);
        
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
        Group root = new Group(userInfoBox, buttonGrid,  dropDownButton, homeButton, manageUserButton);
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
