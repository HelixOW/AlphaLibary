package de.alphahelix.alphalibary.reflection.nms.packets;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;

@SuppressWarnings("ALL")
public class PPOEntityMetadata implements IPacket {

    private static final ReflectionUtil.SaveConstructor PACKET =
            ReflectionUtil.getDeclaredConstructor("PacketPlayOutEntityMetadata",
                    int.class, ReflectionUtil.getNmsClass("DataWatcher"), boolean.class);

    private int entityID;
    private Object dataWatcher;
    private Boolean updateItem; //not sure about that

    public PPOEntityMetadata(int entityID, Object dataWatcher) {
        this.entityID = entityID;
        this.dataWatcher = dataWatcher;
    }

    public int getEntityID() {
        return entityID;
    }

    public PPOEntityMetadata setEntityID(int entityID) {
        this.entityID = entityID;
        return this;
    }

    public Object getDataWatcher() {
        return dataWatcher;
    }

    public PPOEntityMetadata setDataWatcher(Object dataWatcher) {
        this.dataWatcher = dataWatcher;
        return this;
    }

    /**
     * Unsure about meaning of this parameter.
     */
    public boolean isUpdateItem() {
        return updateItem;
    }

    /**
     * Unsure about meaning of this parameter.
     */
    public PPOEntityMetadata setUpdateItem(boolean updateItem) {
        this.updateItem = updateItem;
        return this;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        if (updateItem == null)
            return PACKET.newInstance(stackTrace, entityID, dataWatcher, true);
        return PACKET.newInstance(stackTrace, entityID, dataWatcher, updateItem);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PPOEntityMetadata that = (PPOEntityMetadata) o;
        return getEntityID() == that.getEntityID() &&
                Objects.equal(getDataWatcher(), that.getDataWatcher()) &&
                Objects.equal(isUpdateItem(), that.isUpdateItem());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getEntityID(), getDataWatcher(), isUpdateItem());
    }

    @Override
    public String toString() {
        return "PPOEntityMetadata{" +
                "entityID=" + entityID +
                ", dataWatcher=" + dataWatcher +
                ", updateItem=" + updateItem +
                '}';
    }
}
