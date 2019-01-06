package io.github.alphahelixdev.alpary.fake;

import com.google.gson.annotations.Expose;
import io.github.alphahelixdev.alpary.reflection.nms.packets.EntityDestroyPacket;
import io.github.alphahelixdev.alpary.reflection.nms.packets.EntityHeadRotationPacket;
import io.github.alphahelixdev.alpary.reflection.nms.packets.EntityLookPacket;
import io.github.alphahelixdev.alpary.reflection.nms.packets.RelEntityMovePacket;
import io.github.alphahelixdev.alpary.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class FakeEntity {

    private final String name;
    private final UUID id;
    private final Location start;

    @Expose
    private transient final Object nmsEntity;
    @Expose
    private transient final int entityID;
    @Expose
    private transient final Map<UUID, BukkitTask> follows = new HashMap<>();
    @Expose
    private transient final Map<UUID, BukkitTask> splits = new HashMap<>();
    @Expose
    private transient final Map<UUID, BukkitTask> stares = new HashMap<>();
    @Expose
    private transient Location current;

    public FakeEntity(String name, Location start, Object nmsEntity) {
        this.name = name;
        this.id = UUID.randomUUID();
        this.start = start;
        this.current = start;
        this.nmsEntity = nmsEntity;
        this.entityID = Utils.nms().getNMSEntityID(nmsEntity);
    }

    public void destroy(Player p) {
        Utils.nms().sendPacket(p, new EntityDestroyPacket(this.entityID));
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

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public Location getStart() {
        return start;
    }

    public Object getNmsEntity() {
        return nmsEntity;
    }

    public int getEntityID() {
        return entityID;
    }

    public Location getCurrent() {
        return current;
    }

    public void setCurrent(Location current) {
        this.current = current;
    }

    public abstract <T extends FakeEntity> T spawn(Player p);
}
