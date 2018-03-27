package de.alphahelix.alphalibary.core.utils.abstracts;

import de.alphahelix.alphalibary.core.utilites.Utility;
import de.alphahelix.alphalibary.core.utils.implementations.IStringUtil;
import org.bukkit.ChatColor;

import java.util.Collection;

@Utility(implementation = IStringUtil.class)
public abstract class AbstractStringUtil {
	
	public static AbstractStringUtil instance;
	
	public abstract String generateRandomString(int size);
	
	public abstract String getProgessBar(int current, int maximum, int total, char symbol, ChatColor completed, ChatColor uncompleted);
	
	public abstract boolean isLong(String s);
	
	public abstract boolean isDouble(String s);
	
	public abstract String getFirstColors(String input);
	
	public abstract String repeat(String string, int count);
	
	public abstract String replaceLast(String string, String toReplace, String replacement);
	
	public abstract Collection<String> upperEverything(Collection<String> list);
	
	public abstract Collection<String> lowerEverything(Collection<String> list);
}
