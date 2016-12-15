package fxrocket.hardware;

/**
 * Created by Felix Müllner on 11.04.2017.
 */
public interface Gamepad {
    public void calibrateCenter();

    public void calibrateDeadzoneX(double deadzoneX);

    public void calibrateDeadzoneY(double deadzoneY);

    public double getX();

    public double getY();

    public boolean isStickPressed();
}
