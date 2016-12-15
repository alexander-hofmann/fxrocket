package fxrocket.games.gamescenes.system;

import fxrocket.GamepadWrapper;
import fxrocket.Main;
import fxrocket.games.AccessibilityTestsGame;
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
 * Created by Felix Müllner on 06.04.2017.
 */
public class MainMenuScene implements GameScene {
    private Game game;
    private Gamepad gamepad;
    private GamepadWrapper gamepadWrapper;

    private int menuPosition;

    public MainMenuScene(Game game) {
        if (game == null) {
            throw new NullPointerException();
        }
        this.game = game;

        try {
            this.gamepad = new DefaultGamepad(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.gamepadWrapper = new GamepadWrapper(this.gamepad);

        this.menuPosition = 0;
    }

    @Override
    public void process(long now) {
        int menuPressed = -1;
        this.gamepadWrapper.calculateFrame();

        if (this.gamepadWrapper.isStepUp() && this.menuPosition > 0) {
            this.menuPosition--;
        } else if (this.gamepadWrapper.isStepDown() && this.menuPosition < 4) {
            this.menuPosition++;
        }

        if (this.gamepadWrapper.isStickPressedNew()) {
            menuPressed = this.menuPosition;
        }

        if (menuPressed == 0) {
            this.game.switchGame(new AccessibilityTestsGame());
        } else if (menuPressed == 3) {
            this.game.switchScene("Analog Sticks Demo");
        } else if (menuPressed == 4) {
            this.game.switchScene("Exit");
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.clearRect(0,0,Main.SCREEN_SIZE_X,Main.SCREEN_SIZE_Y);

        String[] menuText = {"Accessibility Tests", "Option B", "Option C", "Analog Sticks Demo", "Exit"};

        gc.setStroke(Color.BLACK);

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        int menuOptionHeight = (int) Math.round(Main.SCREEN_SIZE_Y * 0.125);
        int menuOptionWidth = (int) Math.round(Main.SCREEN_SIZE_X * 0.5);
        int screenCenterX = (int) Math.round(Main.SCREEN_SIZE_X * 0.5);

        for (int i = 0; i < 5; ++i) {
            gc.strokeRect(screenCenterX - menuOptionWidth*0.5, 1.5*menuOptionHeight + menuOptionHeight*i, menuOptionWidth, menuOptionHeight);
            if (this.menuPosition == i) {
                gc.setFill(Color.RED);
                gc.fillRect(screenCenterX - menuOptionWidth*0.5, 1.5*menuOptionHeight + menuOptionHeight*i, menuOptionWidth, menuOptionHeight);
                gc.setFill(Color.WHITE);
                gc.fillText(menuText[i], screenCenterX, 2*menuOptionHeight + menuOptionHeight*i, menuOptionWidth - 10);
            } else {
                gc.setFill(Color.BLACK);
                gc.fillText(menuText[i], screenCenterX, 2*menuOptionHeight + menuOptionHeight*i, menuOptionWidth - 10);
            }
        }
    }
}
