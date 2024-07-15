package com.courtega.hardcoreplusplus.listeners;

import com.courtega.hardcoreplusplus.HardcorePlusPlus;
import com.courtega.hardcoreplusplus.Messenger;
import io.papermc.paper.ban.BanListType;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerDamageListener implements Listener {
    private final HardcorePlusPlus plugin;
    private final FileConfiguration config;
    private final Messenger messenger;
    private final Server server;
    private final BukkitScheduler scheduler;

    public PlayerDamageListener(final HardcorePlusPlus plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.messenger = new Messenger(plugin);
        this.server = plugin.getServer();
        this.scheduler = server.getScheduler();
    }

    @EventHandler
    public void onPlayerDamage(final EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }
        if (victim.isBlocking()) {
            return;
        }

        final World victimWorld = victim.getWorld();
        final Location victimLocation = victim.getLocation();
        final Inventory victimInventory = victim.getInventory();

        // Add blood effect, if it's enabled
        if (config.getBoolean("BloodEffectEnabled")) {
            victimWorld.playEffect(victimLocation, Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
        }

        // Only proceed if damage takes the victim to or below 0 hearts
        if ((victim.getHealth() - event.getFinalDamage()) > 0.0d) {
            return;
        }

        // A "totem fix" of some sort? (Dec 5, 2019 - github.com/AleksanderEvensen)
        final Material mainHandItemType = victim.getInventory().getItemInMainHand().getType();
        final Material offHandItemType = victim.getInventory().getItemInOffHand().getType();
        if (config.getBoolean("TotemOfUndyingWorks")) {
            if (mainHandItemType == Material.TOTEM_OF_UNDYING || offHandItemType == Material.TOTEM_OF_UNDYING) {
                victim.spawnParticle(Particle.TOTEM_OF_UNDYING, victimLocation, 1);
                messenger.sendTotemDeath(victim);
                return;
            }
        }

        final double maxHealth = victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        if (maxHealth - config.getDouble("LoseMaxHealthOnRespawnAmmount") <= 0.0d) {
            return;
        }

        event.setCancelled(true);

        // Custom death announcement
        if (config.getBoolean("AnnounceDeathEnabled")) {
            final EntityDamageEvent.DamageCause damageCause = (config.getBoolean("CustomDeathMessagesEnabled")) ? event.getCause() : EntityDamageEvent.DamageCause.VOID;
            messenger.sendCustomDeath(server, victim, damageCause.toString());
        }

        // Drop loot and XP
        final boolean keepInventory = victimWorld.getGameRuleValue(GameRule.KEEP_INVENTORY);
        if (!keepInventory) {
            for (final ItemStack itemStack : victimInventory.getContents()) {
                if (itemStack == null) continue;
                victimWorld.dropItemNaturally(victimLocation, itemStack);
            }
            victimInventory.clear();

            final int totalXp = victim.getLevel();
            if (totalXp > 0) {
                victimWorld.spawn(victimLocation, ExperienceOrb.class).setExperience(totalXp);
            }

            victim.setLevel(0);
            victim.setExp(0);
        }

        // Clear effects
        victim.getActivePotionEffects().clear();
        victim.setFireTicks(0);

        // Decrease max HP if enabled
        if (config.getBoolean("LoseMaxHealthOnRespawnEnabled")) {
            if (event instanceof EntityDamageByEntityEvent edbeEvent && edbeEvent.getDamager() instanceof Player attacker) {
                final double attackerMaxHealth = attacker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                final double newAttackerMaxHealth = attackerMaxHealth + config.getDouble("LoseMaxHealthOnRespawnAmmount");

                attacker.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newAttackerMaxHealth);
                messenger.sendHealthSteal(attacker);
            }

            // Take heart away
            final double effectiveHealth = maxHealth - config.getDouble("LoseMaxHealthOnRespawnAmmount");
            victim.setHealth(effectiveHealth);
            victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(effectiveHealth);

            messenger.sendHealthStolen(victim);
        } else {
            victim.setHealth(maxHealth);
        }

        // Re-saturate
        victim.setSaturation(5);
        victim.setFoodLevel(20);

        // Respawning
        scheduler.runTaskLater(plugin, () -> respawn(victim), 5L);

        // Trippy effects
        if (config.getBoolean("RespawnSoundEnabled")) {
            victimWorld.playSound(victim, Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 2.0f, 1.0f);
            victim.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 60, 1));
            victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0));
        }

        // Banning on death (basically, replicating hardcore mode)
        if (!config.getBoolean("BanOnDeathEnabled")) {
            return;
        }

        if (victim.hasPermission("hardcoreplusplus.ban_exempt")) {
            messenger.sendBanSpared(victim);
        } else {
            // If this isn't delayed, it gives a weird "user took too long to log in" error
            scheduler.runTaskLater(plugin, () -> {
                final int deathBanDuration = config.getInt("BanOnDeathHoursAmmount");
                server.getBanList(BanListType.PROFILE).addBan(victim.getPlayerProfile(), messenger.getHardcoreBanReason(), getBanDate(deathBanDuration), "HardcorePlusPlus");
            }, 20L);
        }
    }

    private Date getBanDate(final int hours) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    private void respawn(final Player victim) {
        final Location victimSpawnLocation = victim.getRespawnLocation();
        if (victimSpawnLocation != null) {
            victim.teleport(victimSpawnLocation);
        } else {
            // Randomizing world spawn to prevent spawn camping
            final Location spawnLocation = victim.getServer().getWorlds().getFirst().getSpawnLocation();
            final int randomNumber = ThreadLocalRandom.current().nextInt(10, 50 + 1);
            final Location randomizedSpawnLocation = spawnLocation.add(randomNumber, 0, randomNumber);

            // Spawn-finding logic; sea level is around 63, so 15 seems like an appropriate radius. Or at least it seemed like one two years ago.
            final Location safeSpawnLocation = ofSafe(spawnLocation, 50, 15);

            victim.teleport(Objects.requireNonNullElse(safeSpawnLocation, spawnLocation));
        }

        // Grant invincibility for a short period of time
        victim.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60, 1));
        victim.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 60, 127));

        messenger.sendRespawnTimer(victim);

        // De-aggro mobs around player
        final Location location = victim.getLocation();
        final Collection<Entity> nearbyEntities = location.getWorld().getNearbyEntities(location, 7, 7, 7);

        for (final Entity entity : nearbyEntities) {
            if (entity instanceof Mob) {
                final LivingEntity target = ((Mob) entity).getTarget();
                if (!(target instanceof Player) || (target != victim)) {
                    return;
                }

                // Neutral mobs with an anger property that can be changed
                switch (entity.getType()) {
                    case BEE:
                        ((Bee) entity).setAnger(0);
                    case WOLF:
                        ((Wolf) entity).setAngry(false);
                    case ZOMBIFIED_PIGLIN:
                        ((PigZombie) entity).setAngry(false);
                }

                // Otherwise, remove victim from target
                ((Mob) entity).setTarget(null);
            }
        }
    }

    private Location ofSafe(Location origin, int radius, int y_radius) {

        // List of blocks we deem "safe" or most likely to be on the surface
        Set<Material> safeBlocks = new HashSet<>(Arrays.asList(Material.GRASS_BLOCK, Material.SAND, Material.SNOW, Material.SNOW_BLOCK,
                Material.PODZOL, Material.COARSE_DIRT));

        World world = origin.getWorld();

        // How it works:
        // for each co-ordinate, it increases in low-bound until something is found

        for (int x = -radius; x < radius; x++) {
            for (int y = -y_radius; y < y_radius; y++) {
                for (int z = -radius; z < radius; z++) {
                    Block block = world.getBlockAt(origin.getBlockX() + x, origin.getBlockY() + y, origin.getBlockZ() + z);
                    if (safeBlocks.contains(block.getType())) {
                        if (world.getBlockAt(origin.getBlockX() + x, origin.getBlockY() + y + 2, origin.getBlockZ() + z).getType() == Material.AIR) {
                            double good_x = (origin.getBlockX() + x);
                            double good_y = (origin.getBlockY() + y + 2);
                            double good_z = (origin.getBlockZ() + z);
                            return new Location(world, good_x, good_y, good_z);
                        }
                    }
                }
            }
        }
        return null;
    }
}
