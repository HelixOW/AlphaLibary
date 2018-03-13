package de.alphahelix.alphalibary.reflection.nms.enums;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;

public enum REnumDifficulty {
	
	PEACEFUL(0),
	EASY(1),
	NORMAL(2),
	HARD(3);
	
	private int id;
	
	REnumDifficulty(int id) {
		this.id = id;
	}
	
	public Object getEnumDifficulty() {
		return ReflectionUtil.getNmsClass("EnumDifficulty").getEnumConstants()[id];
	}
}
