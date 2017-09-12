package de.alphahelix.alphalibary.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.apache.commons.codec.binary.Base64;

import java.util.UUID;

public class SkinChangeUtil {

    private static final Base64 BASE64 = new Base64();

    public static GameProfile changeSkin(String url) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        PropertyMap propertyMap = profile.getProperties();

        if (propertyMap == null) {
            throw new IllegalStateException("Profile doesn't contain a property map");
        }

        byte[] encodedData = BASE64.encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        propertyMap.put("textures", new Property("textures", new String(encodedData)));

        return profile;
    }
}
