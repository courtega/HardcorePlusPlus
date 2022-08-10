package me.arrivals.hardcoreplusplus;

import me.arrivals.hardcoreplusplus.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class Commands implements CommandExecutor, TabExecutor {
    private HardcorePlus plugin;

    public Commands(HardcorePlus plugin) {
        this.plugin = plugin;
    }

    public void commandErrorMsg(CommandSender sender, String label, String[] args) {
        if (!(args.length == 0)) {
            sender.sendMessage(ChatColor.RED + "Invalid or incomplete command, try /hcpp for help.");
        }
    }

    public boolean isValidUser(String givenUser) {

        if (Objects.equals(givenUser, "@a") || Objects.equals(givenUser, "@p") || Objects.equals(givenUser, "@r") || Objects.equals(givenUser, "@s")) {
            return true;
        } else {
            return getServer().getPlayer(givenUser) != null;
        }

    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Command: hardcoreplusplus
        if (command.getName().equalsIgnoreCase("hardcoreplusplus")) {

            // If no args passed for /hcpp
            if (args.length < 1) {
                sender.sendMessage(ChatColor.GOLD + "" + "Hardcore++ " + plugin.getDescription().getVersion() + " by arrivals");
                sender.sendMessage(ChatColor.GOLD + "Updated version of griimnak's HardcorePlus");
                sender.sendMessage("");
                sender.sendMessage(ChatColor.GRAY + "/hcpp" + ChatColor.WHITE + " - This dialog.");
                sender.sendMessage(ChatColor.GRAY + "/hcpp setmax <targets> [<hp>]" + ChatColor.WHITE + " - Sets the max health of a player.");
                sender.sendMessage(ChatColor.GRAY + "/hcpp disable" + ChatColor.WHITE + " - Disables the plugin.");
                sender.sendMessage(ChatColor.GRAY + "/hcpp enable" + ChatColor.WHITE + " - Enables the plugin.");
            } else {
                if (sender.hasPermission("hardcoreplus.admin")) {
                    switch (args[0].toLowerCase()) {

                        case "reload":
                            ConfigManager.reloadConfig();
                            sender.sendMessage(ChatColor.GREEN + "Configuration reloaded.");
                            break;

                        case "setmax":
                            // Wrong amount of args
                            if (args.length != 3) {
                                commandErrorMsg(sender, label, args);
                                return true;
                            }

                            // Allows for the usage of target selectors like @p
                            Entity[] givenUsers = CommandUtils.getTargets(sender, args[1]);

                            if (isValidUser(args[1])) {
                                // @s will always be the sender
                                if (Objects.equals(args[1], "@s") && sender instanceof Player) {
                                    givenUsers = new Entity[1];
                                    givenUsers[0] = (Entity) sender;
                                } else if ((Objects.equals(args[1], "@s") && !(sender instanceof Player)) || (Objects.equals(args[1], "@p") && !(sender instanceof Player))) {
                                    sender.sendMessage(ChatColor.RED + "Selector '" + args[1] + "' cannot be used by non-living senders.");
                                    return false;
                                }

                                double max_hp;

                                try {
                                    max_hp = Double.parseDouble(args[2]);
                                } catch (NumberFormatException e) {
                                    sender.sendMessage(ChatColor.RED + "Invalid max health value.");
                                    return true;
                                }

                                if (max_hp < 0.0D || max_hp > 2048.0D) {
                                    sender.sendMessage(ChatColor.RED + "Max health must be between 0 and 2048.");
                                }

                                // All good
                                else {
                                    for (Entity entity : givenUsers) {
                                        Player player = getServer().getPlayer(entity.getUniqueId());
                                        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(max_hp);
                                        player.setHealth(max_hp);
                                    }
                                    sender.sendMessage(ChatColor.GREEN + "Max health has been updated successfully.");
                                }

                            } else if (Objects.equals(args[1], "@e")) {
                                sender.sendMessage(ChatColor.RED + "Selector '@e' cannot be used (requires type Player).");
                            } else {
                                sender.sendMessage(ChatColor.RED + "User '" + args[1] + "' not found.");
                            }

                            break;
                        case "disable":

                            if (Globals.pluginEnabled) {
                                Globals.pluginEnabled = false;
                                sender.sendMessage(ChatColor.GREEN + "Disabled Hardcore++.");
                            } else {
                                sender.sendMessage("" + ChatColor.RED + "Plugin is already disabled.");
                            }

                            break;
                        case "enable":

                            if (!Globals.pluginEnabled) {
                                Globals.pluginEnabled = true;
                                sender.sendMessage(ChatColor.GREEN + "Enabled Hardcore++.");
                            } else {
                                sender.sendMessage("" + ChatColor.RED + "Plugin is already enabled.");
                            }

                            break;
                    }
                } else {
                    sender.sendMessage("" + ChatColor.DARK_RED + "You don't have the permission to do this.");
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();

        // without if statements it will keep suggesting the same thing past one arg
        // user types: /plugin blah, without this system repeated tabbing will result in /plugin blah blah blah blah...
        if (args.length == 1 && sender.hasPermission("hardcoreplus.admin")) {
            commands.add("reload");
            commands.add("setmax");
            commands.add("disable");
            commands.add("enable");
        } else if (args.length == 2 && sender.hasPermission("hardcoreplus.admin")) {
            if (Objects.equals(args[0], "setmax")) {
                List<Player> playersOnline = new ArrayList<>(Bukkit.getOnlinePlayers());
                List<String> allowedSelectors = Arrays.asList("@a", "@p", "@s", "@r");

                for (Player player : playersOnline) {
                    completions.add(player.getDisplayName());
                }
                completions.addAll(allowedSelectors);
            }
        } else if (args.length == 3 && sender.hasPermission("hardcoreplus.admin")) {
            if (Objects.equals(args[0], "setmax")) {
                completions.add("20");
            }
        }

        // copy matches of first argument from list (ex: if first arg is 'm' will return just 'minecraft')
        StringUtil.copyPartialMatches(args[0], commands, completions);
        // sort the list
        Collections.sort(completions);
        return completions;
    }
}
