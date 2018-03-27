package de.alphahelix.alphalibary.reflection;

import de.alphahelix.alphalibary.core.AlphaLibary;
import de.alphahelix.alphalibary.core.AlphaModule;
import de.alphahelix.alphalibary.core.utils.abstracts.AbstractReflectionUtil;

public class ReflectionModule implements AlphaModule {
	
	@Override
	public void enable() {
		AlphaLibary.registerUtil(AbstractReflectionUtil.class, IReflectionUtil.class);
	}
}
