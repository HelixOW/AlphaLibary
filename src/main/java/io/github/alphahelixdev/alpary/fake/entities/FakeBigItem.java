package io.github.alphahelixdev.alpary.fake.entities;

import io.github.alphahelixdev.alpary.fake.Fake;
import io.github.alphahelixdev.alpary.fake.FakeEntity;
import io.github.alphahelixdev.alpary.fake.FakeMobType;
import io.github.alphahelixdev.alpary.reflection.nms.enums.REnumEquipSlot;
import io.github.alphahelixdev.alpary.reflection.nms.packets.EntityMetaDataPacket;
import io.github.alphahelixdev.alpary.reflection.nms.wrappers.EntityWrapper;
import io.github.alphahelixdev.alpary.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class FakeBigItem extends FakeEntity {
	
	private final ItemStack itemStack;
	
	FakeBigItem(String name, Location start, Object nmsEntity, ItemStack itemStack) {
		super(name, start, nmsEntity);
		this.itemStack = itemStack;
	}
	
	public static FakeBigItem spawn(Player p, Location loc, String name, ItemStack itemStack) {
		FakeBigItem fBI = spawnTemporary(p, loc, name, itemStack);
		
		if(fBI == null)
			return null;
		
		Fake.storage(FakeBigItem.class).addEntity(fBI);
		
		return fBI;
	}
	
	public static FakeBigItem spawnTemporary(Player p, Location loc, String name, ItemStack stack) {
		FakeMob fakeGiant = FakeMob.spawnTemporary(p, loc, name, FakeMobType.GIANT, false);
		
		EntityWrapper g = new EntityWrapper(fakeGiant.getNmsEntity());
		Object dw = g.getDataWatcher();
		
		g.setInvisible(true);
		
		Utils.nms().sendPacket(p, new EntityMetaDataPacket(g.getEntityID(), dw));
		
		
		fakeGiant.equip(p, stack, REnumEquipSlot.HAND);
		
		FakeBigItem fBI = new FakeBigItem(name, loc, fakeGiant.getNmsEntity(), stack);
		
		Fake.getEntityHandler().addFakeEntity(p, fBI);
		
		return fBI;
	}
	
	public FakeBigItem spawn(Player p) {
		return FakeBigItem.spawnTemporary(p, getStart(), getName(), itemStack);
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		if(!super.equals(o)) return false;
		FakeBigItem that = (FakeBigItem) o;
		return Objects.equals(itemStack, that.itemStack);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), itemStack);
	}
	
	@Override
	public String toString() {
		return "FakeBigItem{" +
				"itemStack=" + itemStack +
				'}';
	}
}
