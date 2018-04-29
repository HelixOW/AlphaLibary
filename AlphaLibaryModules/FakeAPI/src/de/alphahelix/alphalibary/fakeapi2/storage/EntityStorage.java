package de.alphahelix.alphalibary.fakeapi2.storage;

import de.alphahelix.alphalibary.fakeapi2.instances.FakeEntity;
import de.alphahelix.alphalibary.storage.sql2.DatabaseType;
import de.alphahelix.alphalibary.storage.sql2.SQLConnectionHandler;
import de.alphahelix.alphalibary.storage.sql2.dumpers.SimpleSQLListDumper;
import de.alphahelix.alphalibary.storage.sql2.sqlite.SQLiteConnector;
import de.alphahelix.alphalibary.storage.sql2.sqlite.SQLiteInformation;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class EntityStorage<T extends FakeEntity> {
	
	private List<T> loadableEntities;
	private SimpleSQLListDumper<T> listDumper;
	
	public EntityStorage(JavaPlugin plugin, String entityName, Class<T> entityClass) {
		String db = plugin.getDataFolder().getAbsolutePath() + "/fakes.db";
		
		SQLConnectionHandler.addConnector(db, new SQLiteConnector(plugin, new SQLiteInformation(db)));
		
		this.listDumper = new SimpleSQLListDumper<>(entityName, db, DatabaseType.SQLITE, entityClass);
		
		this.loadableEntities = listDumper.getList();
	}
	
	public EntityStorage<T> addEntity(T entity) {
		loadableEntities.add(entity);
		return save();
	}
	
	public EntityStorage<T> save() {
		listDumper.dumpList(loadableEntities);
		return this;
	}
	
	public EntityStorage<T> removeEntity(T entity) {
		loadableEntities.remove(entity);
		return save();
	}
	
	public List<T> getEntities() {
		return loadableEntities;
	}
}
