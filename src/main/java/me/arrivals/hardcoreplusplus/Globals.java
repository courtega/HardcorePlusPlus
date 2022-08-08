package me.arrivals.hardcoreplusplus;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Globals {
    public static boolean pluginEnabled = true;


    public abstract static class Countdown {

        protected final Plugin plugin;
        protected BukkitTask task;
        private int time;


        public Countdown(int time, Plugin plugin) {
            this.time = time;
            this.plugin = plugin;
        }


        public abstract void count(int current);


        public final void start() {
            task = new BukkitRunnable() {

                @Override
                public void run() {
                    count(time);
                    if (time-- <= 0) cancel();
                }

            }.runTaskTimer(plugin, 0L, 20L);
        }

    }

}

