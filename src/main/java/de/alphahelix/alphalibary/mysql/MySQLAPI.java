/*
 * Copyright (C) <2017>  <AlphaHelixDev>
 *
 *       This program is free software: you can redistribute it under the
 *       terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.alphahelix.alphalibary.mysql;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.file.SimpleFile;
import org.bukkit.Bukkit;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public class MySQLAPI implements Serializable {

    private static final ArrayList<MySQLAPI> MY_SQLAPIS = new ArrayList<>();
    private static final HashMap<String, Connection> CONNECTIONS = new HashMap<>();
    private static final MySQLFileManager FILE_MANAGER = new MySQLFileManager();

    static {
        FILE_MANAGER.setupConnection();
    }

    private String username;
    private String password;
    private String database;
    private String host;
    private String port;

    MySQLAPI(String username, String password, String database, String host, String port) {
        this.username = username;
        this.password = password;
        this.database = database;
        this.host = host;
        this.port = port;

        if (getMySQL(database) == null) {
            MY_SQLAPIS.add(this);
        }
    }

    /**
     * Gets the corresponding {@link MySQLAPI} for a database
     *
     * @param db the name of the database
     * @return the {@link MySQLAPI} of this database
     */
    public static MySQLAPI getMySQL(String db) {
        for (MySQLAPI api : MY_SQLAPIS) {
            if (api.getDatabase().equals(db)) return api;
        }
        return null;
    }

    /**
     * Gets every {@link MySQLAPI} connection to every database known inside they mysql.yml
     *
     * @return a {@link ArrayList} with those {@link MySQLAPI}s
     */
    public static ArrayList<MySQLAPI> getMysqlDBs() {
        return MY_SQLAPIS;
    }

    /**
     * Gets the {@link Connection} to the database
     *
     * @return the {@link Connection}
     */
    public Connection getMySQLConnection() {
        if (isConnected()) {
            return CONNECTIONS.get(database);
        } else {
            try {
                Connection c = DriverManager.getConnection(
                        "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);

                CONNECTIONS.put(database, c);

                return c;
            } catch (SQLException ignore) {
                Bukkit.getLogger().log(Level.WARNING, "Failed to reconnect to " + database + "! Check your mysql.yml inside AlphaGameLibary");
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
        return CONNECTIONS.containsKey(database) && CONNECTIONS.get(database) != null;
    }

    /**
     * Creates a {@link Connection} to the database. Run this async inside your onEnable
     */
    public void initMySQLAPI() {
        if (!isConnected()) {
            try {
                CONNECTIONS.put(database, DriverManager.getConnection(
                        "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password));


            } catch (SQLException ignore) {
                Bukkit.getLogger().log(Level.WARNING, "Failed to reconnect to " + database + "! Check your mysql.yml inside AlphaGameLibary");
            }
        }
    }

    /**
     * Closes the {@link Connection} to the database. Run this inside your onDisable
     *
     * @throws SQLException when connection wasn't able to be closed
     */
    public void closeMySQLConnection() throws SQLException {
        if (isConnected()) {
            CONNECTIONS.get(database).close();
            CONNECTIONS.remove(database);
        }
    }

    /**
     * Gets the name of the connected database
     *
     * @return the name of the connected database
     */
    public String getDatabase() {
        return database;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MySQLAPI mySQLAPI = (MySQLAPI) o;
        return Objects.equal(username, mySQLAPI.username) &&
                Objects.equal(password, mySQLAPI.password) &&
                Objects.equal(getDatabase(), mySQLAPI.getDatabase()) &&
                Objects.equal(host, mySQLAPI.host) &&
                Objects.equal(port, mySQLAPI.port);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username, password, getDatabase(), host, port);
    }

    @Override
    public String toString() {
        return "MySQLAPI{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", database='" + database + '\'' +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                '}';
    }

    public enum MySQLDataType implements Serializable {
        INT, TINYINT, SMALLINT, MEDIUMINT, BIGINT, FLOAT, DOUBLE, DECIMAL, DATE, DATETIME, TIMESTAMP, TIME, YEAR,
        CHAR, VARCHAR, BLOB, TEXT, TINYBLOB, TINYTEXT, MEDIUMBLOB, MEDIUMTEXT, LONGBLOB, LONGTEXT, ENUM
    }
}

class MySQLFileManager extends SimpleFile {

    MySQLFileManager() {
        super("plugins/AlphaLibary", "mysql.yml");
        addValues();
    }

    public void addValues() {
        if (getKeys(false).size() > 0) return;
        setDefault("database.username", "root");
        setDefault("database.password", "password");
        setDefault("database.host", "localhost");
        setDefault("database.port", "3306");
    }

    void setupConnection() {
        for (String dbName : getKeys(false)) {
            new MySQLAPI(
                    getString(dbName + ".username"),
                    getString(dbName + ".password"),
                    dbName,
                    getString(dbName + ".host"),
                    getString(dbName + ".port")
            );
        }
    }
}
