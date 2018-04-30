package de.alphahelix.alphalibary.fakeapi2.instances;

import de.alphahelix.alphalibary.fakeapi2.FakeModule;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.reflection.nms.packets.PPOEntityMetadata;
import de.alphahelix.alphalibary.reflection.nms.packets.PPOSpawnEntity;
import de.alphahelix.alphalibary.reflection.nms.wrappers.EntityItemWrapper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FakeItem extends FakeEntity {
	
	private final Material type;
	
	public FakeItem(String name, Location start, Object nmsEntity, Material type) {
		super(name, start, nmsEntity);
		this.type = type;
	}
	
	public static FakeItem spawnItem(Player p, Location loc, String name, Material type) {
		FakeItem fI = spawnTemporary(p, loc, name, type);
		
		if(fI == null)
			return null;
		
		FakeModule.getStorage(FakeItem.class).addEntity(fI);
		return fI;
	}
	
	public static FakeItem spawnTemporary(Player p, Location loc, String name, Material type) {
		Object item = CustomSpawnable.ITEM.newInstance(false,
				ReflectionUtil.getWorldServer(p.getWorld()),
				loc.getX(),
				loc.getY(),
				loc.getZ(),
				ReflectionUtil.getNMSItemStack(new ItemStack(type)));
		EntityItemWrapper i = new EntityItemWrapper(item);
		
		i.setItemStack(new ItemStack(type));
		
		ReflectionUtil.sendPacket(p, new PPOSpawnEntity(item, 2, 0));
		ReflectionUtil.sendPacket(p, new PPOEntityMetadata(i.getEntityID(), i.getDataWatcher()));
		
		FakeItem fI = new FakeItem(name, loc, item, type);
		
		FakeModule.getEntityHandler().addFakeEntity(p, fI);
		
		return fI;
	}
	
	public FakeItem spawn(Player p) {
		return FakeItem.spawnTemporary(p, getStart(), getName(), getType());
	}
	
	public Material getType() {
		return type;
	}
	
	public void setName(Player p, String name) {
		EntityItemWrapper e = new EntityItemWrapper(getNmsEntity());
		
		e.setCustomName(name.replace("&", "§").replace("_", " "));
		e.setCustomNameVisible(true);
		
		ReflectionUtil.sendPacket(p, new PPOEntityMetadata(e.getEntityID(), e.getDataWatcher()));
	}
	
	public void setGravity(Player p, boolean gravity) {
		EntityItemWrapper e = new EntityItemWrapper(getNmsEntity());
		
		e.setNoGravity(!gravity);
		
		ReflectionUtil.sendPacket(p, new PPOEntityMetadata(e.getEntityID(), e.getDataWatcher()));
	}
}
