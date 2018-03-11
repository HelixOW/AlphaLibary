package de.alphahelix.alphalibary.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ArrayUtil {
	
	public static <T> T[] makeArray (T... types) {
		return types;
	}
	
	public static Player[] makePlayerArray (Player... types) {
		return types;
	}
	
	public static Player[] makePlayerArray (List<String> types) {
		ArrayList<Player> playerArrayList = new ArrayList<>();
		
		for(String type : types) {
			if(Bukkit.getPlayer(type) == null) continue;
			playerArrayList.add(Bukkit.getPlayer(type));
		}
		
		return playerArrayList.toArray(new Player[playerArrayList.size()]);
	}
	
	public static Player[] makePlayerArray (Set<String> types) {
		ArrayList<Player> playerArrayList = new ArrayList<>();
		
		for(String type : types) {
			if(Bukkit.getPlayer(type) == null) continue;
			playerArrayList.add(Bukkit.getPlayer(type));
		}
		
		return playerArrayList.toArray(new Player[playerArrayList.size()]);
	}
	
	public static String[] replaceInArray (String pattern, String replace, String... array) {
		for(int i = 0; i < array.length; i++) {
			array[i] = array[i].replace(pattern, replace);
		}
		return array;
	}
	
	public static <T> List<T> getTypesOf (Class<T> clazzType, List<Object> inList) {
		List<T> types = new ArrayList<>();
		for(Object e : inList)
			if(e.getClass().isInstance(clazzType))
				types.add((T) e);
		return types;
	}
}
