package de.alphahelix.alphalibary.core;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public interface AlphaModule extends Listener {
	
	default void enable() {
	}
	
	default void disable() {
	}
	
	default void load() {
	}
	
	default JavaPlugin plugin() {
		return AlphaLibary.getInstance();
	}
}
