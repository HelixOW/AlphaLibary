package de.alphahelix.alphalibary.minigame.status;

import com.google.common.base.Objects;
import com.google.gson.annotations.Expose;
import de.alphahelix.alphalibary.minigame.arena.Arena;
import de.alphahelix.alphalibary.minigame.events.status.ArenaStatusChangeEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.WeakHashMap;


public class ArenaStatus {

    @Expose
    private static final transient WeakHashMap<String, ArenaStatus> ARENA_STATUSES = new WeakHashMap<>();

    @Expose
    private static final transient WeakHashMap<Arena, ArenaStatus> CURRENT = new WeakHashMap<>();

    private String name;
    private String rawName;

    public ArenaStatus(String name) {
        setName(name);

        ARENA_STATUSES.put(rawName, this);
    }

    public static ArenaStatus getArenaState(String name) {
        return ARENA_STATUSES.get(ChatColor.stripColor(name).replace(" ", "_"));
    }

    public static boolean isState(Arena toCheck, ArenaStatus match) {
        return CURRENT.containsKey(toCheck) && CURRENT.get(toCheck).equals(match);
    }

    public static ArenaStatus getCurrentStatus(Arena arena) {
        return CURRENT.get(arena);
    }

    public static void setCurrentStatus(Arena toUpdate, ArenaStatus current) {
        ArenaStatusChangeEvent gameStatusChangeEvent = new ArenaStatusChangeEvent(getCurrentStatus(toUpdate), current);
        Bukkit.getPluginManager().callEvent(gameStatusChangeEvent);

        if (!gameStatusChangeEvent.isCancelled())
            ArenaStatus.CURRENT.put(toUpdate, current);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.rawName = ChatColor.stripColor(name).replace(" ", "_");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArenaStatus that = (ArenaStatus) o;
        return Objects.equal(rawName, that.rawName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(rawName);
    }

    @Override
    public String toString() {
        return "ArenaStatus{" +
                "name='" + name + '\'' +
                ", rawName='" + rawName + '\'' +
                '}';
    }
}
