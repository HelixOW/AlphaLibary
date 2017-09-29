package de.alphahelix.alphalibary.server.netty;

import com.google.gson.JsonElement;

public interface NettyCallback {
    void done(JsonElement data);
}
