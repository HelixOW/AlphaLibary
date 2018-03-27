package de.alphahelix.alphalibary.core.utils.implementations;

import de.alphahelix.alphalibary.core.utils.abstracts.AbstractPlayerUtil;
import org.bukkit.entity.Player;


public class IPlayerUtil extends AbstractPlayerUtil {
	
	public void addTotalPlayer(Player player) {
		addTotalPlayer(player.getName());
	}
	
	public void addTotalPlayer(String player) {
		getTotalPlayers().add(player);
	}
	
	public void removeTotalPlayer(Player player) {
		removeTotalPlayer(player.getName());
	}
	
	public void removeTotalPlayer(String player) {
		if(getTotalPlayers().contains(player))
			getTotalPlayers().remove(player);
		
		if(getAlivePlayers().contains(player))
			getAlivePlayers().remove(player);
		
		if(getDeadPlayers().contains(player))
			getDeadPlayers().remove(player);
	}
	
	public void addAlivePlayer(Player player) {
		addAlivePlayer(player.getName());
	}
	
	public void addAlivePlayer(String player) {
		getAlivePlayers().add(player);
	}
	
	public void removeAlivePlayer(Player player) {
		removeAlivePlayer(player.getName());
	}
	
	public void removeAlivePlayer(String player) {
		if(getAlivePlayers().contains(player))
			getAlivePlayers().remove(player);
		
		if(getDeadPlayers().contains(player))
			getDeadPlayers().remove(player);
	}
	
	public boolean isPlayerAlive(Player player) {
		return isPlayerAlive(player.getName());
	}
	
	public boolean isPlayerAlive(String player) {
		return getAlivePlayers().contains(player);
	}
	
	public void addDeadPlayer(Player player) {
		addDeadPlayer(player.getName());
	}
	
	public void addDeadPlayer(String player) {
		getDeadPlayers().add(player);
	}
	
	public void removeDeadPlayer(Player player) {
		removeDeadPlayer(player.getName());
	}
	
	public void removeDeadPlayer(String player) {
		if(getDeadPlayers().contains(player))
			getDeadPlayers().remove(player);
	}
	
	public boolean isPlayerDead(Player player) {
		return isPlayerDead(player.getName());
	}
	
	public boolean isPlayerDead(String player) {
		return getDeadPlayers().contains(player);
	}
}
