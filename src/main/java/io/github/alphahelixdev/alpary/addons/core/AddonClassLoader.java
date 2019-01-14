package io.github.alphahelixdev.alpary.addons.core;

import io.github.alphahelixdev.alpary.addons.core.exceptions.InvalidAddonException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.Validate;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.function.Supplier;
import java.util.jar.JarFile;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AddonClassLoader extends URLClassLoader {
	
	private final File dataFolder;
	Addon addon;
	
	public AddonClassLoader(ClassLoader parent, File file, File dataFolder) throws InvalidAddonException, MalformedURLException {
		super(new URL[]{file.toURI().toURL()}, parent);
		
		this.dataFolder = dataFolder;
		
		try {
			Class<?> mainClass;
			Addon.AddonInfo info;
			
			try {
				mainClass = findMain(file);
				info = mainClass.getAnnotation(Addon.AddonInfo.class);
			} catch(Throwable throwable) {
				throw new InvalidAddonException("Can't find any main class");
			}
			
			this.addon = (Addon) mainClass.getDeclaredConstructor(Addon.AddonInfo.class).newInstance(info);
		} catch(IllegalAccessException ex) {
			throw new InvalidAddonException("No public constructor", ex);
		} catch(InstantiationException ex) {
			throw new InvalidAddonException("Abnormal addon type", ex);
		} catch(NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		this.initialize(this.addon);
	}
	
	private Class<? extends Addon> findMain(File jarFile) throws Throwable {
		JarFile jf = new JarFile(jarFile);
		
		Class<? extends Addon> cls = (Class<? extends Addon>) jf.stream().filter(jarEntry -> jarEntry.getName().endsWith(".class"))
				.map(jarEntry -> jarEntry.getName().replace(".class", "").replaceAll("/", "\\."))
				.map(className -> {
					try {
						return loadClass(className);
					} catch(ClassNotFoundException e) {
						e.printStackTrace();
						return null;
					}
				}).filter(loadedClass -> Addon.class.isAssignableFrom(loadedClass))
				.findFirst().orElseThrow((Supplier<Throwable>) () -> new IOException());
		
		jf.close();
		
		return cls;
	}
	
	/**
	 * Initialize a {@link Addon}
	 *
	 * @param addon the {@link Addon} to init
	 */
	private synchronized void initialize(Addon addon) {
		Validate.notNull(addon, "Initializing addon cannot be null");
		Validate.isTrue(addon.getClass().getClassLoader() == this, "Cannot initialize plugin outside of this class loader");
		if(this.addon.isLoaded()) {
			throw new IllegalArgumentException("Addon already initialized!");
		} else {
			addon.init(this, this.dataFolder);
		}
	}
}
