package de.alphahelix.alphalibary.storage;

public abstract class AbstractStorage {

    public abstract void setValue(StorageKey key, StorageItem value);

    public abstract <T> T getValue(StorageKey key, String column, Class<T> valueClazz) throws NullPointerException;

    public abstract boolean containsValue(StorageKey key);
}
