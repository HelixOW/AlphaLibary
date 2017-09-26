package de.alphahelix.alphalibary.fakeapi.utils;

import de.alphahelix.alphalibary.fakeapi.instances.FakeEntity;

public interface SpawnCallback<T extends FakeEntity> {
    void done(T entity);
}
