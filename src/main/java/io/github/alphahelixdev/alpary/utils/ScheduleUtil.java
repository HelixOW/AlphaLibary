package io.github.alphahelixdev.alpary.utils;

import io.github.alphahelixdev.alpary.Alpary;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ScheduleUtil {

    public <T> void cooldown(int length, T key, List<T> cooldownList) {
        cooldownList.add(key);
        new BukkitRunnable() {
            public void run() {
                cooldownList.remove(key);
            }
        }.runTaskLaterAsynchronously(Alpary.getInstance(), length);
    }

    public void runLater(long ticks, boolean async, Runnable timer) {
        if (async)
            new BukkitRunnable() {
                public void run() {
                    timer.run();
                }
            }.runTaskLaterAsynchronously(Alpary.getInstance(), ticks);
        else
            new BukkitRunnable() {
                public void run() {
                    timer.run();
                }
            }.runTaskLater(Alpary.getInstance(), ticks);
    }

    public void runTimer(long wait, long ticks, boolean async, Runnable timer) {
        if (async)
            new BukkitRunnable() {
                public void run() {
                    timer.run();
                }
            }.runTaskTimerAsynchronously(Alpary.getInstance(), wait, ticks);
        else
            new BukkitRunnable() {
                public void run() {
                    timer.run();
                }
            }.runTaskTimer(Alpary.getInstance(), wait, ticks);
    }

}
