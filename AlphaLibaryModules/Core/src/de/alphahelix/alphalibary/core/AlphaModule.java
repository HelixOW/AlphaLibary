package de.alphahelix.alphalibary.core;

import org.bukkit.plugin.java.JavaPlugin;

public interface AlphaModule {
	
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
