package de.alphahelix.alphalibary.storage.sql2.mysql;

import de.alphahelix.alphalibary.storage.sql2.SQLInformation;
import de.alphahelix.alphalibary.storage.sql2.SQLInformationFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;

public class MySQLInformationFile extends SQLInformationFile {
	
	public MySQLInformationFile(JavaPlugin plugin) {
		super(plugin, "mysql.json");
	}
	
	public void init() {
		if(jsonContains("informations")) return;
		
		setDefault("informations", new ArrayList<>(Arrays.asList(
				new SQLInformation(
						"root", "localhost", "database", "password", 3306
				),
				new SQLInformation(
						"root", "localhost", "database2", "password", 3306
				)
		)));
	}
	
	public void setup() {
		for(SQLInformation information : getListValues("informations", SQLInformation[].class)) {
			new MySQLConnector(getPlugin(), information);
		}
	}
}
