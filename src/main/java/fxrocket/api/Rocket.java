package fxrocket.api;

import javafx.scene.Scene;

/**
 * Created by Alexander Hofmann on 25.12.2016.
 */
public interface Rocket {
    public Scene getMainScene();
    public void setEnvironment(Environment environment);
}
