package de.polygondev.socialmanager.event;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.polygondev.socialmanager.SocialManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EVENT_PlayerJoin implements Listener {

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServer");

        player.sendPluginMessage(SocialManager.plugin, "BungeeCord", out.toByteArray());

        if (!SocialManager.sqlTable_players.playerExists(player)) {
            if (SocialManager.sqlTable_players.createPlayer(player, SocialManager.server));
        }
    }

}
