package de.alphahelix.alphalibary.storage.sql;

import java.util.function.Consumer;

/**
 * @see java.util.function.Consumer
 * @deprecated Use <<code>Consumer<T></code> instead
 */
public interface DatabaseCallback<T> extends Consumer<T> {
	
	@Override
	default void accept(T t) {
		done(t);
	}
	
	void done(T t);
}
