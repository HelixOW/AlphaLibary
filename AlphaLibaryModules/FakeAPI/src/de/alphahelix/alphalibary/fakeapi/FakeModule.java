package de.alphahelix.alphalibary.fakeapi;

import de.alphahelix.alphalibary.core.AlphaModule;
import de.alphahelix.alphalibary.core.Dependency;

@Dependency(dependencies = {
		"PacketListenerAPI", "StorageModule", "InventoryModule"
})
public class FakeModule implements AlphaModule {
	
}
