package de.alphahelix.alphalibary.reflection.nms.wrappers;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import org.bukkit.Location;


public class EntityWrapper {

    private static final ReflectionUtil.SaveMethod SLOC = ReflectionUtil.getDeclaredMethod("setLocation", "Entity",
            double.class, double.class, double.class, float.class, float.class);

    private static final ReflectionUtil.SaveMethod SINVIS = ReflectionUtil.getDeclaredMethod("setInvisible", "Entity",
            boolean.class);

    private static final ReflectionUtil.SaveMethod SCUSTOMNAME = ReflectionUtil.getDeclaredMethod("setCustomName", "Entity",
            String.class);

    private static final ReflectionUtil.SaveMethod SCUSTOMNAMEVIS = ReflectionUtil.getDeclaredMethod("setCustomNameVisible", "Entity",
            boolean.class);

    private static final ReflectionUtil.SaveMethod STRIDING = ReflectionUtil.getDeclaredMethod("startRiding", "Entity",
            ReflectionUtil.getNmsClass("Entity"));

    private static final ReflectionUtil.SaveMethod SPRIDING = ReflectionUtil.getDeclaredMethod("stopRiding", "Entity");

    private static final ReflectionUtil.SaveMethod SNOGRAV = ReflectionUtil.getDeclaredMethod("setNoGravity", "Entity",
            boolean.class);

    private static final ReflectionUtil.SaveMethod GDATAWATCHER = ReflectionUtil.getDeclaredMethod("getDataWatcher", "Entity");

    private final Object entity;
    private final int entityID;
    private boolean stackTrace = true;

    public EntityWrapper(Object entity) {
        this.entity = entity;
        this.entityID = ReflectionUtil.getEntityID(entity);
    }

    public EntityWrapper(Object entity, boolean stackTrace) {
        this.entity = entity;
        this.stackTrace = stackTrace;
        this.entityID = ReflectionUtil.getEntityID(entity);
    }

    public Object getEntity() {
        return entity;
    }

    public boolean isStackTrace() {
        return stackTrace;
    }

    public EntityWrapper setStackTrace(boolean stackTrace) {
        this.stackTrace = stackTrace;
        return this;
    }

    public int getEntityID() {
        return entityID;
    }

    public void setLocation(Location location) {
        SLOC.invoke(entity, stackTrace, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public void setInvisible(boolean invisible) {
        SINVIS.invoke(entity, stackTrace, invisible);
    }

    public void setCustomName(String name) {
        SCUSTOMNAME.invoke(entity, stackTrace, name);
    }

    public void setCustomNameVisible(boolean visible) {
        SCUSTOMNAMEVIS.invoke(entity, stackTrace, visible);
    }

    public void startRiding(Object entityToRide) {
        STRIDING.invoke(entity, stackTrace, entityToRide);
    }

    public void stopRiding() {
        SPRIDING.invoke(entity, stackTrace);
    }

    public void setNoGravity(boolean noGravity) {
        SNOGRAV.invoke(entity, stackTrace, noGravity);
    }

    public Object getDataWatcher() {
        return GDATAWATCHER.invoke(entity, stackTrace);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityWrapper that = (EntityWrapper) o;
        return getEntityID() == that.getEntityID() &&
                isStackTrace() == that.isStackTrace() &&
                Objects.equal(getEntity(), that.getEntity());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getEntity(), getEntityID(), isStackTrace());
    }

    @Override
    public String toString() {
        return "EntityWrapper{" +
                "entity=" + entity +
                ", entityID=" + entityID +
                ", stackTrace=" + stackTrace +
                '}';
    }
}
