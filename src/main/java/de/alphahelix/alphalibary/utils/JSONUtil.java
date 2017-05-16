package de.alphahelix.alphalibary.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONUtil {

    private static Gson gson = new GsonBuilder().create();

    public static <T> String toJson(T toConvert) {
        return gson.toJson(toConvert);
    }

    public static <T> T getValue(String json, Class<T> definy) {
        return gson.fromJson(json, definy);
    }
}
