package de.alphahelix.alphalibary.spectator;

import de.alphahelix.alphalibary.AlphaLibary;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;

public class Spectator {

    private static HashMap<String, String> prefixes = new HashMap<>();

    public static void setSpectator(Player player, String prefix, Location spawn) {
        if (!prefixes.containsKey(player.getName()))
            prefixes.put(player.getName(), prefix);

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
        for (PotionEffect eff : player.getActivePotionEffects()) {
            player.removePotionEffect(eff.getType());
        }
        player.teleport(spawn);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.hidePlayer(player);
        }

        AlphaLibary.addPlayerDead(player);
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
        for (PotionEffect eff : player.getActivePotionEffects()) {
            player.removePotionEffect(eff.getType());
        }

        if (prefixes.containsKey(player.getName())) {
            player.setCustomName(player.getDisplayName().replace(prefixes.get(player.getName()), ""));
            player.setPlayerListName(player.getDisplayName().replace(prefixes.get(player.getName()), ""));
            player.setDisplayName(player.getDisplayName().replace(prefixes.get(player.getName()), ""));
            player.setCustomNameVisible(true);
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.showPlayer(player);
        }

        AlphaLibary.removePlayerDead(player);
    }
}
