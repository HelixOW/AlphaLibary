package de.alphahelix.alphalibary.storage;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.storage.sql.JSONDatabase;
import de.alphahelix.alphalibary.storage.sql.MySQLAPI;
import de.alphahelix.alphalibary.storage.sql.SQLiteAPI;

import java.util.List;
import java.util.function.Consumer;


public class DataStorage {

    private IDataStorage storage;

    public DataStorage(IDataStorage storage) {
        this.storage = storage;
    }

    public static void init(JSONDatabase.DatabaseType type, String path) {
        if (type == JSONDatabase.DatabaseType.MYSQL) {
            for (MySQLAPI api : MySQLAPI.getMysqlDBs()) {
                api.initMySQLAPI();
            }
        } else if (type == JSONDatabase.DatabaseType.SQLITE)
            new SQLiteAPI(path).initSQLiteAPI();
    }

    public IDataStorage getStorage() {
        return storage;
    }

    public DataStorage setStorage(IDataStorage storage) {
        this.storage = storage;
        return this;
    }

    public void setValue(Object path, Object value) {
        getStorage().setValue(path, value);
    }

    public void setDefaultValue(Object path, Object value) {
        getStorage().setDefaultValue(path, value);
    }

    public void removeValue(Object path) {
        getStorage().removeValue(path);
    }

    public <T> void getValue(Object path, Class<T> definy, Consumer<T> callback) {
        getStorage().getValue(path, definy, callback);
    }

    public void getKeys(Consumer<List<String>> callback) {
        getStorage().getKeys(callback);
    }

    public <T> void getValues(Class<T> definy, Consumer<List<T>> callback) {
        getStorage().getValues(definy, callback);
    }

    public void hasValue(Object path, Consumer<Boolean> callback) {
        getStorage().hasValue(path, callback);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataStorage that = (DataStorage) o;
        return Objects.equal(getStorage(), that.getStorage());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getStorage());
    }

    @Override
    public String toString() {
        return "DataStorage{" +
                ", storage=" + storage +
                '}';
    }
}
