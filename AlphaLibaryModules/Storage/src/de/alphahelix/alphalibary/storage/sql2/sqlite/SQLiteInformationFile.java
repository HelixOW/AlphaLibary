package de.alphahelix.alphalibary.storage.sql2.sqlite;

import de.alphahelix.alphalibary.storage.file2.SimpleFile;
import de.alphahelix.alphalibary.storage.sql2.SQLInformation;
import de.alphahelix.alphalibary.storage.sql2.SQLInformationFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

public class SQLiteInformationFile extends SQLInformationFile {
	
	public SQLiteInformationFile(JavaPlugin plugin) {
		super(plugin, "sqlite.json");
	}
	
	@Override
	public void init() {
		if(jsonContains("informations")) return;
		
		setDefault("informations", new ArrayList<>(Arrays.asList(
				new SQLInformation(
						"root", "localhost", "file:" + getPlugin().getDataFolder().getAbsolutePath() + "/database1.db", "password", 3306
				),
				new SQLInformation(
						"root", "localhost", "file:" + getPlugin().getDataFolder().getAbsolutePath() + "/database2.db", "password", 3306
				)
		)));
	}
	
	@Override
	public void setup() {
		for(SQLInformation information : getListValues("informations", SQLInformation[].class)) {
			try {
				new SimpleFile(new URI(information.getDatabaseName()));
			} catch(URISyntaxException ignore) {
			}
			new SQLiteConnector(getPlugin(), information);
		}
	}
}
