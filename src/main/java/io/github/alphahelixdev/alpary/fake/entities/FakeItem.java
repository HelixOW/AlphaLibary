package io.github.alphahelixdev.alpary.fake.entities;

import io.github.alphahelixdev.alpary.fake.Fake;
import io.github.alphahelixdev.alpary.fake.FakeEntity;
import io.github.alphahelixdev.alpary.reflection.nms.packets.EntityMetaDataPacket;
import io.github.alphahelixdev.alpary.reflection.nms.packets.SpawnEntityPacket;
import io.github.alphahelixdev.alpary.reflection.nms.wrappers.EntityItemWrapper;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.whoisalphahelix.sql.annotations.Column;
import io.github.whoisalphahelix.sql.annotations.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Table("items")
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FakeItem extends FakeEntity {
	
	@Column(name = "type", type = "text")
	private final Material type;
	
	public FakeItem(String name, Location start, Object nmsEntity, Material type) {
		super(name, start, nmsEntity);
		this.type = type;
	}
	
	public static FakeItem spawnItem(Player p, Location loc, String name, Material type) {
		FakeItem fI = spawnTemporary(p, loc, name, type);
		
		if(fI == null)
			return null;
		
		Fake.storage(FakeItem.class).addEntity(fI);
		return fI;
	}
	
	public static FakeItem spawnTemporary(Player p, Location loc, String name, Material type) {
		Object item = CustomSpawnable.ITEM.newInstance(false,
				Utils.nms().getWorldServer(p.getWorld()),
				loc.getX(),
				loc.getY(),
				loc.getZ(),
				Utils.nms().getNMSItemStack(new ItemStack(type)));
		EntityItemWrapper i = new EntityItemWrapper(item);
		
		i.setItemStack(new ItemStack(type));
		
		Utils.nms().sendPacket(p, new SpawnEntityPacket(item, 2, 0));
		Utils.nms().sendPacket(p, new EntityMetaDataPacket(i.getEntityID(), i.getDataWatcher()));
		
		FakeItem fI = new FakeItem(name, loc, item, type);
		
		Fake.getEntityHandler().addFakeEntity(p, fI);
		
		return fI;
	}
	
	public FakeItem spawn(Player p) {
		return FakeItem.spawnTemporary(p, getStart(), getName(), getType());
	}
	
	public void setGravity(Player p, boolean gravity) {
		EntityItemWrapper e = new EntityItemWrapper(getNmsEntity());
		
		e.setNoGravity(!gravity);
		
		Utils.nms().sendPacket(p, new EntityMetaDataPacket(e.getEntityID(), e.getDataWatcher()));
	}
}
