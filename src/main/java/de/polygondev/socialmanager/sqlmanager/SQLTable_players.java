package de.polygondev.socialmanager.sqlmanager;

import de.polygondev.socialmanager.SocialManager;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLTable_players {

    private final String tablename = "players";
    private String tableprefix;

    public SQLTable_players() {
        tableprefix = SocialManager.plugin.getConfig().getString("tableprefix");
    }

    /**
     *
     * @return
     */
    public boolean tableExists() {
        Connection conn = SocialManager.getSqlConnection();
        if (conn == null) {
            return false;
        }

        try {
            PreparedStatement sql = conn.prepareStatement("SELECT * FROM " + this.tableprefix + this.tablename + ";");
            //sql.setString(1, this.tableprefix + this.tablename);

            ResultSet rs = sql.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @return
     */
    public boolean createTable() {
        Connection conn = SocialManager.getSqlConnection();
        if (conn == null) {
            return false;
        }

        try {
            PreparedStatement sql = conn.prepareStatement("CREATE TABLE " + this.tableprefix + this.tablename + " (PlayerID INT(100) NOT NULL AUTO_INCREMENT, UUID VARCHAR(36) UNIQUE DEFAULT NULL, PlayerName VARCHAR(100) DEFAULT NULL, Server VARCHAR(100), PRIMARY KEY (PlayerID));");
            sql.executeUpdate();
            return true;

        } catch (SQLException e) {
            //e.printStackTrace();
        }

        try {
            PreparedStatement sql = conn.prepareStatement("CREATE TABLE \"" + this.tableprefix + this.tablename + "\" (\"PlayerID\"INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \"UUID\"VARCHAR(36) UNIQUE, \"PlayerName\"VARCHAR(100), \"Server\"VARCHAR(100));");
            sql.executeUpdate();
            return true;

        } catch (SQLException e) {
            //e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param player
     * @return
     */
    public boolean playerExists(Player player) {
        return getPlayerId(player) > 0;
    }

    /**
     *
     * @param player
     * @return
     */
    public int getPlayerId(Player player) {
        Connection conn = SocialManager.getSqlConnection();
        if (conn == null) {
            return 0;
        }

        try {
            PreparedStatement sql = conn.prepareStatement("SELECT * FROM " + this.tableprefix + this.tablename + " WHERE UUID=?");
            sql.setString(1, player.getPlayer().getUniqueId().toString());

            ResultSet rs = sql.executeQuery();

            if (rs.next()) {
                return rs.getInt("PlayerID");
            }

        } catch (SQLException | NullPointerException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public String getPlayerName(int x) {
        Connection conn = SocialManager.getSqlConnection();
        if (conn == null) {
            return null;
        }

        try {
            PreparedStatement sql = conn.prepareStatement("SELECT * FROM " + this.tableprefix + this.tablename + " WHERE PlayerID=?");
            sql.setInt(1, x);

            ResultSet rs = sql.executeQuery();

            if (rs.next()) {
                return rs.getString("PlayerName");
            }

        } catch (SQLException | NullPointerException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public UUID getPlayerUUID(int x) {
        Connection conn = SocialManager.getSqlConnection();
        if (conn == null) {
            return null;
        }

        try {
            PreparedStatement sql = conn.prepareStatement("SELECT * FROM " + this.tableprefix + this.tablename + " WHERE PlayerID=?");
            sql.setInt(1, x);

            ResultSet rs = sql.executeQuery();

            if (rs.next()) {
                return UUID.fromString(rs.getString("UUID"));
            }

        } catch (SQLException | NullPointerException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param player
     * @return
     */
    public boolean createPlayer(Player player, String server) {
        Connection conn = SocialManager.getSqlConnection();
        if (conn == null) {
            return false;
        }

        try {
            if (!playerExists(player)) {
                PreparedStatement sql = conn.prepareStatement("INSERT INTO " + this.tableprefix + this.tablename + " VALUES (null, ?, ?, ?);");
                sql.setString(1, player.getUniqueId().toString());
                sql.setString(2, player.getName());
                sql.setString(3, server);
                sql.executeUpdate();
                return true;
            }
        } catch (SQLException | NullPointerException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public String getPlayerServer(Player player) {
        Connection conn = SocialManager.getSqlConnection();
        if (conn == null) {
            return null;
        }

        try {
            if (playerExists(player)) {
                PreparedStatement sql = conn.prepareStatement("SELECT * FROM " + this.tableprefix + this.tablename + " WHERE PlayerName=?");
                sql.setString(1, player.getName());

                ResultSet rs = sql.executeQuery();

                if (rs.next()) {
                    return rs.getString("Server");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updatePlayer(Player player, String server) {
        Connection conn = SocialManager.getSqlConnection();
        if (conn == null) {
            return false;
        }

        try {
            if (playerExists(player)) {
                PreparedStatement sql = conn.prepareStatement("UPDATE " + this.tableprefix + this.tablename + " SET UUID=?, PlayerName=?, Server=?;");
                sql.setString(1, player.getUniqueId().toString());
                sql.setString(2, player.getName());
                sql.setString(3, server);
                sql.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
