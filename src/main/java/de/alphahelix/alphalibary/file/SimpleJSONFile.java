package de.alphahelix.alphalibary.file;

import com.google.gson.*;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import de.alphahelix.alphalibary.utils.GameProfileBuilder;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SimpleJSONFile extends File {

    private static Gson gson = new GsonBuilder().registerTypeAdapter(Location.class, new LocationSerializer()).registerTypeAdapter(GameProfile.class, new GameProfileBuilder.GameProfileSerializer()).registerTypeAdapter(PropertyMap.class, new PropertyMap.Serializer()).create();
    private static JSONParser parser = new JSONParser();
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

    public void addValuesToList(String path, Object... value) {
        JSONArray array = new JSONArray();

        if (contains(path)) {
            array = getValue(path, JSONArray.class);
        }

        for (Object obj : value)
            array.add(obj);

        setValue(path, array);
    }

    public void removeValue(String path) {
        if (!contains(path)) return;

        head.remove(path);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this))) {
            writer.write(gson.toJson(head));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> ArrayList<T> getListValues(String path, Class<T> definy) {
        try {
            String file = FileUtils.readFileToString(this);

            if (file.isEmpty() || !(file.startsWith("{") || file.endsWith("}")))
                return new ArrayList<>();

            JSONObject obj = (JSONObject) parser.parse(FileUtils.readFileToString(this));
            ArrayList<T> typeList = new ArrayList<>();

            for (Object jsonType : (ArrayList<T>) obj.get(path)) {
                typeList.add(gson.fromJson(jsonType.toString(), definy));
            }

            return typeList;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public <T> T getValue(String path, Class<T> definy) {
        try {
            String file = FileUtils.readFileToString(this);

            if (file.isEmpty() || !(file.startsWith("{") || file.endsWith("}")))
                return null;

            JSONObject obj = (JSONObject) parser.parse(FileUtils.readFileToString(this));

            return gson.fromJson(obj.get(path).toString(), definy);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> ArrayList<T> getValues(Class<T> definy) {
        try {
            String file = FileUtils.readFileToString(this);

            if (file.isEmpty() || !(file.startsWith("{") || file.endsWith("}")))
                return new ArrayList<>();

            JSONObject obj = (JSONObject) parser.parse(file);
            ArrayList<T> list = new ArrayList<>();

            for (Object o : obj.keySet()) {
                list.add(gson.fromJson(obj.get(o.toString()).toString(), definy));
            }

            return list;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public ArrayList<String> getPaths() {
        try {
            String file = FileUtils.readFileToString(this);

            if (file.isEmpty() || !(file.startsWith("{") || file.endsWith("}")))
                return new ArrayList<>();

            JSONObject obj = (JSONObject) parser.parse(file);
            ArrayList<String> list = new ArrayList<>();

            for (Object o : obj.keySet()) {
                list.add(o.toString());
            }

            return list;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean contains(String path) {
        try {
            return FileUtils.readFileToString(this).contains(path);
        } catch (IOException e) {
            return false;
        }
    }

    public boolean isEmpty() {
        try {
            return FileUtils.readFileToString(this).isEmpty();
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
    }
}

class LocationSerializer implements JsonSerializer<Location>, JsonDeserializer<Location> {

    @Override
    public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = (JsonObject) json;

        String world = object.has("world") ? object.getAsJsonPrimitive("world").getAsString() : Bukkit.getWorlds().get(0).getName();
        double x = object.getAsJsonPrimitive("x").getAsDouble(), y = object.getAsJsonPrimitive("y").getAsDouble(), z = object.getAsJsonPrimitive("z").getAsDouble();
        float yaw = object.getAsJsonPrimitive("yaw").getAsFloat(), pitch = object.getAsJsonPrimitive("pitch").getAsFloat();

        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    @Override
    public JsonElement serialize(Location src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();

        if (src.getWorld() != null)
            result.addProperty("world", src.getWorld().getName());

        result.addProperty("x", src.getX());
        result.addProperty("y", src.getY());
        result.addProperty("z", src.getZ());
        result.addProperty("yaw", src.getYaw());
        result.addProperty("pitch", src.getPitch());

        return result;
    }
}