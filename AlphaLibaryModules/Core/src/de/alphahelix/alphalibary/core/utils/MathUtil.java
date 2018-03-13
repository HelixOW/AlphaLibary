package de.alphahelix.alphalibary.core.utils;

public class MathUtil {
	
	/**
	 * Converts a float into a angle
	 *
	 * @param v the float to convert
	 *
	 * @return the converted angle as a byte
	 */
	public static byte toAngle(float v) {
		return (byte) ((int) (v * 256.0F / 360.0F));
	}
	
	/**
	 * Converts a double into a delta
	 *
	 * @param v the double to convert
	 *
	 * @return the converted delta as a double
	 */
	public static double toDelta(double v) {
		return ((v * 32) * 128);
	}
	
	/**
	 * Wraps the floor (round down) method from the net.minecraft.server MathHelper
	 *
	 * @param var0 the double to floor
	 *
	 * @return the floored int
	 */
	public static int floor(double var0) {
		int var2 = (int) var0;
		return var0 < (double) var2 ? var2 - 1 : var2;
	}
	
	public static int toMultipleOfNine(int val) {
		return ((val / 9) + 1) * 9;
	}
	
	/**
	 * Rounds a {@link Double} up
	 *
	 * @param value     the {@link Double} to round
	 * @param precision the precision to round up to
	 *
	 * @return the rounded up {@link Double}
	 */
	public static double round(double value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return (double) Math.round(value * scale) / scale;
	}
}
