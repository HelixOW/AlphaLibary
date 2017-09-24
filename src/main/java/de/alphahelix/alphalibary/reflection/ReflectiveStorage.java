package de.alphahelix.alphalibary.reflection;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class ReflectiveStorage {

    private static final ConcurrentHashMap<ReflectionUtil.ClassInfo, Class<?>> CLASSES = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<ReflectionUtil.MethodInfo, ReflectionUtil.SaveMethod> METHODS = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<ReflectionUtil.ConstructorInfo, ReflectionUtil.SaveConstructor<?>> CONSTRUCTORS = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<ReflectionUtil.JarInfo, Set<Class<?>>> JARS = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<ReflectionUtil.ClassInfo, Class<?>> getClasses() {
        return CLASSES;
    }

    public static ConcurrentHashMap<ReflectionUtil.MethodInfo, ReflectionUtil.SaveMethod> getMethods() {
        return METHODS;
    }

    public static ConcurrentHashMap<ReflectionUtil.ConstructorInfo, ReflectionUtil.SaveConstructor<?>> getConstructors() {
        return CONSTRUCTORS;
    }

    public static ConcurrentHashMap<ReflectionUtil.JarInfo, Set<Class<?>>> getJars() {
        return JARS;
    }
}
