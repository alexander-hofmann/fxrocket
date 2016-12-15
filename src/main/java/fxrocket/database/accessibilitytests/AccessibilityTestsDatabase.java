package fxrocket.database.accessibilitytests;

import fxrocket.database.DatabaseAccess;
import javafx.geometry.Point2D;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Felix Müllner on 25.04.2017.
 */
public class AccessibilityTestsDatabase implements AutoCloseable {
    private DatabaseAccess da;

    public AccessibilityTestsDatabase() throws SQLException {
        this.da = new DatabaseAccess("accessibilitytests");
        if (this.da.isNew()) {
            this.initializeDatabase();
        }
    }

    @Override
    public void close() throws Exception {
        this.da.close();
    }

    public List<User> getUsers() {
        String sql = "SELECT * FROM users;";
        ResultSet results = null;
        List<User> users = new ArrayList<User>();

        try {
            results = this.da.executeQuery(sql);
            while (results.next()) {
                int id = results.getInt("id");
                String fullName = results.getString("full_name");
                Date birthdate = new SimpleDateFormat("yyyy-MM-dd").parse(results.getString("birthdate"));
                String sex = results.getString("sex");
                String category = results.getString("category");
                users.add(new User(id, fullName, birthdate, sex, category));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return users;
    }

    public void initializeDatabase() throws SQLException {
        String sql = "PRAGMA foreign_keys = off;\n" +
                "BEGIN TRANSACTION;\n" +
                "CREATE TABLE aim_measurements (id INTEGER PRIMARY KEY, session INTEGER NOT NULL REFERENCES aim_sessions (id), number INTEGER NOT NULL, target_position_phi REAL NOT NULL, target_position_r REAL NOT NULL, target_position_x REAL NOT NULL, target_position_y REAL NOT NULL, aim_time REAL NOT NULL, aim_acceleration REAL NOT NULL, aim_correction REAL NOT NULL, click_time REAL NOT NULL, click_accuracy REAL NOT NULL);\n" +
                "CREATE TABLE aim_sessions (id INTEGER PRIMARY KEY AUTOINCREMENT, user INTEGER REFERENCES users (id) NOT NULL, start DATETIME NOT NULL, finish DATETIME CHECK (finish IS NULL OR finish > start));\n" +
                "CREATE TABLE joystick_measurements (id INTEGER PRIMARY KEY, session INTEGER NOT NULL REFERENCES joystick_sessions (id), number INTEGER NOT NULL, line_length REAL NOT NULL, line_shape TEXT NOT NULL, completion_time REAL NOT NULL, attempts INTEGER NOT NULL CHECK (attempts > 0), amount_on_target REAL NOT NULL, average_deviation REAL NOT NULL);\n" +
                "CREATE TABLE joystick_sessions (id INTEGER PRIMARY KEY AUTOINCREMENT, user INTEGER REFERENCES users (id) NOT NULL, start DATETIME NOT NULL, finish DATETIME CHECK (finish IS NULL OR finish > start));\n" +
                "CREATE TABLE reaction_measurements (id INTEGER PRIMARY KEY, session INTEGER NOT NULL REFERENCES reaction_sessions (id), number INTEGER NOT NULL, reaction_time REAL NOT NULL);\n" +
                "CREATE TABLE reaction_sessions (id INTEGER PRIMARY KEY AUTOINCREMENT, user INTEGER REFERENCES users (id) NOT NULL, start DATETIME NOT NULL, finish DATETIME CHECK (finish IS NULL OR finish > start));\n" +
                "CREATE TABLE recognition_measurements (id INTEGER PRIMARY KEY, session INTEGER NOT NULL REFERENCES recognition_sessions (id), number INTEGER NOT NULL, object_size REAL NOT NULL, reaction REAL NOT NULL, object_shown TEXT NOT NULL, object_recognized TEXT NOT NULL, correct BOOLEAN NOT NULL CHECK (correct = (object_shown = object_recognized)));\n" +
                "CREATE TABLE recognition_sessions (id INTEGER PRIMARY KEY AUTOINCREMENT, user INTEGER REFERENCES users (id) NOT NULL, start DATETIME NOT NULL, finish DATETIME CHECK (finish IS NULL OR finish > start));\n" +
                "CREATE TABLE sequence_measurements (id INTEGER PRIMARY KEY, session INTEGER NOT NULL REFERENCES sequence_sessions (id), number INTEGER NOT NULL, target TEXT NOT NULL, correct BOOLEAN NOT NULL, error_position INTEGER CHECK (correct OR error_position IS NOT NULL), error_button TEXT CHECK (correct OR error_button IS NOT NULL));\n" +
                "CREATE TABLE sequence_sessions (id INTEGER PRIMARY KEY AUTOINCREMENT, user INTEGER REFERENCES users (id) NOT NULL, start DATETIME NOT NULL, finish DATETIME CHECK (finish IS NULL OR finish > start));\n" +
                "CREATE TABLE simultaneous_measurements (id INTEGER PRIMARY KEY, session INTEGER NOT NULL REFERENCES simultaneous_sessions (id), number INTEGER NOT NULL, buttons_target TEXT NOT NULL, buttons_pressed TEXT NOT NULL, completion_time REAL NOT NULL, correct BOOLEAN NOT NULL CHECK (correct = (buttons_target = buttons_pressed)));\n" +
                "CREATE TABLE simultaneous_sessions (id INTEGER PRIMARY KEY AUTOINCREMENT, user INTEGER REFERENCES users (id) NOT NULL, start DATETIME NOT NULL, finish DATETIME CHECK (finish IS NULL OR finish > start));\n" +
                "CREATE TABLE timing_multiple_measurements (id INTEGER PRIMARY KEY, session INTEGER NOT NULL REFERENCES timing_multiple_sessions (id), number INTEGER NOT NULL, tempo REAL NOT NULL, precision REAL CHECK (missed OR precision IS NOT NULL), missed BOOLEAN NOT NULL, button_target TEXT NOT NULL, button_pressed TEXT NOT NULL, correct BOOLEAN NOT NULL CHECK (correct = (button_target = button_pressed)));\n" +
                "CREATE TABLE timing_multiple_sessions (id INTEGER PRIMARY KEY AUTOINCREMENT, user INTEGER REFERENCES users (id) NOT NULL, start DATETIME NOT NULL, finish DATETIME CHECK (finish IS NULL OR finish > start));\n" +
                "CREATE TABLE timing_single_measurements (id INTEGER PRIMARY KEY, session INTEGER NOT NULL REFERENCES timing_single_sessions (id), number INTEGER NOT NULL, tempo REAL NOT NULL, precision REAL CHECK (missed OR precision IS NOT NULL), missed BOOLEAN NOT NULL);\n" +
                "CREATE TABLE timing_single_sessions (id INTEGER PRIMARY KEY AUTOINCREMENT, user INTEGER REFERENCES users (id) NOT NULL, start DATETIME NOT NULL, finish DATETIME CHECK (finish IS NULL OR finish > start));\n" +
                "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, full_name TEXT NOT NULL, birthdate DATE NOT NULL, sex TEXT NOT NULL CHECK (sex IN (\"f\", \"m\", \"o\")), category TEXT NOT NULL);\n" +
                "COMMIT TRANSACTION;\n" +
                "PRAGMA foreign_keys = on;\n";
        this.da.executeUpdate(sql);
    }

    public void saveJoystickMeasurement(int sessionId, int number, double lineLength, List<Point2D> linePoints, double completionTime, int attempts, double amountOnTarget, double averageDeviation) throws SQLException {
        String lineShape = "";
        for (Point2D point : linePoints) {
            lineShape += point.getX() + "," + point.getY() + ";";
        }

        String sql1 = "INSERT INTO joystick_measurements (session, number, line_length, line_shape, completion_time, attempts, amount_on_target, average_deviation) VALUES " +
                "(" + sessionId + ", " + number + ", " + lineLength + ", \"" + lineShape + "\", " + completionTime + ", " + attempts + ", " + amountOnTarget + ", " + averageDeviation + ");";
        this.da.executeUpdate(sql1);

        String dateTimeString = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        String sql2 = "UPDATE joystick_sessions SET finish = \"" + dateTimeString + "\" WHERE id = " + sessionId + ";";
        this.da.executeUpdate(sql2);
    }

    public int startJoystickSession(User user) throws SQLException {
        String dateTimeString = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        String sql = "INSERT INTO joystick_sessions (user, start, finish) VALUES (" + user.getId() + ", \"" + dateTimeString + "\", NULL);";
        this.da.executeUpdate(sql);
        return this.da.getLastInsertRowId();
    }
}
