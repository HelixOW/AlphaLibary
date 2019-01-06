package io.github.alphahelixdev.alpary.fake;

public class Fake {

    private static final EntityHandler ENTITY_HANDLER = new EntityHandler();

    public static EntityHandler getEntityHandler() {
        return ENTITY_HANDLER;
    }
}
