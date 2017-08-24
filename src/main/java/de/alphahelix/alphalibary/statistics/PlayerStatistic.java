package de.alphahelix.alphalibary.statistics;

import de.alphahelix.alphalibary.utils.SerializationUtil;
import de.alphahelix.alphalibary.uuid.UUIDFetcher;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class PlayerStatistic implements Serializable {

    private final UUID player;
    private HashMap<String, GameStatistic> statistics = new HashMap<>();

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

    public Object getStatistic(String name) {
        return statistics.get(name).getValue();
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
