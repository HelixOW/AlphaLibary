package de.alphahelix.alphalibary.server.netty;

import com.google.gson.JsonElement;

public interface RequestProcessor {
    JsonElement getProcessedData();
}
