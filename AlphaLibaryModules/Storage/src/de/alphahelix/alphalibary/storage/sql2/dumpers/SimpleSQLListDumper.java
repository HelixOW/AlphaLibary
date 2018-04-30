package de.alphahelix.alphalibary.storage.sql2.dumpers;

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
import de.alphahelix.alphalibary.storage.sql2.annotations.PrimaryKey;
import de.alphahelix.alphalibary.storage.sql2.annotations.Unique;
import de.alphahelix.alphalibary.storage.sql2.mysql.MySQLDataType;
import de.alphahelix.alphalibary.storage.sql2.sqlite.SQLiteDataType;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SimpleSQLListDumper<T> {
	
	private static final JsonParser PARSER = new JsonParser();
	
	private final SQLDatabaseHandler handler;
	private final List<Class<?>> typeClazzes;
	private final SQLCache<T> cache = new SQLCache<>();
	
	public SimpleSQLListDumper(String table, String database, DatabaseType type, Class<T> listClasses) {
		this.handler = new SQLDatabaseHandler(table, database);
		
		List<String> columnNames = new ArrayList<>();
		boolean acceptMore = true;
		String pUFN = "";
		int pUFID = -1;
		
		List<Class<?>> supers = new ArrayList<>();
		Class<?> sup = listClasses;
		
		supers.add(sup);
		
		while(!sup.equals(Object.class)) {
			sup = sup.getSuperclass();
			
			if(!sup.equals(Object.class)) {
				supers.add(sup);
			}
		}
		
		this.typeClazzes = supers;
		
		if(type == DatabaseType.MYSQL) {
			for(ReflectionHelper.SaveField sf : ReflectionHelper.findFieldsNotAnnotatedWith(Expose.class, supers.toArray(new Class<?>[supers.size()]))) {
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
			}
		} else if(type == DatabaseType.SQLITE) {
			for(ReflectionHelper.SaveField sf : ReflectionHelper.findFieldsNotAnnotatedWith(Expose.class, supers.toArray(new Class<?>[supers.size()]))) {
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
			}
		}
		
		String primaryUniqueFieldName = pUFN;
		int primaryUniqueFieldID = pUFID;
		
		handler.create(columnNames.toArray(new String[columnNames.size()]));
	}
	
	public SimpleSQLListDumper<T> dumpList(List<T> list) {
		handler.empty();
		
		List<String> records = new ArrayList<>();
		
		for(T value : list) {
			for(ReflectionHelper.SaveField field : ReflectionHelper.findFieldsNotAnnotatedWith(Expose.class, this.typeClazzes.toArray(new Class<?>[typeClazzes.size()]))) {
				records.add(JSONUtil.toJson(field.get(value)));
			}
		}
		
		cache.getListCache().clear();
		cache.getListCache().addAll(list);
		
		handler.insert(records.toArray(new String[records.size()]));
		return this;
	}
	
	public void getList(Consumer<List<T>> callback) {
		Bukkit.getScheduler().runTaskAsynchronously(AlphaLibary.getInstance(), () -> {
			callback.accept(getList());
		});
	}
	
	public List<T> getList() {
		List<List<String>> rows = handler.getSyncRows();
		List<T> values = new ArrayList<>();
		
		for(List<String> records : rows) {
			JsonObject obj = new JsonObject();
			
			for(int rID = 0; rID < records.size(); rID++) {
				String record = records.get(rID);
				
				obj.add(handler.getColumnName(rID), PARSER.parse(record));
			}
			
			values.add(JSONUtil.getValue(obj, typeClazzes.get(0)));
		}
		
		return values;
	}
}
