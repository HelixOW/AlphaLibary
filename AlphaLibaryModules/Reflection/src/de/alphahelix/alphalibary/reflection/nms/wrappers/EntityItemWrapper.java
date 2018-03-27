package de.alphahelix.alphalibary.reflection.nms.wrappers;

import de.alphahelix.alphalibary.core.utils.abstracts.AbstractReflectionUtil;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import org.bukkit.inventory.ItemStack;

public class EntityItemWrapper extends EntityWrapper {
	
	private static final AbstractReflectionUtil.SaveMethod SITEMSTACK =
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
