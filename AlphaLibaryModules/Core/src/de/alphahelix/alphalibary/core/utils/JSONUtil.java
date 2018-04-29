package de.alphahelix.alphalibary.core.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import de.alphahelix.alphalibary.core.utils.abstracts.AbstractJsonUtil;


public interface JSONUtil {
	
	static void addTypeAdapter(Class<?> clazz, Object adapter) {
		AbstractJsonUtil.instance.addTypeAdapter(clazz, adapter);
	}
	
	static String toJson(Object toConvert) {
		return AbstractJsonUtil.instance.toJson(toConvert);
	}
	
	static String toJson(Object obj, Class<?> type) {
		return AbstractJsonUtil.instance.toJson(obj, type);
	}
	
	static <T> T getValue(String json, Class<T> definy) {
		return AbstractJsonUtil.instance.getValue(json, definy);
	}
	
	static <T> T getValue(JsonElement json, Class<?> definy) {
		return AbstractJsonUtil.instance.getValue(json, definy);
	}
	
	static void disableEscaping() {
		AbstractJsonUtil.instance.disableEscaping();
	}
	
	static Gson getGson() {
		return AbstractJsonUtil.getGson();
	}
}