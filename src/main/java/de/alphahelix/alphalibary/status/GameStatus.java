package de.alphahelix.alphalibary.status;

import de.alphahelix.alphalibary.events.status.GameStatusChangeEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.ArrayList;

public class GameStatus {

    private static ArrayList<GameStatus> gameStatuses = new ArrayList<>();
    private static GameStatus current = null;

    private String name;
    private String rawName;

    public GameStatus(String name) {
        this.name = name;
        this.rawName = ChatColor.stripColor(name).replace(" ", "_");

        gameStatuses.add(this);
    }

    public static GameStatus getGameState(String name) {
        for (GameStatus status : gameStatuses) {
            if (status.rawName.equalsIgnoreCase(ChatColor.stripColor(name).replace(" ", "_"))) return status;
        }
        return null;
    }

    public static boolean isState(String name) {
        if (current != null) {
            if (current.getRawName().equalsIgnoreCase(ChatColor.stripColor(name).replace(" ", "_"))) return true;
        }
        return false;
    }

    public static GameStatus getCurrentStatus() {
        return current;
    }

    public static void setCurrentStatus(GameStatus current) {
        GameStatusChangeEvent gameStatusChangeEvent = new GameStatusChangeEvent(GameStatus.current, current);
        GameStatus.current = current;
        Bukkit.getPluginManager().callEvent(gameStatusChangeEvent);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.rawName = ChatColor.stripColor(name).replace(" ", "_");
    }

    public String getRawName() {
        return rawName;
    }
}
