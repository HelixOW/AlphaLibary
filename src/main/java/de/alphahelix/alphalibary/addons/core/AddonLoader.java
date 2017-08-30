package de.alphahelix.alphalibary.addons.core;

import de.alphahelix.alphalibary.addons.AddonCore;
import de.alphahelix.alphalibary.addons.core.exceptions.InvalidAddonDescriptionException;
import de.alphahelix.alphalibary.addons.core.exceptions.InvalidAddonException;
import org.apache.commons.lang.Validate;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AddonLoader {

    private static Map<String, AddonClassLoader> loaders = new HashMap<>();

    public static AddonDescriptionFile getAddonDescriptionFile(File file) throws InvalidAddonDescriptionException {
        Validate.notNull(file, "FileHelp cannot be null");

        JarFile jar = null;
        InputStream stream = null;

        try {
            jar = new JarFile(file);
            JarEntry entry = jar.getJarEntry("addon.yml");

            if (entry == null) {
                throw new InvalidAddonDescriptionException(new FileNotFoundException("Jar does not contain addon.yml"));
            }

            stream = jar.getInputStream(entry);

            return new AddonDescriptionFile(stream);

        } catch (IOException | YAMLException ex) {
            throw new InvalidAddonDescriptionException(ex);
        } finally {
            if (jar != null) {
                try {
                    jar.close();
                } catch (IOException ignored) {
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public static Addon loadAddon(File file) throws InvalidAddonException {
        Validate.notNull(file, "FileHelp cannot be null");

        if (!file.exists())
            throw new InvalidAddonException(new FileNotFoundException((new StringBuilder(String.valueOf(file.getPath()))).append(
                    " does not exist").toString()));

        AddonDescriptionFile description;
        try {
            description = getAddonDescriptionFile(file);
        } catch (InvalidAddonDescriptionException e) {
            throw new InvalidAddonException(e);
        }

        File dataFolder = new File(AddonCore.getAddonFolder() + File.separator + description.getName());

        if (!dataFolder.exists())
            dataFolder.mkdir();

        AddonClassLoader loader;

        try {
            loader = new AddonClassLoader(AddonLoader.class.getClassLoader(), description, file, dataFolder);
        } catch (MalformedURLException e) {
            throw new InvalidAddonException(e);
        }

        loaders.put(description.getName(), loader);

        return loader.addon;
    }
}
