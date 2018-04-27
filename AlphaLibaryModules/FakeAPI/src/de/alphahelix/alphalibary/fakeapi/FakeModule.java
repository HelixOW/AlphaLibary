package de.alphahelix.alphalibary.fakeapi;

import de.alphahelix.alphalibary.core.AlphaModule;
import de.alphahelix.alphalibary.core.Dependency;
import de.alphahelix.alphalibary.fakeapi.gui.dimensional.DimensionalGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.ItemStack;

@Dependency(dependencies = {
		"PacketListenerAPI", "StorageModule", "InventoryModule"
})
public class FakeModule implements AlphaModule {
	
	@Override
	public void enable() {
		Bukkit.getPluginManager().registerEvents(this, plugin());
	}
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent e) {
		
		new DimensionalGUI("test01", 0, new ItemStack(Material.GOLD_AXE), new ItemStack(Material.ANVIL)).open(e.getPlayer(), 3);
		
	}
}
