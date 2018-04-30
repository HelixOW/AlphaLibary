package de.alphahelix.alphalibary.fakeapi2.instances;

import com.mojang.authlib.GameProfile;
import de.alphahelix.alphalibary.fakeapi2.FakeMobType;
import de.alphahelix.alphalibary.fakeapi2.FakeModule;
import de.alphahelix.alphalibary.inventories.item.SkullItemBuilder;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.reflection.nms.enums.REnumEquipSlot;
import de.alphahelix.alphalibary.reflection.nms.packets.*;
import de.alphahelix.alphalibary.reflection.nms.wrappers.EntityAgeableWrapper;
import de.alphahelix.alphalibary.reflection.nms.wrappers.EntityWrapper;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class FakeMob extends FakeEntity {
	
	private final FakeMobType fakeMobType;
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
		
		FakeModule.getStorage(FakeMob.class).addEntity(fM);
		
		return fM;
	}
	
	public static FakeMob spawnTemporary(Player p, Location loc, String name, FakeMobType type, boolean baby) {
		Object mob = type.getConstructor().newInstance(false, ReflectionUtil.getWorldServer(loc.getWorld()));
		EntityWrapper m = new EntityWrapper(mob);
		
		m.setLocation(loc);
		
		if(baby) {
			EntityAgeableWrapper mA = new EntityAgeableWrapper(mob);
			
			mA.setAge(-1);
			
			ReflectionUtil.sendPacket(p, new PPOEntityMetadata(mA.getEntityID(), mA.getDataWatcher()));
		}
		
		ReflectionUtil.sendPacket(p, new PPOSpawnEntityLiving(mob));
		ReflectionUtil.sendPacket(p, new PPOEntityHeadRotation(mob, loc.getYaw()));
		ReflectionUtil.sendPacket(p, new PPOEntityLook(m.getEntityID(), loc.getYaw(), loc.getPitch(), true));
		
		FakeMob fM = new FakeMob(name, loc, mob, type, baby);
		
		FakeModule.getEntityHandler().addFakeEntity(p, fM);
		return fM;
	}
	
	public FakeMob spawn(Player p) {
		return FakeMob.spawnTemporary(p, getStart(), getName(), getFakeMobType(), isBaby());
	}
	
	public FakeMobType getFakeMobType() {
		return fakeMobType;
	}
	
	public boolean isBaby() {
		return baby;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), fakeMobType, baby);
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		if(!super.equals(o)) return false;
		FakeMob fakeMob = (FakeMob) o;
		return baby == fakeMob.baby &&
				fakeMobType == fakeMob.fakeMobType;
	}
	
	@Override
	public String toString() {
		return "FakeMob{" +
				"fakeMobType=" + fakeMobType +
				", baby=" + baby +
				'}';
	}
	
	public FakeMob equipSkull(Player p, String textureURL) {
		equip(p, SkullItemBuilder.getCustomSkull(textureURL), REnumEquipSlot.HELMET);
		return this;
	}
	
	public FakeMob equip(Player p, ItemStack item, REnumEquipSlot slot) {
		ReflectionUtil.sendPacket(p, new PPOEntityEquipment(
				getEntityID(),
				item,
				slot
		));
		return this;
	}
	
	public FakeMob equipSkull(Player p, GameProfile profile) {
		equip(p, SkullItemBuilder.getPlayerSkull(profile.getName()), REnumEquipSlot.HELMET);
		return this;
	}
	
	public void stopRide(Player p) {
		EntityWrapper e = new EntityWrapper(getNmsEntity());
		
		e.stopRiding();
		
		ReflectionUtil.sendPacket(p, new PPOMount(getNmsEntity()));
	}
	
	public FakeMob ride(Player p, FakeEntity entity) {
		EntityWrapper e = new EntityWrapper(getNmsEntity());
		
		e.startRiding(entity.getNmsEntity());
		
		ReflectionUtil.sendPacket(p, new PPOMount(entity.getNmsEntity()));
		return this;
	}
	
	public FakeMob ride(Player p, Player toRide) {
		EntityWrapper e = new EntityWrapper(getNmsEntity());
		
		e.startRiding(ReflectionUtil.getEntityPlayer(toRide));
		
		ReflectionUtil.sendPacket(p, new PPOMount(ReflectionUtil.getEntityPlayer(p)));
		return this;
	}
}
