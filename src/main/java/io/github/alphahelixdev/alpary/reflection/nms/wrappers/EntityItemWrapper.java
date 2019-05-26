package io.github.alphahelixdev.alpary.reflection.nms.wrappers;

import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.whoisalphahelix.helix.reflection.SaveMethod;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.inventory.ItemStack;

@EqualsAndHashCode(callSuper = true)
@ToString
public class EntityItemWrapper extends EntityWrapper {

    private static final SaveMethod ENTITY_ITEM_SET_ITEM_STACK = Utils.nms()
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
}
