package de.alphahelix.alphalibary.core.utils;


public class MathUtil {
	
	public static byte toAngle(float v) {
		return (byte) ((int) (v * 256.0F / 360.0F));
	}
	
	public static double toDelta(double v) {
		return ((v * 32) * 128);
	}
	
	public static int floor(double var0) {
		int var2 = (int) var0;
		return var0 < (double) var2 ? var2 - 1 : var2;
	}
	
	public static int toMultipleOfNine(int val) {
		return ((val / 9) + 1) * 9;
	}
	
	public static double round(double value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return (double) Math.round(value * scale) / scale;
	}
	
	public static double trim(double value, int precision) {
		String d = Double.toString(value).split("\\.")[1];
		
		StringBuilder trimmed = new StringBuilder();
		
		for(int i = 0; i < precision; i++) {
			trimmed.append(d.charAt(i));
		}
		
		return Double.valueOf(Double.toString(value).split("\\.")[0] + "." + trimmed.toString());
	}
	
	public static boolean between(double min, double max, double value) {
		return min <= value && value <= max;
	}
	
	public static int decimals(double value) {
		String v = String.valueOf(value).split("\\.")[1];
		
		return v.length();
	}
}
