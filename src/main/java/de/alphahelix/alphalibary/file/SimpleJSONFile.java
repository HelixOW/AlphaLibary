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
import java.util.ArrayList;

public class SimpleJSONFile extends File {

    private JSONParser parser = new JSONParser();
    private JSONObject head = new JSONObject();
    private Gson gson = new GsonBuilder().create();


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

    public void setValue(String path, Object value) {
        setDefault(path, value);
    }

    public void removeValue(String path) {
        head.remove(path);

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

    public <T> ArrayList<T> getValues(Class<T> definy) {
        try {
            JSONObject obj = (JSONObject) parser.parse(FileUtils.readFileToString(this));
            ArrayList<T> list = new ArrayList<>();

            for (Object o : obj.keySet()) {
                list.add(gson.fromJson(obj.get(o.toString()).toString(), definy));
            }

            return list;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getPaths() {
        try {
            JSONObject obj = (JSONObject) parser.parse(FileUtils.readFileToString(this));
            ArrayList<String> list = new ArrayList<>();

            for (Object o : obj.keySet()) {
                list.add(o.toString());
            }

            return list;
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
