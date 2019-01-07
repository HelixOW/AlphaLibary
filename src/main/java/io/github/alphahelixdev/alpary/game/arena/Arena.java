package io.github.alphahelixdev.alpary.game.arena;

import com.google.gson.annotations.Expose;
import io.github.alphahelixdev.alpary.Alpary;
import io.github.alphahelixdev.alpary.utilities.NoInitLocation;
import io.github.alphahelixdev.alpary.utilities.SimpleFolder;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.file.json.JsonFile;
import org.apache.commons.io.FileDeleteStrategy;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Arena {
	
	@Expose
	private static transient JsonFile arenaFile;
	@Expose
	private final transient JavaPlugin plugin;
	private final String name;
	private final String fileName;
	private final ItemStack icon;
	private final List<NoInitLocation> spawns = new ArrayList<>();
	
	public Arena(JavaPlugin plugin, String name, String fileName, ItemStack icon, Collection<NoInitLocation> spawns) {
		this.plugin = plugin;
		this.name = name;
		this.fileName = fileName;
		this.icon = icon;
		this.spawns.addAll(spawns);
		
		arenaFile = new JsonFile(plugin.getDataFolder(), "arenaInfo.json");
		
		if(arenaFile.getHead().size() == 0) {
			List<NoInitLocation> locs = new ArrayList<>();
			
			locs.add(new NoInitLocation(5, 55, 5, 0, 0, "example"));
			locs.add(new NoInitLocation(10, 55, 5, 0, 0, "example"));
			
			arenaFile.setValue("example_arena", new Arena(plugin, "&7Example Arena",
					"example_arena", new ItemStack(Material.NAME_TAG), locs));
		}
		
		new SimpleFolder(plugin, "arenas");
	}
	
	public static Arena getArena(String name) {
		return arenaFile.getValue(ChatColor.stripColor(name).replace(" ", "_"), Arena.class);
	}
	
	public void loadArena() {
		Utils.utils().unzip(plugin.getDataFolder().getAbsolutePath() + "/arenas/" + fileName + ".zip", Bukkit.getWorlds().get(0).getWorldFolder().getParent());
		new BukkitRunnable() {
			public void run() {
				Bukkit.createWorld(new WorldCreator(fileName));
			}
		}.runTaskLater(Alpary.getInstance(), 25);
	}
	
	public void deleteArena() {
		if(Bukkit.getServer().unloadWorld(fileName, false)) {
			try {
				FileDeleteStrategy.FORCE.delete(new File(fileName));
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<Location> getWorldSpawns() {
		return this.spawns.stream().map(NoInitLocation::realize).collect(Collectors.toList());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getPlugin(), this.getName(), this.getFileName(), this.getIcon(), this.getSpawns());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Arena arena = (Arena) o;
		return Objects.equals(this.getPlugin(), arena.getPlugin()) &&
				Objects.equals(this.getName(), arena.getName()) &&
				Objects.equals(this.getFileName(), arena.getFileName()) &&
				Objects.equals(this.getIcon(), arena.getIcon()) &&
				Objects.equals(this.getSpawns(), arena.getSpawns());
	}
	
	public JavaPlugin getPlugin() {
		return this.plugin;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getFileName() {
		return this.fileName;
	}
	
	public ItemStack getIcon() {
		return this.icon;
	}
	
	public List<NoInitLocation> getSpawns() {
		return this.spawns;
	}
	
	@Override
	public String toString() {
		return "Arena{" +
				"plugin=" + plugin +
				", name='" + name + '\'' +
				", fileName='" + fileName + '\'' +
				", icon=" + icon +
				", spawns=" + spawns +
				'}';
	}
}
