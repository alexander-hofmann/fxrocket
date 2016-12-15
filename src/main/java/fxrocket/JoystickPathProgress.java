package fxrocket;

/**
 * Created by Felix Müllner on 18.05.2017.
 */
public class JoystickPathProgress {
    private boolean started;
    private boolean completed;
    private boolean outOfRange;
    private int progress;
    private double distance;

    public JoystickPathProgress(boolean started, boolean completed, boolean outOfRange, int progress, double distance) {
        this.started = started;
        this.completed = completed;
        this.outOfRange = outOfRange;
        this.progress = progress;
        this.distance = distance;
    }

    public boolean isStarted() {
        return this.started;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public boolean isOutOfRange() {
        return this.outOfRange;
    }

    public int getProgress() {
        return this.progress;
    }

    public double getDistance() {
        return this.distance;
    }
}
