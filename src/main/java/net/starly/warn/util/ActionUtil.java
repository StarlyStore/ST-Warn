package net.starly.warn.util;

import net.starly.warn.WarnMain;
import net.starly.warn.data.PlayerWarnData;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import static net.starly.warn.WarnMain.config;

public class ActionUtil {
    public static void action(OfflinePlayer target) {
        int amount = new PlayerWarnData(target).getWarn();

        String action = WarnMain.config.getString("others.actions." + amount);
        if (action == null) return;

        if (action.equalsIgnoreCase("message")) {
            if (target.isOnline()) {
                target.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("others.actionValue.message")));
            }
        } else if (action.equalsIgnoreCase("kick")) {
            if (target.isOnline()) {
                target.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', config.getString("others.actionValue.kick")));
            }
        } else if (action.equalsIgnoreCase("ban")) {
            if (target.isOnline()) {
                target.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', config.getString("others.actionValue.ban")));
            }

            Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(), ChatColor.translateAlternateColorCodes('&', WarnMain.config.getString("others.actionValue.ban")), null, null);
        } else if (action.equalsIgnoreCase("ban-ip")) {
            if (target.isOnline()) {
                target.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', config.getString("others.actionValue.ban-ip")));
            }

            Bukkit.getBanList(BanList.Type.IP).addBan(target.getPlayer().getAddress().getHostName(), ChatColor.translateAlternateColorCodes('&', config.getString("others.actionValue.ban-ip")), null, null);
        }
    }
}
