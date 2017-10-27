package de.alphahelix.alphalibary.storage.sql2;

import de.alphahelix.alphalibary.storage.file.SimpleJSONFile;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class SQLInformationFile extends SimpleJSONFile {

    private final JavaPlugin plugin;

    public SQLInformationFile(JavaPlugin plugin, String child) {
        super(plugin, child);
        this.plugin = plugin;
        init();
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public abstract void init();

    public abstract void setup();
}
