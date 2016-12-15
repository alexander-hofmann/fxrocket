package fxrocket.games.gamescenes.system;

import fxrocket.Main;
import fxrocket.games.Game;
import fxrocket.hardware.DefaultGamepad;
import fxrocket.hardware.Gamepad;
import fxrocket.games.GameScene;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.io.IOException;

/**
 * Created by Felix Müllner on 10.04.2017.
 */
public class GamepadDebugScene implements GameScene {
    private Game game;
    private Gamepad gamepad1;
    private Gamepad gamepad2;

    private int exitScene;

    public GamepadDebugScene(Game game) {
        if (game == null) {
            throw new NullPointerException();
        }
        this.game = game;

        try {
            this.gamepad1 = new DefaultGamepad(1);
            this.gamepad2 = new DefaultGamepad(2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.exitScene = 0;
    }

    @Override
    public void process(long now) {
        if (gamepad1.isStickPressed() && gamepad2.isStickPressed()) {
            this.exitScene++;
        } else {
            this.exitScene = 0;
        }

        if (this.exitScene >= 60) {
            this.game.switchScene("Main Menu");
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.RED);

        int cursorBoundarySize = (int) Math.round(Main.SCREEN_SIZE_X * 0.25);
        int screenCenterX = (int) Math.round(Main.SCREEN_SIZE_X * 0.5);

        int gamepad1CenterX = (int) Math.round(Main.SCREEN_SIZE_X * 0.25);
        int gamepad2CenterX = (int) Math.round(Main.SCREEN_SIZE_X * 0.75);

        //clear canvas
        gc.clearRect(0,0,Main.SCREEN_SIZE_X,Main.SCREEN_SIZE_Y);

        //cursor boundary
        gc.strokeRect(gamepad1CenterX - cursorBoundarySize*0.5,Main.SCREEN_SIZE_Y*0.2,cursorBoundarySize,cursorBoundarySize);
        //cursor
        gc.fillOval(gamepad1CenterX - 5 + cursorBoundarySize*0.5*gamepad1.getX(),(Main.SCREEN_SIZE_Y*0.2 + cursorBoundarySize*0.5) - 5 + cursorBoundarySize*0.5*gamepad1.getY(),10,10);
        //button
        gc.strokeOval(gamepad1CenterX - 10,Main.SCREEN_SIZE_Y*0.9 - 10,20,20);
        if (gamepad1.isStickPressed()) {
            gc.fillOval(gamepad1CenterX - 10,Main.SCREEN_SIZE_Y*0.9 - 10,20,20);
        }

        //cursor boundary
        gc.strokeRect(gamepad2CenterX - cursorBoundarySize*0.5,Main.SCREEN_SIZE_Y*0.2,cursorBoundarySize,cursorBoundarySize);
        //cursor
        gc.fillOval(gamepad2CenterX - 5 + cursorBoundarySize*0.5*gamepad2.getX(),(Main.SCREEN_SIZE_Y*0.2 + cursorBoundarySize*0.5) - 5 + cursorBoundarySize*0.5*gamepad2.getY(),10,10);
        //button
        gc.strokeOval(gamepad2CenterX - 10,Main.SCREEN_SIZE_Y*0.9 - 10,20,20);
        if (gamepad2.isStickPressed()) {
            gc.fillOval(gamepad2CenterX - 10,Main.SCREEN_SIZE_Y*0.9 - 10,20,20);
        }

        //help text
        gc.setFill(Color.BLACK);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText("Press and hold down both sticks to exit.", screenCenterX, Main.SCREEN_SIZE_Y*0.9, Main.SCREEN_SIZE_X * 0.5);
    }
}
