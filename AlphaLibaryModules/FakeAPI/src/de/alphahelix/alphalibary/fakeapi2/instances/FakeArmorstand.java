package de.alphahelix.alphalibary.fakeapi2.instances;

import com.mojang.authlib.GameProfile;
import de.alphahelix.alphalibary.fakeapi2.FakeModule;
import de.alphahelix.alphalibary.inventories.item.SkullItemBuilder;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.reflection.nms.enums.REnumEquipSlot;
import de.alphahelix.alphalibary.reflection.nms.packets.PPOEntityEquipment;
import de.alphahelix.alphalibary.reflection.nms.packets.PPOEntityMetadata;
import de.alphahelix.alphalibary.reflection.nms.packets.PPOSpawnEntityLiving;
import de.alphahelix.alphalibary.reflection.nms.wrappers.EntityWrapper;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FakeArmorstand extends FakeEntity {
	FakeArmorstand(String name, Location start, Object nmsEntity) {
		super(name, start, nmsEntity);
	}
	
	public static FakeArmorstand spawn(Player p, Location loc, String name) {
		FakeArmorstand fakeA = spawnTemporary(p, loc, name);
		
		if(fakeA == null)
			return null;
		
		FakeModule.getStorage(FakeArmorstand.class).addEntity(fakeA);
		
		return fakeA;
	}
	
	public FakeArmorstand spawn(Player p) {
		return FakeArmorstand.spawnTemporary(p, getStart(), getName());
	}
	
	public static FakeArmorstand spawnTemporary(Player p, Location loc, String name) {
		Object armorstand = CustomSpawnable.ARMORSTAND.newInstance(false, ReflectionUtil.getWorldServer(loc.getWorld()));
		EntityWrapper aW = new EntityWrapper(armorstand);
		
		aW.setLocation(loc);
		aW.setInvisible(true);
		aW.setCustomName(name);
		aW.setCustomNameVisible(true);
		
		ReflectionUtil.sendPacket(p, new PPOSpawnEntityLiving(armorstand).getPacket(false));
		
		FakeArmorstand fA = new FakeArmorstand(name, loc, armorstand);
		
		FakeModule.getEntityHandler().addFakeEntity(p, fA);
		
		return fA;
	}
	
	public FakeArmorstand equipSkull(Player p, String textureURL) {
		return equip(p, SkullItemBuilder.getCustomSkull(textureURL), REnumEquipSlot.HELMET);
	}
	
	public FakeArmorstand equip(Player p, ItemStack item, REnumEquipSlot slot) {
		ReflectionUtil.sendPacket(p, new PPOEntityEquipment(
				getEntityID(),
				item, slot
		).getPacket(false));
		return this;
	}
	
	public FakeArmorstand equipSkull(Player p, GameProfile profile) {
		return equip(p, SkullItemBuilder.getPlayerSkull(profile.getName()), REnumEquipSlot.HELMET);
	}
	
	public FakeArmorstand setName(Player p, String name) {
		EntityWrapper a = new EntityWrapper(getNmsEntity());
		
		a.setCustomName(name.replace("&", "§").replace("_", " "));
		
		Object dw = a.getDataWatcher();
		
		ReflectionUtil.sendPacket(p, new PPOEntityMetadata(
				getEntityID(),
				dw
		).getPacket(false));
		return this;
	}
	
	public FakeArmorstand changeVisibilty(Player p, boolean visible) {
		EntityWrapper a = new EntityWrapper(getNmsEntity());
		
		a.setInvisible(!visible);
		
		Object dw = a.getDataWatcher();
		
		ReflectionUtil.sendPacket(p, new PPOEntityMetadata(
				getEntityID(),
				dw
		).getPacket(false));
		return this;
	}
}
