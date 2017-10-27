package de.alphahelix.alphalibary.storage.file.serializers;

public interface CSVSerializer<T> {

    String encode(T type);

    T decode(String csvEncodedLine);
}
