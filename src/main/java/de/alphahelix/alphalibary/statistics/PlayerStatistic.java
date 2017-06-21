package de.alphahelix.alphalibary.statistics;

import com.google.gson.JsonElement;
import de.alphahelix.alphalibary.utils.SerializationUtil;
import de.alphahelix.alphalibary.uuid.UUIDFetcher;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class PlayerStatistic implements Serializable {

    private final UUID player;
    private ArrayList<JsonElement> statistics = new ArrayList<>();

    public PlayerStatistic(UUID player) {
        this.player = player;
    }

    public PlayerStatistic(Player player) {
        this.player = UUIDFetcher.getUUID(player);
    }

    public PlayerStatistic(OfflinePlayer player) {
        this.player = UUIDFetcher.getUUID(player);
    }

    public PlayerStatistic(String player) {
        this.player = UUIDFetcher.getUUID(player);
    }

    public ArrayList<JsonElement> getStatistics() {
        return statistics;
    }

    public PlayerStatistic addStatistics(GameStatistic... gameStatistics) {
        for (GameStatistic gameStatistic : gameStatistics)
            this.statistics.add(gameStatistic.save());
        return this;
    }

    public PlayerStatistic removeStatistics(GameStatistic... gameStatistics) {
        for (GameStatistic gameStatistic : gameStatistics)
            this.statistics.remove(gameStatistic.save());
        return this;
    }

    public boolean hasStatistic(GameStatistic gameStatistic) {
        return this.statistics.contains(gameStatistic.save());
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
