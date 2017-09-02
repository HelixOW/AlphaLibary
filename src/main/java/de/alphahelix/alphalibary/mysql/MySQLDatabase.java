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
import de.alphahelix.alphalibary.uuid.UUIDFetcher;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class MySQLDatabase implements Serializable {

    // Tablename -> Columns
    private static transient final HashMap<String, String> TABLEINFO = new HashMap<>();
    private static transient final ArrayList<String> TABLENAMES = new ArrayList<>();

    private String table;
    private String database;

    /**
     * Creates a new manager for the table
     *
     * @param table    the name of the table you want to manage
     * @param database the database where the table is in
     */
    public MySQLDatabase(String table, String database) {
        this.table = table;
        this.database = database;
    }

    /**
     * Create a new table inside the database
     *
     * @param columns use MySQLDatabase.createColumn()
     */
    public void create(String... columns) {
        String tableinfo;
        if (columns.length > 1) {
            StringBuilder builder = new StringBuilder();
            for (String str : columns) {
                builder.append(", ").append(str);
            }
            tableinfo = builder.toString();
        } else {
            tableinfo = columns[0];
        }

        if (tableinfo == null)
            return;

        tableinfo = tableinfo.replaceFirst(", ", "");
        if (!TABLENAMES.contains(table))
            TABLENAMES.add(table);

        if (!TABLEINFO.containsKey(table))
            TABLEINFO.put(table, tableinfo);

        if (MySQLAPI.getMySQL(database) != null) {
            if (MySQLAPI.getMySQL(database).isConnected()) {
                try {
                    String qry = "CREATE TABLE IF NOT EXISTS " + table + " (" + tableinfo + ")";
                    PreparedStatement prepstate = MySQLAPI.getMySQL(database).getMySQLConnection().prepareStatement(qry);
                    prepstate.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Removes an entry from the table
     *
     * @param condition the column of where the value is in
     * @param value     the value to define which entry should be removed
     */
    public void remove(String condition, String value) {
        if (MySQLAPI.getMySQL(database) != null) {
            if (MySQLAPI.getMySQL(database).isConnected()) {
                try {
                    String qry = "DELETE FROM " + table + " WHERE(" + condition + "='" + value + "')";
                    PreparedStatement prepstate = MySQLAPI.getMySQL(database).getMySQLConnection().prepareStatement(qry);
                    prepstate.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Checks if the UUID of a {@link Player} is inside the table
     *
     * @param p the {@link Player} to check
     * @return if the {@link Player} is inside the table
     */
    public boolean containsPlayer(Player p) {
        final UUID[] id = new UUID[1];

        UUIDFetcher.getUUID(p, id1 ->
                id[0] = id1
        );

        return getResult("uuid", id[0].toString(), "uuid") != null;
    }

    /**
     * Checks if the UUID of a {@link Player} is inside the table
     *
     * @param playername the name of the {@link Player} to check
     * @return if the {@link Player} is inside the table
     */
    public boolean containsPlayer(String playername) {
        final UUID[] id = new UUID[1];

        UUIDFetcher.getUUID(playername, id1 ->
                id[0] = id1
        );

        return getResult("uuid", id[0].toString(), "uuid") != null;
    }

    /**
     * Checks if the UUID of a {@link Player} is inside the table
     *
     * @param id the UUID of the {@link Player} to check
     * @return if the {@link Player} is inside the table
     */
    public boolean containsPlayer(UUID id) {
        return getResult("uuid", id.toString(), "uuid") != null;
    }

    /**
     * Checks if the condition is inside the table
     *
     * @param condition the column to check for
     * @param value     the value of the column
     * @return if the table contains it
     */
    public boolean contains(String condition, String value) {
        return getResult(condition, value, condition) != null;
    }

    /**
     * Insert values into the table
     *
     * @param values the values to insert in the correct order.
     */
    public void insert(String... values) {
        StringBuilder builder = new StringBuilder();
        for (String str : values) {
            builder.append(", '").append(str).append("'");
        }

        if (MySQLAPI.getMySQL(database) != null) {
            if (MySQLAPI.getMySQL(database).isConnected()) {
                try {
                    String qry = "INSERT INTO " + table + " VALUES (" + builder.toString().replaceFirst(", ", "") + ")";
                    PreparedStatement prepstate = MySQLAPI.getMySQL(database).getMySQLConnection().prepareStatement(qry);
                    prepstate.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Order a column by a selection in ascending order
     * QRY: SELECT 'columnToOrder' FROM 'table' ORDER BY 'orderBy' asc
     *
     * @param columnToOrder the selection to order
     * @param orderBy       the column to order it
     * @return the executed query
     */
    public ResultSet orderAscending(String columnToOrder, String orderBy) {
        if (MySQLAPI.getMySQL(database) != null) {
            if (MySQLAPI.getMySQL(database).isConnected()) {
                try {
                    String qry = "SELECT " + columnToOrder + " FROM " + table + " ORDER BY " + orderBy + " asc";
                    PreparedStatement prepstate = MySQLAPI.getMySQL(database).getMySQLConnection().prepareStatement(qry);
                    return prepstate.executeQuery();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Order a column by a selection in ascending order with a specified limit
     * QRY: SELECT 'columnToOrder' FROM 'table' ORDER BY 'orderBy' asc LIMIT 'limit'
     *
     * @param columnToOrder the selection to order
     * @param orderBy       the column to order it
     * @param limit         the limit of the list
     * @return the executed query
     */
    public ResultSet orderLimitAscending(String columnToOrder, String orderBy, long limit) {
        if (MySQLAPI.getMySQL(database) != null) {
            if (MySQLAPI.getMySQL(database).isConnected()) {
                try {
                    String qry = "SELECT " + columnToOrder + " FROM " + table + " ORDER BY " + orderBy + " asc LIMIT " + limit;
                    PreparedStatement prepstate = MySQLAPI.getMySQL(database).getMySQLConnection().prepareStatement(qry);
                    return prepstate.executeQuery();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Order a column by a selection in descending order
     * QRY: SELECT 'columnToOrder' FROM 'table' ORDER BY 'orderBy' desc
     *
     * @param columnToOrder the selection to order
     * @param orderBy       the column to order it
     * @return the executed query
     */
    public ResultSet orderDescending(String columnToOrder, String orderBy) {
        if (MySQLAPI.getMySQL(database) != null) {
            if (MySQLAPI.getMySQL(database).isConnected()) {
                try {
                    String qry = "SELECT " + columnToOrder + " FROM " + table + " ORDER BY " + orderBy + " desc";
                    PreparedStatement prepstate = MySQLAPI.getMySQL(database).getMySQLConnection().prepareStatement(qry);
                    return prepstate.executeQuery();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Order a column by a selection in descending order with a specified limit
     * QRY: SELECT 'columnToOrder' FROM 'table' ORDER BY 'orderBy' desc LIMIT 'limit'
     *
     * @param columnToOrder the selection to order
     * @param orderBy       the column to order it
     * @param limit         the limit of the list
     * @return the executed query
     */
    public ResultSet orderLimitDescending(String columnToOrder, String orderBy, long limit) {
        if (MySQLAPI.getMySQL(database) != null) {
            if (MySQLAPI.getMySQL(database).isConnected()) {
                try {
                    String qry = "SELECT " + columnToOrder + " FROM " + table + " ORDER BY " + orderBy + " desc LIMIT " + limit;
                    PreparedStatement prepstate = MySQLAPI.getMySQL(database).getMySQLConnection().prepareStatement(qry);
                    return prepstate.executeQuery();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Updates a value inside the table
     *
     * @param uuid        the UUID column where to identify where to update
     * @param column      the column which you want to update
     * @param updatevalue the new value
     */
    public void update(UUID uuid, String column, String updatevalue) {
        if (MySQLAPI.getMySQL(database) != null) {
            if (MySQLAPI.getMySQL(database).isConnected()) {
                try {
                    String qry = "UPDATE " + table + " SET " + column + "=? WHERE " + "uuid" + "=?";
                    PreparedStatement prepstate = MySQLAPI.getMySQL(database).getMySQLConnection().prepareStatement(qry);
                    prepstate.setString(1, updatevalue);
                    prepstate.setString(2, uuid.toString());
                    prepstate.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Updates a value inside the table
     *
     * @param condition      the column to identify the row where the table should be updated
     * @param conditionValue the value of the column
     * @param column         the column which you want to update
     * @param updatevalue    the new value
     */
    public void update(String condition, String conditionValue, String column, String updatevalue) {
        if (MySQLAPI.getMySQL(database) != null) {
            if (MySQLAPI.getMySQL(database).isConnected()) {
                try {
                    String qry = "UPDATE " + table + " SET " + column + "=? WHERE " + condition + "=?";
                    PreparedStatement prepstate = MySQLAPI.getMySQL(database).getMySQLConnection().prepareStatement(qry);
                    prepstate.setString(1, updatevalue);
                    prepstate.setString(2, conditionValue);
                    prepstate.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Adds a new Column inside the table.
     * USE ONLY INSIDE create method.
     *
     * @param name the name of the column
     * @param type the {@link MySQLAPI.MySQLDataType} of the column
     * @param size the size of the column
     * @return the mysql query string to create a column
     */
    public String createColumn(String name, MySQLAPI.MySQLDataType type, long size, String... arg) {
        return name + " " + type.name() + "(" + size + ") " + Arrays.toString(arg).replace(", ", " ").replace("[", "").replace("]", "");
    }

    /**
     * Gets the amount of columns inside the table
     *
     * @return the amount of columns inside the table
     */
    public int getColumnAmount() {
        if (TABLEINFO.get(table) == null) {
            return 0;
        }
        if (!TABLEINFO.get(table).contains(", ")) {
            return 1;
        }
        String[] info = TABLEINFO.get(table).split(", ");
        return info.length;
    }

    /**
     * Gets the column name at a specific place
     *
     * @param column the id of the column
     * @return the column name
     */
    public String getColumnName(int column) {
        if (TABLEINFO.get(table) == null) {
            return null;
        }
        String[] info = TABLEINFO.get(table).split(", ");

        return info[column - 1].split(" ")[0];
    }

    /**
     * Returns a {@link ArrayList} with all values from one coloumn
     *
     * @param column the colum to get it's values from
     * @return an {@link ArrayList} with all values
     */
    public ArrayList<String> getList(String column) {
        ArrayList<String> list = new ArrayList<>();

        if (MySQLAPI.getMySQL(database) != null) {
            if (MySQLAPI.getMySQL(database).isConnected()) {

                try {
                    ResultSet rs = MySQLAPI.getMySQL(database).getMySQLConnection().prepareStatement("SELECT " + column + " FROM " + table).executeQuery();

                    if (rs == null) {
                        return new ArrayList<>();
                    }

                    while (rs.next()) {
                        if (!rs.getString(column).contains(", ")) {
                            list.add(rs.getString(column).replace("[", "").replace("]", ""));
                        } else {
                            String[] strlist = rs.getString(column).split(", ");
                            for (String aStrlist : strlist) {
                                list.add(aStrlist.replace("[", "").replace("]", ""));
                            }
                        }
                    }

                    return list;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * Returns a {@link ArrayList} with all values from one coloumn with limited entries
     *
     * @param column the colum to get it's values from
     * @param limit  the limit of the entries
     * @return an {@link ArrayList} with all values
     */
    public ArrayList<String> getLimitedList(String column, long limit) {
        ArrayList<String> list = new ArrayList<>();

        if (MySQLAPI.getMySQL(database) != null) {
            if (MySQLAPI.getMySQL(database).isConnected()) {
                try {
                    ResultSet rs = MySQLAPI.getMySQL(database).getMySQLConnection().prepareStatement("SELECT " + column + " FROM " + table + " LIMIT " + limit).executeQuery();

                    if (rs == null) {
                        return new ArrayList<>();
                    }

                    while (rs.next()) {
                        if (!rs.getString(column).contains(", ")) {
                            list.add(rs.getString(column).replace("[", "").replace("]", ""));
                        } else {
                            String[] strlist = rs.getString(column).split(", ");
                            for (String aStrlist : strlist) {
                                list.add(aStrlist.replace("[", "").replace("]", ""));
                            }
                        }
                    }

                    return list;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public ArrayList<String> getAscendingOrderdList(String columnToOrder, String orderBy) {
        ArrayList<String> list = new ArrayList<>();

        try {
            ResultSet rs = orderAscending(columnToOrder, orderBy);

            if (rs == null) {
                return new ArrayList<>();
            }

            while (rs.next()) {
                if (!rs.getString(columnToOrder).contains(", ")) {
                    list.add(rs.getString(columnToOrder).replace("[", "").replace("]", ""));
                } else {
                    String[] strlist = rs.getString(columnToOrder).split(", ");
                    for (String aStrlist : strlist) {
                        list.add(aStrlist.replace("[", "").replace("]", ""));
                    }
                }
            }

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<String> getDescendingOrderdList(String columnToOrder, String orderBy) {
        ArrayList<String> list = new ArrayList<>();

        try {
            ResultSet rs = orderDescending(columnToOrder, orderBy);

            if (rs == null) {
                return new ArrayList<>();
            }

            while (rs.next()) {
                if (!rs.getString(columnToOrder).contains(", ")) {
                    list.add(rs.getString(columnToOrder).replace("[", "").replace("]", ""));
                } else {
                    String[] strlist = rs.getString(columnToOrder).split(", ");
                    for (String aStrlist : strlist) {
                        list.add(aStrlist.replace("[", "").replace("]", ""));
                    }
                }
            }

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<String> getLimitedAscendingOrderdList(String columnToOrder, String orderBy, long limit) {
        ArrayList<String> list = new ArrayList<>();

        try {
            ResultSet rs = orderLimitAscending(columnToOrder, orderBy, limit);

            if (rs == null) {
                return new ArrayList<>();
            }

            while (rs.next()) {
                if (!rs.getString(columnToOrder).contains(", ")) {
                    list.add(rs.getString(columnToOrder).replace("[", "").replace("]", ""));
                } else {
                    String[] strlist = rs.getString(columnToOrder).split(", ");
                    for (String aStrlist : strlist) {
                        list.add(aStrlist.replace("[", "").replace("]", ""));
                    }
                }
            }

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<String> getLimitedDescendingOrderdList(String columnToOrder, String orderBy, long limit) {
        ArrayList<String> list = new ArrayList<>();

        try {
            ResultSet rs = orderLimitDescending(columnToOrder, orderBy, limit);

            if (rs == null) {
                return new ArrayList<>();
            }

            while (rs.next()) {
                if (!rs.getString(columnToOrder).contains(", ")) {
                    list.add(rs.getString(columnToOrder).replace("[", "").replace("]", ""));
                } else {
                    String[] strlist = rs.getString(columnToOrder).split(", ");
                    for (String aStrlist : strlist) {
                        list.add(aStrlist.replace("[", "").replace("]", ""));
                    }
                }
            }

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Gets a value from the table
     * <p/>
     * <p/>
     * example:
     * getResult("UUID", UUIDFetcher.getUUID("AlphaHelix"), "Kills") returns the amount of kills of AlphaHelix
     *
     * @param condition the condition to identify where to get its value from
     * @param value     the value of the condition
     * @param column    the column to get
     * @return the Object which was saved inside the table
     */
    public Object getResult(String condition, String value, String column) {
        if (MySQLAPI.getMySQL(database) != null) {
            if (MySQLAPI.getMySQL(database).isConnected()) {
                try {
                    String qry = "SELECT * FROM " + table + " WHERE (" + condition + "=?)";
                    PreparedStatement prepstate = MySQLAPI.getMySQL(database).getMySQLConnection().prepareStatement(qry);
                    prepstate.setString(1, value);
                    ResultSet rs = prepstate.executeQuery();

                    if (rs == null) {
                        return null;
                    }

                    if (rs.next()) {
                        return rs.getObject(column);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Performs a custom query
     *
     * @param qry the query to perform
     * @return the executed Query
     */
    public ResultSet customResult(String qry) {
        if (MySQLAPI.getMySQL(database) != null) {
            if (MySQLAPI.getMySQL(database).isConnected()) {
                try {
                    PreparedStatement prepstate = MySQLAPI.getMySQL(database).getMySQLConnection().prepareStatement(qry);
                    return prepstate.executeQuery();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MySQLDatabase that = (MySQLDatabase) o;
        return Objects.equal(table, that.table) &&
                Objects.equal(database, that.database);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(table, database);
    }

    @Override
    public String toString() {
        return "MySQLDatabase{" +
                "table='" + table + '\'' +
                ", database='" + database + '\'' +
                '}';
    }
}
