package de.alphahelix.alphalibary.addons.core;

import de.alphahelix.alphalibary.addons.core.exceptions.InvalidAddonException;
import org.apache.commons.lang.Validate;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Used to load the classes of the {@link Addon}
 *
 * @author AlphaHelix
 * @version 1.0
 * @see Addon
 * @since 1.9.2.1
 */
public class AddonClassLoader extends URLClassLoader {
	
	final AddonDescriptionFile description;
	final File dataFolder;
	final File file;
	Addon addon;
	
	public AddonClassLoader(ClassLoader parent, AddonDescriptionFile description, File file, File dataFolder) throws InvalidAddonException, MalformedURLException {
		super(new URL[]{file.toURI().toURL()}, parent);
		
		this.description = description;
		this.dataFolder = dataFolder;
		this.file = file;
		
		try {
			
			Class<?> jarClass;
			try {
				jarClass = Class.forName(description.getMain(), true, this);
			} catch(ClassNotFoundException ex) {
				throw new InvalidAddonException("Cannot find main class '" + description.getMain() + "'", ex);
			}
			
			Class<? extends Addon> addonClazz;
			try {
				addonClazz = jarClass.asSubclass(Addon.class);
			} catch(ClassCastException ex) {
				throw new InvalidAddonException("main class '" + description.getMain() + "' does not extends Addon", ex);
			}
			
			this.addon = addonClazz.getDeclaredConstructor().newInstance();
		} catch(IllegalAccessException ex) {
			throw new InvalidAddonException("No public constructor", ex);
		} catch(InstantiationException ex) {
			throw new InvalidAddonException("Abnormal addon type", ex);
		} catch(NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		this.initialize(this.addon);
	}
	
	/**
	 * Initialize a {@link Addon}
	 *
	 * @param addon the {@link Addon} to init
	 */
	private synchronized void initialize(Addon addon) {
		Validate.notNull(addon, "Initializing addon cannot be null");
		Validate.isTrue(addon.getClass().getClassLoader() == this, "Cannot initialize plugin outside of this class loader");
		if(this.addon.getDescription() != null) {
			throw new IllegalArgumentException("Addon already initialized!");
		} else {
			addon.init(this, this.dataFolder, this.description);
		}
	}
	
}
