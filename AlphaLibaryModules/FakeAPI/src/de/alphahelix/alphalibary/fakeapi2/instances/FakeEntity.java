package de.alphahelix.alphalibary.fakeapi2.instances;

import com.google.gson.annotations.Expose;
import de.alphahelix.alphalibary.core.AlphaLibary;
import de.alphahelix.alphalibary.core.utilites.players.PlayerMap;
import de.alphahelix.alphalibary.core.utils.LocationUtil;
import de.alphahelix.alphalibary.fakeapi2.FakeModule;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.reflection.nms.packets.*;
import de.alphahelix.alphalibary.storage.sql2.annotations.Unique;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.UUID;

public abstract class FakeEntity {
	
	private final String name;
	@Unique
	private final UUID id;
	private final Location start;
	
	@Expose
	private final Object nmsEntity;
	@Expose
	private final int entityID;
	@Expose
	private final PlayerMap<BukkitTask> followMap = new PlayerMap<>();
	@Expose
	private final PlayerMap<BukkitTask> splitMap = new PlayerMap<>();
	@Expose
	private final PlayerMap<BukkitTask> stareMap = new PlayerMap<>();
	@Expose
	private Location current;
	
	protected FakeEntity(String name, Location start, Object nmsEntity) {
		this.name = name;
		this.id = UUID.randomUUID();
		this.start = start;
		this.current = start;
		this.nmsEntity = nmsEntity;
		this.entityID = ReflectionUtil.getEntityID(nmsEntity);
	}
	
	public final String getName() {
		return name;
	}
	
	public final UUID getId() {
		return id;
	}
	
	public final Location getStart() {
		return start;
	}
	
	public PlayerMap<BukkitTask> getSplit() {
		return splitMap;
	}
	
	public FakeEntity cancelAllSplittedTasks(Player p) {
		if(splitMap.containsKey(p)) {
			splitMap.get(p).cancel();
			splitMap.remove(p);
		}
		return this;
	}
	
	public FakeEntity unFollow(Player p) {
		if(followMap.containsKey(p)) {
			followMap.get(p).cancel();
			followMap.remove(p);
		}
		return this;
	}
	
	public boolean hasFollower(Player toCheck) {
		return followMap.containsKey(toCheck);
	}
	
	public FakeEntity normalizeLook(Player p) {
		if(stareMap.containsKey(p)) {
			stareMap.get(p).cancel();
			stareMap.remove(p);
		}
		return this;
	}
	
	public abstract <T extends FakeEntity> T spawn(Player p);
	
	public void destroy(Player p) {
		ReflectionUtil.sendPacket(p, new PPOEntityDestroy(getEntityID()));
		FakeModule.getEntityHandler().removeFakeEntity(p, this);
	}
	
	public final int getEntityID() {
		return entityID;
	}
	
	public FakeEntity move(Player p, double x, double y, double z, float yaw, float pitch) {
		Location ne = getCurrent().clone().add(x, y, z);
		
		ReflectionUtil.sendPacket(p, new PPORelEntityMove(
				getEntityID(),
				getCurrent().getX() - ne.getX(),
				getCurrent().getY() - ne.getY(),
				getCurrent().getZ() - ne.getZ(),
				false
		));
		ReflectionUtil.sendPacket(p, new PPOEntityHeadRotation(getNmsEntity(), yaw));
		ReflectionUtil.sendPacket(p, new PPOEntityLook(getEntityID(), yaw, pitch, false));
		
		getCurrent().add(x, y, z);
		return this;
	}
	
	public final Location getCurrent() {
		return current;
	}
	
	public final Object getNmsEntity() {
		return nmsEntity;
	}
	
	public final FakeEntity setCurrent(Location current) {
		this.current = current;
		return this;
	}
	
	public FakeEntity follow(Player p, Player toFollow) {
		getFollow().put(p, new BukkitRunnable() {
			@Override
			public void run() {
				try {
					teleport(p, LocationUtil.getLocationBehindPlayer(toFollow, 2));
				} catch(ReflectiveOperationException e) {
					e.printStackTrace();
				}
			}
		}.runTaskTimer(AlphaLibary.getInstance(), 0, 1));
		return this;
	}
	
	public final PlayerMap<BukkitTask> getFollow() {
		return followMap;
	}
	
	public FakeEntity teleport(Player p, Location loc) throws ReflectiveOperationException {
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
		
		ReflectionUtil.sendPacket(p, new PPOEntityTeleport(getNmsEntity()));
		ReflectionUtil.sendPacket(p, new PPOEntityHeadRotation(getNmsEntity(), loc.getYaw()));
		ReflectionUtil.sendPacket(p, new PPOEntityLook(getEntityID(), loc.getYaw(), loc.getPitch(), false));
		
		setCurrent(loc);
		return this;
	}
	
	public FakeEntity splitTeleport(Player p, Location to, int teleportCount, long wait) {
		Vector between = to.toVector().subtract(getCurrent().toVector());
		
		double toMoveInX = between.getX() / teleportCount;
		double toMoveInY = between.getY() / teleportCount;
		double toMoveInZ = between.getZ() / teleportCount;
		
		splitMap.put(p.getName(), new BukkitRunnable() {
			public void run() {
				if(!LocationUtil.isSameLocation(getCurrent(), to)) {
					try {
						teleport(p, getCurrent().clone().add(new Vector(toMoveInX, toMoveInY, toMoveInZ)));
					} catch(ReflectiveOperationException e) {
						e.printStackTrace();
					}
				} else
					this.cancel();
			}
		}.runTaskTimer(AlphaLibary.getInstance(), 0, wait));
		return this;
	}
	
	public FakeEntity stareAtPlayer(final Player p, final Player toStareAt, final de.alphahelix.alphalibary.fakeapi.instances.FakeMob mob) {
		getStare().put(p, new BukkitRunnable() {
			@Override
			public void run() {
				lookAtPlayer(p, toStareAt);
			}
		}.runTaskTimer(AlphaLibary.getInstance(), 0, 1));
		return this;
	}
	
	public final PlayerMap<BukkitTask> getStare() {
		return stareMap;
	}
	
	public FakeEntity lookAtPlayer(Player p, Player toLookAt) {
		ReflectionUtil.sendPacket(p, new PPOEntityHeadRotation(getNmsEntity(), LocationUtil.lookAt(getCurrent(), toLookAt.getLocation()).getYaw()));
		ReflectionUtil.sendPacket(p, new PPOEntityLook(getEntityID(),
				LocationUtil.lookAt(getCurrent(), toLookAt.getLocation()).getYaw(),
				LocationUtil.lookAt(getCurrent(), toLookAt.getLocation()).getPitch(), true));
		return this;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name, id, start, nmsEntity, entityID, current, followMap, splitMap);
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		FakeEntity entity = (FakeEntity) o;
		return entityID == entity.entityID &&
				Objects.equals(name, entity.name) &&
				Objects.equals(id, entity.id) &&
				Objects.equals(start, entity.start) &&
				Objects.equals(nmsEntity, entity.nmsEntity) &&
				Objects.equals(current, entity.current) &&
				Objects.equals(followMap, entity.followMap) &&
				Objects.equals(splitMap, entity.splitMap);
	}
	
	@Override
	public String toString() {
		return "FakeEntity{" +
				"name='" + name + '\'' +
				", id=" + id +
				", start=" + start +
				", nmsEntity=" + nmsEntity +
				", entityID=" + entityID +
				", current=" + current +
				", followMap=" + followMap +
				", splitMap=" + followMap +
				'}';
	}
}
