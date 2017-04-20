package de.alphahelix.alphalibary.arena;

import de.alphahelix.alphalibary.file.SimpleFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ArenaFile extends SimpleFile {
    public ArenaFile() {
        super("plugins/AlphaGameLibary", "arena.yml");
        addValues();
    }

    @Override
    public void addValues() {
        if (!isConfigurationSection("Arenas")) {
            setDefault("Arenas.example_arena.name", "&7Example Arena");
            setDefault("Arenas.example_arena.file", "example_arena");
            setInventoryItem("Arenas.example_arena.icon", new ItemStack(Material.PAPER), 0);
            setLocation("Arenas.example_arena.spawns." + 0, Bukkit.getWorlds().get(0).getSpawnLocation());
            override("Arenas.example_arena.spawns." + 0 + ".world", "example_arena");
        }
    }

    public ArrayList<NotInitLocation> getSpawns(String rawName) {
        ArrayList<NotInitLocation> locs = new ArrayList<>();

        for (String spawns : getConfigurationSection("Arenas." + rawName.toLowerCase() + ".spawns").getKeys(false)) {
            locs.add(getNotInitLocation("Arenas." + rawName.toLowerCase() + ".spawns." + spawns));
        }

        return locs;
    }

    public Arena getArena(String rawName) {
        return new Arena(
                getColorString("Arenas." + rawName.toLowerCase() + ".name"),
                getString("Arenas." + rawName.toLowerCase() + ".file"),
                new ArenaItem(getInventoryItem("Arenas." + rawName.toLowerCase() + ".icon").getItemStack(), getString("Arenas." + rawName.toLowerCase() + ".file")),
                getSpawns(rawName));
    }

    public NotInitLocation getNotInitLocation(String path) {
        double x = getDouble(path + ".x");
        double y = getDouble(path + ".y");
        double z = getDouble(path + ".z");
        float yaw = getInt(path + ".yaw");
        float pitch = getInt(path + ".pitch");
        String w = getString(path + ".world");

        return new NotInitLocation(x, y, z, yaw, pitch, w);
    }

    public static class ArenaItem {

        private ItemStack base;
        private String arenaFileName;

        public ArenaItem(ItemStack base, String arenaFileName) {
            this.base = base;
            this.arenaFileName = arenaFileName;
        }

        public ItemStack getBase() {
            return base;
        }

        public String getArenaFileName() {
            return arenaFileName;
        }
    }

    public static class NotInitLocation {

        private double x, y, z;
        private float yaw, pitch;
        private String worldName;

        public NotInitLocation(double x, double y, double z, float yaw, float pitch, String worldName) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
            this.worldName = worldName;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }

        public float getYaw() {
            return yaw;
        }

        public float getPitch() {
            return pitch;
        }

        public String getWorldName() {
            return worldName;
        }

        public Location realize() {
            return new Location(Bukkit.getWorld(getWorldName()), getX(), getY(), getZ(), getYaw(), getPitch());
        }
    }
}
