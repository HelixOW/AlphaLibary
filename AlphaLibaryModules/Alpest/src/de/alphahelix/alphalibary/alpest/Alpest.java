package de.alphahelix.alphalibary.alpest;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Alpest {
	
	private static final Map<UUID, VirtualPlayer> VIRTUAL_PLAYERS = new HashMap<>();
	
	public static void registerRandomPlayer(JavaPlugin plugin, int amount) {
		for(int i = 0; i < amount; i++) {
			registerPlayer(new VirtualPlayer(plugin, Bukkit.getWorlds().get(0).getSpawnLocation()));
		}
	}
	
	public static void registerPlayer(VirtualPlayer player) {
		VIRTUAL_PLAYERS.put(player.getUniqueId(), player);
	}
	
	public static void registerRandomPlayer(JavaPlugin plugin, String name, int amount) {
		for(int i = 0; i < amount; i++) {
			registerPlayer(new VirtualPlayer(plugin, name, Bukkit.getWorlds().get(0).getSpawnLocation()));
		}
	}
	
	public static void unregisterPlayer(VirtualPlayer player) {
		VIRTUAL_PLAYERS.remove(player.getUniqueId());
	}
	
	public static VirtualPlayer getFromUUID(UUID id) {
		return VIRTUAL_PLAYERS.get(id);
	}
	
	public static VirtualPlayer getRandom() {
		Optional<VirtualPlayer> optionalVirtualPlayer = VIRTUAL_PLAYERS.values().stream().findAny();
		if(optionalVirtualPlayer.isPresent())
			return optionalVirtualPlayer.get();
		throw new NoSuchElementException("No Virtual Players are registered!");
	}
	
}
