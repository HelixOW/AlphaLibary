package io.github.alphahelixdev.alpary.reflection.nms.wrappers;

import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveMethod;

public class EntityLlamaWrapper extends EntityWrapper {

    private static final SaveMethod ENTITY_LLAMA_SET_VARIANT = NMSUtil.getReflections().getDeclaredMethod(
            "setVariant", Utils.nms().getNMSClass("EntityLlama"), int.class);

    public EntityLlamaWrapper(Object entityLlama) {
        super(entityLlama, true);
    }

    public EntityLlamaWrapper(Object entityLlama, boolean stackTrace) {
        super(entityLlama, stackTrace);
    }

    public static SaveMethod getEntityLlamaSetVariant() {
        return EntityLlamaWrapper.ENTITY_LLAMA_SET_VARIANT;
    }

    public void setVariant(int variant) {
        EntityLlamaWrapper.getEntityLlamaSetVariant().invoke(getEntity(), isStackTrace(), variant);
    }

    @Override
    public String toString() {
        return "EntityLlamaWrapper{}";
    }
}
