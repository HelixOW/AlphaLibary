package de.alphahelix.alphalibary.storage.sql;

public interface DatabaseCallback<T> {
    void done(T result);
}
