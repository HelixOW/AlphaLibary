package io.github.alphahelixdev.alpary.reflection.nms.wrappers;

import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveMethod;
import org.bukkit.inventory.ItemStack;

public class EntityItemWrapper extends EntityWrapper {

    private static final SaveMethod ENTITY_ITEM_SET_ITEM_STACK = NMSUtil.getReflections()
            .getDeclaredMethod("setItemStack", Utils.nms().getNMSClass("EntityItem"),
                    Utils.nms().getNMSClass("ItemStack"));

    public EntityItemWrapper(Object entityItem, boolean stackTrace) {
        super(entityItem, stackTrace);
    }

    public EntityItemWrapper(Object entityItem) {
        super(entityItem, true);
    }

    public static SaveMethod getEntityItemSetItemStack() {
        return EntityItemWrapper.ENTITY_ITEM_SET_ITEM_STACK;
    }

    public void setItemStack(ItemStack itemStack) {
        EntityItemWrapper.getEntityItemSetItemStack().invoke(getEntity(), isStackTrace(),
                Utils.nms().getNMSItemStack(itemStack));
    }

    @Override
    public String toString() {
        return "EntityItemWrapper{}";
    }
}
