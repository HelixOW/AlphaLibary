package de.alphahelix.alphalibary.storage.file.serializers;

public class StringSerializer implements CSVSerializer<String> {
    @Override
    public String encode(String type) {
        return type;
    }

    @Override
    public String decode(String csvEncodedLine) {
        return csvEncodedLine;
    }
}
