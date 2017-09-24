package de.alphahelix.alphalibary.addons.core;

import de.alphahelix.alphalibary.addons.core.exceptions.InvalidAddonException;
import org.apache.commons.lang.Validate;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class AddonClassLoader extends URLClassLoader {

    final Addon addon;
    final AddonDescriptionFile description;
    final File dataFolder;
    final File file;

    public AddonClassLoader(ClassLoader parent, AddonDescriptionFile description, File file, File dataFolder) throws InvalidAddonException, MalformedURLException {
        super(new URL[]{file.toURI().toURL()}, parent);

        this.description = description;
        this.dataFolder = dataFolder;
        this.file = file;

        try {

            Class<?> jarClass;
            try {
                jarClass = Class.forName(description.getMain(), true, this);
            } catch (ClassNotFoundException ex) {
                throw new InvalidAddonException("Cannot find main class '" + description.getMain() + "'", ex);
            }

            Class<? extends Addon> addonClazz;
            try {
                addonClazz = jarClass.asSubclass(Addon.class);
            } catch (ClassCastException ex) {
                throw new InvalidAddonException("main class '" + description.getMain() + "' does not extends Addon", ex);
            }

            this.addon = addonClazz.getDeclaredConstructor().newInstance();
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            throw new InvalidAddonException("No public constructor", ex);
        } catch (InstantiationException ex) {
            throw new InvalidAddonException("Abnormal addon type", ex);
        }

        this.initialize(this.addon);
    }

    private synchronized void initialize(Addon addon) {
        Validate.notNull(addon, "Initializing addon cannot be null");
        Validate.isTrue(addon.getClass().getClassLoader() == this, "Cannot initialize plugin outside of this class loader");
        if (this.addon.getDescription() != null) {
            throw new IllegalArgumentException("Addon already initialized!");
        } else {
            addon.init(this, this.dataFolder, this.description);
        }
    }

}
