package de.alphahelix.alphalibary.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONUtil {

    private JSONParser parser = new JSONParser();
    private JSONObject head = new JSONObject();
    private Gson gson = new GsonBuilder().create();

    public <T> String toJson(T toConvert) {
        return gson.toJson(toConvert);
    }

    public <T> T getValue(String json, Class<T> definy) {
        return gson.fromJson(json, definy);
    }


}
