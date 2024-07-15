package com.courtega.hardcoreplusplus;

import com.courtega.hardcoreplusplus.schedulers.ActionBarScheduler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class Messenger {
    private final HardcorePlusPlus plugin;
    private final FileConfiguration config;
    private final LegacyComponentSerializer legacySerializer;

    private final String infoFormat = "&7&o";
    private final String gladFormat = "&a&o";
    private final String badFormat = "&c";

    public Messenger(HardcorePlusPlus plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.legacySerializer = LegacyComponentSerializer.legacyAmpersand();
    }

    public void sendDragonKill(Player player) {
        final TextComponent message = legacySerializer.deserialize(infoFormat + config.getString("DragonKillText", ""));
        player.sendMessage(message);
    }

    public void sendMaxHealthRestored(Player player) {
        final TextComponent message = legacySerializer.deserialize(gladFormat + config.getString("MaxHealthRestoreText", ""));
        player.sendMessage(message);
    }

    public Component getDeathBroadcast(Player player) {
        return legacySerializer.deserialize(badFormat + player.getName() + config.getString("PermaDeathServerText", ""));
    }

    public String getNoHealthCapacityBanReason() {
        return badFormat + config.getString("PermaBanText") + "&r";
    }

    public void sendTotemDeath(Player player) {
        final TextComponent message = legacySerializer.deserialize(gladFormat + config.getString("TotemDeathPlayerText", ""));
        player.sendMessage(message);
    }

    public void sendCustomDeath(Server server, Player victim, String messageId) {
        final String rawMessage = config.getString(messageId, "");
        final TextComponent message = legacySerializer.deserialize(rawMessage.replaceAll("%PLAYER%", String.valueOf(victim.displayName())));
        server.broadcast(message);
    }

    public void sendHealthSteal(Player player) {
        final TextComponent message = legacySerializer.deserialize(gladFormat + config.getString("HealthStealText", ""));
        player.sendMessage(message);
    }

    public void sendHealthStolen(Player player) {
        final TextComponent message = legacySerializer.deserialize(badFormat + config.getString("HealthLossText", ""));
        player.sendMessage(message);
    }

    public void sendRespawnTimer(Player player) {
        final BukkitScheduler bukkitScheduler = plugin.getServer().getScheduler();

        final Component[] frames = new Component[3];
        frames[0] = legacySerializer.deserialize("Reviving.");
        frames[1] = legacySerializer.deserialize("Reviving..");
        frames[2] = legacySerializer.deserialize("Reviving...");
        final ActionBarScheduler actionBarScheduler = new ActionBarScheduler(player, 20, frames);

        actionBarScheduler.runTaskTimerAsynchronously(plugin, 0, 1);
    }

    public void sendBanSpared(Player player) {
        final TextComponent message = legacySerializer.deserialize(gladFormat + "This server has ban-on-death enabled, but you were spared via permission assignment.");
        player.sendMessage(message);
    }

    public String getHardcoreBanReason() {
        return badFormat + config.getString("BanOnDeathText") + "&r";
    }
}
