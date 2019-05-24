package io.github.alphahelixdev.alpary.game;

import io.github.alphahelixdev.alpary.utils.Utils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.OfflinePlayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
@ToString
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
}
