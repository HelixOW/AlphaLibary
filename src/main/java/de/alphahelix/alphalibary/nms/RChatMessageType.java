package de.alphahelix.alphalibary.nms;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;

public enum RChatMessageType {

    CHAT(0),
    SYSTEM(1),
    GAME_INFO(2);

    private int index;

    RChatMessageType(int index) {
        this.index = index;
    }

    public Object getNMSChatMessageType() {
        return ReflectionUtil.getNmsClass("ChatMessageType").getEnumConstants()[index];
    }
}
