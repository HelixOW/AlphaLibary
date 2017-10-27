package de.alphahelix.alphalibary.storage.file.serializers;

public class BooleanSerializer implements CSVSerializer<Boolean> {
    @Override
    public String encode(Boolean type) {
        return type.toString();
    }

    @Override
    public Boolean decode(String csvEncodedLine) {
        return Boolean.parseBoolean(csvEncodedLine);
    }
}
