package com.courtega.hardcoreplusplus.listeners;

import com.courtega.hardcoreplusplus.HardcorePlusPlus;
import com.courtega.hardcoreplusplus.Messenger;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Date;

public class PlayerDeathListener implements Listener {
    private final HardcorePlusPlus plugin;
    private final Messenger messenger;
    private final FileConfiguration config;
    private final BukkitScheduler scheduler;

    public PlayerDeathListener(final HardcorePlusPlus plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.messenger = new Messenger(plugin);
        this.scheduler = plugin.getServer().getScheduler();
    }

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {
        if (!HardcorePlusPlus.getActive()) {
            return;
        }

        final Player player = event.getPlayer();

        final AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        assert maxHealth != null;
        maxHealth.setBaseValue(20.0D);

        if (player.hasPermission("hardcoreplusplus.ban_exempt")) {
            messenger.sendBanSpared(player);
            return;
        }

        event.deathMessage(messenger.getDeathBroadcast(player));

        //TODO: updateSTatsFile();
        if (config.getBoolean("perma_ban_on_final_death")) {
            // 10-tick delay
            scheduler.runTaskLater(plugin, () -> {
                player.ban(messenger.getNoHealthCapacityBanReason(), (Date) null, "HardcorePlusPlus");
            }, 10L);
        }

    }
}
