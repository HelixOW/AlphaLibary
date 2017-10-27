package de.alphahelix.alphalibary.storage;

import java.util.List;
import java.util.function.Consumer;

public interface IDataStorage {

    void setValue(Object path, Object value);

    void setDefaultValue(Object path, Object value);

    void removeValue(Object path);

    <T> void getValue(Object path, Class<T> definy, Consumer<T> callback);

    void getKeys(Consumer<List<String>> callback);

    <T> void getValues(Class<T> definy, Consumer<List<T>> callback);

    void hasValue(Object path, Consumer<Boolean> callback);
}
