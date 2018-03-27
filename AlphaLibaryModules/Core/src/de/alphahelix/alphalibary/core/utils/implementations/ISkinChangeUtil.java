package de.alphahelix.alphalibary.core.utils.implementations;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import de.alphahelix.alphalibary.core.utils.abstracts.AbstractSkinChangeUtil;

import java.util.UUID;

public class ISkinChangeUtil extends AbstractSkinChangeUtil {
	
	public GameProfile changeSkin(String url) {
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		PropertyMap propertyMap = profile.getProperties();
		
		if(propertyMap == null) {
			throw new IllegalStateException("Profile doesn't contain a property map");
		}
		
		byte[] encodedData = getBASE64().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
		propertyMap.put("textures", new Property("textures", new String(encodedData)));
		
		return profile;
	}
}
