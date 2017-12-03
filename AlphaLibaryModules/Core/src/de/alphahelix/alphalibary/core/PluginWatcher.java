package de.alphahelix.alphalibary.core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class PluginWatcher {

    private final BukkitRunnable task;
    private final long lastModified;
    private final long time;
    private final JavaPlugin plugin;

    public PluginWatcher(JavaPlugin plugin) {
        this(plugin, 20);
    }

    public PluginWatcher(JavaPlugin plugin, long time) {
        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                if (check())
                    Bukkit.reload();
            }
        };

        this.lastModified = new File("plugins/", plugin.getName() + ".jar").lastModified();
        this.time = time;
        this.plugin = plugin;
    }

    public void run() {
        task.runTaskTimer(AlphaLibary.getInstance(), time, time);
    }

    public BukkitRunnable getTask() {
        return task;
    }

    public boolean check() {
        File otherJarFile = new File("plugins/", plugin.getName() + ".jar");

        return lastModified < otherJarFile.lastModified();
    }
}
