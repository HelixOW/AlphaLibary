package de.alphahelix.alphalibary.fakeapi2;

import de.alphahelix.alphalibary.core.SimpleLoader;
import de.alphahelix.alphalibary.core.utilites.SimpleListener;
import de.alphahelix.alphalibary.core.utilites.UUIDFetcher;
import de.alphahelix.alphalibary.fakeapi2.instances.FakeEntity;
import de.alphahelix.alphalibary.fakeapi2.storage.EntityStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


@SimpleLoader
public class FakeEventListener extends SimpleListener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		
		UUIDFetcher.getUUID(p, uuid -> FakeModule.getEntityId().put(p.getEntityId(), uuid));
		
		for(EntityStorage<? extends FakeEntity> storages : FakeModule.getStorage().values()) {
			for(FakeEntity entity : storages.getEntities()) {
				entity.spawn(p);
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		
		if(!FakeModule.getEntityHandler().seesNoEntities(p)) {
			for(FakeEntity see : FakeModule.getEntityHandler().getFakeEntites(p)) {
				see.unFollow(p).normalizeLook(p).cancelAllSplittedTasks(p);
			}
			
			FakeModule.getEntityHandler().getFakeEntites(p).clear();
		}
	}
}
