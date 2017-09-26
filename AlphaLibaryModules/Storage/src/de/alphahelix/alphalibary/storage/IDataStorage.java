package de.alphahelix.alphalibary.storage;

import de.alphahelix.alphalibary.storage.sql.DatabaseCallback;

import java.util.ArrayList;

public interface IDataStorage {

    void setValue(Object path, Object value);

    void setDefaultValue(Object path, Object value);

    void removeValue(Object path);

    <T> void getValue(Object path, Class<T> definy, DatabaseCallback<T> callback);

    void getKeys(DatabaseCallback<ArrayList<String>> callback);

    <T> void getValues(Class<T> definy, DatabaseCallback<ArrayList<T>> callback);

    void hasValue(Object path, DatabaseCallback<Boolean> callback);
}
