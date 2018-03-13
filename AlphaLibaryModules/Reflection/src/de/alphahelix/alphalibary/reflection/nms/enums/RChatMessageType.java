package de.alphahelix.alphalibary.reflection.nms.enums;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;

import java.io.Serializable;

public enum RChatMessageType implements Serializable {
	
	CHAT(0),
	SYSTEM(1),
	GAME_INFO(2);
	
	private final int index;
	
	RChatMessageType(int index) {
		this.index = index;
	}
	
	public Object getNMSChatMessageType() {
		return ReflectionUtil.getNmsClass("ChatMessageType").getEnumConstants()[index];
	}
	
	@Override
	public String toString() {
		return "RChatMessageType{" +
				"index=" + index +
				'}';
	}
}
