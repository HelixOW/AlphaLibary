package io.github.alphahelixdev.alpary.utilities.entity;

import java.io.Serializable;

public enum EntityAge implements Serializable {
	
	CHILD(0),
	ADULT(1);
	
	private final int bukkitAge;
	
	EntityAge(int bukkitAge) {
		this.bukkitAge = bukkitAge;
	}
	
	public int getBukkitAge() {
		return this.bukkitAge;
	}
	
	@Override
	public String toString() {
		return "EntityAge{" +
				"bukkitAge=" + this.bukkitAge +
				'}';
	}
}
