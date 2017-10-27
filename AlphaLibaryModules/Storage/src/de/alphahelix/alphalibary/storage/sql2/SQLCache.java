package de.alphahelix.alphalibary.storage.sql2;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class SQLCache<V> {

    private final Map<String, V> cache = new ConcurrentHashMap<>();
    private final List<V> listCache = new LinkedList<>();

    public SQLCache() {
    }

    public Optional<V> getObject(String key) {
        return Optional.ofNullable(cache.get(key));
    }

    public SQLCache save(String key, V object) {
        cache.put(key, object);
        listCache.add(object);
        return this;
    }

    public SQLCache remove(String key) {
        cache.remove(key);
        return this;
    }

    public SQLCache remove(V value) {
        listCache.remove(value);
        return this;
    }

    public Map<String, V> getCache() {
        return cache;
    }

    public List<V> getListCache() {
        return listCache;
    }
}
