package de.alphahelix.alphalibary.reflection.nms;

import com.mojang.authlib.GameProfile;

public interface GameProfileCallback {
    void done(GameProfile gameProfile);
}
