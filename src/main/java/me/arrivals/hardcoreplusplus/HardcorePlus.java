package me.arrivals.hardcoreplusplus;

import me.arrivals.hardcoreplusplus.config.ConfigManager;
import me.arrivals.hardcoreplusplus.listeners.DragonDeathListener;
import me.arrivals.hardcoreplusplus.listeners.PlayerDamageListener;
import me.arrivals.hardcoreplusplus.listeners.PlayerDeathListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/*


HardcorePlusRedux by arrivals

What changed?:
    - Replaced huge if-else chains with switch-case
    - Added new feature that allows players to steal each other's hearts
    - Replaced previous disable logic to a "soft-disable" system, allowing re-enabling
    - Cosmetic changes
    - Many other things

Why not ask to take over the original project?:
    The original developer has not updated this plugin in 3 years, and
    has not been seen on Spigot for 8 weeks. Also, the plugin is licensed
    under the GNU GENERAL PUBLIC LICENSE, allowing derivatives to be created.

As with the previous plugin, this remake also bears the GNU GENERAL PUBLIC LICENSE.


*/

public final class HardcorePlus extends JavaPlugin {

    private static HardcorePlus instance;

    public static HardcorePlus getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        init();
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Hardcore++ has been hard-disabled.");
    }

    private void init() {
        new ConfigManager(this).createConfig(); // load conf
        regEvents();
        Globals.pluginEnabled = true;

        // Register commands
        this.getCommand("hardcoreplusplus").setExecutor(new Commands(this));
        this.getCommand("hardcoreplusplus").setTabCompleter(new Commands(this));


        this.getLogger().info("Hardcore++ initialized.");
    }

    private void regEvents() {
        // HardcorePlus event listeners
        regEvent(new PlayerDamageListener(this));
        regEvent(new PlayerDeathListener(this));
        regEvent(new DragonDeathListener(this));
    }

    private void regEvent(Listener event) {
        // Event register function
        getServer().getPluginManager().registerEvents(event, this);
    }

}
