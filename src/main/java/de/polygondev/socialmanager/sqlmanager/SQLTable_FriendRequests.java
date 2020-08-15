package de.polygondev.socialmanager.sqlmanager;

import de.polygondev.socialmanager.SocialManager;
import org.bukkit.entity.Player;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Calendar;

public class SQLTable_FriendRequests {

    private final String tablename = "friendrequests";
    private String tableprefix;

    public SQLTable_FriendRequests() {
        this.tableprefix = SocialManager.plugin.getConfig().getString("tableprefix");
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

    public boolean createTable() {
        Connection conn = SocialManager.getSqlConnection();
        if (conn == null) {
            return false;
        }

        try {
            PreparedStatement sql = conn.prepareStatement("CREATE TABLE " + this.tableprefix + this.tablename + " (PlayerID INT(100) DEFAULT NULL, RequestID INT(100) DEFAULT NULL, ExpireDate TEXT DEFAULT NULL);");
            sql.executeUpdate();
            return true;

        } catch (SQLException e) {
            //e.printStackTrace();
        }

        /*
        try {
            PreparedStatement sql = conn.prepareStatement("CREATE TABLE \"" + this.tableprefix + this.tablename + "\" (\"PlayerID\" INTEGER, \"RequestID\" INTEGER, \"ExpireDate\" INTEGER);");
            sql.executeUpdate();
            return true;

        } catch (SQLException e) {
            //e.printStackTrace();
        }
        */
        return false;
    }


    public boolean addRequest(Player player, Player request) {
        Connection conn = SocialManager.getSqlConnection();
        if (conn == null) {
            return false;
        }

        try {
            int player1_id = SocialManager.sqlTable_players.getPlayerId(player);
            int player2_id = SocialManager.sqlTable_players.getPlayerId(request);

            if (!this.requestExists(player, request) && player1_id != player2_id) {
                PreparedStatement sql = conn.prepareStatement("INSERT INTO " + this.tableprefix + this.tablename + " VALUES (?, ?, ?);");
                sql.setInt(1, player1_id);
                sql.setInt(2, player2_id);
                sql.setNString(3, (Calendar.getInstance().getTimeInMillis() + 20000) + "");
                sql.executeUpdate();
                return true;
            } else return false;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean requestExists(Player player, Player request) {
        Connection conn = SocialManager.getSqlConnection();
        if (conn == null) {
            return false;
        }

        try {
            int player1_id = SocialManager.sqlTable_players.getPlayerId(player);
            int player2_id = SocialManager.sqlTable_players.getPlayerId(request);

            PreparedStatement sql = conn.prepareStatement("SELECT * FROM " + this.tableprefix + this.tablename + " WHERE (PlayerID=? AND FriendID=?);");
            sql.setInt(1, player1_id);
            sql.setInt(2, player2_id);
            ResultSet rs = sql.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean removeRequest(Player player, Player friend) {
        Connection conn = SocialManager.getSqlConnection();
        if (conn == null) {
            return false;
        }

        try {
            int player1_id = SocialManager.sqlTable_players.getPlayerId(player);
            int player2_id = SocialManager.sqlTable_players.getPlayerId(friend);

            PreparedStatement sql = conn.prepareStatement("DELETE FROM " + this.tableprefix + this.tablename + " WHERE (PlayerID=? AND FriendID=?);");
            sql.setInt(1, player1_id);
            sql.setInt(2, player2_id);
            sql.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet getRequests(Player player) {
        Connection conn = SocialManager.getSqlConnection();
        if (conn == null) {
            return null;
        }

        try {
            int player_id = SocialManager.sqlTable_players.getPlayerId(player);

            PreparedStatement sql = conn.prepareStatement("SELECT * FROM " + this.tableprefix + this.tablename + " WHERE PlayerID=? OR FriendID=?;");
            sql.setInt(1, player_id);
            sql.setInt(2, player_id);
            return sql.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean removeExpiredRequests() {
        Connection conn = SocialManager.getSqlConnection();
        if (conn == null) {
            return false;
        }

        try {
            PreparedStatement sql = conn.prepareStatement("SELECT * FROM " + this.tableprefix + this.tablename);
            ResultSet rs = sql.executeQuery();

            //TODO: Ausgiebig testen ob auch wirklich gelöscht wird
            for (int i = 0; rs.next(); i++) {
                int x = Integer.parseInt(rs.getNString("ExpireDate"));
                if (x <= Calendar.getInstance().getTimeInMillis()) {
                    PreparedStatement sql1 = conn.prepareStatement("DELETE FROM " + this.tableprefix + this.tablename + "WHERE rowid = " + rs.getInt("rowid") + ";");
                    sql.executeUpdate();
                }
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
