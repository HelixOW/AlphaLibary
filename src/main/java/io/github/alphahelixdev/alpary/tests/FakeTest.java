package io.github.alphahelixdev.alpary.tests;

import io.github.alphahelixdev.alpary.annotations.BukkitListener;
import io.github.alphahelixdev.alpary.fake.entities.FakeArmorstand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

@BukkitListener
public class FakeTest implements Listener {

    @EventHandler
    public void onChat(PlayerChatEvent e) {
        Player p = e.getPlayer();

        FakeArmorstand fa = FakeArmorstand.spawnTemporary(p, p.getLocation(), "§aU suck");

    }

}
