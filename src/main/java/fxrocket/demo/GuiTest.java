package fxrocket.demo;

import fxrocket.hardware.DefaultGamepad;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Created by Felix Müllner on 03.04.2017.
 */
public class GuiTest extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        Canvas canvas = new Canvas(800,480);
        root.getChildren().add(canvas);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                System.exit(0);
            }
        });

        final DefaultGamepad gamepad1 = new DefaultGamepad(1);
        gamepad1.calibrateDeadzoneX(0);
        gamepad1.calibrateDeadzoneY(0);
        final DefaultGamepad gamepad2 = new DefaultGamepad(2);
        gamepad2.calibrateDeadzoneX(0);
        gamepad2.calibrateDeadzoneY(0);
        final GraphicsContext gc = canvas.getGraphicsContext2D();

        new AnimationTimer() {
            public void handle(long now) {
                gc.setStroke(Color.BLACK);
                gc.setFill(Color.RED);

                //clear canvas
                gc.clearRect(0,0,800,480);

                //cursor boundary
                gc.strokeRect(100,100,200,200);
                //cursor
                gc.fillOval(195 + 100*gamepad1.getX(),195 + 100*gamepad1.getY(),10,10);
                //button
                gc.strokeOval(195,430,20,20);
                if (gamepad1.isStickPressed()) {
                    gc.fillOval(195,430,20,20);
                }

                //cursor boundary
                gc.strokeRect(500,100,200,200);
                //cursor
                gc.fillOval(595 + 100*gamepad2.getX(),195 + 100*gamepad2.getY(),10,10);
                //button
                gc.strokeOval(595,430,20,20);
                if (gamepad2.isStickPressed()) {
                    gc.fillOval(595,430,20,20);
                }
            }
        }.start();

        primaryStage.show();
    }
}
