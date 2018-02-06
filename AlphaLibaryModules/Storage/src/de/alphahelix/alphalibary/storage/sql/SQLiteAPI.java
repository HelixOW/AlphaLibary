package de.alphahelix.alphalibary.storage.sql;

import com.google.common.base.Objects;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * @see de.alphahelix.alphalibary.storage.sql2
 * @deprecated
 */
public class SQLiteAPI {

    private static final ArrayList<SQLiteAPI> SQ_LITE_APIS = new ArrayList<>();
    private static final HashMap<String, Connection> CONNECTIONS = new HashMap<>();

    private String databasePath;

    public SQLiteAPI(String databasePath) {

        this.databasePath = databasePath;

        if (getSQLLite(databasePath) == null) {
            SQ_LITE_APIS.add(this);
        }
    }

    public SQLiteAPI(JavaPlugin plugin, String databaseName) {
        this(plugin.getDataFolder().getAbsolutePath() + "/" + databaseName + ".db");
    }

    /**
     * Gets the corresponding {@link SQLiteAPI} for a database
     *
     * @param db the path to the .db file
     * @return the {@link SQLiteAPI} of this database
     */
    public static SQLiteAPI getSQLLite(String db) {
        for (SQLiteAPI api : SQ_LITE_APIS) {
            if (api.getDatabasePath().equals(db)) return api;
        }
        return null;
    }

    /**
     * Gets every {@link SQLiteAPI} connection to every database known inside they sql.yml
     *
     * @return a {@link ArrayList} with those {@link SQLiteAPI}s
     */
    public static ArrayList<SQLiteAPI> getSQLLiteDBs() {
        return SQ_LITE_APIS;
    }

    /**
     * Gets the {@link Connection} to the database
     *
     * @return the {@link Connection}
     */
    public Connection getSQLiteConnection() {
        if (isConnected()) {
            return CONNECTIONS.get(this.getDatabasePath());
        } else {
            try {
                closeSQLiteConnection();

                Class.forName("org.sqlite.JDBC");
                Connection c = DriverManager.getConnection(
                        "jdbc:sqlite:" + this.getDatabasePath());

                CONNECTIONS.put(this.getDatabasePath(), c);

                return c;
            } catch (SQLException | ClassNotFoundException ignore) {
                Bukkit.getLogger().log(Level.WARNING, "Failed to reconnect to " + this.getDatabasePath() + "! Check your sqllite.json inside AlphaLibary");
                return null;
            }
        }
    }

    /**
     * Checks if the plugin is connected to the database
     *
     * @return plugin is connected to the database
     */
    public boolean isConnected() {
        return CONNECTIONS.get(this.getDatabasePath()) != null;
    }

    /**
     * Creates a {@link Connection} to the database. Run this async inside your onEnable
     */
    public void initSQLiteAPI() {
        if (!isConnected()) {
            try {
                CONNECTIONS.put(this.getDatabasePath(), DriverManager.getConnection(
                        "jdbc:sqlite:" + this.getDatabasePath()));
            } catch (SQLException ignore) {
                Bukkit.getLogger().log(Level.WARNING, "Failed to reconnect to " + this.getDatabasePath() + "! Check your sqlite.json inside AlphaLibary");
            }
        }
    }

    /**
     * Closes the {@link Connection} to the database. Run this inside your onDisable
     *
     * @throws SQLException when connection wasn't able to be closed
     */
    public void closeSQLiteConnection() throws SQLException {
        if (isConnected()) {
            CONNECTIONS.get(this.getDatabasePath()).close();
            CONNECTIONS.remove(this.getDatabasePath());
        }
    }

    public String getDatabasePath() {
        return databasePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SQLiteAPI api = (SQLiteAPI) o;
        return Objects.equal(getDatabasePath(), api.getDatabasePath());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getDatabasePath());
    }

    @Override
    public String toString() {
        return "SQLiteAPI{" +
                "databasePath='" + databasePath + '\'' +
                '}';
    }

    public enum SQLiteDataType implements Serializable {
        NULL, INTEGER, REAL, TEXT, BLOB
    }
}