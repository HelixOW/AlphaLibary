package de.alphahelix.alphalibary;

import de.alphahelix.alphalibary.arena.ArenaFile;
import de.alphahelix.alphalibary.fakeapi.FakeAPI;
import de.alphahelix.alphalibary.utils.GameProfileBuilder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;

public class AlphaLibary extends JavaPlugin {

    private static AlphaLibary instance;
    private static GameProfileBuilder.GameProfileFile gameProfileFile;
    private static ArenaFile arenaFile;
    private static ArrayList<String> playersTotal = new ArrayList<>();
    private static ArrayList<String> playersInGame = new ArrayList<>();
    private static ArrayList<String> playersDead = new ArrayList<>();

    public static AlphaLibary getInstance() {
        return instance;
    }

    public static GameProfileBuilder.GameProfileFile getGameProfileFile() {
        return gameProfileFile;
    }

    public static ArrayList<String> getPlayersTotal() {
        return playersTotal;
    }

    public static void addPlayerTotal(Player p) {
        playersTotal.add(p.getName());
    }

    public static void removePlayerTotal(Player p) {
        if (playersTotal.contains(p.getName()))
            playersTotal.remove(p.getName());

        if (playersInGame.contains(p.getName()))
            playersInGame.remove(p.getName());

        if (playersDead.contains(p.getName()))
            playersDead.remove(p.getName());
    }

    public static ArrayList<String> getPlayersInGame() {
        return playersInGame;
    }

    public static void addPlayerInGame(Player p) {
        playersInGame.add(p.getName());
    }

    public static void removePlayerInGame(Player p) {
        if (playersInGame.contains(p.getName()))
            playersInGame.remove(p.getName());

        if (playersDead.contains(p.getName()))
            playersDead.remove(p.getName());
    }

    public static boolean isPlayerInGame(Player p) {
        return playersInGame.contains(p.getName());
    }

    public static ArrayList<String> getPlayersDead() {
        return playersDead;
    }

    public static void addPlayerDead(Player p) {
        playersDead.add(p.getName());
    }

    public static void removePlayerDead(Player p) {
        if (playersDead.contains(p.getName()))
            playersDead.remove(p.getName());
    }

    public static boolean isPlayerDead(Player p) {
        return playersDead.contains(p.getName());
    }

    public static ArenaFile getArenaFile() {
        return arenaFile;
    }

    @Override
    public void onLoad() {
        FakeAPI.load();
    }

    @Override
    public void onEnable() {
        instance = this;
        FakeAPI.enable();

        gameProfileFile = new GameProfileBuilder.GameProfileFile();
        arenaFile = new ArenaFile();

        File arenaFolder = new File("plugins/AlphaGameLibary/arenas");

        if (!arenaFolder.exists()) arenaFolder.mkdirs();


    }

    @Override
    public void onDisable() {
        FakeAPI.disable();
    }
}
