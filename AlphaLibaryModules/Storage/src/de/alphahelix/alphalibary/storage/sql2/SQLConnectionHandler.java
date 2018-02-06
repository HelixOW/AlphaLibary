package de.alphahelix.alphalibary.storage.sql2;

import de.alphahelix.alphalibary.storage.file.AbstractFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class SQLConnectionHandler {

    private static final Map<String, SQLConnector> CONNECTORS = new HashMap<>();
    private static final Map<String, Connection> CONNECTION_MAP = new HashMap<>();
    private final SQLInformation information;
    private final JavaPlugin plugin;

    public SQLConnectionHandler(JavaPlugin plugin, SQLInformation information, SQLConnector connector) {
        this.information = information;
        this.plugin = plugin;

        if (information.getDatabaseName().contains(".")) {
            try {
                new AbstractFile(new URI(information.getDatabaseName()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        CONNECTORS.put(information.getDatabaseName(), connector);
    }

    public static void initialize(SQLInformationFile sqlInformationFile) {
        sqlInformationFile.setup();
    }

    public static Optional<SQLConnector> getSQLConnector(String databaseName) {
        return Optional.ofNullable(CONNECTORS.get(databaseName));
    }

    public static Collection<SQLConnector> getSQLConnectors() {
        return CONNECTORS.values();
    }

    public static Map<String, SQLConnector> getConnectors() {
        return CONNECTORS;
    }

    public static Map<String, Connection> getConnectionMap() {
        return CONNECTION_MAP;
    }

    public SQLInformation getInformation() {
        return information;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public boolean isConnected() {
        try {
            return CONNECTION_MAP.containsKey(information.getDatabaseName()) && CONNECTION_MAP.get(information.getDatabaseName()) != null && !CONNECTION_MAP.get(information.getDatabaseName()).isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean disconnect() {
        if (isConnected()) {
            try {
                getConnectionMap().get(getInformation().getDatabaseName()).close();
                getConnectionMap().remove(getInformation().getDatabaseName());
            } catch (SQLException e) {
                return false;
            }
            return true;
        }
        return false;
    }
}
