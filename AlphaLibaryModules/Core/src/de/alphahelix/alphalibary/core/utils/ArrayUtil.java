package de.alphahelix.alphalibary.core.utils;

import de.alphahelix.alphalibary.core.utils.abstracts.AbstractArrayUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;

public interface ArrayUtil {
	
	static <T> T[] makeArray(T... types) {
		return AbstractArrayUtil.instance.makeArray(types);
	}
	
	static Player[] makePlayerArray(List<String> types) {
		return AbstractArrayUtil.instance.makePlayerArray(types);
	}
	
	static Player[] makePlayerArray(Set<String> types) {
		return AbstractArrayUtil.instance.makePlayerArray(types);
	}
	
	static Vector[] makeVectorArray(Location... locations) {
		return AbstractArrayUtil.instance.makeVectorArray(locations);
	}
	
	static Vector[] makeLocationArray(World world, Vector... vectors) {
		return AbstractArrayUtil.instance.makeLocationArray(world, vectors);
	}
	
	static String[] replaceInArray(String pattern, String replace, String... array) {
		return AbstractArrayUtil.instance.replaceInArray(pattern, replace, array);
	}
	
	static <T> List<T> getTypesOf(Class<T> clazzType, List<Object> inList) {
		return AbstractArrayUtil.instance.getTypesOf(clazzType, inList);
	}
	
	static void checkForArguments(Object[] array, int min, int max) {
		AbstractArrayUtil.instance.checkForArguments(array, min, max);
	}
	
	static <T> T[] replaceInArray(T obj, int pos, T... array) {
		return AbstractArrayUtil.instance.replaceInArray(obj, pos, array);
	}
	
	static double min(double... a) {
		return AbstractArrayUtil.instance.min(a);
	}
	
	static int min(int... a) {
		return AbstractArrayUtil.instance.min(a);
	}
	
	static long min(long... a) {
		return AbstractArrayUtil.instance.min(a);
	}
	
	static float min(float... a) {
		return AbstractArrayUtil.instance.min(a);
	}
	
	static double max(double... a) {
		return AbstractArrayUtil.instance.max(a);
	}
	
	static int max(int... a) {
		return AbstractArrayUtil.instance.max(a);
	}
	
	static long max(long... a) {
		return AbstractArrayUtil.instance.max(a);
	}
	
	static float max(float... a) {
		return AbstractArrayUtil.instance.max(a);
	}
	
	static double sum(double... a) {
		return AbstractArrayUtil.instance.sum(a);
	}
	
	static int sum(int... a) {
		return AbstractArrayUtil.instance.sum(a);
	}
	
	static float sum(float... a) {
		return AbstractArrayUtil.instance.sum(a);
	}
	
	static long sum(long... a) {
		return AbstractArrayUtil.instance.sum(a);
	}
}
