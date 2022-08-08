package me.arrivals.hardcoreplusplus.listeners;

import me.arrivals.hardcoreplusplus.GetNearestBlock;
import me.arrivals.hardcoreplusplus.Globals;
import me.arrivals.hardcoreplusplus.HardcorePlus;
import me.arrivals.hardcoreplusplus.config.ConfigManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.BanList.Type;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import static org.bukkit.Bukkit.getLogger;

public class PlayerDamageListener implements Listener {
    private final HardcorePlus plugin;

    public PlayerDamageListener(HardcorePlus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamageUniversal(EntityDamageEvent event) {

        // Is plugin enabled?
        if (Globals.pluginEnabled) {


            if (!(event.getEntity() instanceof Player damaged)) {
                return;
            }

            if (damaged.isBlocking()) {
                return;
            }

            // Blood effect if enabled
            if (ConfigManager.config.getBoolean("BloodEffectEnabled")) {
                damaged.getWorld().playEffect(damaged.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
            }

            // if hp - dmg less or equal 0.0
            if ((damaged.getHealth() - event.getFinalDamage()) <= 0.0D) {
                /*
                    ==========
                    Totem fix [Dec 5 2019]
                    ==========
                    github.com/AleksanderEvensen
                */

                // Check if totems are enabled in the config
                if (ConfigManager.config.getBoolean("TotemOfUndyingWorks")) {
                    // Check if player is holding a Totem of Undying in Main- or Offhand
                    if (damaged.getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING) ||
                            damaged.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING)) {

                        // Play the totem effect.
                        // Removing the totem from the players hand is unnecessary because the spawnParticle will handle it.
                        damaged.spawnParticle(Particle.TOTEM, damaged.getLocation(), 1);
                        damaged.sendMessage(ChatColor.GREEN + "" + ChatColor.ITALIC + ConfigManager.config.getString("TotemDeathPlayerText"));
                        // return so the player won't be teleported to their bed or loose the amount of hearts specified.
                        return;
                    }
                }

                // max hp of damaged player
                double max_hp = damaged.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                if (max_hp - ConfigManager.config.getDouble("LoseMaxHealthOnRespawnAmmount") > 0.0D) {
                    // fake death
                    event.setCancelled(true);

                    // announce death if enabled
                    if (ConfigManager.config.getBoolean("AnnounceDeathEnabled")) {
                        EntityDamageEvent.DamageCause cause = (ConfigManager.config.getBoolean("CustomDeathMessagesEnabled")) ? event.getCause() : null;

                        switch (cause) {
                            case BLOCK_EXPLOSION ->
                                    broadcast(GetConfigString("dmsg_BlockExplosion").replaceAll("%PLAYER%", damaged.getDisplayName()));
                            case CONTACT ->
                                    broadcast(GetConfigString("dmsg_Contact").replaceAll("%PLAYER%", damaged.getDisplayName()));
                            case CRAMMING ->
                                    broadcast(GetConfigString("dmsg_Cramming").replaceAll("%PLAYER%", damaged.getDisplayName()));
                            case DRAGON_BREATH ->
                                    broadcast(GetConfigString("dmsg_DragonBreath").replaceAll("%PLAYER%", damaged.getDisplayName()));
                            case DROWNING ->
                                    broadcast(GetConfigString("dmsg_Drowning").replaceAll("%PLAYER%", damaged.getDisplayName()));
                            case ENTITY_ATTACK ->
                                    broadcast(GetConfigString("dmsg_EntityAttack").replaceAll("%PLAYER%", damaged.getDisplayName()));
                            case ENTITY_EXPLOSION ->
                                    broadcast(GetConfigString("dmsg_EntityExplosion").replaceAll("%PLAYER%", damaged.getDisplayName()));
                            case FALL ->
                                    broadcast(GetConfigString("dmsg_Fall").replaceAll("%PLAYER%", damaged.getDisplayName()));
                            case FALLING_BLOCK ->
                                    broadcast(GetConfigString("dmsg_FallingBlock").replaceAll("%PLAYER%", damaged.getDisplayName()));
                            case FIRE ->
                                    broadcast(GetConfigString("dmsg_Fire").replaceAll("%PLAYER%", damaged.getDisplayName()));
                            case FIRE_TICK ->
                                    broadcast(GetConfigString("dmsg_FireTick").replaceAll("%PLAYER%", damaged.getDisplayName()));
                            case FLY_INTO_WALL ->
                                    broadcast(GetConfigString("dmsg_FlyIntoWall").replaceAll("%PLAYER%", damaged.getDisplayName()));
                            case HOT_FLOOR ->
                                    broadcast(GetConfigString("dmsg_HotFloor").replaceAll("%PLAYER%", damaged.getDisplayName()));
                            case LAVA ->
                                    broadcast(GetConfigString("dmsg_Lava").replaceAll("%PLAYER%", damaged.getDisplayName()));
                            case LIGHTNING ->
                                    broadcast(GetConfigString("dmsg_Lightning").replaceAll("%PLAYER%", damaged.getDisplayName()));
                            case MAGIC ->
                                    broadcast(GetConfigString("dmsg_Magic").replaceAll("%PLAYER%", damaged.getDisplayName()));
                            case POISON ->
                                    broadcast(GetConfigString("dmsg_Poison").replaceAll("%PLAYER%", damaged.getDisplayName()));
                            case PROJECTILE ->
                                    broadcast(GetConfigString("dmsg_Projectile").replaceAll("%PLAYER%", damaged.getDisplayName()));
                            case STARVATION ->
                                    broadcast(GetConfigString("dmsg_Starvation").replaceAll("%PLAYER%", damaged.getDisplayName()));
                            case SUFFOCATION ->
                                    broadcast(GetConfigString("dmsg_Suffocation").replaceAll("%PLAYER%", damaged.getDisplayName()));
                            case SUICIDE ->
                                    broadcast(GetConfigString("dmsg_Suicide").replaceAll("%PLAYER%", damaged.getDisplayName()));
                            case THORNS ->
                                    broadcast(GetConfigString("dmsg_Thorns").replaceAll("%PLAYER%", damaged.getDisplayName()));
                            case VOID ->
                                    broadcast(GetConfigString("dmsg_Void").replaceAll("%PLAYER%", damaged.getDisplayName()));
                            case WITHER ->
                                    broadcast(GetConfigString("dmsg_Wither").replaceAll("%PLAYER%", damaged.getDisplayName()));

                            // equivalent to else
                            default ->
                                    broadcast(GetConfigString("AnnounceDeathText").replaceAll("%PLAYER%", damaged.getDisplayName()));
                        }
                    }

                    // drop loot
                    if (!ConfigManager.config.getBoolean("KeepInventoryEnabled")) {
                        // if item in off hand
                        if (damaged.getInventory().getItemInOffHand().getType() != Material.AIR) {
                            if (damaged.getInventory().getItemInOffHand().getEnchantments().containsKey(Enchantment.VANISHING_CURSE)) {
                                damaged.getInventory().getItemInOffHand().setAmount(0);
                            } else {
                                // drop
                                damaged.getWorld().dropItemNaturally(damaged.getLocation(), damaged.getInventory().getItemInOffHand());
                                damaged.getInventory().setItemInOffHand(null);
                            }
                        }

                        // helm
                        if (damaged.getInventory().getHelmet() != null) {
                            if (damaged.getInventory().getHelmet().getEnchantments().containsKey(Enchantment.VANISHING_CURSE)) {
                                damaged.getInventory().getHelmet().setAmount(0);
                            } else {
                                damaged.getWorld().dropItemNaturally(damaged.getLocation(), damaged.getInventory().getHelmet());
                                damaged.getInventory().setHelmet(null);
                            }
                        }

                        // chest
                        if (damaged.getInventory().getChestplate() != null) {
                            if (damaged.getInventory().getChestplate().getEnchantments().containsKey(Enchantment.VANISHING_CURSE)) {
                                damaged.getInventory().getChestplate().setAmount(0);
                            } else {
                                damaged.getWorld().dropItemNaturally(damaged.getLocation(), damaged.getInventory().getChestplate());
                                damaged.getInventory().setChestplate(null);
                            }
                        }

                        // legs
                        if (damaged.getInventory().getLeggings() != null) {
                            if (damaged.getInventory().getLeggings().getEnchantments().containsKey(Enchantment.VANISHING_CURSE)) {
                                damaged.getInventory().getLeggings().setAmount(0);
                            } else {
                                damaged.getWorld().dropItemNaturally(damaged.getLocation(), damaged.getInventory().getLeggings());
                                damaged.getInventory().setLeggings(null);
                            }
                        }

                        // boots
                        if (damaged.getInventory().getBoots() != null) {
                            if (damaged.getInventory().getBoots().getEnchantments().containsKey(Enchantment.VANISHING_CURSE)) {
                                damaged.getInventory().getBoots().setAmount(0);
                            } else {
                                damaged.getWorld().dropItemNaturally(damaged.getLocation(), damaged.getInventory().getBoots());
                                damaged.getInventory().setBoots(null);
                            }
                        }

                        // for each inv item
                        for (ItemStack i : damaged.getInventory().getContents()) {
                            // drop item
                            if (i != null) {
                                if (i.getEnchantments().containsKey(Enchantment.VANISHING_CURSE)) {
                                    i.setAmount(0);
                                    continue;
                                } else {
                                    damaged.getWorld().dropItemNaturally(damaged.getLocation(), i);
                                    damaged.getInventory().remove(i);
                                }


                            }
                        }
                    }

                    // drop xp
                    if (!ConfigManager.config.getBoolean("KeepExperienceEnabled")) {
                        int total_xp = damaged.getLevel();
                        if (total_xp > 0) {
                            // drop xp
                            damaged.getWorld().spawn(damaged.getLocation(), ExperienceOrb.class).setExperience(total_xp);
                        }
                        // reset
                        damaged.setLevel(0);
                        damaged.setExp(0);
                    }

                    // clear effects
                    damaged.getActivePotionEffects().clear();
                    damaged.setFireTicks(0);

                    // degrade max hp if enabled
                    if (ConfigManager.config.getBoolean("LoseMaxHealthOnRespawnEnabled")) {


                        // give damager hearts
                        if (event instanceof EntityDamageByEntityEvent) {
                            if (ConfigManager.config.getBoolean("StealMaxHealthOnMurder") && ((EntityDamageByEntityEvent) event).getDamager() instanceof Player damager) {
                                double dmgr_max_hp = damager.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                                double new_health = dmgr_max_hp + ConfigManager.config.getDouble("LoseMaxHealthOnRespawnAmmount");
                                plugin.getLogger().info(String.valueOf(new_health));
                                damager.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(new_health);
                                damager.sendMessage(ChatColor.GREEN + "" + ConfigManager.config.getString("HealthStealText"));
                            }
                        }

                        // take damaged hearts
                        damaged.setHealth(max_hp - ConfigManager.config.getDouble("LoseMaxHealthOnRespawnAmmount"));
                        damaged.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(max_hp - ConfigManager.config.getDouble("LoseMaxHealthOnRespawnAmmount"));
                        damaged.sendMessage(ChatColor.RED + "" + ConfigManager.config.getString("HealthLossText"));
                        plugin.getLogger().info(damaged.getDisplayName() + " has lost permanent health.");

                    } else {
                        damaged.setHealth(max_hp);
                    }


                    // re saturate
                    damaged.setSaturation(5);
                    damaged.setFoodLevel(20);

                    // respawn
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        public void run() {
                            if (damaged.getBedSpawnLocation() != null) {
                                damaged.teleport(damaged.getBedSpawnLocation());
                                // damaged.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + ConfigManager.config.getString("RespawnBedText"));
                            } else {

                                // Randomize world spawn to prevent mobs from camping you
                                Location sl1 = damaged.getServer().getWorlds().get(0).getSpawnLocation();
                                int randomNumber = ThreadLocalRandom.current().nextInt(10, 50 + 1);
                                Location sl2 = sl1.add(randomNumber, 0, randomNumber);

                                // Spawn finding logic. Sea level is around 63, so here I will use 63+y_radius
                                int y_radius = 15;

                                Location spawnLocation = new Location(sl2.getWorld(), sl2.getX(), 63 + y_radius, sl2.getZ());
                                Location safe_spawn = GetNearestBlock.ofSafe(spawnLocation, 50, y_radius);


                                damaged.teleport(safe_spawn);
                                // damaged.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + ConfigManager.config.getString("RespawnWildText"));
                            }

                            // Give invincibility for a short period of time to prevent spawn camping
                            damaged.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60, 1));
                            damaged.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 60, 1));

                            new Globals.Countdown(3, plugin) {
                                String load_anim = "";

                                @Override
                                public void count(int current) {
                                    damaged.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "Reviving" + load_anim));
                                    if (current == 3) {
                                        load_anim = ".";
                                    } else if (current == 2) {
                                        load_anim = "..";
                                    } else if (current == 1) {
                                        load_anim = "...";
                                    }
                                }

                            }.start();

                            // de-aggro mobs around player. if player fake respawns near a mob that is targeting them,
                            // it will spawn camp and kill instantly without this code

                            // Edit: as of testing this function does not work at all. No indication as to how to achieve without NMS

                            Location location = damaged.getLocation();
                            Collection<Entity> nearbyEntities = location.getWorld().getNearbyEntities(location, 7, 7, 7);
                            for (Entity e : nearbyEntities) {
                                if (e instanceof Mob) {

                                    LivingEntity target = ((Mob) e).getTarget();
                                    if (target instanceof Player && (target == damaged)) {
                                        getLogger().info(((Mob) e).getTarget().toString());


                                        if (e instanceof Wolf) {
                                            ((Wolf) e).setAngry(false);
                                            getLogger().info(String.valueOf(((Wolf) e).isAngry()));
                                        } else if (e instanceof PigZombie) {
                                            ((PigZombie) e).setAngry(false);
                                            getLogger().info(String.valueOf(((PigZombie) e).isAngry()));
                                        }

                                        ((Mob) e).setTarget(null);

                                    }
                                }
                            }
                        }
                    }, 5);

                    // trippy effects if enabled
                    if (ConfigManager.config.getBoolean("RespawnSoundEnabled")) {
                        damaged.getWorld().playSound(damaged, Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 2, 1);
                    }
                    if (ConfigManager.config.getBoolean("RespawnEffectEnabled")) {
                        damaged.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60, 1));
                        damaged.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0));
                    }

                    // ban effect if enabled
                    if (ConfigManager.config.getBoolean("BanOnDeathEnabled")) {
                        if (damaged.hasPermission("hardcoreplus.ban_exempt")) {
                            // exempt permission
                            damaged.sendMessage("This server has death ban enabled, but you are exempt via permission.");
                            plugin.getLogger().info(damaged.getName() + " is has been pardoned from death ban.");
                        } else {
                            // if this isn't delayed it gives a weird "user took too long to log in" bug.
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                public void run() {
                                    if (damaged.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() > 0) {
                                        plugin.getServer().getBanList(Type.NAME).addBan(damaged.getName(), ChatColor.RED + ConfigManager.config.getString("BanOnDeathText") + ChatColor.RESET, getBanDate(ConfigManager.config.getInt("BanOnDeathHoursAmmount")), "HardcorePlus");
                                        damaged.kickPlayer(ChatColor.RED + ConfigManager.config.getString("BanOnDeathText") + ChatColor.RESET);
                                    }
                                }
                            }, 20);
                        }
                    }
                }
            }
        }
    }


    private Date getBanDate(int h) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.HOUR_OF_DAY, h);
        return c.getTime();
    }

    // Sends a message to the whole server
    private void broadcast(String msg) {
        plugin.getServer().broadcastMessage(msg);
    }

    private String GetConfigString(String configEntry) {
        return ConfigManager.config.getString(configEntry);
    }
}
