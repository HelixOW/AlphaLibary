package de.alphahelix.alphalibary.core.utils;

import de.alphahelix.alphalibary.core.utils.abstracts.AbstractStringUtil;
import org.bukkit.ChatColor;

import java.util.Collection;

public interface StringUtil {
	
	static String generateRandomString(int size) {
		return AbstractStringUtil.instance.generateRandomString(size);
	}
	
	static String getProgessBar(int current, int maximum, int total, char symbol, ChatColor completed, ChatColor uncompleted) {
		return AbstractStringUtil.instance.getProgessBar(current, maximum, total, symbol, completed, uncompleted);
	}
	
	static boolean isLong(String s) {
		return AbstractStringUtil.instance.isLong(s);
	}
	
	static boolean isDouble(String s) {
		return AbstractStringUtil.instance.isDouble(s);
	}
	
	static String getFirstColors(String input) {
		return AbstractStringUtil.instance.getFirstColors(input);
	}
	
	static String repeat(String string, int count) {
		return AbstractStringUtil.instance.repeat(string, count);
	}
	
	static String replaceLast(String string, String toReplace, String replacement) {
		return AbstractStringUtil.instance.replaceLast(string, toReplace, replacement);
	}
	
	static Collection<String> upperEverything(Collection<String> list) {
		return AbstractStringUtil.instance.upperEverything(list);
	}
	
	static Collection<String> lowerEverything(Collection<String> list) {
		return AbstractStringUtil.instance.lowerEverything(list);
	}
}
