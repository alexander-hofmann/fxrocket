package fxrocket.games;

import fxrocket.GamepadWrapper;
import fxrocket.games.gamescenes.accessibilitytests.AccessibilityTestsJoystick;
import fxrocket.games.gamescenes.accessibilitytests.AccessibilityTestsMainMenu;
import fxrocket.games.gamescenes.accessibilitytests.AccessibilityTestsUsers;
import fxrocket.hardware.DefaultGamepad;
import fxrocket.hardware.Gamepad;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Felix Müllner on 23.05.2017.
 */
public class AccessibilityTestsGame implements Game {
    private GameScene currentScene;
    private GameManager gameManager;
    private Map<String, Object> storedData;

    public AccessibilityTestsGame() {
        //create gamepad shared for all scenes
        Gamepad gamepad1 = null;
        GamepadWrapper gamepadWrapper1 = null;
        try {
            gamepad1 = new DefaultGamepad(1);
            gamepadWrapper1 = new GamepadWrapper(gamepad1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.storedData = new HashMap<>();
        this.storedData.put("gamepad1", gamepad1);
        this.storedData.put("gamepadWrapper1", gamepadWrapper1);

        this.currentScene = new AccessibilityTestsMainMenu(this);
    }

    @Override
    public GameScene getCurrentScene() {
        return this.currentScene;
    }

    @Override
    public String getName() {
        return "Accessibility Tests";
    }

    @Override
    public Object retrieveData(String key) {
        return this.storedData.get(key);
    }

    @Override
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void stop() {
        //TODO
    }

    @Override
    public void storeData(String key, Object value) {
        this.storedData.put(key, value);
    }

    @Override
    public void switchGame(Game game) {
        this.gameManager.load(game);
    }

    @Override
    public void switchScene(String sceneName) {
        System.out.println("Changing to scene: " + sceneName);
        switch(sceneName) {
            case "Main Menu":
                this.currentScene = new AccessibilityTestsMainMenu(this);
                break;
            case "Users":
                this.currentScene = new AccessibilityTestsUsers(this);
                break;
            case "Joystick":
                this.currentScene = new AccessibilityTestsJoystick(this);
                break;
            default:
                throw new IllegalArgumentException("No scene of name " + sceneName + "in game " + this.getName());
        }
    }
}
