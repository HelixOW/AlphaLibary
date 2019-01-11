package io.github.alphahelixdev.alpary.tests;

import io.github.alphahelixdev.alpary.annotations.BukkitListener;
import io.github.alphahelixdev.alpary.fake.entities.FakeItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

@BukkitListener
public class FakeTest implements Listener {
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent e) {
		Player p = e.getPlayer();
		
		FakeItem.spawnItem(p, p.getLocation(), "uff", Material.PAPER);
	}
}
