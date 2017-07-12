package de.alphahelix.alphalibary.mysql;

import de.alphahelix.alphalibary.utils.JSONUtil;

public class JSONDatabase {

    private UniqueIdentifier id;
    private MySQLDatabase database;

    public JSONDatabase(UniqueIdentifier id, String table, String database) {
        this.id = id;
        this.database = new MySQLDatabase(table, database);

        this.database.create(this.database.createColumn(id.name(), MySQLAPI.MySQLDataType.VARCHAR, 50), this.database.createColumn("val", MySQLAPI.MySQLDataType.TEXT, 5000));
    }

    public void setValue(String idValue, Object val) {
        if (this.database.contains(id.name(), idValue)) {
            this.database.update(id.name(), idValue, "val", JSONUtil.toJson(val));
        } else {
            this.database.insert(idValue, JSONUtil.toJson(val));
        }
    }

    public <T> T getValue(String idValue, Class<T> define) {
        if (this.database.contains(id.name(), idValue)) {
            return JSONUtil.getValue(this.database.getResult(id.name(), idValue, "val").toString(), define);
        }
        return null;
    }

    public boolean hasValue(String idValue) {
        return this.database.contains(id.name(), idValue);
    }

    public enum UniqueIdentifier {
        NAME, NUMBER, UUID
    }
}
