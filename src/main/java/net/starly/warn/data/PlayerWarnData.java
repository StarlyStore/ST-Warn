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

    /**
     * 플레이어에게 경고를 추가합니다.
     *
     * @param amount 추가할 경고
     */
    public void addWarn(int amount) {
        config.setInt(player.getUniqueId() + "", getWarn() + amount);

        action(player);
    }

    /**
     * 플레이어의 경고를 차감합니다.
     *
     * @param amount 차감할 경고
     */
    public void removeWarn(int amount) {
        config.setInt(player.getUniqueId() + "", getWarn() - amount);
    }

    /**
     * 플레이어의 경고를 설정합니다.
     *
     * @param amount 설정할 누적 경고
     */
    public void setWarn(int amount) {
        config.setInt(player.getUniqueId() + "", amount);

        action(player);
    }

    /**
     * 플레이어의 경고를 불러와 반환합니다.
     *
     * @return 누적 경고
     */
    public int getWarn() {
        return config.getInt(player.getUniqueId() + "");
    }
}
