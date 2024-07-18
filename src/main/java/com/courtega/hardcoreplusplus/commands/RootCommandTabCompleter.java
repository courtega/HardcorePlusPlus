package com.courtega.hardcoreplusplus.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RootCommandTabCompleter implements TabCompleter {
    private final String[] oneCompletions = {"setmax", "getmax", "enable", "disable"};

    @Override
    public List<String> onTabComplete(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String alias, final String[] args) {
        if (args.length == 1) {
            return List.of(oneCompletions);
        }
        return new ArrayList<>();
    }
}
