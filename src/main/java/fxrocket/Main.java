package fxrocket;

import com.sun.glass.ui.Robot;
import fxrocket.games.GameManager;
import fxrocket.games.GameScene;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Felix Müllner on 04.04.2017.
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    public static final int SCREEN_SIZE_X = 800;
    public static final int SCREEN_SIZE_Y = 480;

    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        Canvas canvas = new Canvas(SCREEN_SIZE_X, SCREEN_SIZE_Y);
        root.getChildren().add(canvas);
        final GraphicsContext gc = canvas.getGraphicsContext2D();

        final GameManager gameManager = new GameManager();

        new AnimationTimer() {
            public void handle(long now) {
                GameScene scene = gameManager.getCurrentScene();
                scene.process(now);
                scene.render(gc);
            }
        }.start();

        primaryStage.show();
        scene.setCursor(Cursor.NONE);
        fixMouse(primaryStage);
    }

    // http://stackoverflow.com/questions/41794346/raspberry-pi-cant-hide-mouse-cursor-in-javafx-application/42966191#42966191
    private void fixMouse(Stage primaryStage)
    {
        Platform.runLater(()->{
            //Show mouse cursor
            Robot robot = com.sun.glass.ui.Application.GetApplication().createRobot();

            robot.mouseMove(SCREEN_SIZE_X - 10,SCREEN_SIZE_Y - 10);
            robot.destroy();

            //Show fullscreen dialog
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(primaryStage);

            StackPane dialogLayout = new StackPane();
            dialog.setFullScreen(true);
            dialog.setResizable(false);
            dialog.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

            Scene dialogScene = new Scene(dialogLayout, 0, 0);
            dialogScene.setCursor(Cursor.NONE);
            dialogScene.setFill(Color.BLACK);
            dialogLayout.setBackground(Background.EMPTY);

            dialog.setScene(dialogScene);
            dialog.show();

            // Auto close the dialog
            Platform.runLater(()->{
                dialog.close();
                primaryStage.setFullScreen(true);
            });
        });
    }
}
