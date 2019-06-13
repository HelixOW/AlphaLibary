package io.github.alphahelixdev.alpary.game;

import com.google.gson.annotations.Expose;
import io.github.alphahelixdev.alpary.Alpary;
import io.github.alphahelixdev.alpary.utilities.NoInitLocation;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.whoisalphahelix.helix.IHelix;
import io.github.whoisalphahelix.helix.io.HonFile;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.*;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileDeleteStrategy;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
@ToString
public class GameArena {
	
	@Expose
	private static transient HonFile arenaFile;
	@Expose
	private final transient JavaPlugin plugin;
	
	private final String name;
	private final String fileName;
	private final ItemStack icon;
	private final List<NoInitLocation> spawns = new ArrayList<>();
	
	public GameArena(IHelix helix, JavaPlugin plugin, String name, String fileName, ItemStack icon, Collection<NoInitLocation> spawns) throws IOException {
		this.plugin = plugin;
		this.name = name;
		this.fileName = fileName;
		this.icon = icon;
		this.spawns.addAll(spawns);
		
		arenaFile = new HonFile(plugin.getDataFolder(), "arenaInfo.json", helix);
		
		if(arenaFile.getHon().getAll().size() == 0) {
			List<NoInitLocation> locs = new ArrayList<>();
			
			locs.add(new NoInitLocation(5, 55, 5, 0, 0, "example"));
			locs.add(new NoInitLocation(10, 55, 5, 0, 0, "example"));
			
			arenaFile.getHon().set("example_arena", new GameArena(helix, plugin, "&7Example GameArena",
					"example_arena", new ItemStack(Material.NAME_TAG), locs));
			arenaFile.update();
		}
		
		helix.ioHandler().createFolder(new File(plugin.getDataFolder() + "/arenas"));
	}
	
	public static GameArena getArena(String name) {
		return arenaFile.getHon().get(ChatColor.stripColor(name).replace(" ", "_"));
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
}
