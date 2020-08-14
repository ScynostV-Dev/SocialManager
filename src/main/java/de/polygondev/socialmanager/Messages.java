package de.polygondev.socialmanager;

import de.polygondev.socialmanager.utils.AcceptTimer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Messages {

    public void noFriend(Player player) {
        player.sendMessage(ChatColor.GOLD + "Du hast " + ChatColor.RED + "KEINE" + ChatColor.GOLD + " Freunde!");
        player.sendMessage(ChatColor.GOLD + "Füge einen freund mit " + ChatColor.RED + "/friend add <spielername>" + ChatColor.GOLD + " hinzu.");
    }

    public void usage(Player player) {
        player.sendMessage(ChatColor.GOLD + "Entweder ist der Befehl falsch geschrieben oder es ist ein Fehler aufgetreten.");
        player.sendMessage(ChatColor.GOLD + "Bitte verwende: " + ChatColor.RED + "/friend <list, add, remove, accept, deny>");
    }

    public void usage(ConsoleCommandSender console) {
        console.sendMessage(ChatColor.RED + "Dieser Befehl ist als Spieler auszuführen!");
    }

    public void friendAddException(Player player) {
        player.sendMessage(ChatColor.GOLD + "Beim " + ChatColor.RED + "hinzufügen" + ChatColor.GOLD + " ist ein " + ChatColor.RED + "Fehler" + ChatColor.GOLD + " aufgetreten.");
        player.sendMessage(ChatColor.GOLD + "Ist der Spieler online?");
    }

    public void friendDeny(Player player, Player otherplayer) {
        player.sendMessage(ChatColor.GOLD + "Du hast die Freundesanfrage erfolgreich abgelehnt! Sei glücklich du einsamer ;)");

        otherplayer.sendMessage(ChatColor.GOLD + "Deine Anfrage für: " + ChatColor.BLUE + player.getDisplayName() + " " + ChatColor.RED + "wurde abgelehnt");
    }

    public void friendAccept(Player player, Player otherplayer) {
        player.sendMessage(ChatColor.GOLD + "Du hast die Freundesanfrage erfolgreich angenommen! YaaaY ;)");

        otherplayer.sendMessage(ChatColor.GOLD + "Deine Anfrage für: " + ChatColor.BLUE + player.getDisplayName() + " " + ChatColor.GREEN + "wurde angenommen");
        otherplayer.sendMessage(ChatColor.GOLD + "Er/Sie wurde deiner Freundesliste hinzugefügt");
    }

    public int friendAddRequest(Player player, Player requestedPlayer) {

        TextComponent accept = new TextComponent();
        accept.setText(ChatColor.GREEN + "✔");
        accept.setBold((true));
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GREEN + "/friend accept " + player.getName()).create()));
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend accept " + player.getName()));

        TextComponent deny = new TextComponent();
        deny.setText(ChatColor.RED + "✘");
        deny.setBold((true));
        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "/friend deny " + player.getName()).create()));
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend deny " + player.getName()));

        TextComponent message = new TextComponent();
        message.setText(ChatColor.GOLD + "Möchtest du diese Einladung annehmen? ");
        message.addExtra(accept);
        message.addExtra(" ");
        message.addExtra(deny);
        message.addExtra(" " + ChatColor.RED + "/friend accept " + player.getName());

        requestedPlayer.sendMessage(ChatColor.GOLD + "Du wurdest eingeladen der Freund/die Freundin von " + player.getName() + " zu werden.");
        requestedPlayer.sendMessage(message);

        AcceptTimer ac = new AcceptTimer(player, requestedPlayer);
        return ac.getTask();
    }

    //Table console messages
    public void friendDatabaseExistError() {
        SocialManager.plugin.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[SocialManager] " + ChatColor.RED + "Friends table have probably an error. Please check");
    }

    public void friendDatabaseCreateError() {
        SocialManager.plugin.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[SocialManager] " + ChatColor.GOLD + "Friends create table error. Please check");
    }
}
