package de.alphahelix.alphalibary.storage.sql;

import java.util.function.Consumer;

/**
 * @see java.util.function.Consumer
 * @deprecated Use <<code>Consumer<T></code> instead
 */
public interface DatabaseCallback<T> extends Consumer<T> {

    void done(T t);

    @Override
    default void accept(T t) {
        done(t);
    }
}
