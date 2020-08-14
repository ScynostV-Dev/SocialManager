package de.polygondev.socialmanager.sqlmanager;

import de.polygondev.socialmanager.SocialManager;
import org.bukkit.entity.Player;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLTable_Friends {

    private final String tablename = "friends";
    private String tableprefix;

    public SQLTable_Friends() {
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
            SocialManager.messages.friendDatabaseExistError();
        }
        return false;
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
            PreparedStatement sql = conn.prepareStatement("CREATE TABLE " + this.tableprefix + this.tablename + " (PlayerID INT(100), FriendID INT(100));");
            sql.executeUpdate();
            return true;

        } catch (SQLException e) {
            //e.printStackTrace();
        }
        return false;
    }

    public boolean addFriend(Player player, Player friend) {
        Connection conn = SocialManager.getSqlConnection();
        if (conn == null) {
            return false;
        }

        try {
            int player1_id = SocialManager.sqlTable_players.getPlayerId(player);
            int player2_id = SocialManager.sqlTable_players.getPlayerId(friend);

            if (!this.friendsExists(player, friend) && player1_id != player2_id) {
                PreparedStatement sql = conn.prepareStatement("INSERT INTO " + this.tableprefix + this.tablename + " VALUES (?, ?)");
                sql.setInt(1, player1_id);
                sql.setInt(2, player2_id);
                sql.executeUpdate();
                return true;
            } else return false;

        } catch (SQLException e) {
            //e.printStackTrace();
        }



        return false;
    }

    public boolean removeFriend(Player player, Player friend) {
        Connection conn = SocialManager.getSqlConnection();
        if (conn == null) {
            return false;
        }

        try {
            int player1_id = SocialManager.sqlTable_players.getPlayerId(player);
            int player2_id = SocialManager.sqlTable_players.getPlayerId(friend);

            PreparedStatement sql = conn.prepareStatement("DELETE FROM " + this.tableprefix + this.tablename + " WHERE (PlayerID=? AND FriendID=?) OR (PlayerID=? AND FriendID=?);");
            sql.setInt(1, player1_id);
            sql.setInt(2, player2_id);
            sql.setInt(3, player2_id);
            sql.setInt(4, player1_id);
            sql.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean friendsExists(Player player, Player friend) {
        Connection conn = SocialManager.getSqlConnection();
        if (conn == null) {
            return false;
        }

        try {
            int player1_id = SocialManager.sqlTable_players.getPlayerId(player);
            int player2_id = SocialManager.sqlTable_players.getPlayerId(friend);

            PreparedStatement sql = conn.prepareStatement("SELECT * FROM " + this.tableprefix + this.tablename + " WHERE (PlayerID=? AND FriendID=?) OR (PlayerID=? AND FriendID=?)");
            sql.setInt(1, player1_id);
            sql.setInt(2, player2_id);
            sql.setInt(3, player2_id);
            sql.setInt(4, player1_id);
            ResultSet rs = sql.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet getFriends(Player player) {
        Connection conn = SocialManager.getSqlConnection();
        if (conn == null) {
            return null;
        }

        try {
            int player_id = SocialManager.sqlTable_players.getPlayerId(player);

            PreparedStatement sql = conn.prepareStatement("SELECT * FROM " + this.tableprefix + this.tablename + " WHERE PlayerID=? OR FriendID=?");
            sql.setInt(1, player_id);
            sql.setInt(2, player_id);
            return sql.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
