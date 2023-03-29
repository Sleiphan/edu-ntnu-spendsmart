//TODO: make sure the mouse does not start in the textfield, this makes the prompt text unreadable
//TODO: make it so the user can input the email key with spaces or without. take input and remove spaces

package edu.ntnu.g14.frontend;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import edu.ntnu.g14.User;

public class ApplicationFront extends Application {

    static private Stage stage;
    static User loggedInUser;

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        this.stage = stage;
        stage.setResizable(false);
        stage.setScene(LoginChooseUserScene.scene());
        stage.show();
    }

    public static Stage getStage(){
        return stage;
    }

    public static User getLoggedInUser(){
        return loggedInUser;
    }

    public static void setLoggedInUser(User user){
        loggedInUser = user;
    }
    
    public static void main(String[] args) {
        launch();
    }
}
