package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;

import java.util.Objects;

public class EntityMetaDataPacket implements IPacket {

    private static final SaveConstructor PACKET = NMSUtil.getReflections().getDeclaredConstructor(
            Utils.nms().getNMSClass("PacketPlayOutEntityMetadata"), int.class,
            Utils.nms().getNMSClass("DataWatcher"), boolean.class);

    private int entityID;
    private Object dataWatcher;
    private boolean updateItem = true; //not sure about that

    public EntityMetaDataPacket(int entityID, Object dataWatcher) {
        this.entityID = entityID;
        this.dataWatcher = dataWatcher;
    }

    private static SaveConstructor getPacket() {
        return EntityMetaDataPacket.PACKET;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return EntityMetaDataPacket.getPacket().newInstance(stackTrace, this.getEntityID(), this.getDataWatcher(),
                this.getUpdateItem());
    }

    public int getEntityID() {
        return this.entityID;
    }

    public EntityMetaDataPacket setEntityID(int entityID) {
        this.entityID = entityID;
        return this;
    }

    public Object getDataWatcher() {
        return this.dataWatcher;
    }

    public EntityMetaDataPacket setDataWatcher(Object dataWatcher) {
        this.dataWatcher = dataWatcher;
        return this;
    }

    public Boolean getUpdateItem() {
        return this.updateItem;
    }

    public EntityMetaDataPacket setUpdateItem(Boolean updateItem) {
        this.updateItem = updateItem;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityMetaDataPacket that = (EntityMetaDataPacket) o;
        return this.getEntityID() == that.getEntityID() &&
                Objects.equals(this.getDataWatcher(), that.getDataWatcher()) &&
                Objects.equals(this.getUpdateItem(), that.getUpdateItem());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getEntityID(), this.getDataWatcher(), this.getUpdateItem());
    }

    @Override
    public String toString() {
        return "EntityMetaDataPacket{" +
                "                            entityID=" + this.entityID +
                ",                             dataWatcher=" + this.dataWatcher +
                ",                             updateItem=" + this.updateItem +
                '}';
    }
}
