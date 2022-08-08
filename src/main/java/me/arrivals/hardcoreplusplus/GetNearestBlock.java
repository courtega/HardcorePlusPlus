package me.arrivals.hardcoreplusplus;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GetNearestBlock {
    public static Location ofSafe(Location origin, int radius, int y_radius) {

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
