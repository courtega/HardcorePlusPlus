package com.courtega.hardcoreplusplus.commands;

import com.courtega.hardcoreplusplus.HardcorePlusPlus;
import com.courtega.hardcoreplusplus.Messenger;
import com.courtega.hardcoreplusplus.dependencies.CommandUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class RootCommandExecutor implements CommandExecutor {
    private final HardcorePlusPlus plugin;
    private final Messenger messenger;

    public RootCommandExecutor(final HardcorePlusPlus plugin) {
        this.plugin = plugin;
        this.messenger = new Messenger(plugin);
    }

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (!(sender instanceof final Player playerSender)) {
            sender.sendMessage(Component.text("Only players can execute this command!"));
            return false;
        }

        if (args.length == 0) {
            messenger.sendHelp(playerSender);
            return true;
        } else {
            switch (args[0]) {
                case "setmax" -> {
                    if (playerSender.hasPermission("hardcoreplusplus.admin")) {
                        setMaxHealth(playerSender, args);
                    } else {
                        messenger.sendNoPerms(playerSender);
                    }

                    return true;
                }
                case "getmax" -> {
                }
                case "enable" -> {
                    if (!playerSender.hasPermission("hardcoreplusplus.admin")) {
                        messenger.sendNoPerms(playerSender);
                        return true;
                    }

                    if (HardcorePlusPlus.getActive()) {
                        messenger.sendEnableFail(playerSender);
                    } else {
                        HardcorePlusPlus.setActive(true);
                        messenger.sendEnableSuccess(playerSender);
                    }

                    return true;
                }
                case "disable" -> {
                    if (!playerSender.hasPermission("hardcoreplusplus.admin")) {
                        messenger.sendNoPerms(playerSender);
                        return true;
                    }

                    if (HardcorePlusPlus.getActive()) {
                        HardcorePlusPlus.setActive(false);
                        messenger.sendDisableSuccess(playerSender);
                    } else {
                        messenger.sendDisableFail(playerSender);
                    }

                    return true;
                }
            }
        }

        return false;
    }

    private boolean setMaxHealth(final Player playerSender, @NotNull final String @NotNull [] args) {
        if (args.length != 3) {
            messenger.sendArgLengthError(playerSender, "setmax", args.length - 1, 2);
            return false;
        }

        // Selector logic
        final Entity[] givenUsers = CommandUtils.getTargets(playerSender, args[1]);

        if (!isValidUser(args[1])) {
            messenger.sendSetMaxPlayerNotFound(playerSender);
            return false;
        }

        final double maxHealth;
        try {
            maxHealth = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            messenger.sendSetMaxInvalidHP(playerSender);
            return false;
        }

        if (maxHealth <= 0.0d || maxHealth > 2048.0d) {
            messenger.sendSetMaxOutOfBounds(playerSender);
            return false;
        }

        for (final Entity entity : givenUsers) {
            final Player player = plugin.getServer().getPlayer(entity.getUniqueId());
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
            player.setHealth(maxHealth);
        }

        messenger.sendSetMaxSuccess(playerSender);
        return true;
    }

    public boolean isValidUser(final String givenUser) {
        final String[] selectors = {"@a", "@p", "@r", "s"};
        if (Arrays.stream(selectors).anyMatch(givenUser::equalsIgnoreCase)) {
            return true;
        } else {
            return plugin.getServer().getPlayer(givenUser) != null;
        }
    }
}
