package net.starly.warn;

import net.starly.core.bstats.Metrics;
import net.starly.core.data.Config;
import net.starly.warn.command.WarnCmd;
import net.starly.warn.command.tabcomplete.WarnTab;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class WarnMain extends JavaPlugin {
    private static JavaPlugin plugin;
    public static Config config;

    @Override
    public void onEnable() {
        // DEPENDENCY
        if (Bukkit.getPluginManager().getPlugin("ST-Core") == null) {
            Bukkit.getLogger().warning("[" + getName() + "] ST-Core 플러그인이 적용되지 않았습니다! 플러그인을 비활성화합니다.");
            Bukkit.getLogger().warning("[" + getName() + "] 다운로드 링크 : §fhttp://starly.kr/discord");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        plugin = this;
        new Metrics(this, 17657);

        // CONFIG
        config = new Config("config", plugin);
        config.loadDefaultConfig();
        config.setPrefix("messages.prefix");

        // COMMAND
        Bukkit.getPluginCommand("warn").setExecutor(new WarnCmd());
        Bukkit.getPluginCommand("warn").setTabCompleter(new WarnTab());
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

}