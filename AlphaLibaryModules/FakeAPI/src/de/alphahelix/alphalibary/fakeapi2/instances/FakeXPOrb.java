package de.alphahelix.alphalibary.fakeapi2.instances;

import de.alphahelix.alphalibary.fakeapi2.FakeModule;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.reflection.nms.packets.PPOEntityMetadata;
import de.alphahelix.alphalibary.reflection.nms.wrappers.EntityWrapper;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FakeXPOrb extends FakeEntity {
	
	FakeXPOrb(String name, Location start, Object nmsEntity) {
		super(name, start, nmsEntity);
	}
	
	public static FakeXPOrb spawn(Player p, Location loc, String name) {
		FakeXPOrb fXO = spawnTemporary(p, loc, name);
		
		if(fXO == null)
			return null;
		
		FakeModule.getStorage(FakeXPOrb.class).addEntity(fXO);
		
		return fXO;
	}
	
	public static FakeXPOrb spawnTemporary(Player p, Location loc, String name) {
		Object orb = CustomSpawnable.XP_ORB.newInstance(false, ReflectionUtil.getWorldServer(p.getWorld()));
		EntityWrapper o = new EntityWrapper(orb);
		
		o.setLocation(loc);
		
		ReflectionUtil.sendPacket(p, CustomSpawnable.SPAWN_XP_ORB.newInstance(false, orb));
		
		FakeXPOrb fXO = new FakeXPOrb(name, loc, orb);
		
		FakeModule.getEntityHandler().addFakeEntity(p, fXO);
		
		return fXO;
	}
	
	public FakeXPOrb spawn(Player p) {
		return FakeXPOrb.spawnTemporary(p, getStart(), getName());
	}
	
	public FakeXPOrb setOrbname(Player p, String name) {
		EntityWrapper o = new EntityWrapper(getNmsEntity());
		
		o.setCustomName(name.replace("&", "§").replace("_", " "));
		o.setCustomNameVisible(true);
		
		ReflectionUtil.sendPacket(p, new PPOEntityMetadata(getEntityID(), o.getDataWatcher()));
		return this;
	}
}
