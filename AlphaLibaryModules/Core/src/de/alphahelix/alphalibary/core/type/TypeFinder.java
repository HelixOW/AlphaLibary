package de.alphahelix.alphalibary.core.type;

import de.alphahelix.alphalibary.core.AlphaLibary;
import de.alphahelix.alphalibary.core.utils.ScheduleUtil;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class TypeFinder {
	
	public static void findClassesAnnotatedWith(Class<? extends Annotation> annotation, Consumer<Set<Class<?>>> consumer) {
		getClasses(classes -> {
			Set<Class<?>> clazzSet = new HashSet<>();
			
			for(Class<?> clazz : classes) {
				if(clazz.isAnnotationPresent(annotation) && !annotation.equals(clazz))
					clazzSet.add(clazz);
			}
			
			consumer.accept(clazzSet);
		});
	}
	
	public static void getClasses(Consumer<Set<Class<?>>> consumer) {
		File[] plugins = new File(".", "plugins").listFiles();
		
		if(plugins != null) {
			for(File jars : plugins) {
				if(jars.getName().endsWith(".jar")) {
					TypeFinder.getClasses(jars, consumer);
				}
			}
		}
	}
	
	public static void getClasses(File jarFile, Consumer<Set<Class<?>>> consumer) {
		Set<Class<?>> classes = new HashSet<>();
		
		ScheduleUtil.runLater(1, true, () -> {
			try {
				JarFile file = new JarFile(jarFile);
				
				for(Enumeration<JarEntry> entries = file.entries(); entries.hasMoreElements(); ) {
					JarEntry entry = entries.nextElement();
					String jarName = entry.getName().replace('/', '.');
					
					if(jarName.endsWith(".class")) {
						String clName = jarName.substring(0, jarName.length() - 6);
						
						classes.add(AlphaLibary.class.getClassLoader().loadClass(clName));
						
						//						classes.add(Class.forName(clName, true, AlphaLibary.class.getClassLoader()));
					}
				}
				file.close();
			} catch(IOException | ReflectiveOperationException ex) {
				Bukkit.getLogger().severe("Error ocurred at getting classes, log: " + ex);
				ex.printStackTrace();
			}
			
			consumer.accept(classes);
		});
	}
	
	public static void findClassesImplementing(Class<?> interfac, Consumer<Set<Class<?>>> consumer) {
		getClasses(classes -> {
			Set<Class<?>> clazzSet = new HashSet<>();
			
			for(Class<?> clazz : classes) {
				if(interfac.isAssignableFrom(clazz) && !interfac.equals(clazz))
					clazzSet.add(clazz);
			}
			
			consumer.accept(clazzSet);
		});
	}
}
