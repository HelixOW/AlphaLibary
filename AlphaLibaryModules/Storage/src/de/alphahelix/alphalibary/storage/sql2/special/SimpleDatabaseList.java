package de.alphahelix.alphalibary.storage.sql2.special;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import de.alphahelix.alphalibary.core.utils.JSONUtil;
import de.alphahelix.alphalibary.storage.ReflectionHelper;
import de.alphahelix.alphalibary.storage.sql2.DatabaseType;
import de.alphahelix.alphalibary.storage.sql2.SQLCache;
import de.alphahelix.alphalibary.storage.sql2.SQLConstraints;
import de.alphahelix.alphalibary.storage.sql2.SQLDatabaseHandler;
import de.alphahelix.alphalibary.storage.sql2.annotations.PrimaryKey;
import de.alphahelix.alphalibary.storage.sql2.annotations.Unique;
import de.alphahelix.alphalibary.storage.sql2.mysql.MySQLDataType;
import de.alphahelix.alphalibary.storage.sql2.sqlite.SQLiteDataType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class SimpleDatabaseList<T> {
	
	private static final JsonParser PARSER = new JsonParser();
	
	private final List<String> fieldNames = new ArrayList<>();
	private final String primaryUniqueFieldName;
	private final int primaryUniqueFieldID;
	private final SQLDatabaseHandler handler;
	private final Class<T> typeClazz;
	private final SQLCache<T> cache = new SQLCache<>();
	
	public SimpleDatabaseList(String table, String database, DatabaseType type, Class<T> typeClazz) {
		this.handler = new SQLDatabaseHandler(table, database);
		this.typeClazz = typeClazz;
		
		List<String> columnNames = new ArrayList<>();
		boolean acceptMore = true;
		String pUFN = "";
		int pUFID = -1;
		
		if(type == DatabaseType.MYSQL) {
			for(ReflectionHelper.SaveField sf : ReflectionHelper.findFieldsNotAnnotatedWith(Expose.class, typeClazz)) {
				Field f = sf.field();
				
				if(f.isAnnotationPresent(PrimaryKey.class) && acceptMore) {
					columnNames.add(SQLDatabaseHandler.createMySQLColumn(
							f.getName(), MySQLDataType.VARCHAR, 767, SQLConstraints.PRIMARY_KEY));
					pUFN = f.getName();
					pUFID = sf.index();
					acceptMore = false;
				} else if(f.isAnnotationPresent(Unique.class) && acceptMore) {
					columnNames.add(SQLDatabaseHandler.createMySQLColumn(
							f.getName(), MySQLDataType.VARCHAR, 767, SQLConstraints.UNIQUE));
					pUFN = f.getName();
					pUFID = sf.index();
					acceptMore = false;
				} else {
					columnNames.add(SQLDatabaseHandler.createMySQLColumn(
							f.getName(), MySQLDataType.TEXT, 5000));
				}
				fieldNames.add(f.getName());
			}
		} else if(type == DatabaseType.SQLITE) {
			for(ReflectionHelper.SaveField sf : ReflectionHelper.findFieldsNotAnnotatedWith(Expose.class, typeClazz)) {
				Field f = sf.field();
				
				if(f.isAnnotationPresent(PrimaryKey.class) && acceptMore) {
					columnNames.add(SQLDatabaseHandler.createSQLiteColumn(
							f.getName(), SQLiteDataType.TEXT, SQLConstraints.PRIMARY_KEY));
					pUFN = f.getName();
					pUFID = sf.index();
					acceptMore = false;
				} else if(f.isAnnotationPresent(Unique.class) && acceptMore) {
					columnNames.add(SQLDatabaseHandler.createSQLiteColumn(
							f.getName(), SQLiteDataType.TEXT, SQLConstraints.UNIQUE));
					pUFN = f.getName();
					pUFID = sf.index();
					acceptMore = false;
				} else {
					columnNames.add(SQLDatabaseHandler.createSQLiteColumn(
							f.getName(), SQLiteDataType.TEXT));
				}
				
				fieldNames.add(f.getName());
			}
		}
		
		primaryUniqueFieldName = pUFN;
		primaryUniqueFieldID = pUFID;
		
		handler.create(columnNames.toArray(new String[columnNames.size()]));
	}
	
	public void addDefaultValue(T value) {
		hasValue(value, res -> {
			if(!res)
				addValue(value);
		}, false);
	}
	
	public void hasValue(T value, Consumer<Boolean> callback, boolean cached) {
		if(cached) {
			callback.accept(cache.getListCache().contains(value));
			return;
		}
		getValues(ts -> {
			if(ts == null) {
				callback.accept(false);
				return;
			}
			callback.accept(ts.contains(value));
		});
	}
	
	public void addValue(T value) {
		List<String> values = new ArrayList<>();
		
		for(String fieldName : fieldNames) {
			ReflectionHelper.SaveField sf = ReflectionHelper.getDeclaredField(fieldName, typeClazz);
			
			values.add(JSONUtil.toJson(sf.get(value)));
		}
		
		cache.save("", value);
		
		handler.insert(values.toArray(new String[values.size()]));
	}
	
	public void getValues(Consumer<List<T>> callback) {
		getValues(callback, false);
	}
	
	public void getValues(Consumer<List<T>> callback, boolean cached) {
		getValues(-1, callback, cached);
	}
	
	public void getValues(int limit, Consumer<List<T>> callback, boolean cached) {
		if(cached) {
			callback.accept(cache.getListCache());
			return;
		}
		
		cache.getListCache().clear();
		
		handler.getRows(limit, rows -> {
			List<T> result = new LinkedList<>();
			
			for(List<String> rowValues : rows) {
				JsonObject obj = new JsonObject();
				
				for(int cID = 0; cID < rowValues.size(); cID++) {
					String rowValue = rowValues.get(cID);
					
					obj.add(handler.getColumnName(cID), PARSER.parse(rowValue));
				}
				
				result.add(JSONUtil.getValue(obj, typeClazz));
			}
			
			cache.getListCache().addAll(result);
			callback.accept(result);
		});
	}
	
	public void removeValue(T value) {
		cache.remove(value);
		if(primaryUniqueFieldID == -1) {
			String fN = fieldNames.get(0);
			handler.remove(fN, JSONUtil.toJson(ReflectionHelper.getDeclaredField(fN, typeClazz).get(value)));
		} else {
			handler.remove(primaryUniqueFieldName, JSONUtil.toJson(ReflectionHelper.findFieldAtIndex(primaryUniqueFieldID).get(value)));
		}
	}
	
	public void getValues(int limit, Consumer<List<T>> callback) {
		getValues(limit, callback, false);
	}
	
	public void hasValue(T value, Consumer<Boolean> callback) {
		hasValue(value, callback, false);
	}
	
	public void hasUIDValue(Object key, Consumer<Boolean> callback) {
		hasUIDValue(key, callback, false);
	}
	
	public void hasUIDValue(Object key, Consumer<Boolean> callback, boolean cached) {
		if(cached) {
			for(T t : cache.getListCache()) {
				check(key, t, callback);
			}
			return;
		}
		
		getValues(ts -> {
			if(ts == null) {
				callback.accept(false);
				return;
			}
			
			for(T t : ts) {
				check(key, t, callback);
			}
		});
	}
	
	private void check(Object key, T t, Consumer<Boolean> callback) {
		for(ReflectionHelper.SaveField sf : ReflectionHelper.findFieldsNotAnnotatedWith(Expose.class, t.getClass())) {
			Field f = sf.field();
			
			if(f.isAnnotationPresent(PrimaryKey.class)) {
				Object fVal = sf.get(t);
				if(fVal.equals(key)) {
					callback.accept(true);
					return;
				}
			} else if(f.isAnnotationPresent(Unique.class)) {
				Object fVal = sf.get(t);
				if(fVal.equals(key)) {
					callback.accept(true);
					return;
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return "SimpleDatabaseList{" +
				"fieldNames=" + fieldNames +
				", primaryUniqueFieldName='" + primaryUniqueFieldName + '\'' +
				", primaryUniqueFieldID=" + primaryUniqueFieldID +
				", handler=" + handler +
				", typeClazz=" + typeClazz +
				", cache=" + cache +
				'}';
	}
	
	
}
