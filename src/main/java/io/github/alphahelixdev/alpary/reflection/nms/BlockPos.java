package io.github.alphahelixdev.alpary.reflection.nms;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class BlockPos {
	private final int x, y, z;
}
