package de.alphahelix.alphalibary.inventories.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class IDHolder implements InventoryHolder {
	
	private final String id;
	private Inventory inv;
	
	public IDHolder(String id) {
		this.id = id;
	}
	
	@Override
	public Inventory getInventory() {
		return inv;
	}
	
	public String getId() {
		return id;
	}
}
