package fxrocket;

import fxrocket.hardware.Gamepad;

/**
 * Created by Felix Müllner on 21.04.2017.
 */
public class GamepadWrapper {
    private Gamepad gamepad;

    private boolean currentFrameDown;
    private boolean currentFrameLeft;
    private boolean currentFrameRight;
    private boolean currentFrameUp;
    private boolean currentFrameStickPressed;

    private boolean lastFrameDown;
    private boolean lastFrameLeft;
    private boolean lastFrameRight;
    private boolean lastFrameUp;
    private boolean lastFrameStickPressed;

    private double speed;

    private int xPos;
    private int yPos;

    public GamepadWrapper(Gamepad gamepad) {
        this.gamepad = gamepad;
        this.speed = 4;
        calculateFrame();
        this.xPos = 0;
        this.yPos = 0;
    }

    public void calculateFrame() {
        this.lastFrameDown = this.currentFrameDown;
        this.lastFrameLeft = this.currentFrameLeft;
        this.lastFrameRight = this.currentFrameRight;
        this.lastFrameUp = this.currentFrameUp;
        this.lastFrameStickPressed = this.currentFrameStickPressed;

        this.currentFrameDown = gamepad.getY() >= 0.5;
        this.currentFrameLeft = gamepad.getX() <= -0.5;
        this.currentFrameRight = gamepad.getX() >= 0.5;
        this.currentFrameUp = gamepad.getY() <= -0.5;
        this.currentFrameStickPressed = gamepad.isStickPressed();

        this.xPos = (int) Math.round(this.xPos + this.gamepad.getX() * this.speed);
        this.yPos = (int) Math.round(this.yPos + this.gamepad.getY() * this.speed);

        if (this.xPos < 0) {
            this.xPos = 0;
        } else if (this.xPos > 800) {
            this.xPos = 800;
        }
        if (this.yPos < 0) {
            this.yPos = 0;
        } else if (this.yPos > 480) {
            this.yPos = 480;
        }
    }

    public boolean isStepDown() {
        return this.currentFrameDown && !this.lastFrameDown;
    }

    public boolean isStepLeft() {
        return this.currentFrameLeft && !this.lastFrameLeft;
    }

    public boolean isStepRight() {
        return this.currentFrameRight && !this.lastFrameRight;
    }

    public boolean isStepUp() {
        return this.currentFrameUp && !this.lastFrameUp;
    }

    public boolean isStickPressedNew() {
        return this.currentFrameStickPressed && !this.lastFrameStickPressed;
    }

    public boolean isStickReleasedNew() {
        return !this.currentFrameStickPressed && this.lastFrameStickPressed;
    }

    public double getSpeed() {
        return speed;
    }

    public int getXPos() {
        return this.xPos;
    }

    public int getYPos() {
        return this.yPos;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setPos(int xPos, int yPos) {
        this.setXPos(xPos);
        this.setYPos(yPos);
    }

    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    public void setYPos(int yPos) {
        this.yPos = yPos;
    }
}
