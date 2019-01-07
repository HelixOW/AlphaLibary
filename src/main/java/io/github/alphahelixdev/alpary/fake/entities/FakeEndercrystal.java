package io.github.alphahelixdev.alpary.fake.entities;

import io.github.alphahelixdev.alpary.fake.Fake;
import io.github.alphahelixdev.alpary.fake.FakeEntity;
import io.github.alphahelixdev.alpary.reflection.nms.packets.SpawnEntityPacket;
import io.github.alphahelixdev.alpary.reflection.nms.wrappers.EntityWrapper;
import io.github.alphahelixdev.alpary.utils.Utils;
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
		
		Fake.storage(FakeEndercrystal.class).addEntity(fE);
		
		return fE;
	}
	
	public static FakeEndercrystal spawnTemporary(Player p, Location loc, String name) {
		Object endercrystal = CustomSpawnable.ENDERCRYSTAL.newInstance(false, Utils.nms().getWorldServer(p.getWorld()));
		EntityWrapper e = new EntityWrapper(endercrystal);
		
		e.setLocation(loc);
		
		Utils.nms().sendPacket(p, new SpawnEntityPacket(endercrystal, 51, 0));
		
		FakeEndercrystal fE = new FakeEndercrystal(name, loc, endercrystal);
		
		Fake.getEntityHandler().addFakeEntity(p, fE);
		return fE;
	}
	
	public FakeEndercrystal spawn(Player p) {
		return FakeEndercrystal.spawnTemporary(p, getStart(), getName());
	}
	
}
