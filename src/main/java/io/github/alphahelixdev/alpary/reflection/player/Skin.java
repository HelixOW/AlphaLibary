package io.github.alphahelixdev.alpary.reflection.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.*;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class Skin {
	
	private static final JsonParser PARSER = new JsonParser();
	
	private final String uuid;
	private String skinName;
	private String skinValue;
	private String skinSignature;
	
	public Skin(UUID uuid) throws IOException {
		this.uuid = uuid.toString();
		this.load();
	}
	
	private void load() throws IOException {
		URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + this.uuid + "?unsigned=false");
		URLConnection uc = url.openConnection();
		
		uc.setUseCaches(false);
		uc.setDefaultUseCaches(false);
		uc.addRequestProperty("User-Agent", "Mozilla/5.0");
		uc.addRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
		uc.addRequestProperty("Pragma", "no-cache");
		
		String json = new Scanner(uc.getInputStream(), "UTF-8").useDelimiter("\\A").next();
		Object obj = PARSER.parse(json);
		JsonArray properties = (JsonArray) ((JsonObject) obj).get("properties");
		
		for (JsonElement propertyElement : properties) {
			JsonObject property = (JsonObject) propertyElement;
			this.skinName = property.get("name").getAsString();
			this.skinValue = property.get("skinValue").getAsString();
			this.skinSignature = property.has("skinSignature") ? property.get("skinSignature").getAsString() : null;
		}
	}
	
}
