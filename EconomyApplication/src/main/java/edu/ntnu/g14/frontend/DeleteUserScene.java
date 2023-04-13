package edu.ntnu.g14.frontend;

import java.io.FileNotFoundException;
import java.io.IOException;

import edu.ntnu.g14.User;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.util.Duration;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class DeleteUserScene {

    private VBox vBox;

    private StackPane stackPane;
    static Stage stage = ApplicationFront.getStage();

    private static boolean cancelPressed = false;


    static public Scene scene() throws FileNotFoundException {
        User currentUser = ApplicationFront.loggedInUser;
        Text loggedInUser = ApplicationObjects.newText(currentUser.getFullName(), 40, false, 0, 0);
        Text loggedInUserEmail = ApplicationObjects.newText(currentUser.getEmail(), 20, false, 0, 0);


        // Create a VBox to hold loggedInUser and loggedInUserEmail
        VBox userInfoBox = new VBox(5, loggedInUser, loggedInUserEmail);
        userInfoBox.setAlignment(Pos.CENTER);
        userInfoBox.setLayoutX(220); // Set the VBox's layoutX and layoutY to position it on the screen
        userInfoBox.setLayoutY(75);


        Text warningText = ApplicationObjects.newText("Are you sure you want to delete this user?\n" +
                "This action cannot be undone\n" +
                "10 seconds left", 20, true, 0, 0);
        warningText.setTextAlignment(TextAlignment.CENTER);
        warningText.setVisible(false);
        // Create a countdown timer using a Timeline
        final int[] countdown = {10};
        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (countdown[0] > 0) {
                countdown[0]--;
                warningText.setText("Are you sure you want to delete this user?\n" +
                        "This action cannot be undone\n" +
                        countdown[0] + " seconds left");
            }
        }));
        timer.setCycleCount(10);
        Button confirm = ApplicationObjects.newButton("Delete", 0, 0, 157,25,16);
        confirm.setTextFill(Paint.valueOf("WHITE"));
        confirm.setOnMousePressed(event -> {
            warningText.setVisible(true);
            timer.play();

            timer.setOnFinished(e -> {
                if (!cancelPressed) {
                    //TODO: remove user
                    ApplicationObjects.alertBox("Deleted user", "You have deleted user", currentUser.getFullName() + " R.I.P");
                    confirm.setStyle("-fx-background-color: red");
                    confirm.setTextFill(Paint.valueOf("WHITE"));
                }
            });
        });

        Button cancel = ApplicationObjects.newButton("Cancel", 0, 0, 157,25,16);
        cancel.setOnAction(event -> {
            cancelPressed = true;
            timer.stop();
            try {
                stage.setScene(UserManagementScene.scene());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });



        // Create an VBox to hold warningText, confirm and cancel
        VBox deleteUserBox = new VBox(5, warningText, confirm, cancel);
        deleteUserBox.setAlignment(Pos.CENTER);
        deleteUserBox.setLayoutX(180); // Set the VBox's layoutX and layoutY to position it on the screen
        deleteUserBox.setLayoutY(350);


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
        Group root = new Group(userInfoBox, deleteUserBox, dropDownButton, homeButton, manageUserButton);
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
