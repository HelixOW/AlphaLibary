package io.github.alphahelixdev.alpary.fake;

import io.github.alphahelixdev.alpary.Alpary;
import io.github.alphahelixdev.alpary.utilities.NoInitLocation;
import io.github.whoisalphahelix.helix.hon.Hon;
import io.github.whoisalphahelix.sql.SQL;
import io.github.whoisalphahelix.sql.SQLTable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@EqualsAndHashCode
@ToString
public class EntityStorage<T extends FakeEntity> {
	
	private final Class<T> entityClass;
	private SQLTable tableHandler;
	
	public EntityStorage(JavaPlugin plugin, Class<T> entityClass) {
		String dbPath = plugin.getDataFolder().getAbsolutePath() + "/fakes.db";
		this.entityClass = entityClass;
		
		try {
			Alpary.getInstance().ioHandler().createFile(new File(dbPath));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		Hon config = new Hon(Alpary.getInstance().helix());
		
		config.set("driver", "org.sqlite.JDBC");
		config.set("jdbc-path", "jdbc:sqlite:" + dbPath);
		
		SQL sql = new SQL(Alpary.getInstance().helix(), config);
		
		this.tableHandler = sql.createTable(entityClass);
	}
	
	public EntityStorage<T> addEntity(T entity) {
		this.tableHandler.insert(entity);
		return this;
	}
	
	public EntityStorage<T> removeEntity(T entity) {
		this.tableHandler.remove("id", entity.getId().toString());
		return this;
	}
	
	public List<T> spawnEntities(Player p) {
		List<T> entities = new ArrayList<>();
		List<Class<?>> parameters = new ArrayList<>(Arrays.asList(Player.class, Location.class, String.class));
		
		for(List<String> rowEntry : this.tableHandler.getAll()) {
			List<Object> insert = new ArrayList<>(Arrays.asList(
					p,
					Alpary.getInstance().gson().fromJson(rowEntry.get(2), NoInitLocation.class).realize(),
					rowEntry.get(0)
			));
			
			for(int i = 3; i < rowEntry.size(); i++)
				insert.add(Alpary.getInstance().utilHandler().getJsonUtil().fromJsonTree(
						Alpary.getInstance().ioHandler().getGson(), rowEntry.get(i)));
			
			entities.add((T) Alpary.getInstance().reflections().getMethod("spawnTemporary", this.entityClass, parameters.toArray(new Class[0]))
					.invokeStatic(insert.toArray(new Object[0])));
		}
		return entities;
	}
}
