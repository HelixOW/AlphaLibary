package de.alphahelix.alphalibary.arena;

import de.alphahelix.alphalibary.file.SimpleJSONFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ArenaFile extends SimpleJSONFile {
    public ArenaFile() {
        super("plugins/AlphaGameLibary", "arena.json");
        addValues();
    }

    public void addValues() {
        if (isEmpty()) {
            ArrayList<NotInitLocation> locs = new ArrayList<>();

            locs.add(new NotInitLocation(5, 55, 5, 0, 0, "example"));
            locs.add(new NotInitLocation(10, 55, 5, 0, 0, "example"));

            setValue("example_arena",
                    new Arena(
                            "&7Example Arena",
                            "example_arena",
                            new ArenaItem(new ItemStack(Material.NAME_TAG), "example_arena"),
                            locs));
        }
    }

    public void addArena(Arena arena) {
        setValue(arena.getFileName(), arena);
    }

    public Arena getArena(String rawName) {
        return getValue(rawName.toLowerCase(), Arena.class);
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

        @Override
        public String toString() {
            return "ArenaItem{" +
                    "base=" + base +
                    ", arenaFileName='" + arenaFileName + '\'' +
                    '}';
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

        @Override
        public String toString() {
            return "NotInitLocation{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    ", yaw=" + yaw +
                    ", pitch=" + pitch +
                    ", worldName='" + worldName + '\'' +
                    '}';
        }
    }
}
