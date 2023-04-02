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
        if (!isPluginEnabled("net.starly.core.StarlyCore")) {
            getServer().getLogger().warning("[" + getName() + "] ST-Core 플러그인이 적용되지 않았습니다! 플러그인을 비활성화합니다.");
            getServer().getLogger().warning("[" + getName() + "] 다운로드 링크 : §fhttp://starly.kr/");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        plugin = this;
        new Metrics(this, 17657);

        // CONFIG
        config = new Config("config", plugin);
        config.loadDefaultConfig();
        config.setPrefix("messages.prefix");

        // COMMAND
        getServer().getPluginCommand("warning").setExecutor(new WarnCmd());
        getServer().getPluginCommand("warning").setTabCompleter(new WarnTab());
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    private boolean isPluginEnabled(String path) {
        try {
            Class.forName(path);
            return true;
        } catch (NoClassDefFoundError ignored) {
        } catch (Exception ex) { ex.printStackTrace(); }
        return false;
    }

}