package de.alphahelix.alphalibary.core.utils.abstracts;

import de.alphahelix.alphalibary.core.utilites.Utility;
import de.alphahelix.alphalibary.core.utils.implementations.IArrayUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;

@Utility(implementation = IArrayUtil.class)
public abstract class AbstractArrayUtil {
	
	public static AbstractArrayUtil instance;
	
	public abstract <T> T[] makeArray(T... types);
	
	public abstract Player[] makePlayerArray(List<String> types);
	
	public abstract Player[] makePlayerArray(Set<String> types);
	
	public abstract Vector[] makeVectorArray(Location... locations);
	
	public abstract Location[] makeLocationArray(World world, Vector... vectors);
	
	public abstract String[] replaceInArray(String pattern, String replace, String... array);
	
	public abstract <T> List<T> getTypesOf(Class<T> clazzType, List<Object> inList);
	
	public abstract void checkForArguments(Object[] array, int min, int max);
	
	public abstract <T> T[] replaceInArray(T obj, int pos, T... array);
	
	public abstract double min(double... a);
	
	public abstract int min(int... a);
	
	public abstract long min(long... a);
	
	public abstract float min(float... a);
	
	public abstract double max(double... a);
	
	public abstract int max(int... a);
	
	public abstract long max(long... a);
	
	public abstract float max(float... a);
	
	public abstract double sum(double... a);
	
	public abstract int sum(int... a);
	
	public abstract float sum(float... a);
	
	public abstract long sum(long... a);
	
	public abstract double[] trim(int decimal, double... a);
	
	public abstract Location[] trim(int decimal, Location... locations);
}