package de.alphahelix.alphalibary.forms;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface FormAction {
	void action(Player p, Location loc);
}
