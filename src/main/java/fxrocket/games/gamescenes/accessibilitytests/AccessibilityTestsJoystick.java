package fxrocket.games.gamescenes.accessibilitytests;

import fxrocket.GamepadWrapper;
import fxrocket.JoystickPath;
import fxrocket.JoystickPathProgress;
import fxrocket.Main;
import fxrocket.database.accessibilitytests.AccessibilityTestsDatabase;
import fxrocket.database.accessibilitytests.User;
import fxrocket.games.Game;
import fxrocket.games.GameScene;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Felix Müllner on 26.04.2017.
 */
public class AccessibilityTestsJoystick implements GameScene {
    private int attempts;
    private JoystickPath curve = null;
    private Game game;
    private GamepadWrapper gamepadWrapper;
    private Point2D lastCursorPosition;
    private boolean lastInRange;
    private int lastProgress;
    private boolean pathCompleted;
    private int pathNumber;
    private List<Point2D> pathPoints;
    private List<JoystickPathProgress> pathProgresses;
    private boolean pathStarted;
    private User selectedUser;
    private int sessionId;
    private LocalDateTime timeCompleted;
    private LocalDateTime timeStarted;

    private Random random = new Random(42);

    public AccessibilityTestsJoystick(Game game) {
        if (game == null) {
            throw new NullPointerException();
        }
        this.game = game;

        this.gamepadWrapper = (GamepadWrapper) this.game.retrieveData("gamepadWrapper1");
        this.gamepadWrapper.setPos(0, 0);

        this.attempts = 1;
        this.lastCursorPosition = Point2D.ZERO;
        this.lastInRange = false;
        this.lastProgress = -1;
        this.pathCompleted = false;
        this.pathNumber = 1;
        this.pathProgresses = new ArrayList<>();
        this.pathStarted = false;
        this.selectedUser = (User) this.game.retrieveData("selectedUser");
        this.sessionId = -1;
        this.timeCompleted = null;
        this.timeStarted = null;

        this.generatePath();
    }

    @Override
    public void process(long now) {
        this.gamepadWrapper.calculateFrame();
        Point2D cursorPosition = new Point2D(this.gamepadWrapper.getXPos(), this.gamepadWrapper.getYPos());
        JoystickPathProgress progress = this.curve.trace(cursorPosition, this.lastCursorPosition);

        if (!this.pathStarted && progress.isStarted()) {
            this.pathStarted = true;
            this.timeStarted = LocalDateTime.now();
        } else if (!this.pathCompleted && progress.isCompleted()) {
            this.pathCompleted = true;
            this.timeCompleted = LocalDateTime.now();
            long completionTime = ChronoUnit.MILLIS.between(this.timeStarted, this.timeCompleted);

            double amountOnTarget;
            double averageDeviation = 0;

            int pointsOnTarget = 0;
            int validPoints = 0;

            for (JoystickPathProgress pathProgress : this.pathProgresses) {
                if (!pathProgress.isOutOfRange()) {
                    validPoints++;
                    averageDeviation += pathProgress.getDistance();
                    if (pathProgress.getDistance() < 10) {
                        pointsOnTarget++;
                    }
                }
            }

            amountOnTarget = ((double) pointsOnTarget)/validPoints;
            averageDeviation /= validPoints;

            try {
                AccessibilityTestsDatabase db = new AccessibilityTestsDatabase();

                if (this.sessionId == -1) {
                    this.sessionId = db.startJoystickSession(this.selectedUser);
                }
                db.saveJoystickMeasurement(this.sessionId, this.pathNumber, this.curve.getLength(), this.pathPoints, completionTime, this.attempts, amountOnTarget, averageDeviation);

                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //make new path
            this.generatePath();
            this.attempts = 1;
            this.gamepadWrapper.setPos(0, 0);
            this.lastCursorPosition = Point2D.ZERO;
            this.lastInRange = false;
            this.pathCompleted = false;
            this.pathNumber++;
            this.pathProgresses = new ArrayList<>();
            this.pathStarted = false;
        }

        if (progress.isOutOfRange() && this.lastInRange) {
            this.attempts++;
        }
        if (progress.getProgress() != this.lastProgress) {
            this.pathProgresses.add(progress);
        }

        //check if back button is pressed
        int sideLength = (int) Math.round(Main.SCREEN_SIZE_X * 0.05);
        if (this.gamepadWrapper.isStickPressedNew() && new Rectangle(0, Main.SCREEN_SIZE_Y - sideLength, sideLength, sideLength + 2).contains(this.gamepadWrapper.getXPos(), this.gamepadWrapper.getYPos())) {
            this.game.switchScene("Main Menu");
        }

        this.lastCursorPosition = cursorPosition;
        this.lastInRange = !progress.isOutOfRange();
        this.lastProgress = progress.getProgress();
    }


    @Override
    public void render(GraphicsContext gc) {
        gc.clearRect(0,0,Main.SCREEN_SIZE_X,Main.SCREEN_SIZE_Y);

        this.curve.draw(gc);

        //draw "back" button
        int sideLength = (int) Math.round(Main.SCREEN_SIZE_X * 0.05);
        double sideLengthEight = (double) (sideLength) / 8;
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.fillRect(0, Main.SCREEN_SIZE_Y - sideLength, sideLength, sideLength);
        gc.strokeRect(0, Main.SCREEN_SIZE_Y - sideLength, sideLength, sideLength);
        gc.setFill(Color.BLUE);
        gc.beginPath();
        gc.moveTo(1 * sideLengthEight, Main.SCREEN_SIZE_Y - sideLength + 4 * sideLengthEight);
        gc.lineTo(3 * sideLengthEight, Main.SCREEN_SIZE_Y - sideLength + 2 * sideLengthEight);
        gc.lineTo(3 * sideLengthEight, Main.SCREEN_SIZE_Y - sideLength + 3 * sideLengthEight);
        gc.lineTo(7 * sideLengthEight, Main.SCREEN_SIZE_Y - sideLength + 3 * sideLengthEight);
        gc.lineTo(7 * sideLengthEight, Main.SCREEN_SIZE_Y - sideLength + 5 * sideLengthEight);
        gc.lineTo(3 * sideLengthEight, Main.SCREEN_SIZE_Y - sideLength + 5 * sideLengthEight);
        gc.lineTo(3 * sideLengthEight, Main.SCREEN_SIZE_Y - sideLength + 6 * sideLengthEight);
        gc.closePath();
        gc.fill();

        //draw cursor
        if (new Rectangle(0, Main.SCREEN_SIZE_Y - sideLength, sideLength, sideLength + 2).contains(this.gamepadWrapper.getXPos(), this.gamepadWrapper.getYPos())) {
            gc.setFill(Color.GREEN);
            gc.fillOval(this.gamepadWrapper.getXPos() - 8, this.gamepadWrapper.getYPos() - 8, 16, 16);
        } else {
            gc.setStroke(Color.GREEN);
            gc.strokeLine(this.gamepadWrapper.getXPos() - 20, this.gamepadWrapper.getYPos(), this.gamepadWrapper.getXPos() + 20, this.gamepadWrapper.getYPos());
            gc.strokeLine(this.gamepadWrapper.getXPos(), this.gamepadWrapper.getYPos() - 20, this.gamepadWrapper.getXPos(), this.gamepadWrapper.getYPos() + 20);
        }
    }

    private void generatePath() {
        double stepLength = ((double) Main.SCREEN_SIZE_X) / 11;
        double[] xValues = {1*stepLength, 2*stepLength, 3*stepLength, 4*stepLength, 5*stepLength, 6*stepLength, 7*stepLength, 8*stepLength, 9*stepLength, 10*stepLength};
        this.pathPoints = new ArrayList<>();

        for (double xValue : xValues) {
            Point2D newPoint = new Point2D(xValue + (this.random.nextDouble()*10 - 5), this.random.nextDouble()*Main.SCREEN_SIZE_Y);
            this.pathPoints.add(newPoint);
        }
        this.curve = new JoystickPath(this.pathPoints);
    }
}
