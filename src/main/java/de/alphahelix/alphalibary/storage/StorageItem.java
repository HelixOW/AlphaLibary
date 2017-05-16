package de.alphahelix.alphalibary.storage;

import java.io.Serializable;

public class StorageItem implements Serializable {

    private String coloumn;
    private Object value;

    public StorageItem(String coloumn, Object value) {
        this.coloumn = coloumn;
        this.value = value;
    }

    public StorageItem(Object value) {
        this.value = value;
    }

    public String getColoumn() {
        return coloumn;
    }

    public Object getValue() {
        return value;
    }
}
