package io.github.alphahelixdev.alpary.addons;

import java.util.HashMap;
import java.util.Map;

public class CrossAddonStorage {
	
	private static final Map<String, String> VARIABLES = new HashMap<>();
	
	public static String getVariable(String name) {
		return VARIABLES.getOrDefault(name, "not found");
	}
	
	public static Map<String, String> getVariables() {
		return CrossAddonStorage.VARIABLES;
	}
}
