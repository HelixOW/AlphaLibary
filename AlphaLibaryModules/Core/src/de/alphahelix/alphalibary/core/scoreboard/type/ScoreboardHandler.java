package de.alphahelix.alphalibary.core.scoreboard.type;

import org.bukkit.entity.Player;

import java.util.List;

public interface ScoreboardHandler {
	
	String getTitle(Player player);
	
	List<Entry> getEntries(Player player);
	
}
