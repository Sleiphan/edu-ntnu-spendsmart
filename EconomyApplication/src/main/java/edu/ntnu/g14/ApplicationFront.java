package edu.ntnu.g14;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;




public class ApplicationFront extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        stage.setScene(loginChooseUser());
        stage.show();
    }

    public Scene loginChooseUser(){
        Text chooseUser = new Text("Choose user");
        chooseUser.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
        chooseUser.setX(170);
        chooseUser.setY(40);

        Text registerNew = new Text("Register new account?");
        registerNew.setFont(Font.font("Times New Roman", 10));
        registerNew.setUnderline(true);
        registerNew.setX(400);
        registerNew.setY(280);

        ChoiceBox<String> user = new ChoiceBox();
        user.setStyle("-fx-border-color: #121111;" + 
        "-fx-background-color: #db9393" +
        "-fx-min-width: 400px;" +
        "-fx-min-height: 200px;" +
        "-fx-font-size: 40px;");
        user.setLayoutX(200);
        user.setLayoutY(200);
        String[] users = {"user1", "user2"}; //TODO: make variable based on amount of users
        user.getItems().addAll(users);

        Group root = new Group(chooseUser, registerNew, user);
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

    public Scene loginUser(){
        Group root = new Group();
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

    public Scene loginForgotPasswordFirst(){
        Group root = new Group();
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

    public Scene loginForgotpasswordSecond(){
        Group root = new Group();
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

    public Scene registerFirst(){
        Group root = new Group();
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

    public Scene registerSecond(){
        Group root = new Group();
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

    public Scene registerThird(){
        Group root = new Group();
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

    public Scene overview(){
        Group root = new Group();
        Scene scene = new Scene(root, 800, 500, Color.WHITE);
        return scene;
    }

    public static void main(String[] args){launch();}
}
