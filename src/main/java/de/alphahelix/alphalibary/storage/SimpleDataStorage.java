package de.alphahelix.alphalibary.storage;

import de.alphahelix.alphalibary.mysql.DatabaseCallback;

import java.util.ArrayList;

public class SimpleDataStorage<K, V> {

    private final IDataStorage storage;
    private final Class<V> valueClazz;

    public SimpleDataStorage(IDataStorage storage, Class<V> valueClazz) {
        this.storage = storage;
        this.valueClazz = valueClazz;
    }

    public IDataStorage getStorage() {
        return storage;
    }

    public Class<V> getValueClazz() {
        return valueClazz;
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
}
