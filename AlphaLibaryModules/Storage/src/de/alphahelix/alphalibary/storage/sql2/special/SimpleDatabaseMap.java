package de.alphahelix.alphalibary.storage.sql2.special;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import de.alphahelix.alphalibary.core.AlphaLibary;
import de.alphahelix.alphalibary.core.utils.JSONUtil;
import de.alphahelix.alphalibary.storage.ReflectionHelper;
import de.alphahelix.alphalibary.storage.sql2.DatabaseType;
import de.alphahelix.alphalibary.storage.sql2.SQLCache;
import de.alphahelix.alphalibary.storage.sql2.SQLConstraints;
import de.alphahelix.alphalibary.storage.sql2.SQLDatabaseHandler;
import de.alphahelix.alphalibary.storage.sql2.mysql.MySQLDataType;
import de.alphahelix.alphalibary.storage.sql2.sqlite.SQLiteDataType;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SimpleDatabaseMap<K, V> {
	
	private static final JsonParser PARSER = new JsonParser();
	
	private final List<String> fieldNames = new ArrayList<>();
	private final String keyColumnName;
	private final Class<V> valueClazz;
	
	private final SQLDatabaseHandler handler;
	private final SQLCache cache = new SQLCache();
	
	public SimpleDatabaseMap(String table, String database, Class<K> keyClazz, Class<V> valueClazz, DatabaseType type) {
		this.handler = new SQLDatabaseHandler(table, database);
		
		this.keyColumnName = keyClazz.getSimpleName().toLowerCase();
		this.valueClazz = valueClazz;
		
		List<String> columnNames = new ArrayList<>();
		
		
		if(type == DatabaseType.MYSQL) {
			columnNames.add(SQLDatabaseHandler.createMySQLColumn(keyColumnName, MySQLDataType.VARCHAR, 767, SQLConstraints.PRIMARY_KEY));
			
			for(ReflectionHelper.SaveField valueFields : ReflectionHelper.findFieldsNotAnnotatedWith(Expose.class, valueClazz)) {
				columnNames.add(SQLDatabaseHandler.createMySQLColumn(
						valueFields.field().getName(), MySQLDataType.TEXT, 5000));
				
				fieldNames.add(valueFields.field().getName());
			}
		} else if(type == DatabaseType.SQLITE) {
			columnNames.add(SQLDatabaseHandler.createSQLiteColumn(keyColumnName, SQLiteDataType.TEXT, SQLConstraints.PRIMARY_KEY));
			
			for(ReflectionHelper.SaveField sf : ReflectionHelper.findFieldsNotAnnotatedWith(Expose.class, valueClazz)) {
				columnNames.add(SQLDatabaseHandler.createSQLiteColumn(
						sf.field().getName(), SQLiteDataType.TEXT));
				
				fieldNames.add(sf.field().getName());
			}
		}
		
		handler.create(columnNames.toArray(new String[columnNames.size()]));
	}
	
	public void addDefaultValue(K key, V value) {
		hasValue(key, res -> {
			if(!res) {
				addValue(key, value);
			}
		});
	}
	
	public void hasValue(K key, Consumer<Boolean> callback) {
		hasValue(key, callback, false);
	}
	
	public void addValue(K key, V value) {
		Map<String, String> values = new LinkedHashMap<>();
		
		cache.save(key.toString(), value);
		
		values.put(keyColumnName, key.toString());
		
		for(String fieldName : fieldNames) {
			ReflectionHelper.SaveField sf = ReflectionHelper.getDeclaredField(fieldName, valueClazz);
			
			values.put(fieldName, JSONUtil.toJson(sf.get(value)));
		}
		
		this.handler.contains(keyColumnName, key.toString(), res -> {
			if(res) {
				for(String fieldName : values.keySet()) {
					this.handler.update(keyColumnName, key.toString(), fieldName, values.get(fieldName));
				}
			} else {
				this.handler.insert(values.values().toArray(new String[values.values().size()]));
			}
		});
	}
	
	public void hasValue(K key, Consumer<Boolean> callback, boolean cached) {
		if(cached) {
			getKeys(vs -> callback.accept(vs.contains(key.toString())), true);
			return;
		}
		handler.getList(keyColumnName, objects -> {
			if(objects == null) {
				callback.accept(false);
				return;
			}
			callback.accept(objects.contains(key.toString()));
		});
	}
	
	public void getKeys(Consumer<List<Object>> callback, boolean cached) {
		getKeys(-1, callback, cached);
	}
	
	public void getKeys(int limit, Consumer<List<Object>> callback, boolean cached) {
		if(cached) {
			callback.accept(new ArrayList<>(cache.getCache().keySet()));
			return;
		}
		handler.getList(keyColumnName, limit, callback);
	}
	
	public void removeValue(K key) {
		handler.remove(keyColumnName, key.toString());
		cache.remove(key.toString());
	}
	
	public void getValue(K key, Consumer<V> callback) {
		getValue(key.toString(), callback);
	}
	
	public void getValue(String key, Consumer<V> callback) {
		getValue(key, callback, false);
	}
	
	public void getValue(String key, Consumer<V> callback, boolean cached) {
		Bukkit.getScheduler().runTaskAsynchronously(AlphaLibary.getInstance(), () -> {
			if(cached) {
				if(cache.getObject(key).isPresent()) {
					callback.accept((V) cache.getObject(key));
					return;
				} else
					getValue(key, callback, false);
			}
			cache.save(key, getValue(key));
			handler.syncedCallback(getValue(key), callback);
		});
	}
	
	private V getValue(String key) {
		JsonObject obj = new JsonObject();
		
		for(String fN : fieldNames) {
			JsonElement el = PARSER.parse(handler.getResult(keyColumnName, key, fN).toString());
			
			obj.add(fN, el);
		}
		
		return JSONUtil.getGson().fromJson(obj, valueClazz);
	}
	
	public void getValue(K key, Consumer<V> callback, boolean cached) {
		getValue(key.toString(), callback, cached);
	}
	
	public void getValues(Consumer<List<V>> callback) {
		getValues(callback, false);
	}
	
	public void getValues(Consumer<List<V>> callback, boolean cached) {
		if(cached) {
			callback.accept(new ArrayList<>(cache.getCache().values()));
			return;
		}
		
		getKeys(-1, keys -> Bukkit.getScheduler().runTaskAsynchronously(AlphaLibary.getInstance(), () -> {
			List<V> values = new ArrayList<>();
			
			if(keys == null) {
				handler.syncedCallback(values, callback);
				return;
			}
			
			cache.getCache().clear();
			
			for(Object key : keys) {
				values.add(getValue(key.toString()));
				cache.save(key.toString(), getValue(key.toString()));
			}
			
			handler.syncedCallback(values, callback);
		}));
	}
	
	public void getKeys(int limit, Consumer<List<Object>> callback) {
		getKeys(limit, callback, false);
	}
	
	public void getKeys(Consumer<List<Object>> callback) {
		getKeys(-1, callback, false);
	}
	
	@Override
	public String toString() {
		return "SimpleDatabaseMap{" +
				"fieldNames=" + fieldNames +
				", keyColumnName='" + keyColumnName + '\'' +
				", valueClazz=" + valueClazz +
				", handler=" + handler +
				", cache=" + cache +
				'}';
	}
}
