package fxrocket.games;

public class GameManager {
    private Game currentGame;

    public GameManager() {
        this.load(new SystemGame());
    }

    public GameScene getCurrentScene() {
        return this.currentGame.getCurrentScene();
    }

    public void load(Game game) {
        if (game == null) {
            game = new SystemGame();
        }
        System.out.println("Loading game: " + game.getName());
        if (this.currentGame != null) {
            this.currentGame.stop();
        }
        this.currentGame = game;
        this.currentGame.setGameManager(this);
    }
}
