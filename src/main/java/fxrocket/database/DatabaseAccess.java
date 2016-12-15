package fxrocket.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Felix Müllner on 13.04.2017.
 */
public class DatabaseAccess implements AutoCloseable {
    private Connection connection;
    private boolean newDatabase;

    public DatabaseAccess() {
        this.connection = null;
    }

    public DatabaseAccess(String databaseName) throws SQLException {
        this();
        this.open(databaseName);
    }

    public void close() throws SQLException {
        this.connection.close();
    }

    public boolean isNew() {
        return this.newDatabase;
    }

    public void open(String databaseName) throws SQLException {
        this.newDatabase = !new File(databaseName + ".sqlite3").exists();
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + databaseName + ".sqlite3");
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        if (this.connection == null) {
            throw new IllegalStateException("No database open");
        }
        return this.connection.createStatement().executeQuery(sql);
    }

    public void executeUpdate(String sql) throws SQLException {
        if (this.connection == null) {
            throw new IllegalStateException("No database open");
        }
        this.connection.createStatement().executeUpdate(sql);
    }

    public int getLastInsertRowId() throws SQLException {
        if (this.connection == null) {
            throw new IllegalStateException("No database open");
        }
        ResultSet result =  this.connection.createStatement().executeQuery("SELECT last_insert_rowid();");
        result.next();
        return result.getInt(1);
    }
}
