package de.alphahelix.alphalibary.addons.core;

import java.io.File;

public abstract class Addon {

    private File dataFolder;
    private AddonDescriptionFile description;
    private ClassLoader loader;

    final void init(AddonClassLoader classLoader, File dataFolder, AddonDescriptionFile description) {
        this.dataFolder = dataFolder;
        this.description = description;
        this.loader = classLoader;
        onEnable();
    }

    public File getDataFolder() {
        return dataFolder;
    }

    public AddonDescriptionFile getDescription() {
        return description;
    }

    public ClassLoader getLoader() {
        return loader;
    }

    public abstract void onEnable();
}
