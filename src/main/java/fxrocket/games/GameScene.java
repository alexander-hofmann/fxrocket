package fxrocket.games;

import javafx.scene.canvas.GraphicsContext;

/**
 * Created by Felix Müllner on 06.04.2017.
 */
public interface GameScene {
    void process(long now);

    void render(GraphicsContext gc);
}
