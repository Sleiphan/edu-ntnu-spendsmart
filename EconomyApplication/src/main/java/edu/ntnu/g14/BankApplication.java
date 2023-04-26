package edu.ntnu.g14;


import edu.ntnu.g14.frontend.LoginChooseUserScene;
import edu.ntnu.g14.model.FileManagement;
import edu.ntnu.g14.model.User;
import java.io.FileInputStream;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 * The class is the main entry point for the application. It extends the JavaFX Application class
 * and contains the start() method, which is responsible for setting up the initial stage and
 * loading the LoginChooseUserScene. It also provides methods for getting the current stage and
 * setting the logged in user.
 */
public class BankApplication extends Application {

  static private Stage stage;
  public static User loggedInUser;

  /**
   * The start() method sets up the initial stage and loads the LoginChooseUserScene.
   *
   * @param stage the primary stage for this application
   * @throws IOException          if an error occurs while loading the scene
   * @throws InterruptedException if an error occurs while initializing the scene
   */
  @Override
  public void start(Stage stage) throws IOException, InterruptedException {
    FileManagement.initialize();

    BankApplication.stage = stage;
    stage.setTitle("SpendSmart");
    stage.setResizable(false);
    stage.setScene(LoginChooseUserScene.scene());
    stage.getIcons().add(new Image(new FileInputStream("src/main/resources/images/icon.png")));
    stage.show();
  }

  /**
   * Gets the current primary stage for the application.
   *
   * @return the primary stage for the application
   */
  public static Stage getStage() {
    return stage;
  }

  /**
   * Sets the currently logged in user.
   *
   * @param user the user that is currently logged in
   */
  public static void setLoggedInUser(User user) {
    loggedInUser = user;
  }


  /**
   * The main method of the BankApplication class, which launches the JavaFX application.
   *
   * @param args command line arguments
   * @throws IOException if an error occurs while launching the application
   */
  public static void main(String[] args) throws IOException {
    launch();
  }
}
