package de.alphahelix.alphalibary.nms.wrappers;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import org.bukkit.inventory.ItemStack;

public class EntityItemWrapper extends EntityWrapper {

    private static final ReflectionUtil.SaveMethod SITEMSTACK =
            ReflectionUtil.getDeclaredMethod("setItemStack", "EntityItem", ReflectionUtil.getNmsClass("ItemStack"));


    public EntityItemWrapper(Object entityItem, boolean stackTrace) {
        super(entityItem, stackTrace);
    }

    public EntityItemWrapper(Object entityItem) {
        super(entityItem, true);
    }

    public void setItemStack(ItemStack itemStack) {
        SITEMSTACK.invoke(getEntity(), isStackTrace(), ReflectionUtil.getNMSItemStack(itemStack));
    }
}
