package de.alphahelix.alphalibary.storage;

import io.netty.util.internal.ConcurrentSet;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;


public class SimpleDataStorage<K, V> {

    private final IDataStorage storage;
    private final Class<V> valueClazz;

    private final Map<K, V> cache = new ConcurrentHashMap<>();
    private final Set<String> keyCache = new ConcurrentSet<>();
    private final Set<V> valueCache = new ConcurrentSet<>();

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

    public void setValue(K key, V value) {
        cache.put(key, value);
        getStorage().setValue(key, value);
    }

    public void setDefaultValue(K key, V value) {
        if (!cache.containsKey(key))
            cache.put(key, value);
        getStorage().setDefaultValue(key, value);
    }

    public void removeValue(K key) {
        cache.remove(key);
        getStorage().removeValue(key);
    }

    public void getValue(K key, Consumer<V> callback) {
        getStorage().getValue(key, this.valueClazz, callback);
    }

    public void getCachedValue(K key, Consumer<V> callback, boolean forceNew) {
        if (!cache.containsKey(key) || forceNew) {
            getValue(key, result -> {
                cache.put(key, result);
                callback.accept(result);
            });
        } else {
            callback.accept(cache.get(key));
        }
    }

    public void getKeys(Consumer<List<String>> callback) {
        getStorage().getKeys(callback);
    }

    public void getCachedKeys(Consumer<Set<String>> callback, boolean forceNew) {
        if (keyCache.isEmpty() || forceNew) {
            keyCache.clear();

            getKeys(result -> {
                result.forEach(s -> keyCache.add(s));
                callback.accept(keyCache);
            });
        } else {
            callback.accept(keyCache);
        }
    }

    public void getValues(Consumer<List<V>> callback) {
        getStorage().getValues(this.valueClazz, callback);
    }

    public void getCachedValues(Consumer<Set<V>> callback, boolean forceNew) {
        if (valueCache.isEmpty() || forceNew) {
            valueCache.clear();

            getValues(result -> {
                result.forEach(v -> valueCache.add(v));
                callback.accept(valueCache);
            });
        } else {
            callback.accept(valueCache);
        }
    }

    public void hasValue(K key, Consumer<Boolean> callback) {
        getStorage().hasValue(key, callback);
    }
}
