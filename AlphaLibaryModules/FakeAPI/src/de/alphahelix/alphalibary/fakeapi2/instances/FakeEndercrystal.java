package de.alphahelix.alphalibary.fakeapi2.instances;

import de.alphahelix.alphalibary.fakeapi2.FakeModule;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.reflection.nms.packets.PPOEntityMetadata;
import de.alphahelix.alphalibary.reflection.nms.packets.PPOSpawnEntity;
import de.alphahelix.alphalibary.reflection.nms.wrappers.EntityWrapper;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FakeEndercrystal extends FakeEntity {
	FakeEndercrystal(String name, Location start, Object nmsEntity) {
		super(name, start, nmsEntity);
	}
	
	public static FakeEndercrystal spawn(Player p, Location loc, String name) {
		FakeEndercrystal fE = spawnTemporary(p, loc, name);
		
		if(fE == null)
			return null;
		
		FakeModule.getStorage(FakeEndercrystal.class).addEntity(fE);
		
		return fE;
	}
	
	public static FakeEndercrystal spawnTemporary(Player p, Location loc, String name) {
		Object endercrystal = CustomSpawnable.ENDERCRYSTAL.newInstance(false, ReflectionUtil.getWorldServer(p.getWorld()));
		EntityWrapper e = new EntityWrapper(endercrystal);
		
		e.setLocation(loc);
		
		ReflectionUtil.sendPacket(p, new PPOSpawnEntity(endercrystal, 51, 0));
		
		FakeEndercrystal fE = new FakeEndercrystal(name, loc, endercrystal);
		
		FakeModule.getEntityHandler().addFakeEntity(p, fE);
		return fE;
	}
	
	public FakeEndercrystal spawn(Player p) {
		return FakeEndercrystal.spawnTemporary(p, getStart(), getName());
	}
	
	public FakeEndercrystal setName(Player p, String name) {
		EntityWrapper e = new EntityWrapper(getNmsEntity());
		
		e.setCustomName(name.replace("&", "§").replace("_", " "));
		e.setCustomNameVisible(true);
		
		ReflectionUtil.sendPacket(p, new PPOEntityMetadata(e.getEntityID(), e.getDataWatcher()));
		return this;
	}
}
