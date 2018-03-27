package de.alphahelix.alphalibary.core.utils.abstracts;

import de.alphahelix.alphalibary.core.utilites.Utility;
import de.alphahelix.alphalibary.core.utils.implementations.IMathUtil;

@Utility(implementation = IMathUtil.class)
public abstract class AbstractMathUtil {
	
	public static AbstractMathUtil instance;
	
	/**
	 * Converts a float into a angle
	 *
	 * @param v the float to convert
	 *
	 * @return the converted angle as a byte
	 */
	public abstract byte toAngle(float v);
	
	/**
	 * Converts a double into a delta
	 *
	 * @param v the double to convert
	 *
	 * @return the converted delta as a double
	 */
	public abstract double toDelta(double v);
	
	/**
	 * Wraps the floor (round down) method from the net.minecraft.server MathHelper
	 *
	 * @param var0 the double to floor
	 *
	 * @return the floored int
	 */
	public abstract int floor(double var0);
	
	public abstract int toMultipleOfNine(int val);
	
	/**
	 * Rounds a {@link Double} up
	 *
	 * @param value     the {@link Double} to round
	 * @param precision the precision to round up to
	 *
	 * @return the rounded up {@link Double}
	 */
	public abstract double round(double value, int precision);
	
	public abstract double trim(double value, int precision);
	
	public abstract int decimals(double value);
}
