package fxrocket.games;

/**
 * Created by Felix Müllner on 23.05.2017.
 */
public interface Game {
    GameScene getCurrentScene();

    String getName();

    Object retrieveData(String key);

    void setGameManager(GameManager gameManager);

    void stop();

    void storeData(String key, Object value);

    void switchGame(Game game);

    void switchScene(String sceneName);
}
