package com.courtega.hardcoreplusplus.listeners;

import com.courtega.hardcoreplusplus.HardcorePlusPlus;
import com.courtega.hardcoreplusplus.Messenger;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class DragonDeathListener implements Listener {
    private final HardcorePlusPlus plugin;
    private final FileConfiguration config;
    private final Messenger messenger;

    public DragonDeathListener(final HardcorePlusPlus plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.messenger = new Messenger(plugin);
    }

    @EventHandler
    public void onEnderDragonDeath(final EntityDeathEvent event) {
        if (config.getBoolean("DragonKillRestoresMaxHealthEnabled")) {
            if (!(event.getEntity() instanceof EnderDragon)) {
                return;
            }

            for (final Player player : plugin.getServer().getOnlinePlayers()) {
                if (player.getWorld().getEnvironment().equals(World.Environment.THE_END)) {

                    messenger.sendDragonKill(player);

                    final AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                    assert maxHealth != null;
                    if (maxHealth.getBaseValue() <= 20.0d) {
                        maxHealth.setBaseValue(20.0d);
                    }

                    messenger.sendMaxHealthRestored(player);
                }
            }
        }
    }
}
