package de.alphahelix.alphalibary.storage.file;

import de.alphahelix.alphalibary.storage.file.serializers.StringSerializer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.net.URI;

public class SimpleTXTFile extends SimpleCSVFile<String> {
	public SimpleTXTFile(String parent, String child) {
		super(parent, child, new StringSerializer());
	}
	
	public SimpleTXTFile(File parent, String child) {
		super(parent, child, new StringSerializer());
	}
	
	public SimpleTXTFile(URI uri) {
		super(uri, new StringSerializer());
	}
	
	public SimpleTXTFile(JavaPlugin plugin, String child) {
		super(plugin, child, new StringSerializer());
	}
	
	public SimpleTXTFile(AbstractFile file) {
		super(file, new StringSerializer());
	}
}
