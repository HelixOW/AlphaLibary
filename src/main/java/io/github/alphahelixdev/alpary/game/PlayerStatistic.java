package io.github.alphahelixdev.alpary.game;

import io.github.alphahelixdev.alpary.utils.Utils;
import org.bukkit.OfflinePlayer;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class PlayerStatistic implements Serializable {

    private final UUID owner;
    private final List<Object> statistics = new ArrayList<>();

    public PlayerStatistic(UUID owner, Object... statistics) {
        this.owner = owner;
        this.statistics.addAll(Arrays.asList(statistics));
    }

    public PlayerStatistic(OfflinePlayer p, Object... statistics) {
        this(p.getUniqueId(), statistics);
    }

    public static PlayerStatistic of(String base64) {
        return Utils.serializations().decodeBase64(base64, PlayerStatistic.class);
    }

    public String encode() {
        return Utils.serializations().encodeBase64(this);
    }

    public <T> List<T> getStatistic(Class<T> type) {
        return (List<T>) getStatistics().stream().filter(o -> o.getClass().equals(type)).collect(Collectors.toList());
    }

    public UUID getOwner() {
        return owner;
    }

    public List<Object> getStatistics() {
        return statistics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerStatistic that = (PlayerStatistic) o;
        return Objects.equals(owner, that.owner) &&
                Objects.equals(statistics, that.statistics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, statistics);
    }

    @Override
    public String toString() {
        return "PlayerStatistic{" +
                "owner=" + owner +
                ", statistics=" + statistics +
                '}';
    }
}
