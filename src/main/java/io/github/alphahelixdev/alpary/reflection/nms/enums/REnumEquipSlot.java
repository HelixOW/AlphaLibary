package io.github.alphahelixdev.alpary.reflection.nms.enums;

import io.github.alphahelixdev.alpary.utils.Utils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
@RequiredArgsConstructor
public enum REnumEquipSlot implements Serializable {
	
	HELMET(4, 5),
	CHESTPLATE(3, 4),
	LEGGINGS(2, 3),
	BOOTS(1, 2),
	OFF_HAND(0, 1),
	HAND(0, 0);
	
	private final int nmsSlot;
	private final int past;
	
	public Object getNmsSlot() {
		return Utils.nms().getNMSEnumConstant("EnumItemSlot", past);
	}
}
