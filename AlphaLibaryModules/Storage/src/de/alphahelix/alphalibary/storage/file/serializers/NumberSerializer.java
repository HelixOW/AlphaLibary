package de.alphahelix.alphalibary.storage.file.serializers;

public class NumberSerializer implements CSVSerializer<Number> {
    @Override
    public String encode(Number type) {
        return type.toString();
    }

    @Override
    public Number decode(String csvEncodedLine) {
        return new Number() {
            @Override
            public int intValue() {
                return Integer.parseInt(csvEncodedLine);
            }

            @Override
            public long longValue() {
                return Long.parseLong(csvEncodedLine);
            }

            @Override
            public float floatValue() {
                return Float.parseFloat(csvEncodedLine);
            }

            @Override
            public double doubleValue() {
                return Double.parseDouble(csvEncodedLine);
            }
        };
    }
}
