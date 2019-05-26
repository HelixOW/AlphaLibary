package io.github.alphahelixdev.alpary.reflection.nms.enums;

import io.github.alphahelixdev.alpary.utils.Utils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
@RequiredArgsConstructor
public enum RAction implements Serializable {
	
	INTERACT(0),
	ATTACK(1),
	INTERACT_AT(2);
	
	private final int c;
	
	public Object getEnumAction() {
		return Utils.nms().getNMSEnumConstant("PacketPlayInUseEntity$EnumEntityUseAction", getC());
	}
}
