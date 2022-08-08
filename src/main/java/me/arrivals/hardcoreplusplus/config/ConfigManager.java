package me.arrivals.hardcoreplusplus.config;

import me.arrivals.hardcoreplusplus.HardcorePlus;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {
    public static FileConfiguration config;
    private static File cfile;
    private HardcorePlus plugin;

    public ConfigManager(HardcorePlus plugin) {
        this.plugin = plugin;
    }

    public static void reloadConfig() {
        // re assign config to current cfile
        config = YamlConfiguration.loadConfiguration(cfile);
    }

    public void createConfig() {
        config = plugin.getConfig();
        // Copy defaults if not present
        config.options().copyDefaults(true);
        plugin.saveDefaultConfig();

        cfile = new File(plugin.getDataFolder(), "config.yml");
        plugin.getLogger().info("Config loaded.");
    }
}
