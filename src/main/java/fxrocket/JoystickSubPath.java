package fxrocket;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix Müllner on 09.05.2017.
 */
public class JoystickSubPath {
    private List<Point2D> points;
    private int traced;
    public static final int steps = 500;

    public JoystickSubPath(Point2D p0, Point2D p1, Point2D p2, Point2D p3) {
        this.points = new ArrayList<>();
        this.traced = 0;

        for (int i = 0; i <= JoystickSubPath.steps; ++i) {
            double t = i/((double) JoystickSubPath.steps);
            Point2D nextPoint = p0.multiply(Math.pow(1 - t, 3))
                    .add(p1.multiply(3*Math.pow(1 - t, 2)*t))
                    .add(p2.multiply(3*(1 - t)*t*t))
                    .add(p3.multiply(t*t*t));
            this.points.add(nextPoint);
        }
    }

    public void draw(GraphicsContext gc) {
        gc.beginPath();
        gc.setStroke(Color.RED);
        gc.moveTo(this.points.get(0).getX(), this.points.get(0).getY());
        for (int i = 1; i < this.points.size(); ++i) {
            if (i == (traced + 1)) {
                gc.stroke();
                gc.beginPath();
                gc.setStroke(Color.BLACK);
                gc.moveTo(this.points.get(i - 1).getX(), this.points.get(i - 1).getY());
            }
            gc.lineTo(this.points.get(i).getX(), this.points.get(i).getY());
        }
        gc.stroke();
    }

    public double getLength() {
        double length = 0;
        for (int i = 1; i < this.points.size(); ++i) {
            length += this.points.get(i).distance(this.points.get(i - 1));
        }
        return length;
    }

    public JoystickPathProgress trace(Point2D cursorPosition, Point2D lastCursorPosition) {
        boolean started = false;
        boolean completed = false;
        boolean outOfRange = false;
        double distance = -1;

        for (int i = this.traced + 1; i <= JoystickSubPath.steps; ++i) {
            //make area facing "forward" from point
            Point2D pathDirection = this.points.get(i).subtract(this.points.get(i - 1));
            Point2D orthogonalDirection = new Point2D(pathDirection.getY() * -1, pathDirection.getX());

            Point2D areaPointA = this.points.get(i).add(orthogonalDirection.normalize().multiply(999));
            Point2D areaPointB = areaPointA.add(pathDirection.normalize().multiply(999));
            Point2D areaPointD = this.points.get(i).add(orthogonalDirection.multiply(-1).normalize().multiply(999));
            Point2D areaPointC = areaPointD.add(pathDirection.normalize().multiply(999));

            double[] areaCoordinates = {
                    areaPointA.getX(), areaPointA.getY(),
                    areaPointB.getX(), areaPointB.getY(),
                    areaPointC.getX(), areaPointC.getY(),
                    areaPointD.getX(), areaPointD.getY()
            };

            Polygon area = new Polygon(areaCoordinates);

            //check if we passed the point
            if (area.contains(cursorPosition)) {
                if (this.points.get(i).distance(cursorPosition) < 30) {
                    this.traced = i;
                    distance = this.points.get(i).distance(this.getIntersection(cursorPosition, lastCursorPosition, this.points.get(i), this.points.get(i).add(orthogonalDirection)));
                } else {
                    //got too far away
                    outOfRange = true;
                    break;
                }
            } else {
                break;
            }
        }

        if (this.traced > 0) {
            started = true;
            if (this.traced == JoystickSubPath.steps) {
                completed = true;
            }
        }

        return new JoystickPathProgress(started, completed, outOfRange, this.traced, distance);
    }

    private Point2D getIntersection(Point2D point1, Point2D point2, Point2D point3, Point2D point4) {
        double xDividend = (point1.getX()*point2.getY() - point1.getY()*point2.getX())*(point3.getX() - point4.getX()) -
                (point1.getX() - point2.getX())*(point3.getX()*point4.getY() - point3.getY()*point4.getX());
        double yDividend = (point1.getX()*point2.getY() - point1.getY()*point2.getX())*(point3.getY() - point4.getY()) -
                (point1.getY() - point2.getY())*(point3.getX()*point4.getY() - point3.getY()*point4.getX());
        double divisor = (point1.getX() - point2.getX())*(point3.getY() - point4.getY()) -
                (point1.getY() - point2.getY())*(point3.getX() - point4.getX());

        double xIntersect = xDividend / divisor;
        double yIntersect = yDividend / divisor;

        return new Point2D(xIntersect, yIntersect);
    }
}
