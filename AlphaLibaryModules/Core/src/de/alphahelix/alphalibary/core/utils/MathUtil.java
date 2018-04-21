package de.alphahelix.alphalibary.core.utils;

import de.alphahelix.alphalibary.core.utils.abstracts.AbstractMathUtil;

public interface MathUtil {
	
	static byte toAngle(float v) {
		return AbstractMathUtil.instance.toAngle(v);
	}
	
	static double toDelta(double v) {
		return AbstractMathUtil.instance.toDelta(v);
	}
	
	static int floor(double v) {
		return AbstractMathUtil.instance.floor(v);
	}
	
	static int toMultipleOfNine(int val) {
		return AbstractMathUtil.instance.toMultipleOfNine(val);
	}
	
	static double round(double value, int precision) {
		return AbstractMathUtil.instance.round(value, precision);
	}
	
	static double trim(double value, int precision) {
		return AbstractMathUtil.instance.trim(value, precision);
	}
	
	static boolean between(double min, double max, double value) {
		return AbstractMathUtil.instance.between(min, max, value);
	}
	
	static int decimals(double value) {
		return AbstractMathUtil.instance.decimals(value);
	}
}
