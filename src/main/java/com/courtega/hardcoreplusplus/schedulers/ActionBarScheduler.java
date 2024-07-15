package com.courtega.hardcoreplusplus.schedulers;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ActionBarScheduler extends BukkitRunnable {
    private final Player player;
    private final int frameDelay;
    private final Component[] frames;
    private int currentFrame;
    private int timeUnitsElapsed = 0;
    private boolean complete = false;

    public ActionBarScheduler(final Player player, final int frameDelay, final Component[] frames) {
        this.player = player;
        this.frameDelay = frameDelay;
        this.frames = frames;
    }

    @Override
    public void run() {
        timeUnitsElapsed++;

        if (complete) {
            this.cancel();
            return;
        }

        player.sendActionBar(frames[currentFrame]);

        if (timeUnitsElapsed >= frameDelay) {
            currentFrame++;
            timeUnitsElapsed = 0;
        }

        if (currentFrame >= frames.length) {
            timeUnitsElapsed = 0;
            currentFrame = 0;
            complete = true;
        }
    }
}
