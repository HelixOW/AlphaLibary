package io.github.alphahelixdev.alpary.reflection.nms.wrappers;

import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveMethod;
import org.bukkit.Location;

import java.util.Objects;

public class EntityWrapper {

    private static final SaveMethod ENTITY_SET_LOC = NMSUtil.getReflections().getDeclaredMethod(
            "setLocation", Utils.nms().getNMSClass("Entity"), double.class, double.class, double.class,
            float.class, float.class);

    private static final SaveMethod ENTITY_SET_INVISIBLE = NMSUtil.getReflections().getDeclaredMethod(
            "setInvisible", Utils.nms().getNMSClass("Entity"), boolean.class);

    private static final SaveMethod ENTITY_SET_CUSTOM_NAME = NMSUtil.getReflections().getDeclaredMethod(
            "setCustomName", Utils.nms().getNMSClass("Entity"), String.class);

    private static final SaveMethod ENTITY_SET_CUSTOM_NAME_VISIBLE = NMSUtil.getReflections().getDeclaredMethod(
            "setCustomNameVisible", Utils.nms().getNMSClass("Entity"), boolean.class);

    private static final SaveMethod ENTITY_START_RIDING = NMSUtil.getReflections().getDeclaredMethod(
            "startRiding", Utils.nms().getNMSClass("Entity"), Utils.nms().getNMSClass("Entity"));

    private static final SaveMethod ENTITY_STOP_RIDING = NMSUtil.getReflections().getDeclaredMethod(
            "stopRiding", Utils.nms().getNMSClass("Entity"));

    private static final SaveMethod ENTITY_SET_NO_GRAVITIY = NMSUtil.getReflections().getDeclaredMethod(
            "setNoGravity", Utils.nms().getNMSClass("Entity"), boolean.class);

    private static final SaveMethod ENTITY_GET_DATA_WATCHER = NMSUtil.getReflections().getDeclaredMethod(
            "getDataWatcher", Utils.nms().getNMSClass("Entity"));

    private final Object entity;
    private final int entityID;
    private boolean stackTrace = true;

    public EntityWrapper(Object entity) {
        this.entity = entity;
        this.entityID = Utils.nms().getNMSEntityID(entity);
    }

    public EntityWrapper(Object entity, boolean stackTrace) {
        this.entity = entity;
        this.stackTrace = stackTrace;
        this.entityID = Utils.nms().getNMSEntityID(entity);
    }

    public static SaveMethod getEntitySetLoc() {
        return EntityWrapper.ENTITY_SET_LOC;
    }

    public static SaveMethod getEntitySetInvisible() {
        return EntityWrapper.ENTITY_SET_INVISIBLE;
    }

    public static SaveMethod getEntitySetCustomName() {
        return EntityWrapper.ENTITY_SET_CUSTOM_NAME;
    }

    public static SaveMethod getEntitySetCustomNameVisible() {
        return EntityWrapper.ENTITY_SET_CUSTOM_NAME_VISIBLE;
    }

    public static SaveMethod getStriding() {
        return EntityWrapper.ENTITY_START_RIDING;
    }

    public static SaveMethod getEntityStopRiding() {
        return EntityWrapper.ENTITY_STOP_RIDING;
    }

    public static SaveMethod getEntitySetNoGravitiy() {
        return EntityWrapper.ENTITY_SET_NO_GRAVITIY;
    }

    public static SaveMethod getEntityGetDataWatcher() {
        return EntityWrapper.ENTITY_GET_DATA_WATCHER;
    }

    public void setLocation(Location location) {
        EntityWrapper.getEntitySetLoc().invoke(this.getEntity(), this.isStackTrace(), location.getX(), location.getY(),
                location.getZ(), location.getYaw(), location.getPitch());
    }

    public void setInvisible(boolean invisible) {
        EntityWrapper.getEntitySetInvisible().invoke(this.getEntity(), this.isStackTrace(), invisible);
    }

    public void setCustomName(String name) {
        EntityWrapper.getEntitySetCustomName().invoke(this.getEntity(), this.isStackTrace(), name);
    }

    public void setCustomNameVisible(boolean visible) {
        EntityWrapper.getEntitySetCustomNameVisible().invoke(this.getEntity(), this.isStackTrace(), visible);
    }

    public void startRiding(Object entityToRide) {
        EntityWrapper.getStriding().invoke(this.getEntity(), this.isStackTrace(), entityToRide);
    }

    public void stopRiding() {
        EntityWrapper.getEntityStopRiding().invoke(this.getEntity(), this.isStackTrace());
    }

    public void setNoGravity(boolean noGravity) {
        EntityWrapper.getEntitySetNoGravitiy().invoke(this.getEntity(), this.isStackTrace(), noGravity);
    }

    public Object getDataWatcher() {
        return EntityWrapper.getEntityGetDataWatcher().invoke(this.getEntity(), this.isStackTrace());
    }

    public Object getEntity() {
        return this.entity;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public boolean isStackTrace() {
        return this.stackTrace;
    }

    public EntityWrapper setStackTrace(boolean stackTrace) {
        this.stackTrace = stackTrace;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityWrapper that = (EntityWrapper) o;
        return this.getEntityID() == that.getEntityID() &&
                this.isStackTrace() == that.isStackTrace() &&
                Objects.equals(this.getEntity(), that.getEntity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getEntity(), this.getEntityID(), this.isStackTrace());
    }

    @Override
    public String toString() {
        return "EntityWrapper{" +
                "                            entity=" + this.entity +
                ",                             entityID=" + this.entityID +
                ",                             stackTrace=" + this.stackTrace +
                '}';
    }
}
