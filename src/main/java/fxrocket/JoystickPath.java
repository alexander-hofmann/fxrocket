package fxrocket;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix Müllner on 09.05.2017.
 */
public class JoystickPath {
    private List<JoystickSubPath> subPaths;

    public JoystickPath(List<Point2D> points) {
        if (points.size() == 1 || points.size() % 3 != 1) {
            throw new IllegalArgumentException("Invalid number of points");
        }

        this.subPaths = new ArrayList<>();
        for (int i = 0; (i + 3) < points.size(); i += 3) {
            this.subPaths.add(new JoystickSubPath(points.get(i), points.get(i + 1), points.get(i + 2), points.get(i + 3)));
        }
    }

    public void draw(GraphicsContext gc) {
        for (JoystickSubPath subPath : this.subPaths) {
            subPath.draw(gc);
        }
    }

    public double getLength() {
        double length = 0;
        for (JoystickSubPath subPath : this.subPaths) {
            length += subPath.getLength();
        }
        return length;
    }

    public JoystickPathProgress trace(Point2D cursorPosition, Point2D lastCursorPosition) {
        JoystickPathProgress progress = null;
        for (JoystickSubPath subPath : this.subPaths) {
            progress = subPath.trace(cursorPosition, lastCursorPosition);
            if (!progress.isCompleted()) {
                break;
            }
        }
        return progress;
    }
}
