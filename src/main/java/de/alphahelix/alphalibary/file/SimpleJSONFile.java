package de.alphahelix.alphalibary.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SimpleJSONFile extends File {
    private JSONParser parser = new JSONParser();
    private JSONObject head = new JSONObject();


    public SimpleJSONFile(String parent, String child) {
        super(parent, child);
        if (!this.exists() && !isDirectory()) {
            try {
                getParentFile().mkdirs();
                createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setDefault(String path, Object value) {
        Gson gson = new GsonBuilder().create();
        Object enteredValue = value;

        if (value instanceof String)
            enteredValue = ((String) value).replace("§", "&");

        head.put(path, enteredValue);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this))) {
            writer.write(gson.toJson(head));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> T getValue(String path, Class<T> definy) {
        try {
            JSONObject obj = (JSONObject) parser.parse(FileUtils.readFileToString(this));
            Gson gson = new GsonBuilder().create();

            return gson.fromJson(obj.get(path).toString(), definy);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean contains(String path) {
        try {
            return FileUtils.readFileToString(this).contains(path);
        } catch (IOException e) {
            return false;
        }
    }
}
