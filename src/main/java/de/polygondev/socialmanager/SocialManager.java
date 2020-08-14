package de.polygondev.socialmanager;

import de.polygondev.socialmanager.commands.CMD_Reload;
import de.polygondev.socialmanager.event.EVENT_PlayerJoin;
import de.polygondev.socialmanager.friend.commands.CMD_Friend;
import de.polygondev.socialmanager.sqlmanager.MySql;
import de.polygondev.socialmanager.sqlmanager.SQLTable_Friends;
import de.polygondev.socialmanager.sqlmanager.SQLTable_players;
import de.polygondev.socialmanager.sqlmanager.SQLite;
import de.polygondev.socialmanager.utils.Interface_BungeeCord;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.util.HashMap;

public final class SocialManager extends JavaPlugin {

    public static JavaPlugin plugin;
    public static MySql mysql;
    public static SQLite sqLite;

    //MySql Table_Interfaces
    public static SQLTable_players sqlTable_players;
    public static SQLTable_Friends sqlTable_friends;

    public static Messages messages;

    public static String server = "NoBungee";

    public static HashMap<Player, Integer> friendAcceptTaskScheduler = new HashMap<Player, Integer>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        //Bungeecord
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new Interface_BungeeCord());

        messages = new Messages();

        loadConfig();
        if (plugin.getConfig().getString("module").equalsIgnoreCase("mysql")) {
            mysql = new MySql(
                    plugin.getConfig().getString("host"),
                    plugin.getConfig().getInt("port"),
                    plugin.getConfig().getString("database"),
                    plugin.getConfig().getString("username"),
                    plugin.getConfig().getString("password")
            );
        } else if (plugin.getConfig().getString(("module")).equalsIgnoreCase("sqlite")) {
            sqLite = new SQLite(plugin.getConfig().getString("database"));
        }

        sqlTable_players = new SQLTable_players();
        sqlTable_friends = new SQLTable_Friends();
        if (!sqlTable_players.tableExists()) sqlTable_players.createTable();
        if (!sqlTable_friends.tableExists()) sqlTable_friends.createTable();

        this.getServer().getPluginManager().registerEvents(new EVENT_PlayerJoin(), this);
        this.getCommand("friend").setExecutor(new CMD_Friend());
        this.getCommand("socialmanager").setExecutor(new CMD_Reload());


    }

    public static Connection getSqlConnection() {
        if (plugin.getConfig().getString("module").equalsIgnoreCase("mysql")) {
            return mysql.getConnection();
        } else if (plugin.getConfig().getString("module").equalsIgnoreCase("sqlite")) {
            return sqLite.getConnection();
        }
        return null;
    }

    public void loadConfig() {
        this.saveDefaultConfig();
        this.reloadConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        mysql.disconnect();
        sqLite.disconnect();
    }
}
