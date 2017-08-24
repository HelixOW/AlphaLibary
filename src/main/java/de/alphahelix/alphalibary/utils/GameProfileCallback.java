package de.alphahelix.alphalibary.utils;

import com.mojang.authlib.GameProfile;

public interface GameProfileCallback {
    void done(GameProfile gameProfile);
}
