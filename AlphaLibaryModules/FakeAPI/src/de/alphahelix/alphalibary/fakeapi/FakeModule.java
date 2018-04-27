package de.alphahelix.alphalibary.fakeapi;

import de.alphahelix.alphalibary.core.AlphaModule;
import de.alphahelix.alphalibary.core.Dependency;
import org.bukkit.Bukkit;

@Dependency(dependencies = {
		"PacketListenerAPI", "StorageModule", "InventoryModule"
})
public class FakeModule implements AlphaModule {
	
	@Override
	public void enable() {
		Bukkit.getPluginManager().registerEvents(this, plugin());
	}
	
}
