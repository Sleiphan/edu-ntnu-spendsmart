//TODO: make sure the mouse does not start in the textfield, this makes the prompt text unreadable
//TODO: make it so the user can input the email key with spaces or without. take input and remove spaces

package edu.ntnu.g14;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
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
        Text chooseUser = newText("Choose user", 30, false, 170, 40);
        Text registerNew = newText("Register new account", 10, true, 400, 280);
        registerNew.setOnMouseClicked(e -> {
            stage.setScene(registerFirst());
        });

        String[] users = {"Barack Obama", "Donald Trump"}; //TODO: make variable
        ChoiceBox<String> user = newChoiceBox(users, "black", "white", 250, 20, 15, 125, 70);

        Button confirm = newButton("Confirm", 175, 110, "black", "white", 150, 20, 15);
        confirm.setOnAction(e -> {
            stage.setScene(loginUser(user.getValue()));
        });

        Group root = new Group(chooseUser, registerNew, user, confirm);
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;

    }

    public Scene loginUser(String user){ //TODO: add User user as parameter
        Text notYou = newText("Is this not you?", 10, true, 400, 280);
        notYou.setOnMouseClicked(e -> {
            stage.setScene(loginChooseUser());
        });

        Text forgotPassword = newText("Forgot password", 10, true, 400, 260);
        forgotPassword.setOnMouseClicked(e -> {
            stage.setScene(loginForgotPasswordFirst(user));
        });

        TextField textField = newTextField("Password", 120, 60, "black", "white", 250, 40, 25);
        Button loginButton = newButton("Login", 185, 130, "black", "white", 100, 30, 25);
        loginButton.setOnAction(e -> {
            if(textField.getText() == "Passord123"){ //add password based on user
                //stage.setScene(overview(user));
            } else {
                //add what happens if the password is wrong
            }
        });

        Group root = new Group(newText("Welcome back " + user, 25, false, 120, 40),
        notYou, forgotPassword, textField, loginButton);
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

    public Scene loginForgotPasswordFirst(String user){
        Text goBack = newText("Go back?", 10, true, 400, 260);
        goBack.setOnMouseClicked(e -> {
            stage.setScene(loginUser(user));
        });

        TextField keyField = newTextField("Code-key", 120, 70, "black", "white", 250, 30, 25);
        Button loginButton = newButton("Make new password", 120, 140, "black", "white", 250, 20, 15);
        

        Group root = new Group(newText("An email with a code-key has been sent to:", 15, false, 130, 40),
        newText("Oboooma@gmail.com", 15, false, 170, 60), 
        goBack, keyField, loginButton);
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

    public Scene loginForgotpasswordSecond(){
        TextField newPassword = newTextField("New password", 125, 70, "black", "white", 250, 20, 15);
        TextField retypeNewPassword = newTextField("Retype new password", 125, 110, "black", "white", 250, 20, 15);
        Button confirmButton = newButton("Confirm", 175, 150, "black", "white", 150, 20, 15);
        Text goBack = newText("Go back", 10, true, 400, 260);

        Group root = new Group(goBack, newPassword, retypeNewPassword, confirmButton);
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

    public Scene registerFirst(){
        TextField firstName = newTextField("", 10, 35, "black", "white", 100, 20, 15);
        TextField lastName = newTextField("", 10, 135, "black", "white", 100, 20, 15);
        TextField email = newTextField("", 10, 235, "black", "white", 100, 20, 15);
        TextField password = newTextField("", 260, 35, "black", "white", 100, 20, 15);
        TextField confirmPassword = newTextField("", 260, 135, "black", "white", 100, 20, 15);
        Button next = newButton("Next", 260, 235, "black", "white", 100, 20, 15);
        next.setOnAction(e -> {
            if(firstName.getText() != "" && lastName.getText() != ""
            && email.getText() != "" && password.getText() != ""
            && confirmPassword.getText() != ""){
                //TODO: add register user
                String key = EmailVertification.sendVertificationKey(email.getText().replace(" ", ""));
                stage.setScene(registerSecond(key, email.getText()));
            } else {
                alertBox("ERROR", "Missing information", "Pleace fill out all required information");
            }
        });

        Group root = new Group(firstName, lastName,
        email, password, confirmPassword, next,
        newText("First name", 30, false, 10, 25),
        newText("Last name", 30, false, 10, 125),
        newText("Email", 30, false, 10, 225),
        newText("Password", 30, false, 260, 25),
        newText("Confirm password", 30, false, 260, 125));
        Scene scene = new Scene(root, 500, 300, Color.WHITE);
        return scene;
    }

    public Scene registerSecond(String key, String email){
        TextField keyInput = newTextField("", 120, 195, "black", "white", 100, 20, 15);
        Button next = newButton("Next", 240, 195, "black", "white", 100, 20, 15);
        next.setOnAction(e -> {
            if(keyInput.getText().replace(" ", "").equals(key.replace(" ", ""))){
                stage.setScene(registerThird());
            } else {
                alertBox("ERROR", "Wrong key", "The wrong key has been input");
            }
        });
        
        Group root = new Group(keyInput, next,
        newText("An email with a confirmation code", 25, false, 60, 25),
        newText("has been sent to", 25, false, 140, 50),
        newText(email, 25, false, 140, 75),
        newText("Code:", 30, false, 20, 225));
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

    

    public Button newButton(String text, int x, int y, String borderColor,
    String backgroundColor, int width, int height, int fontSize){
        Button button = new Button(text);
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setStyle("-fx-border-color: " + borderColor + ";" + 
        "-fx-background-color: " + backgroundColor + ";" +
        "-fx-pref-width: " + width + ";" +
        "-fx-pref-height: " + height + ";" +
        "-fx-font-size: " + fontSize + "px;");
        return button;
    }

    public TextField newTextField(String promptText, int x, int y, String borderColor,
    String backgroundColor, int width, int height, int fontSize){
        TextField textField = new TextField ();
        textField.setPromptText(promptText);
        textField.setLayoutX(x);
        textField.setLayoutY(y);
        textField.setStyle("-fx-border-color: " + borderColor + ";" + 
        "-fx-background-color: " + backgroundColor + ";" +
        "-fx-pref-width: " + width + ";" +
        "-fx-pref-height: " + height + ";" +
        "-fx-font-size: " + fontSize + "px;");
        return textField;
    }

    public Text newText(String title, int size, boolean underline, int x, int y){
        Text text = new Text(title);
        text.setFont(Font.font("Times New Roman", FontWeight.BOLD, size));
        text.setUnderline(underline);
        text.setX(x);
        text.setY(y);
        return text;
    }

    public ChoiceBox<String> newChoiceBox(String[] choices, String borderColor,
    String backgroundColor, int width, int height, int fontSize, int x, int y){
        ChoiceBox<String> choiceBox = new ChoiceBox();
        choiceBox.getItems().addAll(choices);
        choiceBox.setStyle("-fx-border-color: " + borderColor + ";" + 
        "-fx-background-color: " + backgroundColor + ";" +
        "-fx-pref-width: " + width + ";" +
        "-fx-pref-height: " + height + ";" +
        "-fx-font-size: " + fontSize + "px;");
        choiceBox.setLayoutX(x);
        choiceBox.setLayoutY(y);
        return choiceBox;
    }

    public void alertBox(String title, String header, String content){
        Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
    }

    public static void main(String[] args){launch();}
}
