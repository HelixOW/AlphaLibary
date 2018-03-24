package de.alphahelix.alphalibary.alpest;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * Used to manage {@link VirtualPlayer}s for testing purpose
 *
 * @author AlphaHelix
 * @version 1.0
 * @see VirtualPlayer
 * @since 1.9.2.1
 */
public class Alpest {
	
	private static final Map<UUID, VirtualPlayer> VIRTUAL_PLAYERS = new HashMap<>();
	
	/**
	 * Registers a random {@link VirtualPlayer} for the given {@link JavaPlugin}
	 *
	 * @param plugin the {@link JavaPlugin} to load the {@link VirtualPlayer} for
	 * @param amount the amount of {@link VirtualPlayer} to load
	 *
	 * @return the last registered {@link VirtualPlayer}
	 *
	 * @see #registerRandomPlayer(JavaPlugin, String, int)
	 */
	public static VirtualPlayer registerRandomPlayer(JavaPlugin plugin, int amount) {
		VirtualPlayer vp = null;
		for(int i = 0; i < amount; i++) {
			vp = new VirtualPlayer(plugin, Bukkit.getWorlds().get(0).getSpawnLocation());
			registerPlayer(vp);
		}
		return vp;
	}
	
	/**
	 * Registers a given {@link VirtualPlayer}
	 *
	 * @param player the {@link VirtualPlayer} to register
	 *
	 * @return the registered {@link VirtualPlayer}
	 *
	 * @see #unregisterPlayer(VirtualPlayer)
	 */
	public static VirtualPlayer registerPlayer(VirtualPlayer player) {
		VIRTUAL_PLAYERS.put(player.getUniqueId(), player);
		return player;
	}
	
	/**
	 * Registers a random {@link VirtualPlayer} for the given {@link JavaPlugin}
	 *
	 * @param plugin the {@link JavaPlugin} to load the {@link VirtualPlayer} for
	 * @param name   the name of the {@link VirtualPlayer}
	 * @param amount the amount of {@link VirtualPlayer} to load
	 *
	 * @return the last registered {@link VirtualPlayer}
	 *
	 * @see #registerRandomPlayer(JavaPlugin, int)
	 */
	public static VirtualPlayer registerRandomPlayer(JavaPlugin plugin, String name, int amount) {
		VirtualPlayer vp = null;
		for(int i = 0; i < amount; i++) {
			vp = new VirtualPlayer(plugin, name, Bukkit.getWorlds().get(0).getSpawnLocation());
			registerPlayer(vp);
		}
		return vp;
	}
	
	/**
	 * Removes a {@link VirtualPlayer} from the server
	 *
	 * @param player the {@link VirtualPlayer} to remove
	 */
	public static void unregisterPlayer(VirtualPlayer player) {
		VIRTUAL_PLAYERS.remove(player.getUniqueId());
	}
	
	/**
	 * Receives a {@link VirtualPlayer} from his {@link UUID}
	 *
	 * @param id the {@link UUID} of the {@link VirtualPlayer}
	 * @return the {@link VirtualPlayer} with the given {@link UUID}
	 * @see VirtualPlayer#getId()
	 */
	public static VirtualPlayer getFromUUID(UUID id) {
		return VIRTUAL_PLAYERS.get(id);
	}
	
	/**
	 * Returns any {@link VirtualPlayer}
	 *
	 * @return a random {@link VirtualPlayer}
	 * @throws NoSuchElementException when there is no {@link VirtualPlayer} registered
	 * @see #registerPlayer(VirtualPlayer)
	 * @see #registerRandomPlayer(JavaPlugin, int)
	 * @see #registerRandomPlayer(JavaPlugin, String, int)
	 */
	public static VirtualPlayer getRandom() {
		Optional<VirtualPlayer> optionalVirtualPlayer = VIRTUAL_PLAYERS.values().stream().findAny();
		if(optionalVirtualPlayer.isPresent())
			return optionalVirtualPlayer.get();
		throw new NoSuchElementException("No Virtual Players are registered!");
	}
	
}
