package io.github.alphahelixdev.alpary.fake.entities;

import com.mojang.authlib.GameProfile;
import io.github.alphahelixdev.alpary.fake.Fake;
import io.github.alphahelixdev.alpary.fake.FakeEntity;
import io.github.alphahelixdev.alpary.reflection.nms.enums.REquipSlot;
import io.github.alphahelixdev.alpary.reflection.nms.packets.EntityEquipmentPacket;
import io.github.alphahelixdev.alpary.reflection.nms.packets.EntityMetaDataPacket;
import io.github.alphahelixdev.alpary.reflection.nms.packets.SpawnEntityLivingPacket;
import io.github.alphahelixdev.alpary.reflection.nms.wrappers.EntityWrapper;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.whoisalphahelix.sql.annotations.Table;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Table("armorstands")
@EqualsAndHashCode(callSuper = true)
@ToString
public class FakeArmorstand extends FakeEntity {
	
	FakeArmorstand(String name, Location start, Object nmsEntity) {
		super(name, start, nmsEntity);
	}
	
	public static FakeArmorstand spawn(Player p, Location loc, String name) {
		FakeArmorstand fakeA = spawnTemporary(p, loc, name);
		
		if(fakeA == null)
			return null;
		
		Fake.storage(FakeArmorstand.class).addEntity(fakeA);
		
		return fakeA;
	}
	
	public static FakeArmorstand spawnTemporary(Player p, Location loc, String name) {
		Object armorstand = CustomSpawnable.ARMORSTAND.newInstance(false,
				Utils.nms().getWorldServer(loc.getWorld()));
		EntityWrapper aW = new EntityWrapper(armorstand);
		
		aW.setLocation(loc);
		aW.setInvisible(true);
		aW.setCustomName(name);
		aW.setCustomNameVisible(true);
		
		Utils.nms().sendPacket(p, new SpawnEntityLivingPacket(armorstand).getPacket(false));
		
		FakeArmorstand fA = new FakeArmorstand(name, loc, armorstand);
		
		Fake.getEntityHandler().addFakeEntity(p, fA);
		
		return fA;
	}
	
	public FakeArmorstand spawn(Player p) {
		return FakeArmorstand.spawnTemporary(p, getStart(), getName());
	}
	
	public FakeArmorstand equipSkull(Player p, String textureURL) {
        return this.equip(p, Utils.skulls().getCustomSkull(textureURL), REquipSlot.HELMET);
	}

    public FakeArmorstand equip(Player p, ItemStack item, REquipSlot slot) {
		Utils.nms().sendPacket(p, new EntityEquipmentPacket(this.getEntityID(), item, slot).getPacket(false));
		return this;
	}
	
	public FakeArmorstand equipSkull(Player p, GameProfile profile) {
        return this.equip(p, Utils.skulls().getPlayerSkull(profile.getName()), REquipSlot.HELMET);
	}
	
	public FakeArmorstand changeVisibilty(Player p, boolean visible) {
		EntityWrapper a = new EntityWrapper(getNmsEntity());
		
		a.setInvisible(!visible);
		
		Object dw = a.getDataWatcher();
		
		Utils.nms().sendPacket(p, new EntityMetaDataPacket(this.getEntityID(), dw).getPacket(false));
		return this;
	}
}
