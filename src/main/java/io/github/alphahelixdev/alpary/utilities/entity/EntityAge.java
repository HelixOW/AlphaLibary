package io.github.alphahelixdev.alpary.utilities.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
@RequiredArgsConstructor
public enum EntityAge implements Serializable {
	
	CHILD(0),
	ADULT(1);
	
	private final int bukkitAge;
}
