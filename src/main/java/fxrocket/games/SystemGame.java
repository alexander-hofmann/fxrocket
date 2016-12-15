package fxrocket.games;

import fxrocket.games.gamescenes.system.ExitScene;
import fxrocket.games.gamescenes.system.GamepadDebugScene;
import fxrocket.games.gamescenes.system.MainMenuScene;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Felix Müllner on 23.05.2017.
 */
public class SystemGame implements Game {
    private GameScene currentScene;
    private GameManager gameManager;
    private Map<String, GameScene> scenes;

    public SystemGame() {
        this.scenes = new HashMap<>();
        this.scenes.put("Main Menu", new MainMenuScene(this));
        this.scenes.put("Analog Sticks Demo", new GamepadDebugScene(this));
        this.scenes.put("Exit", new ExitScene());

        this.currentScene = this.scenes.get("Main Menu");
    }

    @Override
    public GameScene getCurrentScene() {
        return this.currentScene;
    }

    @Override
    public String getName() {
        return "System";
    }

    @Override
    public Object retrieveData(String key) {
        //TODO
        return null;
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
        //TODO
    }

    @Override
    public void switchGame(Game game) {
        this.gameManager.load(game);
    }

    @Override
    public void switchScene(String sceneName) {
        System.out.println("Changing to scene: " + sceneName);
        if (this.scenes.containsKey(sceneName)) {
            this.currentScene = this.scenes.get(sceneName);
        } else {
            throw new IllegalArgumentException("No scene of name " + sceneName + "in game " + this.getName());
        }
    }
}
