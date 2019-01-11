package io.github.alphahelixdev.alpary.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.alphahelixdev.alpary.Alpary;

public class JsonUtil extends io.github.alphahelixdev.helius.utils.JsonUtil {
	
	private final JsonParser parser = new JsonParser();
	
	public JsonElement toJsonTree(Object obj) {
		JsonObject head = new JsonObject();
		
		head.add("body", Alpary.getInstance().gson().toJsonTree(obj));
		head.addProperty("type", obj.getClass().getName());
		
		return head;
	}
	
	public Object fromJsonTree(String json) {
		JsonObject obj = (JsonObject) parser.parse(json);
		
		try {
			return Alpary.getInstance().gson().fromJson(obj.get("body"), Class.forName(obj.get("type").getAsString()));
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private class JsonTreeWrapper {
		private Object obj;
		
		public JsonTreeWrapper(Object obj) {
			this.obj = obj;
		}
	}
}
