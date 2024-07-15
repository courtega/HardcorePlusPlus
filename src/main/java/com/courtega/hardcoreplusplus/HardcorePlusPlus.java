package com.courtega.hardcoreplusplus;

import com.courtega.hardcoreplusplus.listeners.DragonDeathListener;
import com.courtega.hardcoreplusplus.listeners.PlayerDamageListener;
import com.courtega.hardcoreplusplus.listeners.PlayerDeathListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class HardcorePlusPlus extends JavaPlugin {
    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        registerListeners(
                new PlayerDamageListener(this),
                new PlayerDeathListener(this),
                new DragonDeathListener(this)
        );

        this.getLogger().info("HardcorePlusPlus is enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}
