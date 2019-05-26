package io.github.alphahelixdev.alpary.reflection.nms.wrappers;

import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.whoisalphahelix.helix.reflection.SaveMethod;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString
public class EntityLlamaWrapper extends EntityWrapper {

    private static final SaveMethod ENTITY_LLAMA_SET_VARIANT = Utils.nms().getDeclaredMethod(
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
}
