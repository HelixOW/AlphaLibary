package de.alphahelix.alphalibary.minigame.spectator;

import de.alphahelix.alphalibary.core.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;


public class Spectator {
	
	private static final HashMap<String, String> PREFIXES = new HashMap<>();
	
	public static void setSpectator(Player player, String prefix, Location spawn) {
		if(!PREFIXES.containsKey(player.getName()))
			PREFIXES.put(player.getName(), prefix);
		
		player.setFoodLevel(20);
		player.setLevel(0);
		player.setGameMode(GameMode.ADVENTURE);
		player.setAllowFlight(true);
		player.setPlayerListName(prefix + player.getDisplayName());
		player.setDisplayName(prefix + player.getDisplayName());
		player.setExp(0);
		player.setHealthScale(20);
		player.setHealth(20);
		player.setTotalExperience(0);
		player.setCanPickupItems(false);
		player.setCustomName(prefix + player.getDisplayName());
		player.setCustomNameVisible(true);
		for(PotionEffect eff : player.getActivePotionEffects()) {
			player.removePotionEffect(eff.getType());
		}
		player.teleport(spawn);
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.hidePlayer(player);
		}
		
		PlayerUtil.addDeadPlayer(player);
	}
	
	public static void removeSpectator(Player player) {
		player.setFoodLevel(20);
		player.setLevel(0);
		player.setGameMode(GameMode.SURVIVAL);
		player.setAllowFlight(false);
		player.setExp(0);
		player.setHealthScale(20);
		player.setHealth(20);
		player.setTotalExperience(0);
		player.setCanPickupItems(false);
		for(PotionEffect eff : player.getActivePotionEffects()) {
			player.removePotionEffect(eff.getType());
		}
		
		if(PREFIXES.containsKey(player.getName())) {
			player.setCustomName(player.getDisplayName().replace(PREFIXES.get(player.getName()), ""));
			player.setPlayerListName(player.getDisplayName().replace(PREFIXES.get(player.getName()), ""));
			player.setDisplayName(player.getDisplayName().replace(PREFIXES.get(player.getName()), ""));
			player.setCustomNameVisible(true);
		}
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.showPlayer(player);
		}
		
		PlayerUtil.removeDeadPlayer(player);
	}
}
