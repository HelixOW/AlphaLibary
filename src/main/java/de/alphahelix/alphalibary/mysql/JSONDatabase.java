/*
 *
 *  * Copyright (C) <2017>  <AlphaHelixDev>
 *  *
 *  *       This program is free software: you can redistribute it under the
 *  *       terms of the GNU General Public License as published by
 *  *       the Free Software Foundation, either version 3 of the License.
 *  *
 *  *       This program is distributed in the hope that it will be useful,
 *  *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *       GNU General Public License for more details.
 *  *
 *  *       You should have received a copy of the GNU General Public License
 *  *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.alphahelix.alphalibary.mysql;

import de.alphahelix.alphalibary.utils.JSONUtil;

import java.util.ArrayList;

public class JSONDatabase {

    private UniqueIdentifier id;
    private DatabaseType type;

    private AsyncMySQLDatabase mySQLDatabase;
    private SQLiteDatabase sqliteDatabase;

    public JSONDatabase(UniqueIdentifier id, String table, String database, DatabaseType type) {
        this.id = id;
        this.type = type;

        if (type == DatabaseType.MYSQL) {
            this.mySQLDatabase = new AsyncMySQLDatabase(table, database);
            this.mySQLDatabase.create(
                    AsyncMySQLDatabase.createColumn(id.name().toLowerCase(), MySQLAPI.MySQLDataType.VARCHAR, 50, "PRIMARY KEY"),
                    AsyncMySQLDatabase.createColumn("val", MySQLAPI.MySQLDataType.TEXT, 5000));
        } else if (type == DatabaseType.SQLITE) {
            this.sqliteDatabase = new SQLiteDatabase(table, database);
            this.sqliteDatabase.create(
                    SQLiteDatabase.createColumn(id.name().toLowerCase(), SQLiteAPI.SQLiteDataType.TEXT, "PRIMARY KEY"),
                    SQLiteDatabase.createColumn("val", SQLiteAPI.SQLiteDataType.TEXT)
            );
        }
    }

    public JSONDatabase(String table, String database, DatabaseType type) {
        if (type == DatabaseType.MYSQL) {
            this.mySQLDatabase = new AsyncMySQLDatabase(table, database);
            this.mySQLDatabase.create(
                    AsyncMySQLDatabase.createColumn("val", MySQLAPI.MySQLDataType.TEXT, 5000));
        } else if (type == DatabaseType.SQLITE) {
            this.sqliteDatabase = new SQLiteDatabase(table, database);
            this.sqliteDatabase.create(
                    SQLiteDatabase.createColumn("val", SQLiteAPI.SQLiteDataType.TEXT)
            );
        }
    }

    public void addValue(Object val) {
        if (type == DatabaseType.MYSQL)
            this.mySQLDatabase.insert(JSONUtil.toJson(val));
        else if (type == DatabaseType.SQLITE)
            this.sqliteDatabase.insert(JSONUtil.toJson(val));
    }

    public void setValue(String idValue, Object val) {
        if (type == DatabaseType.MYSQL) {
            this.mySQLDatabase.contains(id.name().toLowerCase(), idValue, result -> {
                if (result)
                    this.mySQLDatabase.update(id.name().toLowerCase(), idValue, "val", JSONUtil.toJson(val));
                else
                    this.mySQLDatabase.insert(idValue, JSONUtil.toJson(val));
            });
        } else if (type == DatabaseType.SQLITE) {
            this.sqliteDatabase.contains(id.name().toLowerCase(), idValue, result -> {
                if (result)
                    this.sqliteDatabase.update(id.name().toLowerCase(), idValue, "val", JSONUtil.toJson(val));
                else
                    this.sqliteDatabase.insert(idValue, JSONUtil.toJson(val));
            });
        }
    }

    public <T> void getValue(String idValue, Class<T> define, DatabaseCallback<T> callback) {
        if (type == DatabaseType.MYSQL) {
            this.mySQLDatabase.contains(id.name().toLowerCase(), idValue, result -> {
                if (result)
                    this.mySQLDatabase.getResult(id.name().toLowerCase(), idValue, "val", result1 -> callback.done(JSONUtil.getValue(result1.toString(), define)));
            });
        } else if (type == DatabaseType.SQLITE) {
            this.sqliteDatabase.contains(id.name().toLowerCase(), idValue, result -> {
                if (result)
                    this.sqliteDatabase.getResult(id.name().toLowerCase(), idValue, "val", result1 -> callback.done(JSONUtil.getValue(result1.toString(), define)));
            });
        }
    }

    public <T> ArrayList<T> getValues(Class<T> define) {
        ArrayList<T> vals = new ArrayList<>();

        if (type == DatabaseType.MYSQL) {
            this.mySQLDatabase.getList("val", result -> {
                for (String json : result) {
                    vals.add(JSONUtil.getValue(json, define));
                }
            });
        } else if (type == DatabaseType.SQLITE) {
            this.sqliteDatabase.getList("val", result -> {
                for (String json : result) {
                    vals.add(JSONUtil.getValue(json, define));
                }
            });
        }

        return vals;
    }

    public ArrayList<String> getKeys() {
        ArrayList<String> keys = new ArrayList<>();

        if (type == DatabaseType.MYSQL) {
            this.mySQLDatabase.getList(id.name(), keys::addAll);
        } else if (type == DatabaseType.SQLITE) {
            this.sqliteDatabase.getList(id.name(), keys::addAll);
        }

        return keys;
    }

    public void hasValue(String idValue, DatabaseCallback<Boolean> callback) {
        if (type == DatabaseType.MYSQL)
            this.mySQLDatabase.contains(id.name().toLowerCase(), idValue, callback);
        else if (type == DatabaseType.SQLITE)
            this.sqliteDatabase.contains(id.name().toLowerCase(), idValue, callback);
    }

    public enum UniqueIdentifier {
        NAME, NUMBER, UUID
    }

    public enum DatabaseType {
        MYSQL, SQLITE
    }
}
