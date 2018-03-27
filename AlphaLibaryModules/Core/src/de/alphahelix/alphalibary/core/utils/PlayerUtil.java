package de.alphahelix.alphalibary.core.utils;

import de.alphahelix.alphalibary.core.utils.abstracts.AbstractPlayerUtil;
import org.bukkit.entity.Player;

public interface PlayerUtil {
	
	static void addTotalPlayer(Player player) {
		AbstractPlayerUtil.instance.addTotalPlayer(player);
	}
	
	static void addTotalPlayer(String player) {
		AbstractPlayerUtil.instance.addTotalPlayer(player);
	}
	
	static void removeTotalPlayer(Player player) {
		AbstractPlayerUtil.instance.removeTotalPlayer(player);
	}
	
	static void removeTotalPlayer(String player) {
		AbstractPlayerUtil.instance.removeTotalPlayer(player);
	}
	
	static void addAlivePlayer(Player player) {
		AbstractPlayerUtil.instance.addAlivePlayer(player);
	}
	
	static void addAlivePlayer(String player) {
		AbstractPlayerUtil.instance.addAlivePlayer(player);
	}
	
	static void removeAlivePlayer(Player player) {
		AbstractPlayerUtil.instance.removeAlivePlayer(player);
	}
	
	static void removeAlivePlayer(String player) {
		AbstractPlayerUtil.instance.removeAlivePlayer(player);
	}
	
	static boolean isPlayerAlive(Player player) {
		return AbstractPlayerUtil.instance.isPlayerAlive(player);
	}
	
	static boolean isPlayerAlive(String player) {
		return AbstractPlayerUtil.instance.isPlayerAlive(player);
	}
	
	static void addDeadPlayer(Player player) {
		AbstractPlayerUtil.instance.addDeadPlayer(player);
	}
	
	static void addDeadPlayer(String player) {
		AbstractPlayerUtil.instance.addDeadPlayer(player);
	}
	
	static void removeDeadPlayer(Player player) {
		AbstractPlayerUtil.instance.removeDeadPlayer(player);
	}
	
	static void removeDeadPlayer(String player) {
		AbstractPlayerUtil.instance.removeDeadPlayer(player);
	}
	
	static boolean isPlayerDead(Player player) {
		return AbstractPlayerUtil.instance.isPlayerDead(player);
	}
	
	static boolean isPlayerDead(String player) {
		return AbstractPlayerUtil.instance.isPlayerDead(player);
	}
}
