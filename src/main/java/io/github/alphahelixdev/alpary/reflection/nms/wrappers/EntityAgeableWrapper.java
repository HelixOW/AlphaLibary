package io.github.alphahelixdev.alpary.reflection.nms.wrappers;

import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveMethod;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString
public class EntityAgeableWrapper extends EntityWrapper {
	
	private static final SaveMethod ENTITY_AGEABLE_SET_AGE = NMSUtil.getReflections().getDeclaredMethod("setAge",
			Utils.nms().getNMSClass("EntityAgeable"),
			int.class);
	
	public EntityAgeableWrapper(Object entityAgeable, boolean stackTrace) {
		super(entityAgeable, stackTrace);
	}
	
	public EntityAgeableWrapper(Object entityAgeable) {
		super(entityAgeable, true);
	}
	
	public static SaveMethod getEntityAgeableSetAge() {
		return EntityAgeableWrapper.ENTITY_AGEABLE_SET_AGE;
	}
	
	public void setAge(int age) {
		EntityAgeableWrapper.getEntityAgeableSetAge().invoke(getEntity(), isStackTrace(), age);
	}
}
