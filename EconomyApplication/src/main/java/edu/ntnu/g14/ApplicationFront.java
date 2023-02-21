package edu.ntnu.g14;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ApplicationFront extends Application {
    
    private Stage stage;

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        this.stage = stage;
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
        user.setStyle("-fx-border-color: black;" + 
        "-fx-background-color: white;" +
        "-fx-pref-width: 250;" +
        "-fx-pref-height: 20;" +
        "-fx-font-size: 15px;");
        user.setLayoutX(125);
        user.setLayoutY(70);
        String[] users = {"Barack Obama", "Donald Trump"}; //TODO: make variable
        user.getItems().addAll(users);

        Button confirm = new Button("Confirm");
        confirm.setLayoutX(175);
        confirm.setLayoutY(110);
        confirm.setStyle("-fx-border-color: black;" + 
        "-fx-background-color: white;" +
        "-fx-pref-width: 150;" +
        "-fx-pref-height: 20;" +
        "-fx-font-size: 15px;");
        confirm.setOnAction(e -> {
            stage.setScene(loginUser(user.getValue()));
        });

        Group root = new Group(chooseUser, registerNew, user, confirm);
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;

    }

    public Scene loginUser(String user){ //TODO: add User user as parameter
        Text chooseUser = new Text("Welcome back " + user); //TODO: add variable name
        chooseUser.setFont(Font.font("Times New Roman", FontWeight.BOLD, 25));
        chooseUser.setX(120);
        chooseUser.setY(40);

        Text notYou = new Text("Is this not you?");
        notYou.setFont(Font.font("Times New Roman", 10));
        notYou.setUnderline(true);
        notYou.setX(400);
        notYou.setY(280);
        notYou.setOnMouseClicked(e -> {
            stage.setScene(loginChooseUser());
        });

        Text forgotPassword = new Text("Forgot password?");
        forgotPassword.setFont(Font.font("Times New Roman", 10));
        forgotPassword.setUnderline(true);
        forgotPassword.setX(400);
        forgotPassword.setY(260);
        forgotPassword.setOnMouseClicked(e -> {
            stage.setScene(loginForgotPasswordFirst(user));
        });

        TextField textField = new TextField ();
        textField.setLayoutX(120);
        textField.setLayoutY(60);
        textField.setStyle("-fx-border-color: black;" + 
        "-fx-background-color: white;" +
        "-fx-pref-width: 250;" +
        "-fx-pref-height: 40;" +
        "-fx-font-size: 25px;");
        textField.setPromptText("Password..");

        Button loginButton = new Button("Login");
        loginButton.setLayoutX(185);
        loginButton.setLayoutY(130);
        loginButton.setStyle("-fx-border-color: black;" + 
        "-fx-background-color: white;" +
        "-fx-pref-width: 100;" +
        "-fx-pref-height: 30;" +
        "-fx-font-size: 25px;");
        loginButton.setOnAction(e -> {
            if(textField.getText() == "Passord123"){ //add password based on user
                //stage.setScene(overview(user));
            } else {
                //add what happens if the password is wrong
            }
        });
        //TODO: make sure the mouse does not start in the textfield, this makes the prompt text unreadable


        Group root = new Group(chooseUser, notYou, forgotPassword, textField, loginButton);
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

    public Scene loginForgotPasswordFirst(String user){
        Text codeText = new Text("An email with a code-key has been sent to:");
        codeText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
        codeText.setX(130);
        codeText.setY(40);

        Text emailText = new Text("Oboooma@gmail.com");
        emailText.setFont(Font.font("Times New Roman", 15));
        emailText.setX(170);
        emailText.setY(60);

        Text goBack = new Text("Go back?");
        goBack.setFont(Font.font("Times New Roman", FontWeight.BOLD, 10));
        goBack.setUnderline(true);
        goBack.setX(400);
        goBack.setY(260);
        goBack.setOnMouseClicked(e -> {
            stage.setScene(loginUser(user));
        });

        TextField keyField = new TextField ();
        keyField.setLayoutX(120);
        keyField.setLayoutY(70);
        keyField.setStyle("-fx-border-color: black;" + 
        "-fx-background-color: white;" +
        "-fx-pref-width: 250;" +
        "-fx-pref-height: 30;" +
        "-fx-font-size: 25px;");
        keyField.setPromptText("Code-key..");

        Button loginButton = new Button("Make new password");
        loginButton.setLayoutX(120);
        loginButton.setLayoutY(140);
        loginButton.setStyle("-fx-border-color: black;" + 
        "-fx-background-color: white;" +
        "-fx-pref-width: 250;" +
        "-fx-pref-height: 20;" +
        "-fx-font-size: 15px;");

        Group root = new Group(codeText, emailText, goBack, keyField, loginButton);
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

    public Scene loginForgotpasswordSecond(){
        Text goBack = new Text("Go back?");
        goBack.setFont(Font.font("Times New Roman", FontWeight.BOLD, 10));
        goBack.setUnderline(true);
        goBack.setX(400);
        goBack.setY(260);

        TextField newPassword = new TextField ();
        newPassword.setLayoutX(125);
        newPassword.setLayoutY(70);
        newPassword.setStyle("-fx-border-color: black;" + 
        "-fx-background-color: white;" +
        "-fx-pref-width: 250;" +
        "-fx-pref-height: 20;" +
        "-fx-font-size: 15px;");
        newPassword.setPromptText("New password..");

        TextField reNewPassword = new TextField ();
        reNewPassword.setLayoutX(125);
        reNewPassword.setLayoutY(110);
        reNewPassword.setStyle("-fx-border-color: black;" + 
        "-fx-background-color: white;" +
        "-fx-pref-width: 250;" +
        "-fx-pref-height: 20;" +
        "-fx-font-size: 15px;");
        reNewPassword.setPromptText("retype new password..");

        Button confirm = new Button("Confirm");
        confirm.setLayoutX(175);
        confirm.setLayoutY(150);
        confirm.setStyle("-fx-border-color: black;" + 
        "-fx-background-color: white;" +
        "-fx-pref-width: 150;" +
        "-fx-pref-height: 20;" +
        "-fx-font-size: 15px;");

        Group root = new Group(goBack, newPassword, reNewPassword, confirm);
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
