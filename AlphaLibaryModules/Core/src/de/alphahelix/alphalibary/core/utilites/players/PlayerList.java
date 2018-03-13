package de.alphahelix.alphalibary.core.utilites.players;

import de.alphahelix.alphalibary.core.utils.ArrayUtil;
import org.bukkit.entity.Player;

import java.util.LinkedList;


public class PlayerList extends LinkedList<String> {
	public boolean remove(Player p) {
		return remove(p.getName());
	}
	
	public void addIfNotExisting(Player p) {
		if(!has(p))
			add(p);
	}
	
	public boolean has(Player p) {
		return contains(p.getName());
	}
	
	public void add(Player p) {
		add(p.getName());
	}
	
	public Player[] getPlayers() {
		return ArrayUtil.makePlayerArray(this);
	}
}
