package de.alphahelix.alphalibary.utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerUtil {

    private static ArrayList<String> totalPlayers = new ArrayList<>();
    private static ArrayList<String> alivePlayers = new ArrayList<>();
    private static ArrayList<String> deadPlayers = new ArrayList<>();

    public static ArrayList<String> getTotalPlayers() {
        return totalPlayers;
    }

    public static void addTotalPlayer(String player) {
        totalPlayers.add(player);
    }

    public static void addTotalPlayer(Player player) {
        addTotalPlayer(player.getName());


    }

    public static void removeTotalPlayer(String player) {
        if (totalPlayers.contains(player))
            totalPlayers.remove(player);

        if (alivePlayers.contains(player))
            alivePlayers.remove(player);

        if (deadPlayers.contains(player))
            deadPlayers.remove(player);
    }

    public static void removeTotalPlayer(Player player) {
        removeTotalPlayer(player.getName());
    }

    public static ArrayList<String> getAlivePlayers() {
        return alivePlayers;
    }

    public static void addAlivePlayer(String player) {
        alivePlayers.add(player);
    }

    public static void addAlivePlayer(Player player) {
        addAlivePlayer(player.getName());
    }

    public static void removeAlivePlayer(String player) {
        if (alivePlayers.contains(player))
            alivePlayers.remove(player);

        if (deadPlayers.contains(player))
            deadPlayers.remove(player);
    }

    public static void removeAlivePlayer(Player player) {
        removeAlivePlayer(player.getName());
    }

    public static boolean isPlayerAlive(String player) {
        return alivePlayers.contains(player);
    }

    public static boolean isPlayerAlive(Player player) {
        return isPlayerAlive(player.getName());
    }

    public static ArrayList<String> getDeadPlayers() {
        return deadPlayers;
    }

    public static void addDeadPlayer(String player) {
        deadPlayers.add(player);
    }

    public static void addDeadPlayer(Player player) {
        addDeadPlayer(player.getName());
    }

    public static void removeDeadPlayer(String player) {
        if (deadPlayers.contains(player))
            deadPlayers.remove(player);
    }

    public static void removeDeadPlayer(Player player) {
        removeDeadPlayer(player.getName());
    }

    public static boolean isPlayerDead(String player) {
        return deadPlayers.contains(player);
    }

    public static boolean isPlayerDead(Player player) {
        return isPlayerDead(player.getName());
    }
}
