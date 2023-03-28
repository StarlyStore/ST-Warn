package net.starly.warn.data;

import net.starly.core.data.Config;
import net.starly.warn.WarnMain;
import org.bukkit.OfflinePlayer;

import static net.starly.warn.util.ActionUtil.action;


public class PlayerWarnData {
    private final OfflinePlayer player;
    private final Config config;

    public PlayerWarnData(OfflinePlayer player) {
        this.player = player;
        this.config = new Config("warnData", WarnMain.getPlugin());
    }

    public void addWarn(int amount) {
        config.setInt(player.getUniqueId() + "", getWarn() + amount);

        action(player);
    }

    public void removeWarn(int amount) {
        config.setInt(player.getUniqueId() + "", getWarn() - amount);
    }

    public void setWarn(int amount) {
        config.setInt(player.getUniqueId() + "", amount);

        action(player);
    }

    public int getWarn() {
        return config.getInt(player.getUniqueId() + "");
    }
}
