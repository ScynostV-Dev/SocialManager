package de.polygondev.socialmanager.friend.commands;

import com.destroystokyo.paper.profile.PlayerProfile;
import de.polygondev.inventoryapi.InventoryApi;
import de.polygondev.inventoryapi.inventory.Inventory;
import de.polygondev.socialmanager.SocialManager;
import de.polygondev.socialmanager.inventories.INV_Friend;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class CMD_Friend implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                showFriendList(player);
            } else {
                switch (args[0]) {
                    //Friend list command
                    case "list":
                        showFriendList(player);
                        break;
                        //friend add command
                    case "add":
                        if (args[1] != null) {
                            Player otherplayer = Bukkit.getPlayer(args[1]);
                            int x = SocialManager.messages.friendAddRequest(player, otherplayer);
                            SocialManager.friendAcceptTaskScheduler.put(player, x);
                            //addFriend(player, otherplayer);
                        }
                        break;
                        //friend remove command
                    case "remove":
                        if (args[1] != null) {
                            Player otherplayer = Bukkit.getPlayer(args[1]);
                            return removeFriend(player, otherplayer);
                        }
                        break;
                        //friend accept command
                    case "accept":
                        if (args[1] != null) {
                            Player otherplayer = Bukkit.getPlayer(args[1]);
                            if (SocialManager.friendAcceptTaskScheduler.get(otherplayer) != null) {
                                return acceptFriend(player, otherplayer);
                            }
                        }
                        break;
                        //friend deny command
                    case "deny":
                        if (args[1] != null) {
                            Player otherplayer = Bukkit.getPlayer(args[1]);
                            if (SocialManager.friendAcceptTaskScheduler.get(otherplayer) != null) {
                                return denyFriend(player, otherplayer);
                            }
                        }
                        break;
                }
            }
            //SocialManager.messages.usage(player);
        }

        //SocialManager.messages.usage(SocialManager.plugin.getServer().getConsoleSender());
        return true;
    }

    private boolean acceptFriend(Player player, Player otherplayer) {
        this.addFriend(player, otherplayer);
        SocialManager.messages.friendAccept(player, otherplayer);

        Bukkit.getScheduler().cancelTask(SocialManager.friendAcceptTaskScheduler.get(otherplayer));
        SocialManager.friendAcceptTaskScheduler.remove(otherplayer);

        return true;
    }

    private boolean denyFriend(Player player, Player otherplayer) {
        SocialManager.messages.friendDeny(player, otherplayer);

        Bukkit.getScheduler().cancelTask(SocialManager.friendAcceptTaskScheduler.get(otherplayer));
        SocialManager.friendAcceptTaskScheduler.remove(otherplayer);

        return true;
    }

    private boolean removeFriend(Player player, Player otherplayer) {
        if (otherplayer != null) {
            //Freund hinzufügen
            if (SocialManager.sqlTable_friends.removeFriend(player, otherplayer)) {
                return true;
            }
        }
        return false;
    }

    private boolean addFriend(Player player, Player otherplayer) {
        if (otherplayer != null) {
            //Freund hinzufügen
            if (SocialManager.sqlTable_friends.addFriend(player, otherplayer)) {
                return true;
            }

            SocialManager.messages.friendAddRequest(player, otherplayer);
        }
        return false;
    }

    private void showFriendList(Player player) {
        Inventory invFriend = null;
        if (InventoryApi.INV_REGISTER.checkInventoryFromPlayerExisting(player, "inv_friend") == (-1)) {
            invFriend = new INV_Friend("[§cFriends§r]", "inv_friend");

            InventoryApi.INV_REGISTER.addInventoryToPlayer(player, invFriend);
        } else {
            invFriend = InventoryApi.INV_REGISTER.getInventoryFromPlayer(player, "inv_friend");
        }


        if (!SocialManager.sqlTable_players.playerExists(player)) {
            SocialManager.sqlTable_players.createPlayer(player, SocialManager.server); //Player im Playerlist einfügen wenn nicht existent
            SocialManager.messages.noFriend(player);
            return;
        }

        try {
            if (!SocialManager.sqlTable_friends.getFriends(player).next()) {
                SocialManager.messages.noFriend(player);
                return;
            }
        } catch (SQLException e) {
            SocialManager.messages.noFriend(player);
            e.printStackTrace();
            return;
        }
        int playerid = SocialManager.sqlTable_players.getPlayerId(player);

        ResultSet rs = SocialManager.sqlTable_friends.getFriends(player);
        ArrayList<Integer> al = new ArrayList<>();
        try {
            while (rs.next()) {
                int i1 = rs.getInt("PlayerID");
                int i2 = rs.getInt("FriendID");

                if (i1!= playerid)  {
                    al.add(i1);
                } else if (i2 != playerid) {
                    al.add(i2);
                } else {
                    throw new SQLException();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < al.size(); i++) {
            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta playerHeadMeta = (SkullMeta) playerHead.getItemMeta();

            UUID uuid = SocialManager.sqlTable_players.getPlayerUUID(al.get(i));

            if (Bukkit.getServer().getPlayer(uuid) != null) {
                PlayerProfile pf = Bukkit.getServer().getPlayer(uuid).getPlayerProfile();
                playerHeadMeta.setPlayerProfile(pf);

            } else {

                OfflinePlayer op = Bukkit.getServer().getOfflinePlayer(uuid);
                playerHeadMeta.setOwningPlayer(op);
            }

            playerHead.setItemMeta(playerHeadMeta);

            invFriend.setItem(i, playerHead);
        }

        invFriend.openInventory();
    }
}
