package de.alphahelix.alphalibary.minigame.arena;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.core.AlphaLibary;
import de.alphahelix.alphalibary.core.utils.Util;
import de.alphahelix.alphalibary.storage.file.SimpleFolder;
import org.apache.commons.io.FileDeleteStrategy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("ALL")
public class Arena implements Serializable {

    private static final ArenaFile ARENA_FILE = new ArenaFile();

    static {
        new SimpleFolder("plugins/AlphaGameLibary", "arenas");
    }

    private final String name;
    private final String fileName;
    private final ArenaFile.ArenaItem icon;
    private final transient ArrayList<Location> realSpawns = new ArrayList<>();
    private ArrayList<ArenaFile.NotInitLocation> spawns = new ArrayList<>();

    public Arena(String name, String fileName, ArenaFile.ArenaItem icon, ArrayList<ArenaFile.NotInitLocation> spawns) {
        this.name = name;
        this.fileName = fileName;
        this.icon = icon;
        this.spawns = spawns;
    }

    /**
     * Gets a instance of an arena by its name
     *
     * @param name the name of the arena
     * @return the instance
     */
    public static Arena getArena(String name) {
        return ARENA_FILE.getArena(ChatColor.stripColor(name).replace(" ", "_"));
    }

    /**
     * Transforms the arena into a world
     */
    public void loadArena() {
        Util.unzip("plugins/AlphaGameLibary/arenas/" + fileName + ".zip", Bukkit.getWorlds().get(0).getWorldFolder().getParent());
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

