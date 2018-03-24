package de.alphahelix.alphalibary.annotations;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Converts final fields or methods into non final fields or methods and makes them accessible
 *
 * @author AlphaHelix
 * @version 1.0
 * @since 1.9.2.1
 */
public class Accessor {
	
	public static Field access(Field f) throws ReflectiveOperationException {
		f.setAccessible(true);
		Field mods = Field.class.getDeclaredField("modifiers");
		mods.setAccessible(true);
		mods.setInt(f, f.getModifiers() & 0xFFFFFFEF);
		return f;
	}
	
	public static Method access(Method m) {
		m.setAccessible(true);
		return m;
	}
}
