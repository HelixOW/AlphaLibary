package io.github.alphahelixdev.alpary.reflection.nms.enums;

import io.github.alphahelixdev.alpary.utils.Utils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public enum REnumDifficulty {
	
	PEACEFUL(0),
	EASY(1),
	NORMAL(2),
	HARD(3);
	
	private final int id;
	
	public Object getEnumDifficulty() {
		return Utils.nms().getNMSEnumConstant("EnumDifficulty", id);
	}
}
