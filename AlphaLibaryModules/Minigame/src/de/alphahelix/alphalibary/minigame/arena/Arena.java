package de.alphahelix.alphalibary.minigame.arena;

import com.google.common.base.Objects;
import com.google.gson.annotations.Expose;
import de.alphahelix.alphalibary.core.AlphaLibary;
import de.alphahelix.alphalibary.core.utils.Util;
import de.alphahelix.alphalibary.storage.file.SimpleFolder;
import org.apache.commons.io.FileDeleteStrategy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("ALL")
public class Arena implements Serializable {

    private static ArenaFile arenaFile;

    private final String name;
    private final String fileName;
    private final ArenaFile.ArenaItem icon;
    @Expose
    private final transient JavaPlugin plugin;
    @Expose
    private final transient ArrayList<Location> realSpawns = new ArrayList<>();
    private ArrayList<ArenaFile.NotInitLocation> spawns = new ArrayList<>();

    public Arena(JavaPlugin plugin, String name, String fileName, ArenaFile.ArenaItem icon, ArrayList<ArenaFile.NotInitLocation> spawns) {
        this.name = name;
        this.fileName = fileName;
        this.icon = icon;
        this.spawns = spawns;
        this.plugin = plugin;

        arenaFile = new ArenaFile(plugin);
        new SimpleFolder(plugin, "arenas");
    }

    /**
     * Gets a instance of an arena by its name
     *
     * @param name the name of the arena
     * @return the instance
     */
    public static Arena getArena(String name) {
        return arenaFile.getArena(ChatColor.stripColor(name).replace(" ", "_"));
    }

    /**
     * Transforms the arena into a world
     */
    public void loadArena() {
        Util.unzip(plugin.getDataFolder().getAbsolutePath() + "/arenas/" + fileName + ".zip", Bukkit.getWorlds().get(0).getWorldFolder().getParent());
        new BukkitRunnable() {
            public void run() {
                Bukkit.createWorld(new WorldCreator(fileName));

                for (ArenaFile.NotInitLocation notInitLocation : getSpawns()) {
                    realSpawns.add(notInitLocation.realize());
                }
            }
        }.runTaskLater(AlphaLibary.getInstance(), 25);
    }

    /**
     * Deletes the folder of the world
     */
    public void deleteArena() {
        if (Bukkit.getServer().unloadWorld(fileName, false)) {
            try {
                FileDeleteStrategy.FORCE.delete(new File(fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getName() {
        return name;
    }

    public ArrayList<ArenaFile.NotInitLocation> getSpawns() {
        return spawns;
    }

    public ArrayList<Location> getRealSpawns() {
        return realSpawns;
    }

    public ArenaFile.ArenaItem getIcon() {
        return icon;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arena arena = (Arena) o;
        return Objects.equal(getName(), arena.getName()) &&
                Objects.equal(getFileName(), arena.getFileName()) &&
                Objects.equal(getIcon(), arena.getIcon()) &&
                Objects.equal(getSpawns(), arena.getSpawns());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName(), getFileName(), getIcon(), getSpawns());
    }

    @Override
    public String toString() {
        return "Arena{" +
                "name='" + name + '\'' +
                ", fileName='" + fileName + '\'' +
                ", icon=" + icon +
                ", spawns=" + spawns +
                '}';
    }
}

