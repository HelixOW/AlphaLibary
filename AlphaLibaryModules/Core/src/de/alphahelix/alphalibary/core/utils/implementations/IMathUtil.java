package de.alphahelix.alphalibary.core.utils.implementations;

import de.alphahelix.alphalibary.core.utils.abstracts.AbstractMathUtil;

public class IMathUtil extends AbstractMathUtil {
	
	public byte toAngle(float v) {
		return (byte) ((int) (v * 256.0F / 360.0F));
	}
	
	public double toDelta(double v) {
		return ((v * 32) * 128);
	}
	
	public int floor(double var0) {
		int var2 = (int) var0;
		return var0 < (double) var2 ? var2 - 1 : var2;
	}
	
	public int toMultipleOfNine(int val) {
		return ((val / 9) + 1) * 9;
	}
	
	public double round(double value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return (double) Math.round(value * scale) / scale;
	}
	
	public double trim(double value, int precision) {
		String d = Double.toString(value).split("\\.")[1];
		
		StringBuilder trimmed = new StringBuilder();
		
		for(int i = 0; i < precision; i++) {
			trimmed.append(d.charAt(i));
		}
		
		return Double.valueOf(Double.toString(value).split("\\.")[0] + "." + trimmed.toString());
	}
	
	@Override
	public boolean between(double min, double max, double value) {
		return min <= value && value <= max;
	}
	
	public int decimals(double value) {
		String v = String.valueOf(value).split("\\.")[1];
		
		return v.length();
	}
}
