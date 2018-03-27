package de.alphahelix.alphalibary.core.utils.abstracts;

import de.alphahelix.alphalibary.core.utilites.Utility;
import de.alphahelix.alphalibary.core.utils.implementations.IPlayerUtil;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

@Utility(implementation = IPlayerUtil.class)
public abstract class AbstractPlayerUtil {
	
	private static final Set<String> TOTAL_PLAYERS = new HashSet<>();
	private static final Set<String> ALIVE_PLAYERS = new HashSet<>();
	private static final Set<String> DEAD_PLAYERS = new HashSet<>();
	public static AbstractPlayerUtil instance;
	
	public static Set<String> getTotalPlayers() {
		return TOTAL_PLAYERS;
	}
	
	public static Set<String> getAlivePlayers() {
		return ALIVE_PLAYERS;
	}
	
	public static Set<String> getDeadPlayers() {
		return DEAD_PLAYERS;
	}
	
	public abstract void addTotalPlayer(Player player);
	
	public abstract void addTotalPlayer(String player);
	
	public abstract void removeTotalPlayer(Player player);
	
	public abstract void removeTotalPlayer(String player);
	
	public abstract void addAlivePlayer(Player player);
	
	public abstract void addAlivePlayer(String player);
	
	public abstract void removeAlivePlayer(Player player);
	
	public abstract void removeAlivePlayer(String player);
	
	public abstract boolean isPlayerAlive(Player player);
	
	public abstract boolean isPlayerAlive(String player);
	
	public abstract void addDeadPlayer(Player player);
	
	public abstract void addDeadPlayer(String player);
	
	public abstract void removeDeadPlayer(Player player);
	
	public abstract void removeDeadPlayer(String player);
	
	public abstract boolean isPlayerDead(Player player);
	
	public abstract boolean isPlayerDead(String player);
}
