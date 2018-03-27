package de.alphahelix.alphalibary.core.utils.implementations;

import com.google.gson.JsonElement;
import de.alphahelix.alphalibary.core.utils.abstracts.AbstractJsonUtil;

public class IJSONUtil extends AbstractJsonUtil {
	
	public void addTypeAdapter(Class<?> clazz, Object adapter) {
		getBuilder().registerTypeHierarchyAdapter(clazz, adapter);
		setGson(getBuilder().create());
	}
	
	public String toJson(Object toConvert) {
		return getGson().toJson(toConvert);
	}
	
	public String toJson(Object obj, Class<?> type) {
		return getGson().toJson(obj, type);
	}
	
	public <T> T getValue(String json, Class<T> definy) {
		return getGson().fromJson(json, definy);
	}
	
	public <T> T getValue(JsonElement json, Class<?> definy) {
		return getGson().fromJson(json, (Class<T>) definy);
	}
	
	public void disableEscaping() {
		setGson(getBuilder().disableHtmlEscaping().create());
	}
}