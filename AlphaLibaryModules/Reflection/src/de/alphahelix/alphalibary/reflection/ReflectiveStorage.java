package de.alphahelix.alphalibary.reflection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ReflectiveStorage {

    private static final Map<ReflectionUtil.ClassInfo, Class<?>> CLASSES = new ConcurrentHashMap<>();
    private static final Map<ReflectionUtil.MethodInfo, ReflectionUtil.SaveMethod> METHODS = new ConcurrentHashMap<>();
    private static final Map<ReflectionUtil.ConstructorInfo, ReflectionUtil.SaveConstructor<?>> CONSTRUCTORS = new ConcurrentHashMap<>();
    private static final Map<ReflectionUtil.JarInfo, Class<?>[]> JARS = new ConcurrentHashMap<>();

    public static Map<ReflectionUtil.ClassInfo, Class<?>> getClasses() {
        return CLASSES;
    }

    public static Map<ReflectionUtil.MethodInfo, ReflectionUtil.SaveMethod> getMethods() {
        return METHODS;
    }

    public static Map<ReflectionUtil.ConstructorInfo, ReflectionUtil.SaveConstructor<?>> getConstructors() {
        return CONSTRUCTORS;
    }

    public static Map<ReflectionUtil.JarInfo, Class<?>[]> getJars() {
        return JARS;
    }
}
