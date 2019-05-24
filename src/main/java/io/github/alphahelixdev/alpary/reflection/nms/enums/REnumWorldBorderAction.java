package io.github.alphahelixdev.alpary.reflection.nms.enums;

import io.github.alphahelixdev.alpary.utils.Utils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
@RequiredArgsConstructor
public enum REnumWorldBorderAction implements Serializable {
	
	SET_SIZE(0),
	LERP_SIZE(1),
	SET_CENTER(2),
	INITIALIZE(3),
	SET_WARNING_TIME(4),
	SET_WARNING_BLOCKS(5);
	
	private final int index;
	
	public Object getEnumWorldBorderAction() {
		return Utils.nms().getNMSEnumConstant("PacketPlayOutWorldBorder$EnumWorldBorderAction", index);
	}
}
