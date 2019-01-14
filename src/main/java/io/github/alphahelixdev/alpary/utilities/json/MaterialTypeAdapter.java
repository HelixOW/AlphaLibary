package io.github.alphahelixdev.alpary.utilities.json;

import com.google.gson.*;
import org.bukkit.Material;

import java.lang.reflect.Type;

public class MaterialTypeAdapter implements JsonSerializer<Material>, JsonDeserializer<Material> {
	@Override
	public Material deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		JsonObject obj = (JsonObject) jsonElement;
		
		return Material.getMaterial(obj.get("material").getAsString().toUpperCase());
	}
	
	@Override
	public JsonElement serialize(Material material, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject obj = new JsonObject();
		
		obj.addProperty("material", material.getKey().getKey());
		
		return obj;
	}
}
