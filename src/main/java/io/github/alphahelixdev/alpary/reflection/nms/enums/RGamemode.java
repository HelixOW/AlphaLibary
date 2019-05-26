package io.github.alphahelixdev.alpary.reflection.nms.enums;

import io.github.alphahelixdev.alpary.utils.Utils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.entity.Player;

import java.io.Serializable;

@Getter
@ToString
@RequiredArgsConstructor
public enum RGamemode implements Serializable {
	
	NOT_SET(0),
	SURVIVAL(1),
	CREATIVE(2),
	ADVENTURE(3),
	SPECTATOR(4);
	
	private final int c;

    public static RGamemode getFromPlayer(Player player) {
		switch (player.getGameMode()) {
			case ADVENTURE:
				return ADVENTURE;
			case CREATIVE:
				return CREATIVE;
			case SURVIVAL:
				return SURVIVAL;
			case SPECTATOR:
				return SPECTATOR;
			default:
				return NOT_SET;
		}
	}
	
	public Object getEnumGamemode() {
		return Utils.nms().getNMSEnumConstant("EnumGamemode", c);
	}
}
