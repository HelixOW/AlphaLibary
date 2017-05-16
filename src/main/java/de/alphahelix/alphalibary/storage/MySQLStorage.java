package de.alphahelix.alphalibary.storage;

import de.alphahelix.alphalibary.mysql.MySQLDatabase;
import de.alphahelix.alphalibary.utils.JSONUtil;

public class MySQLStorage extends AbstractStorage {

    private MySQLDatabase mySQLDatabase;

    public MySQLStorage(MySQLDatabase database) {
        this.mySQLDatabase = database;
    }

    public void registerEntry(StorageItem... coloumnValues) {
        String[] items = new String[coloumnValues.length];

        for (int i = 0; i < coloumnValues.length; i++) {
            items[i] = coloumnValues[i].toString();
        }

        mySQLDatabase.insert(items);
    }

    @Override
    public void setValue(StorageKey key, StorageItem value) {
        if (mySQLDatabase.contains(key.getColoumn(), key.getValue())) {
            mySQLDatabase.update(key.getColoumn(), key.getValue(), value.getColoumn(), JSONUtil.toJson(value));
        }
    }

    @Override
    public <T> T getValue(StorageKey key, String column, Class<T> valueClazz) throws NullPointerException {
        if (mySQLDatabase.contains(key.getColoumn(), key.getValue())) {
            String json = (String) mySQLDatabase.getResult(key.getColoumn(), key.getValue(), column);

            return JSONUtil.getValue(JSONUtil.getValue(json, StorageItem.class).getValue().toString(), valueClazz);
        }
        throw new NullPointerException("Inside this MySQL table is no entry (" + key.getValue() + ")");
    }

    @Override
    public boolean containsValue(StorageKey key) {
        return mySQLDatabase.contains(key.getColoumn(), key.getValue());
    }
}
