package de.alphahelix.alphalibary.nms.packets;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;

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
}
