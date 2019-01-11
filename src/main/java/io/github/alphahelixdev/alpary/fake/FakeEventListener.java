package io.github.alphahelixdev.alpary.fake;

import io.github.alphahelixdev.alpary.Alpary;
import io.github.alphahelixdev.alpary.annotations.BukkitListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@BukkitListener
public class FakeEventListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		Alpary.getInstance().uuidFetcher().getUUID(p, uuid -> Fake.entityIDMap().put(p.getEntityId(), uuid));
		
		for(EntityStorage<? extends FakeEntity> storages : Fake.storage().values()) {
			storages.spawnEntities(p);
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		
		if(!Fake.getEntityHandler().seesNoEntities(p)) {
			for(FakeEntity see : Fake.getEntityHandler().getFakeEntites(p)) {
				see.cancelFollows(p).normalizeLook(p).cancelAllSplitted(p);
			}
			
			Fake.getEntityHandler().getFakeEntites(p).clear();
		}
	}
}
