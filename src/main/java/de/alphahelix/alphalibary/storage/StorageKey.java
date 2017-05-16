package de.alphahelix.alphalibary.storage;

public class StorageKey {

    private String coloumn;
    private String value;

    public StorageKey(String coloumn, String value) {
        this.coloumn = coloumn;
        this.value = value;
    }

    public StorageKey(String value) {
        this.value = value;
    }

    public String getColoumn() {
        return coloumn;
    }

    public String getValue() {
        return value;
    }
}
