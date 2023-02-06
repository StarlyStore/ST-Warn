package net.starly.warn.data;

import net.starly.core.data.Config;
import net.starly.warn.WarnMain;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class WarnData {
    private final Config config = new Config("warnData", WarnMain.getPlugin()); // warnData.yml

    /**
     * 플레이어에게 경고를 지급합니다.
     * @param target 경고를 지급할 플레이어
     * @param amount 지급할 경고의 양
     */
    public void give(Player target, int amount) {
        config.setInt(String.valueOf(target.getUniqueId()), get(target) + amount);
        action(target);
    }

    /**
     * 플레이어의 경고를 차감합니다.
     * @param target 경고를 차감할 플레이어
     * @param amount 차감할 경고의 양
     */
    public void take(Player target, int amount) {
        config.setInt(String.valueOf(target.getUniqueId()), get(target) - amount);
    }

    /**
     * 플레이어의 경고를 설정합니다.
     * @param target 경고를 설정할 플레이어
     * @param amount 설정할 경고의 양
     */
    public void set(Player target, int amount) {
        config.setInt(String.valueOf(target.getUniqueId()), amount);
        action(target);
    }

    /**
     * 플레이어의 경고를 warnData.yml 에서 불러와 리턴합니다.
     * @param target 경고를 불러올 플레이어
     * @return 경고의 양
     */
    public int get(Player target) {
        return config.getInt(String.valueOf(target.getUniqueId()));
    }
    
    public void action(Player target) {
        int amount = get(target);
        String action = WarnMain.config.getString("others.actions." + amount);
        if (action == null) return;

        if (action.equalsIgnoreCase("message")) target.sendMessage(WarnMain.config.getMessage("others.actions_value.message"));

        else if (action.equalsIgnoreCase("kick")) target.kickPlayer(WarnMain.config.getMessage("others.actions_value.kick"));

        else if (action.equalsIgnoreCase("ban")) {
            target.kickPlayer(WarnMain.config.getMessage("others.actions_value.ban"));
            Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(), WarnMain.config.getMessage("others.actions_value.ban"), null, null);
        } else if (action.equalsIgnoreCase("ban-ip")) {
            target.kickPlayer(WarnMain.config.getMessage("others.actions_value.ban-ip"));
            Bukkit.getBanList(BanList.Type.IP).addBan(target.getAddress().getHostName(), WarnMain.config.getMessage("others.actions_value.ban-ip"), null, null);
        }

    }
}
