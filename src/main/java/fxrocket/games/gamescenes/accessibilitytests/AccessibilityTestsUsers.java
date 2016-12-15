package fxrocket.games.gamescenes.accessibilitytests;

import fxrocket.GamepadWrapper;
import fxrocket.Main;
import fxrocket.database.accessibilitytests.AccessibilityTestsDatabase;
import fxrocket.database.accessibilitytests.User;
import fxrocket.games.Game;
import fxrocket.games.GameScene;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix Müllner on 26.04.2017.
 */
public class AccessibilityTestsUsers implements GameScene {
    private Game game;
    private GamepadWrapper gamepadWrapper;

    private int menuPosition;
    private int page;

    private AccessibilityTestsDatabase db;
    private List<User> userList;

    public AccessibilityTestsUsers(Game game) {
        if (game == null) {
            throw new NullPointerException();
        }
        this.game = game;

        this.gamepadWrapper = (GamepadWrapper) this.game.retrieveData("gamepadWrapper1");

        this.page = 0;

        try {
            this.db = new AccessibilityTestsDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.userList = this.db.getUsers();

        if (this.userList.size() > 0) {
            this.menuPosition = 1;
        } else {
            this.menuPosition = 0;
        }

        try {
            this.db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.db = null;
    }

    @Override
    public void process(long now) {
        this.gamepadWrapper.calculateFrame();
        int menuPressed = -1;

        int userNumber = this.userList.size();

        if (this.gamepadWrapper.isStepUp() && this.menuPosition > 0) {
            this.menuPosition--;
        } else if (this.gamepadWrapper.isStepDown() && this.menuPosition < userNumber) {
            this.menuPosition++;
        }

        if (this.gamepadWrapper.isStickPressedNew()) {
            menuPressed = this.menuPosition;
        }

        if (menuPressed != -1) {
            if (menuPressed != 0) {
                User selectedUser = this.userList.get(menuPressed - 1);
                this.game.storeData("selectedUser", selectedUser);
            }
            this.game.switchScene("Main Menu");
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.clearRect(0,0,Main.SCREEN_SIZE_X,Main.SCREEN_SIZE_Y);

        List<String> menuList = new ArrayList<>();
        menuList.add(0, "Back");
        for (User user : this.userList) {
            menuList.add(user.getFullName());
        }

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        int textHeight = (int) Math.round(Main.SCREEN_SIZE_Y * 0.1);
        int screenCenterX = (int) Math.round(Main.SCREEN_SIZE_X * 0.5);

        for (int i = 0; i < menuList.size(); ++i) {
            if (this.menuPosition == i) {
                gc.setFill(Color.RED);
            } else {
                gc.setFill(Color.BLACK);
            }
            gc.fillText(menuList.get(i), screenCenterX, textHeight + i*textHeight);
        }
    }
}
