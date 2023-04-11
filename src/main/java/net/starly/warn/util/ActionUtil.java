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
                target.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("others.actionValue.message")
                        .replace("{player}", target.getName())
                        .replace("{uuid}", String.valueOf(target.getUniqueId()))
                        .replace("{amount}", String.valueOf(amount))));
            }
        } else if (action.equalsIgnoreCase("kick")) {
            if (target.isOnline()) {
                target.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', config.getString("others.actionValue.kick")
                        .replace("{player}", target.getName())
                        .replace("{uuid}", String.valueOf(target.getUniqueId()))
                        .replace("{amount}", String.valueOf(amount))));
            }
        } else if (action.equalsIgnoreCase("ban")) {
            String msg = ChatColor.translateAlternateColorCodes('&', WarnMain.config.getString("others.actionValue.ban")
                    .replace("{player}", target.getName())
                    .replace("{uuid}", String.valueOf(target.getUniqueId()))
                    .replace("{amount}", String.valueOf(amount)));

            if (target.isOnline()) target.getPlayer().kickPlayer(msg);

            Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(), msg, null, null);
        } else if (action.equalsIgnoreCase("ban-ip")) {
            String msg = ChatColor.translateAlternateColorCodes('&', config.getString("others.actionValue.ban-ip")
                    .replace("{player}", target.getName())
                    .replace("{uuid}", String.valueOf(target.getUniqueId()))
                    .replace("{amount}", String.valueOf(amount)));

            if (target.isOnline()) {
                target.getPlayer().kickPlayer(msg);
            }

            Bukkit.getBanList(BanList.Type.IP).addBan(target.getPlayer().getAddress().getHostName(), msg, null, null);
        }
    }
}
