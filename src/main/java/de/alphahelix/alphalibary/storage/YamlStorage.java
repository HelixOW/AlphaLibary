package de.alphahelix.alphalibary.storage;

import de.alphahelix.alphalibary.file.SimpleFile;
import de.alphahelix.alphalibary.utils.JSONUtil;

public class YamlStorage extends AbstractStorage {

    private SimpleFile yamlFile;

    public YamlStorage(String path, String name) {
        this.yamlFile = new SimpleFile(path, name);
    }

    @Override
    public void setValue(StorageKey key, StorageItem value) {
        yamlFile.setDefault(key.getValue(), JSONUtil.toJson(value));
    }

    @Override
    public <T> T getValue(StorageKey key, String column, Class<T> valueClazz) throws NullPointerException {
        return JSONUtil.getValue(JSONUtil.getValue(yamlFile.getString(key.getValue()), StorageItem.class).getValue().toString(), valueClazz);
    }

    @Override
    public boolean containsValue(StorageKey key) {
        return yamlFile.configContains(key.getValue());
    }
}
