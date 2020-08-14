package de.polygondev.socialmanager.commands;

import de.polygondev.socialmanager.SocialManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CMD_Reload implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        SocialManager.plugin.reloadConfig();

        return SocialManager.mysql.reconnect(
                SocialManager.plugin.getConfig().getString("host"),
                SocialManager.plugin.getConfig().getInt("port"),
                SocialManager.plugin.getConfig().getString("database"),
                SocialManager.plugin.getConfig().getString("username"),
                SocialManager.plugin.getConfig().getString("password")
        );

    }

}
