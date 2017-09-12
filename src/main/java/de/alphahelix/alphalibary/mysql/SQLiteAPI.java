package de.alphahelix.alphalibary.mysql;

import com.google.common.base.Objects;
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

    private static final ArrayList<SQLiteAPI> SQ_LITE_APIS = new ArrayList<>();
    private static final HashMap<String, Connection> CONNECTIONS = new HashMap<>();
    private static final SQLiteFileManager FILE_MANAGER = new SQLiteFileManager();

    static {
        FILE_MANAGER.setupConnection();
    }

    private SQLiteInfo info;

    SQLiteAPI(SQLiteInfo info) {
        this.info = info;

        if (getSQLLite(info.getDatabasePath()) == null) {
            SQ_LITE_APIS.add(this);
        }
    }

    /**
     * Gets the corresponding {@link SQLiteAPI} for a database
     *
     * @param db the path to the .db file
     * @return the {@link SQLiteAPI} of this database
     */
    public static SQLiteAPI getSQLLite(String db) {
        for (SQLiteAPI api : SQ_LITE_APIS) {
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
        return SQ_LITE_APIS;
    }

    /**
     * Gets the {@link Connection} to the database
     *
     * @return the {@link Connection}
     */
    public Connection getSQLiteConnection() {
        if (isConnected()) {
            return CONNECTIONS.get(info.getDatabasePath());
        } else {
            try {
                closeSQLiteConnection();

                Class.forName("org.sqlite.JDBC");
                Connection c = DriverManager.getConnection(
                        "jdbc:sqlite:" + info.getDatabasePath());

                CONNECTIONS.put(info.getDatabasePath(), c);

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
        return CONNECTIONS.get(info.getDatabasePath()) != null;
    }

    /**
     * Creates a {@link Connection} to the database. Run this async inside your onEnable
     */
    public void initSQLiteAPI() {
        if (!isConnected()) {
            try {
                CONNECTIONS.put(info.getDatabasePath(), DriverManager.getConnection(
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
            CONNECTIONS.get(info.getDatabasePath()).close();
            CONNECTIONS.remove(info.getDatabasePath());
        }
    }

    public SQLiteInfo getInfo() {
        return info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SQLiteAPI sqLiteAPI = (SQLiteAPI) o;
        return Objects.equal(getInfo(), sqLiteAPI.getInfo());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getInfo());
    }

    @Override
    public String toString() {
        return "SQLiteAPI{" +
                "info=" + info +
                '}';
    }

    public enum SQLiteDataType implements Serializable {
        NULL, INTEGER, REAL, TEXT, BLOB
    }

    public static class SQLiteInfo {

        private final String databasePath;

        SQLiteInfo(String databasePath) {
            this.databasePath = databasePath;
        }

        String getDatabasePath() {
            return databasePath;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SQLiteInfo that = (SQLiteInfo) o;
            return Objects.equal(getDatabasePath(), that.getDatabasePath());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(getDatabasePath());
        }

        @Override
        public String toString() {
            return "SQLiteInfo{" +
                    "databasePath='" + databasePath + '\'' +
                    '}';
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