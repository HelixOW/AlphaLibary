package de.alphahelix.alphalibary.reflection;

import de.alphahelix.alphalibary.core.utils.abstracts.AbstractReflectionUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ReflectiveStorage {
	
	private static final Map<AbstractReflectionUtil.ClassInfo, Class<?>> CLASSES = new ConcurrentHashMap<>();
	private static final Map<AbstractReflectionUtil.MethodInfo, AbstractReflectionUtil.SaveMethod> METHODS = new ConcurrentHashMap<>();
	private static final Map<AbstractReflectionUtil.ConstructorInfo, AbstractReflectionUtil.SaveConstructor<?>> CONSTRUCTORS = new ConcurrentHashMap<>();
	private static final Map<AbstractReflectionUtil.JarInfo, Class<?>[]> JARS = new ConcurrentHashMap<>();
	
	public static Map<AbstractReflectionUtil.ClassInfo, Class<?>> getClasses() {
		return CLASSES;
	}
	
	public static Map<AbstractReflectionUtil.MethodInfo, AbstractReflectionUtil.SaveMethod> getMethods() {
		return METHODS;
	}
	
	public static Map<AbstractReflectionUtil.ConstructorInfo, AbstractReflectionUtil.SaveConstructor<?>> getConstructors() {
		return CONSTRUCTORS;
	}
	
	public static Map<AbstractReflectionUtil.JarInfo, Class<?>[]> getJars() {
		return JARS;
	}
}
