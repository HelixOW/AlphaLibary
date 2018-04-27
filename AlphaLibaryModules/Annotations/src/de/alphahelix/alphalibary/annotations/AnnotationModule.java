package de.alphahelix.alphalibary.annotations;

import de.alphahelix.alphalibary.core.AlphaModule;
import de.alphahelix.alphalibary.core.Dependency;

@Dependency(dependencies = {
		"InventoryModule"
})
public class AnnotationModule implements AlphaModule {
}
