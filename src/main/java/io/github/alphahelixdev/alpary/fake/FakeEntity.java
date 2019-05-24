package io.github.alphahelixdev.alpary.fake;

import io.github.alphahelixdev.alpary.Alpary;
import io.github.alphahelixdev.alpary.reflection.nms.packets.*;
import io.github.alphahelixdev.alpary.reflection.nms.wrappers.EntityWrapper;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.whoisalphahelix.helix.reflection.SaveField;
import io.github.whoisalphahelix.sql.annotations.Column;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@ToString
public abstract class FakeEntity {
	
	private final Object nmsEntity;
	private final int entityID;
	private final Map<UUID, BukkitTask> follows = new HashMap<>();
	private final Map<UUID, BukkitTask> splits = new HashMap<>();
	private final Map<UUID, BukkitTask> stares = new HashMap<>();
	
	@Column(name = "name", type = "text")
	private String name;
	@Column(name = "id", type = "text")
	private UUID id;
	@Column(name = "start", type = "text")
	private Location start;
	@Setter
	private Location current;
	
	public FakeEntity(String name, Location start, Object nmsEntity) {
		this.name = name;
		this.id = UUID.randomUUID();
		this.start = start;
		this.current = start;
		this.nmsEntity = nmsEntity;
		this.entityID = Utils.nms().getNMSEntityID(nmsEntity);
	}
	
	public void destroy(Player p) {
		Utils.nms().sendPacket(p, new EntityDestroyPacket(new int[]{this.entityID}));
		Fake.getEntityHandler().removeFakeEntity(p, this);
	}
	
	public FakeEntity move(Player p, double x, double y, double z, float yaw, float pitch) {
		Location ne = getCurrent().clone().add(x, y, z);
		
		Utils.nms().sendPackets(p, new RelEntityMovePacket(
				this.getEntityID(),
				this.getCurrent().getX() - ne.getX(),
				this.getCurrent().getY() - ne.getY(),
				this.getCurrent().getZ() - ne.getZ(),
				false), new EntityHeadRotationPacket(this.nmsEntity, yaw), new EntityLookPacket(
				this.entityID, yaw, pitch, false
		));
		
		this.getCurrent().add(x, y, z);
		return this;
	}
	
	public FakeEntity move(Player p, Vector by, float yaw, float pitch) {
		return move(p, by.getX(), by.getY(), by.getZ(), yaw, pitch);
	}
	
	public FakeEntity follow(Player p, Player toFollow) {
		this.follows.put(p.getUniqueId(), new BukkitRunnable() {
			@Override
			public void run() {
				teleport(p, Utils.locations().getLocationBehindPlayer(toFollow, 2));
			}
		}.runTaskTimer(Alpary.getInstance(), 0, 1));
		return this;
	}
	
	public FakeEntity teleport(Player p, Location loc) {
		SaveField x = Alpary.getInstance().reflections().getField("locX", Utils.nms().getNMSClass("Entity")),
				y = Alpary.getInstance().reflections().getField("locY", Utils.nms().getNMSClass("Entity")),
				z = Alpary.getInstance().reflections().getField("locZ", Utils.nms().getNMSClass("Entity")),
				yaw = Alpary.getInstance().reflections().getField("yaw", Utils.nms().getNMSClass("Entity")),
				pitch = Alpary.getInstance().reflections().getField("pitch", Utils.nms().getNMSClass("Entity"));
		
		x.set(getNmsEntity(), loc.getX());
		y.set(getNmsEntity(), loc.getY());
		z.set(getNmsEntity(), loc.getZ());
		yaw.set(getNmsEntity(), loc.getYaw());
		pitch.set(getNmsEntity(), loc.getPitch());
		
		Utils.nms().sendPackets(p, new EntityTeleportPacket(this.getNmsEntity()),
				new EntityHeadRotationPacket(this.getNmsEntity(), loc.getYaw()), new EntityLookPacket(
						this.getEntityID(), loc.getYaw(), loc.getPitch(), false));
		
		this.setCurrent(loc);
		return this;
	}
	
	public FakeEntity splitTeleport(Player p, Location to, int teleportCount, long wait) {
		Vector between = to.toVector().subtract(getCurrent().toVector());
		
		double toMoveInX = between.getX() / teleportCount;
		double toMoveInY = between.getY() / teleportCount;
		double toMoveInZ = between.getZ() / teleportCount;
		
		this.splits.put(p.getUniqueId(), new BukkitRunnable() {
			public void run() {
				if(!Utils.locations().isSameBlockLocation(getCurrent(), to)) {
					teleport(p, getCurrent().clone().add(new Vector(toMoveInX, toMoveInY, toMoveInZ)));
				} else
					this.cancel();
			}
		}.runTaskTimer(Alpary.getInstance(), 0, wait));
		return this;
	}
	
	public FakeEntity stareAtPlayer(Player p, Player toStareAt) {
		stares.put(p.getUniqueId(), new BukkitRunnable() {
			@Override
			public void run() {
				lookAtPlayer(p, toStareAt);
			}
		}.runTaskTimer(Alpary.getInstance(), 0, 1));
		return this;
	}
	
	public FakeEntity lookAtPlayer(Player p, Player toLookAt) {
		Location delta = Utils.locations().lookAt(this.getCurrent(), toLookAt.getLocation());
		
		Utils.nms().sendPackets(p,
				new EntityHeadRotationPacket(this.getNmsEntity(), delta.getYaw()),
				new EntityLookPacket(this.getEntityID(), delta.getYaw(), delta.getPitch(), true));
		return this;
	}
	
	public FakeEntity setName(Player p, String name) {
		EntityWrapper e = new EntityWrapper(getNmsEntity());
		
		e.setCustomName(name.replace("&", "§").replace("_", " "));
		e.setCustomNameVisible(true);
		
		Utils.nms().sendPacket(p, new EntityMetaDataPacket(e.getEntityID(), e.getDataWatcher()));
		return this;
	}
	
	public FakeEntity normalizeLook(Player p) {
		if (this.stares.containsKey(p.getUniqueId())) {
			this.stares.get(p.getUniqueId()).cancel();
			this.stares.remove(p.getUniqueId());
		}
		return this;
	}
	
	public FakeEntity cancelAllSplitted(Player p) {
		if (this.splits.containsKey(p.getUniqueId())) {
			this.splits.get(p.getUniqueId()).cancel();
			this.splits.remove(p.getUniqueId());
		}
		return this;
	}
	
	public boolean hasFollower(Player p) {
		return this.follows.containsKey(p.getUniqueId());
	}
	
	public FakeEntity cancelFollows(Player p) {
		if (hasFollower(p)) {
			this.follows.get(p.getUniqueId()).cancel();
			this.follows.remove(p.getUniqueId());
		}
		return this;
	}
	
	public abstract <T extends FakeEntity> T spawn(Player p);
}
