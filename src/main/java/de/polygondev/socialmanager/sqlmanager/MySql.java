package de.polygondev.socialmanager.sqlmanager;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySql {

    private Connection connection;

    public MySql() {}

    public MySql(String host, int port, String database, String username, String password) {
        this.connect(host, port, database, username, password);
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

    public boolean connect(String host, int port, String database, String username, String password) {
        try {
            synchronized (this) {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://" + host+ ":" + port + "/" + database, username, password);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean reconnect(String host, int port, String database, String username, String password) {
        disconnect();
        return this.connect(host, port, database, username, password);
    }
}
