package de.alphahelix.alphalibary.storage.file2.yaml;

import java.io.*;

public abstract class FileConfig extends ConfigSection {

    public FileConfig() {
        super(null, "", null);
    }

    public void save(File file) throws IOException {
        if (file == null)
            return;

        file.getParentFile().mkdirs();

        String data = saveToString();

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(data);
        }
    }

    public void save(String filePath) throws IOException {
        if (filePath == null || filePath.equals(""))
            return;

        save(new File(filePath));
    }

    public void load(File file) throws IOException {
        load(new FileInputStream(file));
    }

    public void load(InputStream stream) throws IOException {
        if (stream == null)
            return;

        InputStreamReader reader = new InputStreamReader(stream);
        StringBuilder builder = new StringBuilder();

        try (BufferedReader input = new BufferedReader(reader)) {
            String line;

            while ((line = input.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }

        }

        loadFromString(builder.toString());
    }

    public void load(String file) throws IOException {
        if (file == null || file.equals(""))
            return;

        load(new File(file));
    }

    public abstract String saveToString();

    public abstract void loadFromString(String contents);
}
