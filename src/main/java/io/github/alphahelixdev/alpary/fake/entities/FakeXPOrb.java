package io.github.alphahelixdev.alpary.fake.entities;

import io.github.alphahelixdev.alpary.fake.Fake;
import io.github.alphahelixdev.alpary.fake.FakeEntity;
import io.github.alphahelixdev.alpary.reflection.nms.wrappers.EntityWrapper;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.whoisalphahelix.sql.annotations.Table;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Table("xporbs")
@EqualsAndHashCode(callSuper = true)
@ToString
public class FakeXPOrb extends FakeEntity {
	
	FakeXPOrb(String name, Location start, Object nmsEntity) {
		super(name, start, nmsEntity);
	}
	
	public static FakeXPOrb spawn(Player p, Location loc, String name) {
		FakeXPOrb fXO = spawnTemporary(p, loc, name);
		
		if(fXO == null)
			return null;
		
		Fake.storage(FakeXPOrb.class).addEntity(fXO);
		
		return fXO;
	}
	
	public static FakeXPOrb spawnTemporary(Player p, Location loc, String name) {
		Object orb = CustomSpawnable.XP_ORB.newInstance(false, Utils.nms().getWorldServer(p.getWorld()));
		EntityWrapper o = new EntityWrapper(orb);
		
		o.setLocation(loc);
		
		Utils.nms().sendPacket(p, CustomSpawnable.SPAWN_XP_ORB.newInstance(false, orb));
		
		FakeXPOrb fXO = new FakeXPOrb(name, loc, orb);
		
		Fake.getEntityHandler().addFakeEntity(p, fXO);
		
		return fXO;
	}
	
	public FakeXPOrb spawn(Player p) {
		return FakeXPOrb.spawnTemporary(p, getStart(), getName());
	}
}
