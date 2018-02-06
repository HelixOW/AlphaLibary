package de.alphahelix.alphalibary.storage.file;

import de.alphahelix.alphalibary.storage.file.serializers.CSVSerializer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class SimpleCSVFile<T> extends AbstractFile {

    private List<String> csvs = new ArrayList<>();
    private CSVSerializer<T> serializer;

    public SimpleCSVFile(String parent, String child, CSVSerializer<T> serializer) {
        super(parent, child);
        this.serializer = serializer;
    }

    public SimpleCSVFile(File parent, String child, CSVSerializer<T> serializer) {
        super(parent, child);
        this.serializer = serializer;
    }

    public SimpleCSVFile(URI uri, CSVSerializer<T> serializer) {
        super(uri);
        this.serializer = serializer;
    }

    public SimpleCSVFile(JavaPlugin plugin, String child, CSVSerializer<T> serializer) {
        super(plugin.getDataFolder().getAbsolutePath(), child);
        this.serializer = serializer;
    }

    public SimpleCSVFile(AbstractFile file, CSVSerializer<T> serializer) {
        super(file);
        this.serializer = serializer;
    }

    public void setValue(T value) {
        csvs.add(serializer.encode(value));

        update();
    }

    public void removeValue(T value) {
        csvs.remove(serializer.encode(value));

        update();
    }

    public List<T> getValues() {
        List<T> vals = new ArrayList<>();

        csvs.forEach(s -> vals.add(serializer.decode(s)));

        return vals;
    }

    public boolean hasValue(T value, CSVSerializer<T> serializer) {
        return csvs.contains(serializer.encode(value));
    }

    private void update() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this))) {
            for (String line : csvs) {
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        csvs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(this))) {
            reader.lines().forEach(s -> csvs.add(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
