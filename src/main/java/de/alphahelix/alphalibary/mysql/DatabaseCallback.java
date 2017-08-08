package de.alphahelix.alphalibary.mysql;

public interface DatabaseCallback<T> {
    void done(T result);
}
