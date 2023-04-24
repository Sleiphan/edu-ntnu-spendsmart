//TODO: make sure the mouse does not start in the textfield, this makes the prompt text unreadable
//TODO: make it so the user can input the email key with spaces or without. take input and remove spaces

package edu.ntnu.g14;


import java.io.FileInputStream;
import java.io.IOException;

import edu.ntnu.g14.frontend.LoginChooseUserScene;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import edu.ntnu.g14.User;

public class BankApplication extends Application {

    static private Stage stage;
    static User loggedInUser;

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        this.stage = stage;
        stage.setTitle("Bank Application");
        stage.setResizable(false);
        stage.setScene(LoginChooseUserScene.scene());
        stage.getIcons().add(new Image(new FileInputStream("src/main/resources/images/icon.png")));
        stage.show();
    }

    public static Stage getStage(){
        return stage;
    }

    public static void setLoggedInUser(User user){
        loggedInUser = user;
    }



    public static void main(String[] args) throws IOException{
        launch();
    }
}
