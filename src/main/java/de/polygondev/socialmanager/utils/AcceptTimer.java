package de.polygondev.socialmanager.utils;

import de.polygondev.socialmanager.SocialManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AcceptTimer implements Runnable {

    private int task;
    private Player player;
    private Player requestedPlayer;

    public int getTask() {
        return task;
    }

    public AcceptTimer(Player player, Player requestedPlayer) {
        this.player = player;
        this.requestedPlayer = requestedPlayer;

        this.task = Bukkit.getScheduler().scheduleSyncDelayedTask(SocialManager.plugin, this, 400);
    }

    @Override
    public void run() {
        player.sendMessage("Asche in asche kohle in freeeude   " + requestedPlayer.getDisplayName());

        SocialManager.friendAcceptTaskScheduler.remove(player);
        Bukkit.getScheduler().cancelTask(task);
    }

}
