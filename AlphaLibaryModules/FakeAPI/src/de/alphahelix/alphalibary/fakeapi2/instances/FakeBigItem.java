package de.alphahelix.alphalibary.fakeapi2.instances;

import de.alphahelix.alphalibary.fakeapi2.FakeMobType;
import de.alphahelix.alphalibary.fakeapi2.FakeModule;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.reflection.nms.enums.REnumEquipSlot;
import de.alphahelix.alphalibary.reflection.nms.packets.PPOEntityMetadata;
import de.alphahelix.alphalibary.reflection.nms.wrappers.EntityWrapper;
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
		
		FakeModule.getStorage(FakeBigItem.class).addEntity(fBI);
		
		return fBI;
	}
	
	public static FakeBigItem spawnTemporary(Player p, Location loc, String name, ItemStack stack) {
		FakeMob fakeGiant = FakeMob.spawnTemporary(p, loc, name, FakeMobType.GIANT, false);
		
		EntityWrapper g = new EntityWrapper(fakeGiant.getNmsEntity());
		Object dw = g.getDataWatcher();
		
		g.setInvisible(true);
		
		ReflectionUtil.sendPacket(p, new PPOEntityMetadata(g.getEntityID(), dw));
		
		
		fakeGiant.equip(p, stack, REnumEquipSlot.HAND);
		
		FakeBigItem fBI = new FakeBigItem(name, loc, fakeGiant.getNmsEntity(), stack);
		
		FakeModule.getEntityHandler().addFakeEntity(p, fBI);
		
		return fBI;
	}
	
	public FakeBigItem spawn(Player p) {
		return FakeBigItem.spawnTemporary(p, getStart(), getName(), itemStack);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), itemStack);
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
	public String toString() {
		return "FakeBigItem{" +
				"itemStack=" + itemStack +
				'}';
	}
}
