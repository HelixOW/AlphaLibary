package de.alphahelix.alphalibary.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ArrayUtil {
	
	@SafeVarargs
	public static <T> T[] makeArray(T... types) {
		return types;
	}
	
	public static Player[] makePlayerArray(Player... types) {
		return types;
	}
	
	public static Player[] makePlayerArray(List<String> types) {
		List<Player> playerArrayList = new ArrayList<>();
		
		for(String type : types) {
			if(Bukkit.getPlayer(type) == null) continue;
			playerArrayList.add(Bukkit.getPlayer(type));
		}
		
		return playerArrayList.toArray(new Player[playerArrayList.size()]);
	}
	
	public static Player[] makePlayerArray(Set<String> types) {
		List<Player> playerArrayList = new ArrayList<>();
		
		for(String type : types) {
			if(Bukkit.getPlayer(type) == null) continue;
			playerArrayList.add(Bukkit.getPlayer(type));
		}
		
		return playerArrayList.toArray(new Player[playerArrayList.size()]);
	}
	
	public static Vector[] makeVectorArray(Location... locations) {
		Vector[] vectors = new Vector[locations.length];
		
		for(int i = 0; i < vectors.length; i++)
			vectors[i] = locations[i].toVector();
		
		return vectors;
	}
	
	public static Vector[] makeLocationArray(World world, Vector... vectors) {
		Location[] locations = new Location[vectors.length];
		
		for(int i = 0; i < locations.length; i++)
			locations[i] = vectors[i].toLocation(world);
		
		return vectors;
	}
	
	public static String[] replaceInArray(String pattern, String replace, String... array) {
		for(int i = 0; i < array.length; i++) {
			array[i] = array[i].replace(pattern, replace);
		}
		return array;
	}
	
	public static <T> List<T> getTypesOf(Class<T> clazzType, List<Object> inList) {
		List<T> types = new ArrayList<>();
		for(Object e : inList)
			if(e.getClass().isInstance(clazzType))
				types.add((T) e);
		return types;
	}
	
	public static void checkForArguments(Object[] array, int min, int max) {
		if(array.length < min || array.length > max)
			throw new ArrayIndexOutOfBoundsException("You have to at least parse " + min + " arguments and up to " + max);
	}
	
	public static double min(double... a) {
		double lowest = a[0];
		
		for(double l : a)
			lowest = Math.min(l, lowest);
		
		return lowest;
	}
	
	public static int min(int... a) {
		int lowest = a[0];
		
		for(int l : a)
			lowest = Math.min(l, lowest);
		
		return lowest;
	}
	
	public static long min(long... a) {
		long lowest = a[0];
		
		for(long l : a)
			lowest = Math.min(l, lowest);
		
		return lowest;
	}
	
	public static float min(float... a) {
		float lowest = a[0];
		
		for(float l : a)
			lowest = Math.min(l, lowest);
		
		return lowest;
	}
	
	public static double max(double... a) {
		double highest = a[0];
		
		for(double l : a)
			highest = Math.max(l, highest);
		
		return highest;
	}
	
	public static int max(int... a) {
		int highest = a[0];
		
		for(int l : a)
			highest = Math.max(l, highest);
		
		return highest;
	}
	
	public static long max(long... a) {
		long highest = a[0];
		
		for(long l : a)
			highest = Math.max(l, highest);
		
		return highest;
	}
	
	public static float max(float... a) {
		float highest = a[0];
		
		for(float l : a)
			highest = Math.max(l, highest);
		
		return highest;
	}
}
