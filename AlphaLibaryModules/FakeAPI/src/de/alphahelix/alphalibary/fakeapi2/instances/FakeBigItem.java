package de.alphahelix.alphalibary.fakeapi2.instances;

import de.alphahelix.alphalibary.fakeapi.FakeAPI;
import de.alphahelix.alphalibary.fakeapi.FakeMobType;
import de.alphahelix.alphalibary.fakeapi.instances.FakeMob;
import de.alphahelix.alphalibary.fakeapi.utils.MobFakeUtil;
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
	
	public FakeBigItem(String name, Location start, Object nmsEntity, ItemStack itemStack) {
		super(name, start, nmsEntity);
		this.itemStack = itemStack;
	}
	
	public static FakeBigItem spawnBigItem(Player p, Location loc, String name, ItemStack itemStack) {
		FakeBigItem fBI = spawnTemporaryBigItem(p, loc, name, itemStack);
		
		if(fBI == null)
			return null;
		
		FakeModule.getStorage(FakeBigItem.class).addEntity(fBI);
		
		return fBI;
	}
	
	public static FakeBigItem spawnTemporaryBigItem(Player p, Location loc, String name, ItemStack stack) {
		FakeMob fakeGiant = MobFakeUtil.spawnTemporaryMob(p, loc, name, FakeMobType.GIANT, false);
		
		//TODO: Actual version 2.0
		
		EntityWrapper g = new EntityWrapper(fakeGiant.getNmsEntity());
		Object dw = g.getDataWatcher();
		
		g.setInvisible(true);
		
		ReflectionUtil.sendPacket(p, new PPOEntityMetadata(g.getEntityID(), dw));
		
		MobFakeUtil.equipMob(p, fakeGiant, stack, REnumEquipSlot.HAND);
		
		de.alphahelix.alphalibary.fakeapi.instances.FakeBigItem fBI = new de.alphahelix.alphalibary.fakeapi.instances.FakeBigItem(loc, name, fakeGiant.getNmsEntity(), stack);
		
		FakeAPI.addFakeEntity(p, fBI);
		return fBI;
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
