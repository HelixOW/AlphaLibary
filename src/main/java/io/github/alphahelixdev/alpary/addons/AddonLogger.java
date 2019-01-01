package io.github.alphahelixdev.alpary.addons;

import java.util.logging.Level;

public class AddonLogger {
	
	private static final String PREFIX = "[AlphaLibaryAddon] ";
	private static final String INFO = "(info)";
	private static final String ERROR = "(ERROR)";
	
	public void log(Level level, String msg) {
		StringBuilder builder = new StringBuilder();
		builder.append(AddonLogger.PREFIX);
		
		if(level == Level.INFO)
			builder.append(AddonLogger.INFO);
		else if(level == Level.SEVERE)
			builder.append(AddonLogger.ERROR);
		
		builder.append(" ").append(msg);
		
		System.out.println(builder.toString());
	}
}
