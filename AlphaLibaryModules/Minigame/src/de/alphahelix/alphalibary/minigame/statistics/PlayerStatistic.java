package de.alphahelix.alphalibary.minigame.statistics;

import de.alphahelix.alphalibary.core.utilites.UUIDFetcher;
import de.alphahelix.alphalibary.core.utils.SerializationUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.*;


public class PlayerStatistic implements Serializable {

    private final UUID player;
    private final HashMap<String, GameStatistic> statistics = new HashMap<>();

    public PlayerStatistic(UUID player, GameStatistic... array) {
        this.player = player;
        for (GameStatistic gameStatistic : array) {
            this.statistics.put(gameStatistic.getName(), gameStatistic);
        }
    }

    public PlayerStatistic(Player player, GameStatistic... array) {
        final UUID[] id = new UUID[1];
        UUIDFetcher.getUUID(player, id1 -> id[0] = id1);

        this.player = id[0];
        for (GameStatistic gameStatistic : array) {
            this.statistics.put(gameStatistic.getName(), gameStatistic);
        }
    }

    public PlayerStatistic(OfflinePlayer player, GameStatistic... array) {
        final UUID[] id = new UUID[1];
        UUIDFetcher.getUUID(player, id1 -> id[0] = id1);

        this.player = id[0];
        for (GameStatistic gameStatistic : array) {
            this.statistics.put(gameStatistic.getName(), gameStatistic);
        }
    }

    public PlayerStatistic(String player, GameStatistic... array) {
        final UUID[] id = new UUID[1];
        UUIDFetcher.getUUID(player, id1 -> id[0] = id1);

        this.player = id[0];
        for (GameStatistic gameStatistic : array) {
            this.statistics.put(gameStatistic.getName(), gameStatistic);
        }
    }

    public static PlayerStatistic decode(String base64) {
        return SerializationUtil.decodeBase64(base64, PlayerStatistic.class);
    }

    public UUID getPlayer() {
        return player;
    }

    public Collection<GameStatistic> getStatistics() {
        return statistics.values();
    }

    public <T> T getStatistic(String name) {
        return (T) statistics.get(name).getValue();
    }

    public <T> List<GameStatistic<T>> getStatistics(Class<T> type) {
        List<GameStatistic<T>> stats = new ArrayList<>();
        for (GameStatistic<?> s : statistics.values())
            if (s.getClass().isInstance(type))
                stats.add((GameStatistic<T>) s);
        return stats;
    }

    public PlayerStatistic addStatistics(GameStatistic... gameStatistics) {
        for (GameStatistic gameStatistic : gameStatistics)
            statistics.put(gameStatistic.getName(), gameStatistic);
        return this;
    }

    public PlayerStatistic removeStatistics(GameStatistic... gameStatistics) {
        for (GameStatistic gameStatistic : gameStatistics)
            statistics.remove(gameStatistic.getName());
        return this;
    }

    public boolean hasStatistic(GameStatistic gameStatistic) {
        return getStatistic(gameStatistic.getName()) != null;
    }

    public boolean hasStatistic(String name) {
        return getStatistic(name) != null;
    }

    public String encodeInBase64() {
        return SerializationUtil.encodeBase64(this);
    }

    @Override
    public String toString() {
        return "PlayerStatistic{" +
                "player=" + player +
                ", statistics=" + statistics +
                '}';
    }
}
