package de.polygondev.socialmanager.utils;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import de.polygondev.socialmanager.SocialManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class Interface_BungeeCord implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if  (!channel.equalsIgnoreCase("BungeeCord")) return;

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("GetServer")) {
            //nachricht vom bungeecord
            SocialManager.server = in.readUTF();
            SocialManager.sqlTable_players.updatePlayer(player, SocialManager.server);
            player.sendMessage("Server: " + SocialManager.sqlTable_players.getPlayerServer(player));
        }
    }

}
