package io.github.alphahelixdev.alpary.fake;

import com.google.gson.annotations.Expose;
import io.github.alphahelixdev.alpary.Alpary;
import io.github.alphahelixdev.alpary.utilities.NoInitLocation;
import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.Helius;
import io.github.alphahelixdev.helius.reflection.SaveField;
import io.github.alphahelixdev.helius.sql.SQLColumn;
import io.github.alphahelixdev.helius.sql.SQLTableHandler;
import io.github.alphahelixdev.helius.sql.exceptions.NoConnectionException;
import io.github.alphahelixdev.helius.sql.sqlite.SQLiteDataType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EntityStorage<T extends FakeEntity> {
	
	private final Class<T> entityClass;
	private SQLTableHandler tableHandler;
	private List<SaveField> custom = new ArrayList<>();
	
	public EntityStorage(JavaPlugin plugin, String entityName, Class<T> entityClass) {
		String dbPath = plugin.getDataFolder().getAbsolutePath() + "/fakes.db";
		this.entityClass = entityClass;
		
		try {
			this.tableHandler = Helius.fastSQLiteConnect(dbPath, entityName);
			List<SQLColumn> columns = new ArrayList<>(Arrays.asList(
					new SQLColumn("name", SQLiteDataType.TEXT),
					new SQLColumn("id", SQLiteDataType.TEXT),
					new SQLColumn("start", SQLiteDataType.BLOB)));
			
			this.custom = Helius.getReflections().getDeclaredFieldsNotAnnotated(entityClass, Expose.class);
			
			this.custom.stream().map(f -> f.asNormal().getName())
					.forEach(s -> columns.add(new SQLColumn(s, SQLiteDataType.BLOB)));
			
			this.tableHandler.create(columns.toArray(new SQLColumn[0]));
		} catch(NoConnectionException e) {
			e.printStackTrace();
		}
	}
	
	public EntityStorage<T> addEntity(T entity) {
		List<String> values = new ArrayList<>(Arrays.asList(
				entity.getName(),
				entity.getId().toString(),
				Alpary.getInstance().gson().toJson(new NoInitLocation(entity.getStart()))));
		
		values.addAll(this.custom.stream().map(saveField -> saveField.get(entity))
				.map(o -> Utils.json().toJsonTree(o).toString()).collect(Collectors.toList()));
		
		this.tableHandler.insert(values.toArray(new String[0]));
		return this;
	}
	
	public EntityStorage<T> removeEntity(T entity) {
		this.tableHandler.remove("id", entity.getId().toString());
		return this;
	}
	
	public List<T> spawnEntities(Player p) {
		List<T> entities = new ArrayList<>();
		List<Class<?>> parameters = new ArrayList<>(Arrays.asList(Player.class, Location.class, String.class));
		
		parameters.addAll(this.custom.stream().map(saveField -> saveField.asNormal().getType()).collect(Collectors.toList()));
		
		for(List<String> rowEntry : this.tableHandler.getSyncRows()) {
			List<Object> insert = new ArrayList<>(Arrays.asList(
					p,
					Alpary.getInstance().gson().fromJson(rowEntry.get(2), NoInitLocation.class).realize(),
					rowEntry.get(0)
			));
			
			for(int i = 3; i < rowEntry.size(); i++)
				insert.add(Utils.json().fromJsonTree(rowEntry.get(i)));
			
			System.out.println(insert);
			
			entities.add((T) NMSUtil.getReflections().getMethod("spawnTemporary", this.entityClass, parameters.toArray(new Class[0]))
					.invokeStatic(insert.toArray(new Object[0])));
		}
		return entities;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getTableHandler(), this.custom);
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		EntityStorage<?> that = (EntityStorage<?>) o;
		return Objects.equals(this.getTableHandler(), that.getTableHandler()) &&
				Objects.equals(this.custom, that.custom);
	}
	
	public SQLTableHandler getTableHandler() {
		return this.tableHandler;
	}
	
	@Override
	public String toString() {
		return "EntityStorage{" +
				"tableHandler=" + tableHandler +
				", custom=" + custom +
				'}';
	}
}
