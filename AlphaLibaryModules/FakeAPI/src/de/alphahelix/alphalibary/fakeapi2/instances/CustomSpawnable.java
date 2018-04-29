package de.alphahelix.alphalibary.fakeapi2.instances;

import de.alphahelix.alphalibary.core.utils.abstracts.AbstractReflectionUtil;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;

public interface CustomSpawnable {
	
	AbstractReflectionUtil.SaveConstructor ENTITY_ARMORSTAND = ReflectionUtil.getDeclaredConstructor("EntityArmorStand", ReflectionUtil.getNmsClass("World"));
	
	
}
