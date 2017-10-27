package de.alphahelix.alphalibary.storage.sql;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.core.AlphaLibary;
import de.alphahelix.alphalibary.core.uuid.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("ALL")
public class SQLiteDatabase {

    private static transient final HashMap<String, String> TABLEINFO = new HashMap<>();
    private static transient final ArrayList<String> TABLENAMES = new ArrayList<>();

    private final String tableName;
    private final String databasePath;
    private final SQLiteAPI api;

    public SQLiteDatabase(String tableName, String databasePath) {
        this.tableName = tableName;
        this.databasePath = databasePath;
        this.api = SQLiteAPI.getSQLLite(databasePath);
    }

    /**
     * Adds a new Column inside the table.
     * USE ONLY INSIDE create method.
     *
     * @param name the name of the column
     * @param type the {@link MySQLAPI.MySQLDataType} of the column
     * @return the sql query string to create a column
     */
    public static String createColumn(String name, SQLiteAPI.SQLiteDataType type, String... arg) {
        return name + " " + type.name() + " " + Arrays.toString(arg).replace(", ", " ").replace("[", "").replace("]", "");
    }

    public void create(String... columns) {
        Bukkit.getScheduler().runTaskAsynchronously(AlphaLibary.getInstance(), () -> {
            String tableinfo;

            if (columns.length > 1) {
                StringBuilder builder = new StringBuilder();
                for (String str : columns) {
                    builder.append(",").append(str);
                }
                tableinfo = builder.toString();
            } else {
                tableinfo = columns[0];
            }

            if (tableinfo == null)
                return;

            tableinfo = tableinfo.replaceFirst(",", "");
            if (!TABLENAMES.contains(tableName))
                TABLENAMES.add(tableName);

            if (!TABLEINFO.containsKey(tableName))
                TABLEINFO.put(tableName, tableinfo);

            if (api != null) {
                if (api.isConnected()) {
                    try {
                        String qry = "CREATE TABLE IF NOT EXISTS " + tableName + "(" + tableinfo + ");";
                        PreparedStatement prepstate = api.getSQLiteConnection().prepareStatement(qry);
                        prepstate.execute();

                    } catch (SQLException ignored) {
                    }
                }
            }
        });
    }

    /**
     * Removes an entry from the table
     *
     * @param condition the column of where the value is in
     * @param value     the value to define which entry should be removed
     */
    public void remove(String condition, String value) {
        Bukkit.getScheduler().runTaskAsynchronously(AlphaLibary.getInstance(), () -> {
            if (api != null) {
                if (api.isConnected()) {
                    try {
                        String qry = "DELETE FROM " + tableName + " WHERE(" + condition + "='" + value + "')";
                        PreparedStatement prepstate = api.getSQLiteConnection().prepareStatement(qry);
                        prepstate.executeUpdate();

                    } catch (SQLException ignored) {
                    }
                }
            }
        });
    }

    /**
     * Checks if the UUID of a {@link Player} is inside the table
     *
     * @param p the {@link Player} to check
     * @return if the {@link Player} is inside the table
     */
    public void containsPlayer(Player p, Consumer<Boolean> check) {
        UUIDFetcher.getUUID(p, id ->
                getResult("uuid", id.toString(), "uuid", result -> check.accept(result != null))
        );
    }

    /**
     * Checks if the UUID of a {@link Player} is inside the table
     *
     * @param playername the name of the {@link Player} to check
     * @return if the {@link Player} is inside the table
     */
    public void containsPlayer(String playername, Consumer<Boolean> check) {
        UUIDFetcher.getUUID(playername, id ->
                getResult("uuid", id.toString(), "uuid", result -> check.accept(result != null))
        );
    }

    /**
     * Checks if the UUID of a {@link Player} is inside the table
     *
     * @param id the UUID of the {@link Player} to check
     * @return if the {@link Player} is inside the table
     */
    public void containsPlayer(UUID id, Consumer<Boolean> check) {
        getResult("uuid", id.toString(), "uuid", result -> check.accept(result != null));
    }

    /**
     * Checks if the condition is inside the table
     *
     * @param condition the column to check for
     * @param value     the value of the column
     * @return if the table contains it
     */
    public void contains(String condition, String value, Consumer<Boolean> check) {
        getResult(condition, value, condition, result -> check.accept(result != null));
    }

    /**
     * Insert values into the table
     *
     * @param values the values to insert in the correct order.
     */
    public void insert(String... values) {
        Bukkit.getScheduler().runTaskAsynchronously(AlphaLibary.getInstance(), () -> {
            StringBuilder builder = new StringBuilder();
            for (String str : values)
                builder.append(",").append(str);

            StringBuilder builder2 = new StringBuilder();
            String info;

            for (int c = 1; c <= getColumnAmount(); c++) {
                builder2.append(",").append(getColumnName(c));
            }

            info = builder2.toString().replaceFirst(",", "");

            if (api != null) {
                if (api.isConnected()) {
                    try {
                        String qry = "INSERT INTO " + tableName + " (" + info + ") VALUES (\'" + builder.toString().replaceFirst(",", "") + "\');";
                        PreparedStatement prepstate = api.getSQLiteConnection().prepareStatement(qry);
                        prepstate.executeUpdate();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * Updates a value inside the table
     *
     * @param uuid        the UUID column where to identify where to update
     * @param column      the column which you want to update
     * @param updatevalue the new value
     */
    public void update(UUID uuid, String column, String updatevalue) {
        Bukkit.getScheduler().runTaskAsynchronously(AlphaLibary.getInstance(), () -> {
            if (api != null) {
                if (api.isConnected()) {
                    try {
                        String qry = "UPDATE " + tableName + " SET " + column + "=? WHERE " + "uuid" + "=?;";
                        PreparedStatement prepstate = api.getSQLiteConnection().prepareStatement(qry);
                        prepstate.setString(1, updatevalue);
                        prepstate.setString(2, uuid.toString());
                        prepstate.executeUpdate();

                    } catch (SQLException ignored) {
                    }
                }
            }
        });
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
        Bukkit.getScheduler().runTaskAsynchronously(AlphaLibary.getInstance(), () -> {
            if (api != null) {
                if (api.isConnected()) {
                    try {
                        String qry = "UPDATE " + tableName + " SET " + column + "=? WHERE " + condition + "=?;";
                        PreparedStatement prepstate = api.getSQLiteConnection().prepareStatement(qry);
                        prepstate.setString(1, updatevalue);
                        prepstate.setString(2, conditionValue);
                        prepstate.executeUpdate();

                    } catch (SQLException ignored) {
                    }
                }
            }
        });
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
    public void getResult(String condition, String value, String column, Consumer<Object> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(AlphaLibary.getInstance(), () -> {
            if (api != null) {
                if (api.isConnected()) {
                    try {
                        String qry = "SELECT * FROM " + tableName + " WHERE " + condition + "=?;";
                        PreparedStatement prepstate = api.getSQLiteConnection().prepareStatement(qry);
                        prepstate.setString(1, value);
                        ResultSet rs = prepstate.executeQuery();


                        if (rs == null)
                            return;

                        if (rs.next()) {
                            Bukkit.getScheduler().runTask(AlphaLibary.getInstance(), () -> {
                                try {
                                    callback.accept(rs.getObject(column));
                                } catch (SQLException e) {
                                    callback.accept(null);
                                }
                            });
                            return;
                        }
                    } catch (SQLException ignored) {
                    }
                }
            }
            Bukkit.getScheduler().runTask(AlphaLibary.getInstance(), () -> callback.accept(null));
        });
    }

    /**
     * Performs a custom query
     *
     * @param qry the query to perform
     */
    public void customResult(String qry, Consumer<ResultSet> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(AlphaLibary.getInstance(), () -> {
            if (api != null) {
                if (api.isConnected()) {
                    try {
                        PreparedStatement prepstate = api.getSQLiteConnection().prepareStatement(qry);
                        Bukkit.getScheduler().runTask(AlphaLibary.getInstance(), () -> {
                            try {
                                callback.accept(prepstate.executeQuery());
                            } catch (SQLException e) {
                                callback.accept(null);
                            }
                        });

                        return;
                    } catch (SQLException ignored) {
                    }
                }
            }
            Bukkit.getScheduler().runTask(AlphaLibary.getInstance(), () -> callback.accept(null));
        });
    }

    /**
     * Order a column by a selection in ascending order
     * QRY: SELECT 'columnToOrder' FROM 'table' ORDER BY 'orderBy' asc
     *
     * @param columnToOrder the selection to order
     * @param orderBy       the column to order it
     */
    public void orderAscending(String columnToOrder, String orderBy, Consumer<ResultSet> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(AlphaLibary.getInstance(), () -> {
            if (api != null) {
                if (api.isConnected()) {
                    try {
                        String qry = "SELECT " + columnToOrder + " FROM " + tableName + " ORDER BY " + orderBy + " ASC;";
                        PreparedStatement prepstate = api.getSQLiteConnection().prepareStatement(qry);
                        Bukkit.getScheduler().runTask(AlphaLibary.getInstance(), () -> {
                            try {
                                callback.accept(prepstate.executeQuery());
                            } catch (SQLException e) {
                                callback.accept(null);
                            }
                        });

                        return;
                    } catch (SQLException ignored) {
                    }
                }
            }
            Bukkit.getScheduler().runTask(AlphaLibary.getInstance(), () -> callback.accept(null));
        });
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
    public void orderLimitAscending(String columnToOrder, String orderBy, long limit, Consumer<ResultSet> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(AlphaLibary.getInstance(), () -> {
            if (api != null) {
                if (api.isConnected()) {
                    try {
                        String qry = "SELECT " + columnToOrder + " FROM " + tableName + " ORDER BY " + orderBy + " ASC LIMIT " + limit + ";";
                        PreparedStatement prepstate = api.getSQLiteConnection().prepareStatement(qry);
                        Bukkit.getScheduler().runTask(AlphaLibary.getInstance(), () -> {
                            try {
                                callback.accept(prepstate.executeQuery());
                            } catch (SQLException e) {
                                callback.accept(null);
                            }
                        });

                        return;
                    } catch (SQLException ignored) {
                    }
                }
            }
            Bukkit.getScheduler().runTask(AlphaLibary.getInstance(), () -> callback.accept(null));
        });
    }

    /**
     * Order a column by a selection in descending order
     * QRY: SELECT 'columnToOrder' FROM 'table' ORDER BY 'orderBy' desc
     *
     * @param columnToOrder the selection to order
     * @param orderBy       the column to order it
     * @return the executed query
     */
    public void orderDescending(String columnToOrder, String orderBy, Consumer<ResultSet> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(AlphaLibary.getInstance(), () -> {
            if (api != null) {
                if (api.isConnected()) {
                    try {
                        String qry = "SELECT " + columnToOrder + " FROM " + tableName + " ORDER BY " + orderBy + " DESC;";
                        PreparedStatement prepstate = api.getSQLiteConnection().prepareStatement(qry);
                        Bukkit.getScheduler().runTask(AlphaLibary.getInstance(), () -> {
                            try {
                                callback.accept(prepstate.executeQuery());
                            } catch (SQLException e) {
                                callback.accept(null);
                            }
                        });

                        return;
                    } catch (SQLException ignored) {
                    }
                }
            }
            Bukkit.getScheduler().runTask(AlphaLibary.getInstance(), () -> callback.accept(null));
        });
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
    public void orderLimitDescending(String columnToOrder, String orderBy, long limit, Consumer<ResultSet> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(AlphaLibary.getInstance(), () -> {
            if (api != null) {
                if (api.isConnected()) {
                    try {
                        String qry = "SELECT " + columnToOrder + " FROM " + tableName + " ORDER BY " + orderBy + " DESC LIMIT " + limit + ";";
                        PreparedStatement prepstate = api.getSQLiteConnection().prepareStatement(qry);
                        Bukkit.getScheduler().runTask(AlphaLibary.getInstance(), () -> {
                            try {
                                callback.accept(prepstate.executeQuery());
                            } catch (SQLException e) {
                                callback.accept(null);
                            }
                        });
                        return;
                    } catch (SQLException ignored) {
                    }
                }
            }
            Bukkit.getScheduler().runTask(AlphaLibary.getInstance(), () -> callback.accept(null));
        });
    }

    /**
     * Gets the amount of columns inside the table
     *
     * @return the amount of columns inside the table
     */
    public int getColumnAmount() {
        if (TABLEINFO.get(tableName) == null) {
            return 0;
        }
        if (!TABLEINFO.get(tableName).contains(",")) {
            return 1;
        }

        String[] info = TABLEINFO.get(tableName).split(",");

        return info.length;
    }

    /**
     * Gets the column name at a specific place
     *
     * @param column the id of the column
     * @return the column name
     */
    public String getColumnName(int column) {
        if (TABLEINFO.get(tableName) == null) {
            return null;
        }

        String[] info = TABLEINFO.get(tableName).split(",");

        return info[column - 1].split(" ")[0];
    }

    /**
     * Returns a {@link ArrayList} with all values from one coloumn
     *
     * @param column the colum to get it's values from
     * @return an {@link ArrayList} with all values
     */
    public void getList(String column, Consumer<List<String>> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(AlphaLibary.getInstance(), () -> {
            List<String> list = new LinkedList<>();
            if (api != null) {
                if (api.isConnected()) {
                    try {
                        ResultSet rs = api.getSQLiteConnection().prepareStatement("SELECT " + column + " FROM " + tableName + ";").executeQuery();

                        if (rs == null) {
                            Bukkit.getScheduler().runTask(AlphaLibary.getInstance(), () -> callback.accept(list));
                            return;
                        }

                        while (rs.next()) {
                            String str = rs.getString(column);

                            if (str.startsWith("{") && str.endsWith("}"))
                                list.add(str);
                            else {
                                if (!str.contains(", ")) {
                                    list.add(str.replace("[", "").replace("]", ""));
                                } else {
                                    String[] strlist = str.split(", ");
                                    for (String aStrlist : strlist) {
                                        list.add(aStrlist.replace("[", "").replace("]", ""));
                                    }
                                }
                            }
                        }

                        Bukkit.getScheduler().runTask(AlphaLibary.getInstance(), () -> callback.accept(list));
                        return;
                    } catch (SQLException ignored) {
                    }
                }
            }
            Bukkit.getScheduler().runTask(AlphaLibary.getInstance(), () -> callback.accept(list));
        });
    }

    /**
     * Returns a {@link ArrayList} with all values from one coloumn with limited entries
     *
     * @param column the colum to get it's values from
     * @param limit  the limit of the entries
     * @return an {@link ArrayList} with all values
     */
    public void getLimitedList(String column, long limit, Consumer<ArrayList<String>> callback) {
        ArrayList<String> list = new ArrayList<>();

        Bukkit.getScheduler().runTaskAsynchronously(AlphaLibary.getInstance(), () -> {
            if (api != null) {
                if (api.isConnected()) {
                    try {
                        ResultSet rs = api.getSQLiteConnection().prepareStatement("SELECT " + column + " FROM " + tableName + " LIMIT " + limit + ";").executeQuery();

                        if (rs == null) {
                            Bukkit.getScheduler().runTask(AlphaLibary.getInstance(), () -> callback.accept(list));
                            return;
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

                        Bukkit.getScheduler().runTask(AlphaLibary.getInstance(), () -> callback.accept(list));
                        return;
                    } catch (SQLException ignored) {
                    }
                }
            }
            Bukkit.getScheduler().runTask(AlphaLibary.getInstance(), () -> callback.accept(list));
        });
    }

    public ArrayList<String> getAscendingOrderdList(String columnToOrder, String orderBy) {
        ArrayList<String> list = new ArrayList<>();

        orderAscending(columnToOrder, orderBy, rs -> {
            if (rs == null)
                return;

            try {
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
            } catch (SQLException ignored) {
            }
        });

        return list;
    }

    public ArrayList<String> getDescendingOrderdList(String columnToOrder, String orderBy) {
        ArrayList<String> list = new ArrayList<>();

        orderDescending(columnToOrder, orderBy, rs -> {
            try {
                if (rs == null)
                    return;

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
            } catch (SQLException ignored) {
            }
        });

        return list;
    }

    public ArrayList<String> getLimitedAscendingOrderdList(String columnToOrder, String orderBy, long limit) {
        ArrayList<String> list = new ArrayList<>();

        orderLimitAscending(columnToOrder, orderBy, limit, rs -> {
            try {
                if (rs == null)
                    return;

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
            } catch (SQLException ignored) {
            }
        });

        return list;
    }

    public ArrayList<String> getLimitedDescendingOrderdList(String columnToOrder, String orderBy, long limit) {
        ArrayList<String> list = new ArrayList<>();

        orderLimitDescending(columnToOrder, orderBy, limit, rs -> {
            try {
                if (rs == null)
                    return;

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
            } catch (SQLException ignored) {
            }
        });
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SQLiteDatabase that = (SQLiteDatabase) o;
        return Objects.equal(tableName, that.tableName) &&
                Objects.equal(databasePath, that.databasePath);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tableName, databasePath);
    }

    @Override
    public String toString() {
        return "SQLiteDatabase{" +
                "tableName='" + tableName + '\'' +
                ", databasePath='" + databasePath + '\'' +
                '}';
    }
}
