package com.courtega.hardcoreplusplus;

import com.courtega.hardcoreplusplus.schedulers.ActionBarScheduler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

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
        final TextComponent message = legacySerializer.deserialize(infoFormat + config.getString("msg_dragon_kill", ""));
        player.sendMessage(message);
    }

    public void sendMaxHealthRestored(Player player) {
        final TextComponent message = legacySerializer.deserialize(gladFormat + config.getString("msg_max_health_restore", ""));
        player.sendMessage(message);
    }

    public Component getDeathBroadcast(Player player) {
        return legacySerializer.deserialize(badFormat + player.getName() + config.getString("msg_perma_death", ""));
    }

    public String getNoHealthCapacityBanReason() {
        return config.getString("perma_ban_reason");
    }

    public void sendTotemDeath(Player player) {
        final TextComponent message = legacySerializer.deserialize(gladFormat + config.getString("msg_totem_clutch", ""));
        player.sendMessage(message);
    }

    public void sendCustomDeath(Server server, Player victim, String messageId) {
        final String rawMessage = config.getString(messageId, "");
        final String playerName = victim.getName();
        final TextComponent message = legacySerializer.deserialize(rawMessage.replaceAll("%PLAYER%", playerName));
        server.broadcast(message);
    }

    public void sendHealthSteal(Player player) {
        final TextComponent message = legacySerializer.deserialize(gladFormat + config.getString("msg_health_steal", ""));
        player.sendMessage(message);
    }

    public void sendHealthStolen(Player player) {
        final TextComponent message = legacySerializer.deserialize(badFormat + config.getString("msg_health_loss", ""));
        player.sendMessage(message);
    }

    public void sendRespawnTimer(Player player) {
        final String reviveStatusString = config.getString("msg_revive_status", "");
        final Component[] frames = new Component[3];
        frames[0] = legacySerializer.deserialize(reviveStatusString + ".");
        frames[1] = legacySerializer.deserialize(reviveStatusString + "..");
        frames[2] = legacySerializer.deserialize(reviveStatusString + "...");
        final ActionBarScheduler actionBarScheduler = new ActionBarScheduler(player, 20, frames);

        actionBarScheduler.runTaskTimerAsynchronously(plugin, 0, 1);
    }

    public void sendBanSpared(Player player) {
        final TextComponent message = legacySerializer.deserialize(gladFormat + config.getString("msg_ban_spared", ""));
        player.sendMessage(message);
    }

    public String getHardcoreBanReason() {
        return config.getString("msg_ban_on_death");
    }

    public void sendHelp(final Player player) {
        final Component component = legacySerializer.deserialize("""
                &6Hardcore++ by courtega&r
                &6Continuation of griimnak's HardcorePlus&r
                                    
                /hcpp - This dialog
                /hcpp setmax <targets> [<hp>] - Sets the max health of a player
                """);
        player.sendMessage(component);
    }

    public void sendSetMaxPlayerNotFound(final Player player) {
        final TextComponent message = legacySerializer.deserialize(badFormat + config.getString("setmax_player_not_found", ""));
        player.sendMessage(message);
    }

    public void sendSetMaxInvalidHP(final Player player) {
        final TextComponent message = legacySerializer.deserialize(badFormat + config.getString("setmax_health_not_valid", ""));
        player.sendMessage(message);
    }

    public void sendSetMaxOutOfBounds(final Player player) {
        final TextComponent message = legacySerializer.deserialize(badFormat + config.getString("setmax_out_of_bounds", ""));
        player.sendMessage(message);
    }

    public void sendSetMaxSuccess(final Player player) {
        final TextComponent message = legacySerializer.deserialize(gladFormat + config.getString("setmax_success", ""));
        player.sendMessage(message);
    }

    public void sendEnableSuccess(final Player player) {
        final TextComponent message = legacySerializer.deserialize(gladFormat + config.getString("enable_success", ""));
        player.sendMessage(message);
    }

    public void sendDisableSuccess(final Player player) {
        final TextComponent message = legacySerializer.deserialize(gladFormat + config.getString("disable_success", ""));
        player.sendMessage(message);
    }

    public void sendEnableFail(final Player player) {
        final TextComponent message = legacySerializer.deserialize(badFormat + config.getString("enable_fail", ""));
        player.sendMessage(message);
    }

    public void sendDisableFail(final Player player) {
        final TextComponent message = legacySerializer.deserialize(badFormat + config.getString("disable_fail", ""));
        player.sendMessage(message);
    }

    public void sendNoPerms(final Player player) {
        final TextComponent message = legacySerializer.deserialize(badFormat + config.getString("msg_no_perms", ""));
        player.sendMessage(message);
    }

    public void sendArgLengthError(final Player player, final String subCommand, final int argSize, final int argSizeExpected) {
        final String rawMessage = config.getString("msg_arg_length_error", "");
        final TextComponent message = legacySerializer.deserialize(badFormat + String.format(rawMessage, argSize, subCommand, argSizeExpected));
        player.sendMessage(message);
    }
}
