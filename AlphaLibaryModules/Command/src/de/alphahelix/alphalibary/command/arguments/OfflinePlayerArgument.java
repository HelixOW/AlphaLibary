package de.alphahelix.alphalibary.command.arguments;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class OfflinePlayerArgument extends Argument<OfflinePlayer> {
	@Override
	public boolean matches() {
		return fromArgument() != null;
	}
	
	@Override
	public OfflinePlayer fromArgument() {
		if(matches())
			return Bukkit.getOfflinePlayer(UUID.fromString(getEnteredArgument()));
		return null;
	}
}
