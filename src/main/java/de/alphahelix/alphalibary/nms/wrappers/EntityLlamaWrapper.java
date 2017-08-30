package de.alphahelix.alphalibary.nms.wrappers;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;

public class EntityLlamaWrapper extends EntityWrapper {

    private static final ReflectionUtil.SaveMethod SVAR = ReflectionUtil.getDeclaredMethod("setVariant", "EntityLlama",
            int.class);

    public EntityLlamaWrapper(Object entityLlama) {
        super(entityLlama, true);
    }

    public EntityLlamaWrapper(Object entityLlama, boolean stackTrace) {
        super(entityLlama, stackTrace);
    }

    public void setVariant(int variant) {
        SVAR.invoke(getEntity(), isStackTrace(), variant);
    }
}
