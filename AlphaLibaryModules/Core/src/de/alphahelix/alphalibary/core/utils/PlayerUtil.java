package de.alphahelix.alphalibary.core.utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class PlayerUtil {

    private static final List<String> TOTAL_PLAYERS = new ArrayList<>();
    private static final List<String> ALIVE_PLAYERS = new ArrayList<>();
    private static final List<String> DEAD_PLAYERS = new ArrayList<>();

    public static List<String> getTotalPlayers() {
        return TOTAL_PLAYERS;
    }

    public static void addTotalPlayer(String player) {
        TOTAL_PLAYERS.add(player);
    }

    public static void addTotalPlayer(Player player) {
        addTotalPlayer(player.getName());


    }

    public static void removeTotalPlayer(String player) {
        if (TOTAL_PLAYERS.contains(player))
            TOTAL_PLAYERS.remove(player);

        if (ALIVE_PLAYERS.contains(player))
            ALIVE_PLAYERS.remove(player);

        if (DEAD_PLAYERS.contains(player))
            DEAD_PLAYERS.remove(player);
    }

    public static void removeTotalPlayer(Player player) {
        removeTotalPlayer(player.getName());
    }

    public static List<String> getAlivePlayers() {
        return ALIVE_PLAYERS;
    }

    public static void addAlivePlayer(String player) {
        ALIVE_PLAYERS.add(player);
    }

    public static void addAlivePlayer(Player player) {
        addAlivePlayer(player.getName());
    }

    public static void removeAlivePlayer(String player) {
        if (ALIVE_PLAYERS.contains(player))
            ALIVE_PLAYERS.remove(player);

        if (DEAD_PLAYERS.contains(player))
            DEAD_PLAYERS.remove(player);
    }

    public static void removeAlivePlayer(Player player) {
        removeAlivePlayer(player.getName());
    }

    public static boolean isPlayerAlive(String player) {
        return ALIVE_PLAYERS.contains(player);
    }

    public static boolean isPlayerAlive(Player player) {
        return isPlayerAlive(player.getName());
    }

    public static List<String> getDeadPlayers() {
        return DEAD_PLAYERS;
    }

    public static void addDeadPlayer(String player) {
        DEAD_PLAYERS.add(player);
    }

    public static void addDeadPlayer(Player player) {
        addDeadPlayer(player.getName());
    }

    public static void removeDeadPlayer(String player) {
        if (DEAD_PLAYERS.contains(player))
            DEAD_PLAYERS.remove(player);
    }

    public static void removeDeadPlayer(Player player) {
        removeDeadPlayer(player.getName());
    }

    public static boolean isPlayerDead(String player) {
        return DEAD_PLAYERS.contains(player);
    }

    public static boolean isPlayerDead(Player player) {
        return isPlayerDead(player.getName());
    }
}
