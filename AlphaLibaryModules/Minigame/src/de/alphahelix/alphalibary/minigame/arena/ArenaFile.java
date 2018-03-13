package de.alphahelix.alphalibary.minigame.arena;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.storage.file.SimpleJSONFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;


public class ArenaFile extends SimpleJSONFile {
	
	private final JavaPlugin plugin;
	
	public ArenaFile(JavaPlugin plugin) {
		super(plugin, "arena.json");
		
		this.plugin = plugin;
		
		addValues();
	}
	
	public void addValues() {
		if(isEmpty()) {
			ArrayList<NotInitLocation> locs = new ArrayList<>();
			
			locs.add(new NotInitLocation(5, 55, 5, 0, 0, "example"));
			locs.add(new NotInitLocation(10, 55, 5, 0, 0, "example"));
			
			setValue("example_arena",
					new Arena(plugin,
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
		
		private final ItemStack base;
		private final String arenaFileName;
		
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
		
		private final double x;
		private final double y;
		private final double z;
		private final float yaw;
		private final float pitch;
		private final String worldName;
		
		public NotInitLocation(double x, double y, double z, float yaw, float pitch, String worldName) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.yaw = yaw;
			this.pitch = pitch;
			this.worldName = worldName;
		}
		
		public Location realize() {
			return new Location(Bukkit.getWorld(getWorldName()), getX(), getY(), getZ(), getYaw(), getPitch());
		}
		
		public String getWorldName() {
			return worldName;
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
		
		@Override
		public int hashCode() {
			return Objects.hashCode(getX(), getY(), getZ(), getYaw(), getPitch(), getWorldName());
		}
		
		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			NotInitLocation that = (NotInitLocation) o;
			return Double.compare(that.getX(), getX()) == 0 &&
					Double.compare(that.getY(), getY()) == 0 &&
					Double.compare(that.getZ(), getZ()) == 0 &&
					Float.compare(that.getYaw(), getYaw()) == 0 &&
					Float.compare(that.getPitch(), getPitch()) == 0 &&
					Objects.equal(getWorldName(), that.getWorldName());
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
