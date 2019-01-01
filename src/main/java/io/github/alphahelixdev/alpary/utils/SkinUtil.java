package io.github.alphahelixdev.alpary.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import java.util.Base64;
import java.util.UUID;

public class SkinUtil {

    private final Base64.Encoder base64Encoder = Base64.getEncoder();

    public GameProfile changeSkin(String url) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        PropertyMap propertyMap = profile.getProperties();

        if (propertyMap == null)
            throw new IllegalStateException("Profile doesn't contain a property map");

        byte[] encodedData = base64Encoder.encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        propertyMap.put("textures", new Property("textures", new String(encodedData)));

        return profile;
    }

}