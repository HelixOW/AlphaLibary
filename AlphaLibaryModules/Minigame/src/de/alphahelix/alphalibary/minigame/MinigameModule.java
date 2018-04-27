package de.alphahelix.alphalibary.minigame;

import de.alphahelix.alphalibary.core.AlphaModule;
import de.alphahelix.alphalibary.core.Dependency;

@Dependency(dependencies = {
		"PacketListenerAPI", "StorageModule", "InventoryModule"
})
public class MinigameModule implements AlphaModule {
}
