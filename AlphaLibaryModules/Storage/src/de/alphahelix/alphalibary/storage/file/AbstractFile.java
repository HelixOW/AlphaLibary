package de.alphahelix.alphalibary.storage.file;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class AbstractFile extends File {

    public AbstractFile(String parent, String child) {
        super(parent, child);
        if (!this.exists() && !isDirectory()) {
            try {
                getParentFile().mkdirs();
                createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public AbstractFile(File parent, String child) {
        this(parent.getAbsolutePath(), child);
    }

    public AbstractFile(URI uri) {
        super(uri);
        if (!this.exists() && !isDirectory()) {
            try {
                getParentFile().mkdirs();
                createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public AbstractFile(JavaPlugin plugin, String child) {
        this(plugin.getDataFolder().getAbsolutePath(), child);
    }

    public AbstractFile(AbstractFile file) {
        this(file.getParent(), file.getName());
    }
}
