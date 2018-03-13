package de.alphahelix.alphalibary.storage.sql2;

import de.alphahelix.alphalibary.core.AlphaLibary;
import io.netty.util.internal.ConcurrentSet;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class SQLDatabaseHandler {
	
	private static final Map<String, String> TABLE_INFO = new ConcurrentHashMap<>();
	private static final Set<String> TABLE_NAMES = new ConcurrentSet<>();
	
	private final String table;
	private final SQLConnector connector;
	
	public SQLDatabaseHandler(String table, String database) {
		this.table = table;
		this.connector = SQLConnectionHandler.getSQLConnector(database).orElse(null);
		
		if(connector != null)
			connector.connect();
	}
	
	public static String createMySQLColumn(String name, SQLDataType type, long size, SQLConstraints... constraints) {
		if(Arrays.asList(constraints).isEmpty())
			return name + " " + type.sqlName() + "(" + size + ")";
		return name + " " + type.sqlName() + "(" + size + ") " +
				Arrays.toString(constraints).replace("_", " ").replace(", ", ",").replace("[", "").replace("]", "");
	}
	
	public static String createSQLiteColumn(String name, SQLDataType type, SQLConstraints... constraints) {
		if(Arrays.asList(constraints).isEmpty())
			return name + " " + type.sqlName();
		return name + " " + type.sqlName() + " " +
				Arrays.toString(constraints).replace("_", " ").replace(", ", " ").replace("[", "").replace("]", "");
	}
	
	public void create(String... columns) {
		Bukkit.getScheduler().runTaskAsynchronously(AlphaLibary.getInstance(), () -> {
			String tableinfo;
			
			if(columns.length > 1) {
				StringBuilder builder = new StringBuilder();
				for(String str : columns) {
					builder.append(",").append(str);
				}
				tableinfo = builder.toString();
			} else {
				tableinfo = columns[0];
			}
			
			if(tableinfo == null)
				return;
			
			tableinfo = tableinfo.replaceFirst(",", "");
			if(!TABLE_NAMES.contains(table))
				TABLE_NAMES.add(table);
			
			if(!TABLE_INFO.containsKey(table))
				TABLE_INFO.put(table, tableinfo);
			
			if(connector != null) {
				if(connector.handler().isConnected()) {
					try {
						String qry = "CREATE TABLE IF NOT EXISTS " + table + "(" + tableinfo + ");";
						PreparedStatement prepstate = connector.connect().prepareStatement(qry);
						prepstate.execute();
					} catch(SQLException ignored) {
					}
				}
			}
		});
	}
	
	public void drop() {
		doChecks(() -> {
			try {
				connector.connect().prepareStatement("DROP TABLE " + table + ";").executeQuery();
			} catch(SQLException ignored) {
			}
		});
	}
	
	private void doChecks(Runnable runnable) {
		Bukkit.getScheduler().runTaskAsynchronously(AlphaLibary.getInstance(), () -> {
			if(connector != null) {
				if(connector.handler().isConnected()) {
					runnable.run();
				}
			}
		});
	}
	
	public void empty() {
		doChecks(() -> {
			try {
				connector.connect().prepareStatement("TRUNCATE TABLE " + table + ";").executeQuery();
			} catch(SQLException ignored) {
			}
		});
	}
	
	public void remove(String condition, String value) {
		doChecks(() -> {
			try {
				String qry = "DELETE FROM " + table + " WHERE(" + condition + "='" + value + "')";
				connector.connect().prepareStatement(qry).executeUpdate();
			} catch(SQLException ignored) {
			}
		});
	}
	
	public void insert(String... values) {
		Bukkit.getScheduler().runTaskAsynchronously(AlphaLibary.getInstance(), () -> {
			StringBuilder builder = new StringBuilder();
			
			for(String str : values)
				builder.append(",").append("\'").append(str).append("\'");
			
			StringBuilder builder2 = new StringBuilder();
			String info;
			
			for(int c = 1; c <= getColumnAmount(); c++) {
				builder2.append(",").append(getColumnName(c));
			}
			
			info = builder2.toString().replaceFirst(",", "");
			
			if(connector != null) {
				if(connector.handler().isConnected()) {
					try {
						String qry = "INSERT INTO " + table + " (" + info + ") VALUES (" + builder.toString().replaceFirst(",", "") + ");";
						connector.connect().prepareStatement(qry).executeUpdate();
					} catch(SQLException ignored) {
					}
				}
			}
		});
	}
	
	public int getColumnAmount() {
		if(TABLE_INFO.get(table) == null) {
			return 0;
		}
		if(!TABLE_INFO.get(table).contains(",")) {
			return 1;
		}
		
		String[] info = TABLE_INFO.get(table).split(",");
		
		return info.length;
	}
	
	/**
	 * Gets the column name at a specific place
	 *
	 * @param column the id of the column
	 *
	 * @return the column name
	 */
	public String getColumnName(int column) {
		if(TABLE_INFO.get(table) == null) {
			return null;
		}
		
		String[] info = TABLE_INFO.get(table).split(",");
		
		return info[column - 1].split(" ")[0];
	}
	
	public void update(String condition, String conditionValue, String column, String updatevalue) {
		doChecks(() -> {
			try {
				String qry = "UPDATE " + table + " SET " + column + "=? WHERE " + condition + "=?;";
				PreparedStatement prepstate = connector.connect().prepareStatement(qry);
				prepstate.setString(1, updatevalue);
				prepstate.setString(2, conditionValue);
				
				prepstate.executeUpdate();
			} catch(SQLException ignored) {
			}
		});
	}
	
	public void alter(String alterQry) {
		customQuery("ALTER TABLE " + table + " " + alterQry + ";", result -> {
		});
	}
	
	public void customQuery(String qry, Consumer<ResultSet> callback) {
		doChecks(() -> {
			try {
				callback.accept(connector.connect().prepareStatement(qry).executeQuery());
			} catch(SQLException ignored) {
				syncedCallback(null, callback);
			}
		});
	}
	
	public <T> void syncedCallback(T obj, Consumer<T> consumer) {
		Bukkit.getScheduler().runTask(AlphaLibary.getInstance(), () -> consumer.accept(obj));
	}
	
	public void contains(String condition, String value, Consumer<Boolean> check) {
		getResult(condition, value, condition, result -> check.accept(result != null));
	}
	
	public <T> void getResult(String condition, String value, String column, Consumer<T> callback) {
		doChecks(() -> syncedCallback(getResult(condition, value, column), callback));
	}
	
	public <T> T getResult(String condition, String value, String column) {
		if(connector != null) {
			if(connector.handler().isConnected()) {
				try {
					String qry = "SELECT * FROM " + table + " WHERE " + condition + "=?;";
					PreparedStatement prepstate = connector.connect().prepareStatement(qry);
					
					prepstate.setString(1, value);
					ResultSet rs = prepstate.executeQuery();
					
					if(rs == null)
						return null;
					
					if(rs.next())
						return (T) rs.getObject(column);
					else
						return null;
				} catch(SQLException ignored) {
					return null;
				}
			}
		}
		
		return null;
	}
	
	public void getList(String column, Consumer<List<Object>> callback) {
		getList(column, -1, callback);
	}
	
	public void getList(String column, int limit, Consumer<List<Object>> callback) {
		doChecks(() -> {
			String qry = "SELECT " + column + " FROM " + table + " LIMIT " + limit + ";";
			if(limit == -1)
				qry = "SELECT " + column + " FROM " + table + ";";
			
			try {
				ResultSet rs = connector.connect().prepareStatement(qry).executeQuery();
				
				List<Object> objs = new ArrayList<>();
				
				while(rs.next())
					objs.add(rs.getObject(column));
				
				syncedCallback(objs, callback);
			} catch(SQLException ignored) {
				syncedCallback(new ArrayList<>(), callback);
			}
		});
	}
	
	public void getRows(Consumer<List<List<String>>> callback) {
		getRows(-1, callback);
	}
	
	public void getRows(int limit, Consumer<List<List<String>>> callback) {
		doChecks(() -> {
			String qry = "SELECT * FROM " + table + " LIMIT " + limit + ";";
			if(limit == -1)
				qry = "SELECT * FROM " + table + ";";
			
			try {
				ResultSet rs = connector.connect().prepareStatement(qry).executeQuery();
				List<List<String>> res = new LinkedList<>();
				
				List<String> rowObjects = new ArrayList<>();
				int cID = 1;
				
				while(rs.next()) {
					rowObjects.add(rs.getString(getColumnName(cID)));
					cID++;
					
					if(cID > getColumnAmount()) {
						res.add(rowObjects);
						rowObjects.clear();
					}
				}
				
				syncedCallback(res, callback);
			} catch(SQLException ignored) {
				syncedCallback(new ArrayList<>(), callback);
			}
		});
	}
}
