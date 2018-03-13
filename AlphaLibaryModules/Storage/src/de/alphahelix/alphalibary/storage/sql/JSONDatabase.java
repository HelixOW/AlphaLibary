/*
 *
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
 *
 */

package de.alphahelix.alphalibary.storage.sql;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.core.utils.JSONUtil;
import de.alphahelix.alphalibary.storage.IDataStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @see de.alphahelix.alphalibary.storage.sql2.special.JSONDatabase
 * @deprecated
 */
public class JSONDatabase implements IDataStorage {
	
	private final UniqueIdentifier id;
	private final DatabaseType type;
	
	private final AsyncMySQLDatabase mySQLDatabase;
	private final SQLiteDatabase sqliteDatabase;
	
	public JSONDatabase(UniqueIdentifier id, String table, String database, DatabaseType type) {
		this.id = id;
		this.type = type;
		
		switch(type) {
			case MYSQL:
				this.mySQLDatabase = new AsyncMySQLDatabase(table, database);
				this.mySQLDatabase.create(
						AsyncMySQLDatabase.createColumn(id.name().toLowerCase(), MySQLAPI.MySQLDataType.VARCHAR, 50, "PRIMARY KEY"),
						AsyncMySQLDatabase.createColumn("val", MySQLAPI.MySQLDataType.TEXT, 5000));
				this.sqliteDatabase = null;
				break;
			case SQLITE:
				this.sqliteDatabase = new SQLiteDatabase(table, database);
				this.sqliteDatabase.create(
						SQLiteDatabase.createColumn(id.name().toLowerCase(), SQLiteAPI.SQLiteDataType.TEXT, "PRIMARY KEY"),
						SQLiteDatabase.createColumn("val", SQLiteAPI.SQLiteDataType.TEXT)
				);
				this.mySQLDatabase = null;
				break;
			default:
				this.mySQLDatabase = null;
				this.sqliteDatabase = null;
				break;
		}
	}
	
	public JSONDatabase(String table, String database, DatabaseType type) {
		this.id = null;
		this.type = type;
		
		switch(type) {
			case MYSQL:
				this.mySQLDatabase = new AsyncMySQLDatabase(table, database);
				this.mySQLDatabase.create(
						AsyncMySQLDatabase.createColumn("val", MySQLAPI.MySQLDataType.TEXT, 5000));
				
				this.sqliteDatabase = null;
				break;
			case SQLITE:
				this.sqliteDatabase = new SQLiteDatabase(table, database);
				this.sqliteDatabase.create(
						SQLiteDatabase.createColumn("val", SQLiteAPI.SQLiteDataType.TEXT)
				);
				this.mySQLDatabase = null;
				break;
			default:
				this.mySQLDatabase = null;
				this.sqliteDatabase = null;
				break;
		}
	}
	
	public <T> ArrayList<T> getValues(Class<T> define) {
		ArrayList<T> vals = new ArrayList<>();
		
		if(type == DatabaseType.MYSQL) {
			this.mySQLDatabase.getList("val", result -> {
				for(String json : result) {
					vals.add(JSONUtil.getValue(json, define));
				}
			});
		} else if(type == DatabaseType.SQLITE) {
			this.sqliteDatabase.getList("val", result -> {
				for(String json : result) {
					vals.add(JSONUtil.getValue(json, define));
				}
			});
		}
		
		return vals;
	}
	
	public ArrayList<String> getKeys() {
		ArrayList<String> keys = new ArrayList<>();
		
		if(type == DatabaseType.MYSQL) {
			this.mySQLDatabase.getList(id.name(), keys::addAll);
		} else if(type == DatabaseType.SQLITE) {
			this.sqliteDatabase.getList(id.name(), keys::addAll);
		}
		
		return keys;
	}
	
	public void addValue(Object val) {
		if(type == DatabaseType.MYSQL)
			this.mySQLDatabase.insert(JSONUtil.toJson(val));
		else if(type == DatabaseType.SQLITE)
			this.sqliteDatabase.insert(JSONUtil.toJson(val));
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(getId(), getType(), getMySQLDatabase(), getSqliteDatabase());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		JSONDatabase that = (JSONDatabase) o;
		return getId() == that.getId() &&
				getType() == that.getType() &&
				Objects.equal(getMySQLDatabase(), that.getMySQLDatabase()) &&
				Objects.equal(getSqliteDatabase(), that.getSqliteDatabase());
	}
	
	public void setValue(String idValue, Object val) {
		if(type == DatabaseType.MYSQL) {
			if(id == null) {
				addValue(val);
				return;
			}
			this.mySQLDatabase.contains(id.name().toLowerCase(), idValue, result -> {
				if(result)
					this.mySQLDatabase.update(id.name().toLowerCase(), idValue, "val", JSONUtil.toJson(val));
				else
					this.mySQLDatabase.insert(idValue, JSONUtil.toJson(val));
			});
		} else if(type == DatabaseType.SQLITE) {
			if(id == null) {
				addValue(val);
				return;
			}
			this.sqliteDatabase.contains(id.name().toLowerCase(), idValue, result -> {
				if(result)
					this.sqliteDatabase.update(id.name().toLowerCase(), idValue, "val", JSONUtil.toJson(val));
				else
					this.sqliteDatabase.insert(idValue, JSONUtil.toJson(val));
			});
		}
	}
	
	@Override
	public String toString() {
		return "JSONDatabase{" +
				"id=" + id +
				", type=" + type +
				", mySQLDatabase=" + mySQLDatabase +
				", sqliteDatabase=" + sqliteDatabase +
				'}';
	}
	
	public UniqueIdentifier getId() {
		return this.id;
	}
	
	public <T> void getValue(String idValue, Class<T> define, Consumer<T> callback) {
		if(type == DatabaseType.MYSQL) {
			if(id == null)
				return;
			this.mySQLDatabase.contains(id.name().toLowerCase(), idValue, result -> {
				if(result)
					this.mySQLDatabase.getResult(id.name().toLowerCase(), idValue, "val", result1 -> callback.accept(JSONUtil.getValue(result1.toString(), define)));
			});
		} else if(type == DatabaseType.SQLITE) {
			if(id == null)
				return;
			this.sqliteDatabase.contains(id.name().toLowerCase(), idValue, result -> {
				if(result)
					this.sqliteDatabase.getResult(id.name().toLowerCase(), idValue, "val", result1 -> callback.accept(JSONUtil.getValue(result1.toString(), define)));
			});
		}
	}
	
	public DatabaseType getType() {
		return this.type;
	}
	
	public AsyncMySQLDatabase getMySQLDatabase() {
		return this.mySQLDatabase;
	}
	
	public SQLiteDatabase getSqliteDatabase() {
		return this.sqliteDatabase;
	}
	
	@Override
	public <T> void getValues(Class<T> definy, Consumer<List<T>> callback) {
		if(type == DatabaseType.MYSQL) {
			this.mySQLDatabase.getList("val", result -> {
				ArrayList<T> vals = new ArrayList<>();
				for(String json : result) {
					vals.add(JSONUtil.getValue(json, definy));
				}
				callback.accept(vals);
			});
		} else if(type == DatabaseType.SQLITE) {
			this.sqliteDatabase.getList("val", result -> {
				ArrayList<T> vals = new ArrayList<>();
				for(String json : result) {
					vals.add(JSONUtil.getValue(json, definy));
				}
				callback.accept(vals);
			});
		}
	}
	
	public enum UniqueIdentifier {
		NAME, NUMBER, UUID
	}
	
	public enum DatabaseType {
		MYSQL, SQLITE
	}
	
	public void hasValue(String idValue, Consumer<Boolean> callback) {
		if(type == DatabaseType.MYSQL)
			this.mySQLDatabase.contains(id.name().toLowerCase(), idValue, callback);
		else if(type == DatabaseType.SQLITE)
			this.sqliteDatabase.contains(id.name().toLowerCase(), idValue, callback);
	}
	
	@Override
	public void setValue(Object path, Object value) {
		setValue(path.toString(), value);
	}
	
	@Override
	public void setDefaultValue(Object path, Object value) {
		hasValue(path.toString(), result -> {
			if(result)
				setValue(path, value);
		});
	}
	
	@Override
	public <T> void getValue(Object path, Class<T> definy, Consumer<T> callback) {
		getValue(path.toString(), definy, callback);
	}
	
	@Override
	public void removeValue(Object path) {
		if(type == DatabaseType.MYSQL) {
			this.mySQLDatabase.contains(id.name().toLowerCase(), path.toString(), result -> {
				if(result)
					this.mySQLDatabase.remove(id.name().toLowerCase(), path.toString());
			});
		} else if(type == DatabaseType.SQLITE) {
			this.sqliteDatabase.contains(id.name().toLowerCase(), path.toString(), result -> {
				if(result)
					this.sqliteDatabase.remove(id.name().toLowerCase(), path.toString());
			});
		}
	}
	
	@Override
	public void hasValue(Object path, Consumer<Boolean> callback) {
		hasValue(path.toString(), callback);
	}
	
	@Override
	public void getKeys(Consumer<List<String>> callback) {
		if(id != null) {
			if(getType() == DatabaseType.MYSQL)
				this.mySQLDatabase.getList(id.name(), callback);
			else
				this.sqliteDatabase.getList(id.name(), callback);
		}
	}
	

	

	

	

	

	

	

	

	

}
