package de.alphahelix.alphalibary.storage;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.mysql.DatabaseCallback;
import de.alphahelix.alphalibary.mysql.JSONDatabase;
import de.alphahelix.alphalibary.mysql.MySQLAPI;
import de.alphahelix.alphalibary.mysql.SQLiteAPI;

import java.util.ArrayList;

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

    public <T> void getValue(Object path, Class<T> definy, DatabaseCallback<T> callback) {
        getStorage().getValue(path, definy, callback);
    }

    public void getKeys(DatabaseCallback<ArrayList<String>> callback) {
        getStorage().getKeys(callback);
    }

    public <T> void getValues(Class<T> definy, DatabaseCallback<ArrayList<T>> callback) {
        getStorage().getValues(definy, callback);
    }

    public void hasValue(Object path, DatabaseCallback<Boolean> callback) {
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
