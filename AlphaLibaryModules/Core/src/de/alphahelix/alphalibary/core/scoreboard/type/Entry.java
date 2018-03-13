package de.alphahelix.alphalibary.core.scoreboard.type;

import org.bukkit.ChatColor;

public class Entry {
	
	private String name;
	private int position;
	
	public Entry(String name, int position) {
		this.name = ChatColor.translateAlternateColorCodes('&', name);
		this.position = position;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getPosition() {
		return position;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
}
