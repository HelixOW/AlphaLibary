package de.alphahelix.alphalibary.storage;

import de.alphahelix.alphalibary.file.SimpleJSONFile;
import de.alphahelix.alphalibary.utils.JSONUtil;

public class JsonStorage extends AbstractStorage {

    private static SimpleJSONFile jsonFile;

    public JsonStorage(String path, String name) {
        jsonFile = new SimpleJSONFile(path, name);
    }

    @Override
    public void setValue(StorageKey key, StorageItem value) {
        jsonFile.setValue(key.getValue(), value);
    }

    @Override
    public <T> T getValue(StorageKey key, String column, Class<T> valueClazz) {
        return JSONUtil.getValue(jsonFile.getValue(key.getValue(), StorageItem.class).getValue().toString(), valueClazz);
    }

    @Override
    public boolean containsValue(StorageKey key) {
        return jsonFile.jsonContains(key.getValue());
    }
}
