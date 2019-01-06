package io.github.alphahelixdev.alpary.fake;

import io.github.alphahelixdev.helius.Helius;
import io.github.alphahelixdev.helius.sql.exceptions.NoConnectionException;
import io.github.alphahelixdev.helius.sql.tables.ObjectTable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.stream.Collectors;

public class EntityStorage<T extends FakeEntity> {

    private List<T> loadableEntities;
    private ObjectTable tableHandler;

    public EntityStorage(JavaPlugin plugin, String entityName, Class<T> entityClass) {
        String dbPath = plugin.getDataFolder().getAbsolutePath() + "/fakes.db";

        try {
            this.tableHandler = new ObjectTable(Helius.fastSQLiteConnect(dbPath, entityName), entityClass);
            this.loadableEntities = this.tableHandler.getObjects().stream().map(obj -> (T) obj).collect(Collectors.toList());
        } catch (NoConnectionException | ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    public EntityStorage<T> addEntity(T entity) {
        this.loadableEntities.add(entity);
        this.tableHandler.addObject(entity);
        return this;
    }

    public EntityStorage<T> removeEntity(T entity) {
        this.loadableEntities.remove(entity);
        this.tableHandler.getTableHandler().remove("id", entity.);
        return this;
    }
}
