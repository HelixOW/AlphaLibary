package io.github.alphahelixdev.alpary.fake.entities;

import com.mojang.authlib.GameProfile;
import io.github.alphahelixdev.alpary.fake.Fake;
import io.github.alphahelixdev.alpary.fake.FakeEntity;
import io.github.alphahelixdev.alpary.fake.FakeMobType;
import io.github.alphahelixdev.alpary.reflection.nms.enums.REnumEquipSlot;
import io.github.alphahelixdev.alpary.reflection.nms.packets.*;
import io.github.alphahelixdev.alpary.reflection.nms.wrappers.EntityAgeableWrapper;
import io.github.alphahelixdev.alpary.reflection.nms.wrappers.EntityWrapper;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.whoisalphahelix.sql.annotations.Column;
import io.github.whoisalphahelix.sql.annotations.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Table(name = "mobs")
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class FakeMob extends FakeEntity {
	
	@Column(name = "mobtype")
	private final FakeMobType fakeMobType;
	@Column(name = "baby")
	private final boolean baby;
	
	FakeMob(String name, Location start, Object nmsEntity, FakeMobType fakeMobType, boolean baby) {
		super(name, start, nmsEntity);
		this.fakeMobType = fakeMobType;
		this.baby = baby;
	}
	
	public static FakeMob spawn(Player p, Location loc, String name, FakeMobType type, boolean baby) {
		FakeMob fM = spawnTemporary(p, loc, name, type, baby);
		
		if(fM == null)
			return null;
		
		Fake.storage(FakeMob.class).addEntity(fM);
		
		return fM;
	}
	
	public static FakeMob spawnTemporary(Player p, Location loc, String name, FakeMobType type, boolean baby) {
		Object mob = type.getConstructor().newInstance(false, Utils.nms().getWorldServer(loc.getWorld()));
		EntityWrapper m = new EntityWrapper(mob);
		
		m.setLocation(loc);
		
		if(baby) {
			EntityAgeableWrapper mA = new EntityAgeableWrapper(mob);
			
			mA.setAge(-1);
			
			Utils.nms().sendPacket(p, new EntityMetaDataPacket(mA.getEntityID(), mA.getDataWatcher()));
		}
		
		Utils.nms().sendPackets(p, new SpawnEntityLivingPacket(mob),
				new EntityHeadRotationPacket(mob, loc.getYaw()),
				new EntityLookPacket(m.getEntityID(), loc.getYaw(), loc.getPitch(), true));
		
		FakeMob fM = new FakeMob(name, loc, mob, type, baby);
		
		Fake.getEntityHandler().addFakeEntity(p, fM);
		return fM;
	}
	
	public FakeMob spawn(Player p) {
		return FakeMob.spawnTemporary(p, getStart(), getName(), getFakeMobType(), isBaby());
	}
	
	public FakeMob equipSkull(Player p, String textureURL) {
		equip(p, Utils.skulls().getCustomSkull(textureURL), REnumEquipSlot.HELMET);
		return this;
	}
	
	public FakeMob equip(Player p, ItemStack item, REnumEquipSlot slot) {
		Utils.nms().sendPacket(p, new EntityEquipmentPacket(getEntityID(), item, slot));
		return this;
	}
	
	public FakeMob equipSkull(Player p, GameProfile profile) {
		equip(p, Utils.skulls().getPlayerSkull(profile.getName()), REnumEquipSlot.HELMET);
		return this;
	}
	
	public void stopRide(Player p) {
		EntityWrapper e = new EntityWrapper(getNmsEntity());
		
		e.stopRiding();
		
		Utils.nms().sendPacket(p, new MountPacket(getNmsEntity()));
	}
	
	public FakeMob ride(Player p, FakeEntity entity) {
		EntityWrapper e = new EntityWrapper(getNmsEntity());
		
		e.startRiding(entity.getNmsEntity());
		
		Utils.nms().sendPacket(p, new MountPacket(entity.getNmsEntity()));
		return this;
	}
	
	public FakeMob ride(Player p, Player toRide) {
		EntityWrapper e = new EntityWrapper(getNmsEntity());
		
		e.startRiding(Utils.nms().getCraftPlayer(toRide));
		
		Utils.nms().sendPacket(p, new MountPacket(Utils.nms().getCraftPlayer(p)));
		return this;
	}
}
