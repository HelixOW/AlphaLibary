package de.alphahelix.alphalibary.inventories;

import de.alphahelix.alphalibary.core.AlphaModule;
import de.alphahelix.alphalibary.inventories.inventory.InventoryManager;

public class InventoryModule implements AlphaModule {
	
	@Override
	public void enable() {
		new InventoryManager();
	}
}
