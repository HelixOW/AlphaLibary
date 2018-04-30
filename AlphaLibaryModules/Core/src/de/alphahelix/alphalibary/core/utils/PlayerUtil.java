package de.alphahelix.alphalibary.core.utils;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;


public class PlayerUtil {
	
	private static final Set<String> TOTAL_PLAYERS = new HashSet<>();
	private static final Set<String> ALIVE_PLAYERS = new HashSet<>();
	private static final Set<String> DEAD_PLAYERS = new HashSet<>();
	
	public static void addTotalPlayer(Player player) {
		addTotalPlayer(player.getName());
	}
	
	public static void addTotalPlayer(String player) {
		getTotalPlayers().add(player);
	}
	
	public static Set<String> getTotalPlayers() {
		return TOTAL_PLAYERS;
	}
	
	public static void removeTotalPlayer(Player player) {
		removeTotalPlayer(player.getName());
	}
	
	public static void removeTotalPlayer(String player) {
		getTotalPlayers().remove(player);
		
		getAlivePlayers().remove(player);
		
		getDeadPlayers().remove(player);
	}
	
	public static Set<String> getAlivePlayers() {
		return ALIVE_PLAYERS;
	}
	
	public static Set<String> getDeadPlayers() {
		return DEAD_PLAYERS;
	}
	
	public static void addAlivePlayer(Player player) {
		addAlivePlayer(player.getName());
	}
	
	public static void addAlivePlayer(String player) {
		getAlivePlayers().add(player);
	}
	
	public static void removeAlivePlayer(Player player) {
		removeAlivePlayer(player.getName());
	}
	
	public static void removeAlivePlayer(String player) {
		getAlivePlayers().remove(player);
		
		getDeadPlayers().remove(player);
	}
	
	public static boolean isPlayerAlive(Player player) {
		return isPlayerAlive(player.getName());
	}
	
	public static boolean isPlayerAlive(String player) {
		return getAlivePlayers().contains(player);
	}
	
	public static void addDeadPlayer(Player player) {
		addDeadPlayer(player.getName());
	}
	
	public static void addDeadPlayer(String player) {
		getDeadPlayers().add(player);
	}
	
	public static void removeDeadPlayer(Player player) {
		removeDeadPlayer(player.getName());
	}
	
	public static void removeDeadPlayer(String player) {
		getDeadPlayers().remove(player);
	}
	
	public static boolean isPlayerDead(Player player) {
		return isPlayerDead(player.getName());
	}
	
	public static boolean isPlayerDead(String player) {
		return getDeadPlayers().contains(player);
	}
}
