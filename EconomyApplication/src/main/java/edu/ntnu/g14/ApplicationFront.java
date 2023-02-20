package edu.ntnu.g14;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;




public class ApplicationFront extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        Group root = new Group();
        Scene scene = new Scene(root, 800, 800, Color.WHITE);
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args){launch();}
}
