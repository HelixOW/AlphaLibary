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

package de.alphahelix.alphalibary.storage.sql2.special;

import de.alphahelix.alphalibary.core.utils.JSONUtil;
import de.alphahelix.alphalibary.storage.IDataStorage;
import de.alphahelix.alphalibary.storage.sql2.DatabaseType;
import de.alphahelix.alphalibary.storage.sql2.SQLConstraints;
import de.alphahelix.alphalibary.storage.sql2.SQLDatabaseHandler;
import de.alphahelix.alphalibary.storage.sql2.mysql.MySQLDataType;
import de.alphahelix.alphalibary.storage.sql2.sqlite.SQLiteDataType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class JSONDatabase implements IDataStorage {
	
	private final UniqueIdentifier id;
	
	private final SQLDatabaseHandler sqlDatabaseHandler;
	
	public JSONDatabase(UniqueIdentifier id, String table, String database, DatabaseType type) {
		this.id = id;
		
		this.sqlDatabaseHandler = new SQLDatabaseHandler(table, database);
		
		if(type == DatabaseType.MYSQL) {
			this.sqlDatabaseHandler.create(
					SQLDatabaseHandler.createMySQLColumn(id.name().toLowerCase(), MySQLDataType.VARCHAR, 50, SQLConstraints.PRIMARY_KEY),
					SQLDatabaseHandler.createMySQLColumn("val", MySQLDataType.TEXT, 5000));
		} else if(type == DatabaseType.SQLITE) {
			this.sqlDatabaseHandler.create(
					SQLDatabaseHandler.createSQLiteColumn(id.name().toLowerCase(), SQLiteDataType.TEXT, SQLConstraints.PRIMARY_KEY),
					SQLDatabaseHandler.createSQLiteColumn("val", MySQLDataType.TEXT));
		}
	}
	
	public JSONDatabase(String table, String database, DatabaseType type) {
		this.id = null;
		
		this.sqlDatabaseHandler = new SQLDatabaseHandler(table, database);
		
		if(type == DatabaseType.MYSQL) {
			this.sqlDatabaseHandler.create(
					SQLDatabaseHandler.createMySQLColumn("val", MySQLDataType.TEXT, 5000));
		} else if(type == DatabaseType.SQLITE) {
			this.sqlDatabaseHandler.create(
					SQLDatabaseHandler.createSQLiteColumn("val", MySQLDataType.TEXT));
		}
	}
	
	public <T> ArrayList<T> getValues(Class<T> define) {
		ArrayList<T> vals = new ArrayList<>();
		
		this.sqlDatabaseHandler.getList("val", result -> {
			for(Object json : result) {
				vals.add(JSONUtil.getValue(json.toString(), define));
			}
		});
		
		return vals;
	}
	
	public void setValue(String idValue, Object val) {
		
		if(id == null) {
			addValue(val);
			return;
		}
		
		this.sqlDatabaseHandler.contains(id.name().toLowerCase(), idValue, res -> {
			if(res)
				this.sqlDatabaseHandler.update(id.name().toLowerCase(), idValue, "val", JSONUtil.toJson(val));
			else
				this.sqlDatabaseHandler.insert(idValue, JSONUtil.toJson(val));
		});
	}
	
	public void addValue(Object val) {
		this.sqlDatabaseHandler.insert(JSONUtil.toJson(val));
	}
	
	@Override
	public int hashCode() {
		return java.util.Objects.hash(getId(), getSqlDatabaseHandler());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		JSONDatabase that = (JSONDatabase) o;
		return getId() == that.getId() &&
				java.util.Objects.equals(getSqlDatabaseHandler(), that.getSqlDatabaseHandler());
	}
	public UniqueIdentifier getId() {
		return this.id;
	}
	
	public SQLDatabaseHandler getSqlDatabaseHandler() {
		return sqlDatabaseHandler;
	}
	
	public <T> void getValue(String idValue, Class<T> define, Consumer<T> callback) {
		if(id == null) return;
		
		this.sqlDatabaseHandler.contains(id.name().toLowerCase(), idValue, result -> {
			if(result)
				this.sqlDatabaseHandler.getResult(id.name().toLowerCase(), idValue, "val", result1 -> callback.accept(JSONUtil.getValue(result1.toString(), define)));
		});
	}
	
	public void hasValue(String idValue, Consumer<Boolean> callback) {
		this.sqlDatabaseHandler.contains(id.name().toLowerCase(), idValue, callback);
	}
	
	@Override
	public String toString() {
		return "JSONDatabase{" +
				"id=" + id +
				", sqlDatabaseHandler=" + sqlDatabaseHandler +
				'}';
	}
	
	public enum UniqueIdentifier {
		NAME,
		NUMBER,
		UUID
	}
	
	
	@Override
	public <T> void getValues(Class<T> definy, Consumer<List<T>> callback) {
		this.sqlDatabaseHandler.getList("val", result -> {
			ArrayList<T> vals = new ArrayList<>();
			for(Object json : result) {
				vals.add(JSONUtil.getValue(json.toString(), definy));
			}
			callback.accept(vals);
		});
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
		this.sqlDatabaseHandler.contains(id.name().toLowerCase(), path.toString(), result -> {
			if(result)
				this.sqlDatabaseHandler.remove(id.name().toLowerCase(), path.toString());
		});
	}
	
	@Override
	public void hasValue(Object path, Consumer<Boolean> callback) {
		hasValue(path.toString(), callback);
	}
	
	@Override
	public void getKeys(Consumer<List<String>> callback) {
		if(id != null) {
			this.sqlDatabaseHandler.getList(id.name(), objects -> {
				List<String> names = new LinkedList<>();
				
				for(Object obj : objects)
					names.add(obj.toString());
				
				callback.accept(names);
			});
		}
	}
}
