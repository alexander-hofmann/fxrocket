package fxrocket.games.gamescenes.accessibilitytests;

import fxrocket.GamepadWrapper;
import fxrocket.Main;
import fxrocket.database.accessibilitytests.User;
import fxrocket.games.Game;
import fxrocket.games.GameScene;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

/**
 * Created by Felix Müllner on 24.04.2017.
 */
public class AccessibilityTestsMainMenu implements GameScene {
    private Game game;
    private GamepadWrapper gamepadWrapper;

    private int menuPositionX, menuPositionY;

    private User selectedUser;

    public AccessibilityTestsMainMenu(Game game) {
        if (game == null) {
            throw new NullPointerException();
        }
        this.game = game;

        this.gamepadWrapper = (GamepadWrapper) this.game.retrieveData("gamepadWrapper1");

        this.menuPositionX = 0;
        this.menuPositionY = 0;

        this.selectedUser = (User) this.game.retrieveData("selectedUser");
    }

    @Override
    public void process(long now) {
        int menuPressedX = -1;
        int menuPressedY = -1;
        this.gamepadWrapper.calculateFrame();

        if (this.gamepadWrapper.isStepUp() && this.menuPositionY > 0) {
            this.menuPositionY--;
        } else if (this.gamepadWrapper.isStepDown() && this.menuPositionY < 2) {
            this.menuPositionY++;
        } else if (this.gamepadWrapper.isStepLeft() && this.menuPositionX > 0) {
            this.menuPositionX--;
        } else if (this.gamepadWrapper.isStepRight() && this.menuPositionX < 3) {
            this.menuPositionX++;
        }

        if (this.gamepadWrapper.isStickPressedNew()) {
            menuPressedX = this.menuPositionX;
            menuPressedY = this.menuPositionY;
        }

        if (menuPressedY == 0 && this.selectedUser != null) {
            if (menuPressedX == 0) {
                this.game.switchScene("Joystick");
            } else if (menuPressedX == 1) {
                //TODO
            } else if (menuPressedX == 2) {
                //TODO
            } else if (menuPressedX == 3) {
                //TODO
            }
        } else if (menuPressedY == 1 && this.selectedUser != null) {
            if (menuPressedX == 0) {
                //TODO
            } else if (menuPressedX == 1) {
                //TODO
            } else if (menuPressedX == 2) {
                //TODO
            } else if (menuPressedX == 3) {
                //TODO
            }
        } else if (menuPressedY == 2) {
            if (menuPressedX == 1) {
                this.game.switchScene("Users");
            } else if (menuPressedX == 2) {
                this.game.switchGame(null);
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.clearRect(0,0,Main.SCREEN_SIZE_X,Main.SCREEN_SIZE_Y);

        String[] menuText = {
                "Joystick", "Aim", "Recognition", "Reaction Time",
                "Timing (Single)", "Timing (Multiple)", "Sequence", "Simultaneous",
                "", "Users", "Back", ""
        };

        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        int menuOptionSize = (int) Math.round(Main.SCREEN_SIZE_Y * 0.2);
        int screenCenterX = (int) Math.round(Main.SCREEN_SIZE_X * 0.5);

        if (this.selectedUser == null) {
            gc.fillText("No user selected", screenCenterX, 30, Main.SCREEN_SIZE_X * 0.75);
            gc.setFill(Color.GRAY);
        } else {
            gc.fillText("Selected user: " + this.selectedUser.getFullName(), screenCenterX, 30, Main.SCREEN_SIZE_X * 0.75);
        }

        for (int i = 0; i < 3; ++i) {
            if (i == 2) {
                gc.setFill(Color.BLACK);
            }
            for (int j = 0; j < 4; ++j) {
                gc.strokeRoundRect((Main.SCREEN_SIZE_X - 4*menuOptionSize)*0.2 + j*(menuOptionSize + (Main.SCREEN_SIZE_X - 4*menuOptionSize)*0.2), (Main.SCREEN_SIZE_Y - 3*menuOptionSize)*0.25 + i*(menuOptionSize + (Main.SCREEN_SIZE_Y - 3*menuOptionSize)*0.25), menuOptionSize, menuOptionSize, 20, 20);
                gc.fillText(menuText[i*4+j], (Main.SCREEN_SIZE_X - 4*menuOptionSize)*0.2 + menuOptionSize*0.5 + j*(menuOptionSize + (Main.SCREEN_SIZE_X - 4*menuOptionSize)*0.2), (Main.SCREEN_SIZE_Y - 3*menuOptionSize)*0.25 + menuOptionSize*0.5 + i*(menuOptionSize + (Main.SCREEN_SIZE_Y - 3*menuOptionSize)*0.25), 95);
                if (j == menuPositionX && i == menuPositionY) {
                    gc.setStroke(Color.RED);
                    gc.strokeRoundRect(((Main.SCREEN_SIZE_X - 4*menuOptionSize)*0.2 - 10) + j*(menuOptionSize + (Main.SCREEN_SIZE_X - 4*menuOptionSize)*0.2), ((Main.SCREEN_SIZE_Y - 3*menuOptionSize)*0.25 - 10) + i*(menuOptionSize + (Main.SCREEN_SIZE_Y - 3*menuOptionSize)*0.25), menuOptionSize + 20, menuOptionSize + 20, 20, 20);
                    gc.setStroke(Color.BLACK);
                }
            }
        }
    }
}
