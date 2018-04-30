package de.alphahelix.alphalibary.reflection.nms.wrappers;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;

public class EntityAgeableWrapper extends EntityWrapper {
	
	private static final ReflectionUtil.SaveMethod SAGE = ReflectionUtil.getDeclaredMethod("setAge", "EntityAgeable",
			int.class);
	
	public EntityAgeableWrapper(Object entityAgeable, boolean stackTrace) {
		super(entityAgeable, stackTrace);
	}
	
	public EntityAgeableWrapper(Object entityAgeable) {
		super(entityAgeable, true);
	}
	
	public void setAge(int age) {
		SAGE.invoke(getEntity(), isStackTrace(), age);
	}
}
