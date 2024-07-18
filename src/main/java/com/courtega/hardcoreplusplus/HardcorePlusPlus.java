package com.courtega.hardcoreplusplus;

import com.courtega.hardcoreplusplus.commands.RootCommandExecutor;
import com.courtega.hardcoreplusplus.commands.RootCommandTabCompleter;
import com.courtega.hardcoreplusplus.listeners.DragonDeathListener;
import com.courtega.hardcoreplusplus.listeners.PlayerDamageListener;
import com.courtega.hardcoreplusplus.listeners.PlayerDeathListener;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class HardcorePlusPlus extends JavaPlugin {
    private static volatile boolean isActive = false;

    public static boolean getActive() {
        return isActive;
    }

    public static void setActive(final boolean active) {
        isActive = active;
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        registerListeners(
                new PlayerDamageListener(this),
                new PlayerDeathListener(this),
                new DragonDeathListener(this)
        );

        final PluginCommand rootCommand = this.getCommand("hcpp");
        assert rootCommand != null;
        rootCommand.setExecutor(new RootCommandExecutor(this));
        rootCommand.setTabCompleter(new RootCommandTabCompleter());

        isActive = true;
        this.getLogger().info("HardcorePlusPlus is loaded and enabled.");
    }

    @Override
    public void onDisable() {
        isActive = false;
        // Plugin shutdown logic
    }

    private void registerListeners(final Listener... listeners) {
        for (final Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}
