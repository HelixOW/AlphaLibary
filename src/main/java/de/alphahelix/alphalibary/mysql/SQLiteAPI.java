package de.alphahelix.alphalibary.mysql;

import de.alphahelix.alphalibary.file.SimpleJSONFile;
import org.bukkit.Bukkit;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public class SQLiteAPI {

    private static ArrayList<SQLiteAPI> SQLiteAPIS = new ArrayList<>();
    private static HashMap<String, Connection> cons = new HashMap<>();
    private static SQLiteFileManager fm = new SQLiteFileManager();

    static {
        fm.setupConnection();
    }

    private SQLiteInfo info;

    SQLiteAPI(SQLiteInfo info) {
        this.info = info;

        if (getSQLLite(info.getDatabasePath()) == null) {
            SQLiteAPIS.add(this);
        }
    }

    /**
     * Gets the corresponding {@link SQLiteAPI} for a database
     *
     * @param db the path to the .db file
     * @return the {@link SQLiteAPI} of this database
     */
    public static SQLiteAPI getSQLLite(String db) {
        for (SQLiteAPI api : SQLiteAPIS) {
            if (api.info.getDatabasePath().equals(db)) return api;
        }
        return null;
    }

    /**
     * Gets every {@link SQLiteAPI} connection to every database known inside they mysql.yml
     *
     * @return a {@link ArrayList} with those {@link SQLiteAPI}s
     */
    public static ArrayList<SQLiteAPI> getSQLLiteDBs() {
        return SQLiteAPIS;
    }

    /**
     * Gets the {@link Connection} to the database
     *
     * @return the {@link Connection}
     */
    public Connection getSQLiteConnection() {
        if (isConnected()) {
            return cons.get(info.getDatabasePath());
        } else {
            try {
                closeSQLiteConnection();

                Class.forName("org.sqlite.JDBC");
                Connection c = DriverManager.getConnection(
                        "jdbc:sqlite:" + info.getDatabasePath());

                cons.put(info.getDatabasePath(), c);

                return c;
            } catch (SQLException | ClassNotFoundException ignore) {
                Bukkit.getLogger().log(Level.WARNING, "Failed to reconnect to " + info.getDatabasePath() + "! Check your sqllite.json inside AlphaLibary");
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
        return cons.get(info.getDatabasePath()) != null;
    }

    /**
     * Creates a {@link Connection} to the database. Run this async inside your onEnable
     */
    public void initSQLiteAPI() {
        if (!isConnected()) {
            try {
                cons.put(info.getDatabasePath(), DriverManager.getConnection(
                        "jdbc:sqlite:" + info.getDatabasePath()));
            } catch (SQLException ignore) {
                Bukkit.getLogger().log(Level.WARNING, "Failed to reconnect to " + info.getDatabasePath() + "! Check your sqlite.json inside AlphaLibary");
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
            cons.get(info.getDatabasePath()).close();
            cons.remove(info.getDatabasePath());
        }
    }

    public SQLiteInfo getInfo() {
        return info;
    }

    public enum SQLiteDataType implements Serializable {
        NULL, INTEGER, REAL, TEXT, BLOB
    }

    public static class SQLiteInfo {

        private String databasePath;

        public SQLiteInfo(String databasePath) {
            this.databasePath = databasePath;
        }

        public String getDatabasePath() {
            return databasePath;
        }
    }
}

class SQLiteFileManager extends SimpleJSONFile {
    SQLiteFileManager() {
        super("plugins/AlphaLibary", "sqllite.json");
        addValues();
    }

    private void addValues() {
        if (jsonContains("databases")) return;

        addValuesToList("databases", new SQLiteAPI.SQLiteInfo("./test.db"));
    }

    void setupConnection() {
        for (SQLiteAPI.SQLiteInfo info : getListValues("databases", SQLiteAPI.SQLiteInfo[].class)) {
            new SQLiteAPI(info);
        }
    }
}