package fxrocket.games.gamescenes.system;

import fxrocket.games.GameScene;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;

/**
 * Created by Felix Müllner on 10.04.2017.
 */
public class ExitScene implements GameScene {
    @Override
    public void process(long now) {
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void render(GraphicsContext gc) {
        //noop
    }
}
