package de.alphahelix.alphalibary.fakeapi2.instances;

import com.mojang.authlib.GameProfile;
import de.alphahelix.alphalibary.core.AlphaLibary;
import de.alphahelix.alphalibary.core.utils.LocationUtil;
import de.alphahelix.alphalibary.fakeapi2.FakeModule;
import de.alphahelix.alphalibary.inventories.item.SkullItemBuilder;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.reflection.nms.enums.REnumEquipSlot;
import de.alphahelix.alphalibary.reflection.nms.packets.*;
import de.alphahelix.alphalibary.reflection.nms.wrappers.EntityWrapper;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;

public class FakeArmorstand extends FakeEntity {
	public FakeArmorstand(String name, Location start, Object nmsEntity) {
		super(name, start, nmsEntity);
	}
	
	public static FakeArmorstand spawn(Player p, Location loc, String name) {
		FakeArmorstand fakeA = spawnTemporary(p, loc, name);
		
		if(fakeA == null)
			return null;
		
		FakeModule.getStorage(FakeArmorstand.class).addEntity(fakeA);
		
		return fakeA;
	}
	
	public static FakeArmorstand spawnTemporary(Player p, Location loc, String name) {
		Object armorstand = CustomSpawnable.ENTITY_ARMORSTAND.newInstance(false, ReflectionUtil.getWorldServer(loc.getWorld()));
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
	
	public void destroy(Player p) {
		ReflectionUtil.sendPacket(p, new PPOEntityDestroy(ReflectionUtil.getEntityID(getNmsEntity())).getPacket(false));
		
		FakeModule.getStorage(FakeArmorstand.class).removeEntity(this);
	}
	
	public FakeArmorstand move(Player p, double x, double y, double z) {
		Location ne = getCurrent().clone().add(x, y, z);
		
		ReflectionUtil.sendPacket(p, new PPORelEntityMove(
				ReflectionUtil.getEntityID(getNmsEntity()),
				getCurrent().getX() - ne.getX(), getCurrent().getY() - ne.getY(), getCurrent().getZ() - ne.getZ(), false
		).getPacket(false));
		
		getCurrent().add(x, y, z);
		return this;
	}
	
	public FakeArmorstand splitTeleport(Player p, Location to, int teleportCount, long wait) {
		Vector between = to.clone().subtract(getCurrent()).toVector();
		
		double toMoveInX = between.getX() / teleportCount;
		double toMoveInY = between.getY() / teleportCount;
		double toMoveInZ = between.getZ() / teleportCount;
		
		getSplit().put(p, new BukkitRunnable() {
			public void run() {
				if(!LocationUtil.isSameLocation(getCurrent(), to)) {
					try {
						teleport(p, getCurrent().clone().add(toMoveInX, toMoveInY, toMoveInZ));
					} catch(ReflectiveOperationException e) {
						e.printStackTrace();
					}
				} else
					this.cancel();
			}
		}.runTaskTimer(AlphaLibary.getInstance(), 0, wait));
		return this;
	}
	
	public FakeArmorstand teleport(Player p, Location loc) throws ReflectiveOperationException {
		Field x = ReflectionUtil.getNmsClass("Entity").getField("locX"), y = ReflectionUtil.getNmsClass("Entity").getField("locY"), z = ReflectionUtil.getNmsClass("Entity").getField("locZ"), yaw = ReflectionUtil.getNmsClass("Entity").getField("yaw"), pitch = ReflectionUtil.getNmsClass("Entity").getField("pitch");
		
		x.setAccessible(true);
		y.setAccessible(true);
		z.setAccessible(true);
		yaw.setAccessible(true);
		pitch.setAccessible(true);
		
		x.set(getNmsEntity(), loc.getX());
		y.set(getNmsEntity(), loc.getY());
		z.set(getNmsEntity(), loc.getZ());
		yaw.set(getNmsEntity(), loc.getYaw());
		pitch.set(getNmsEntity(), loc.getPitch());
		
		ReflectionUtil.sendPacket(p, new PPOEntityTeleport(getNmsEntity()).getPacket(false));
		
		setCurrent(loc);
		return this;
	}
	
	public FakeArmorstand equipSkull(Player p, String textureURL) {
		return equip(p, SkullItemBuilder.getCustomSkull(textureURL), REnumEquipSlot.HELMET);
	}
	
	public FakeArmorstand equip(Player p, ItemStack item, REnumEquipSlot slot) {
		ReflectionUtil.sendPacket(p, new PPOEntityEquipment(
				ReflectionUtil.getEntityID(getNmsEntity()),
				item, slot
		).getPacket(false));
		return this;
	}
	
	public FakeArmorstand equipSkull(Player p, GameProfile profile) {
		return equip(p, SkullItemBuilder.getPlayerSkull(profile.getName()), REnumEquipSlot.HELMET);
	}
	
	public FakeArmorstand followArmorstand(Player p, Player toFollow) {
		getFollow().put(p.getName(), new BukkitRunnable() {
			@Override
			public void run() {
				try {
					teleport(p, toFollow.getLocation());
				} catch(ReflectiveOperationException e) {
					e.printStackTrace();
				}
			}
		}.runTaskTimer(AlphaLibary.getInstance(), 0, 1));
		return this;
	}
	
	public FakeArmorstand setName(Player p, String name) {
		EntityWrapper a = new EntityWrapper(getNmsEntity());
		
		a.setCustomName(name.replace("&", "§").replace("_", " "));
		
		Object dw = a.getDataWatcher();
		
		ReflectionUtil.sendPacket(p, new PPOEntityMetadata(
				ReflectionUtil.getEntityID(getNmsEntity()),
				dw
		).getPacket(false));
		return this;
	}
	
	public FakeArmorstand changeVisibilty(Player p, boolean visible) {
		EntityWrapper a = new EntityWrapper(getNmsEntity());
		
		a.setInvisible(!visible);
		
		Object dw = a.getDataWatcher();
		
		ReflectionUtil.sendPacket(p, new PPOEntityMetadata(
				ReflectionUtil.getEntityID(getNmsEntity()),
				dw
		).getPacket(false));
		return this;
	}
}
