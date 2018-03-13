package de.alphahelix.alphalibary.storage.sql2.mysql;

import de.alphahelix.alphalibary.storage.sql2.SQLConnectionHandler;
import de.alphahelix.alphalibary.storage.sql2.SQLConnector;
import de.alphahelix.alphalibary.storage.sql2.SQLInformation;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class MySQLConnector implements SQLConnector {
	
	private final SQLConnectionHandler handler;
	
	protected MySQLConnector(JavaPlugin plugin, SQLInformation information) {
		this.handler = new SQLConnectionHandler(plugin, information, this);
	}
	
	@Override
	public Connection connect() {
		if(handler.isConnected()) {
			return SQLConnectionHandler.getConnectionMap().get(handler.getInformation().getDatabaseName());
		} else {
			try {
				handler.disconnect();
				
				Connection c = DriverManager.getConnection(
						"jdbc:mysql://" + handler.getInformation().getHost() + ":" + handler.getInformation().getPort() + "/" + handler.getInformation().getDatabaseName() + "?autoReconnect=true", handler.getInformation().getUserName(), handler.getInformation().getPassword());
				
				SQLConnectionHandler.getConnectionMap().put(handler.getInformation().getDatabaseName(), c);
				
				return c;
			} catch(SQLException ignore) {
				Bukkit.getLogger().log(Level.WARNING, "Failed to connect to " + handler.getInformation().getDatabaseName() + "! Check your mysql.yml inside the folder of " + handler.getPlugin().getName());
				return null;
			}
		}
	}
	
	@Override
	public SQLConnectionHandler handler() {
		return handler;
	}
}

