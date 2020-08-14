package de.polygondev.socialmanager.sqlmanager;

import de.polygondev.socialmanager.SocialManager;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLite {

    private Connection connection;

    public SQLite() {}

    public SQLite(String database) {
        this.connect(database);
    }

    public void disconnect() {
        try {
            connection.close();
            connection = null;
        } catch (Exception ignored) {}
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isConnected() {
        return connection != null;
    }

    public boolean connect(String database) {
        try {
            synchronized (this) {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:plugins/" + SocialManager.plugin.getName() + "/" + database + ".sqlite");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean reconnect(String database) {
        disconnect();
        return this.connect(database);
    }
}
