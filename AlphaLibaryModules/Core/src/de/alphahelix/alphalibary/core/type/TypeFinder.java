package de.alphahelix.alphalibary.core.type;

import de.alphahelix.alphalibary.core.AlphaLibary;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class TypeFinder {
	
	public static Set<Class<?>> findClassesAnnotatedWith(Class<? extends Annotation> annotation) {
		Set<Class<?>> clazzSet = new HashSet<>();
		
		for(Class<?> clazz : getClasses()) {
			if(clazz.isAnnotationPresent(annotation) && !annotation.equals(clazz))
				clazzSet.add(clazz);
		}
		
		return clazzSet;
	}
	
	public static Set<Class<?>> getClasses() {
		File[] plugins = new File(".", "plugins").listFiles();
		
		if(plugins != null) {
			for(File jars : plugins) {
				if(jars.getName().endsWith(".jar")) {
					return TypeFinder.getClasses(jars);
				}
			}
		}
		return new HashSet<>();
	}
	
	public static Set<Class<?>> getClasses(File jarFile) {
		Set<Class<?>> classes = new HashSet<>();
		
		try {
			JarFile file = new JarFile(jarFile);
			
			for(Enumeration<JarEntry> entries = file.entries(); entries.hasMoreElements(); ) {
				JarEntry entry = entries.nextElement();
				String jarName = entry.getName().replace('/', '.');
				
				if(jarName.endsWith(".class")) {
					String clName = jarName.substring(0, jarName.length() - 6);
					
					classes.add(AlphaLibary.class.getClassLoader().loadClass(clName));
				}
			}
			file.close();
		} catch(IOException | ReflectiveOperationException ex) {
			Bukkit.getLogger().severe("Error ocurred at getting classes, log: " + ex);
			ex.printStackTrace();
		}
		
		return classes;
	}
	
	public static Set<Class<?>> findClassesImplementing(Class<?> interfaze) {
		Set<Class<?>> clazzSet = new HashSet<>();
		
		for(Class<?> clazz : getClasses()) {
			if(interfaze.isAssignableFrom(clazz) && !interfaze.equals(clazz))
				clazzSet.add(clazz);
		}
		
		return clazzSet;
	}
}
