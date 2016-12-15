package fxrocket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Start extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        URL url = getClass().getResource("fxrocket.fxml");
        if (url == null) {
            System.err.println("fxrocket.fxml not found!");
            return;

        }

        Parent root = FXMLLoader.load(getClass().getResource("fxrocket.fxml"));
        primaryStage.setTitle("fxrocket environment");
        primaryStage.setScene(new Scene(root, 800, 480));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
