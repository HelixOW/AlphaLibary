package io.github.alphahelixdev.alpary.reflection.nms.enums;

import io.github.alphahelixdev.alpary.utils.Utils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
@RequiredArgsConstructor
public enum REnumHand implements Serializable {
	
	MAIN_HAND(0),
	OFF_HAND(1);
	
	private final int nms;
	
	public Object getEnumHand() {
		return Utils.nms().getNMSEnumConstant("EnumHand", nms);
	}
}
