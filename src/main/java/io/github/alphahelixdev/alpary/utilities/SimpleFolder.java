package io.github.alphahelixdev.alpary.utilities;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SimpleFolder extends File {
	
	public SimpleFolder(JavaPlugin plugin, String child) {
		this(plugin.getDataFolder().getAbsolutePath(), child);
	}
	
	public SimpleFolder(String parent, String child) {
		super(parent, child);
		
		if(!this.exists())
			this.mkdirs();
	}
	
	public SimpleFolder(String pathname) {
		super(pathname);
		
		if(!this.exists())
			this.mkdirs();
	}
}
