package de.alphahelix.alphalibary.nms.packets;

import de.alphahelix.alphalibary.nms.enums.REnumEquipSlot;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import org.bukkit.inventory.ItemStack;

public class PPOEntityEquipment implements IPacket {

    private static final ReflectionUtil.SaveConstructor PACKET =
            ReflectionUtil.getDeclaredConstructor("PacketPlayOutEntityEquipment",
                    int.class, ReflectionUtil.getNmsClass("EnumItemSlot"), ReflectionUtil.getNmsClass("ItemStack"));

    private int entityID;
    private ItemStack itemStack;
    private REnumEquipSlot equipSlot;

    public PPOEntityEquipment(int entityID, ItemStack itemStack, REnumEquipSlot equipSlot) {
        this.entityID = entityID;
        this.itemStack = itemStack;
        this.equipSlot = equipSlot;
    }

    public int getEntityID() {
        return entityID;
    }

    public PPOEntityEquipment setEntityID(int entityID) {
        this.entityID = entityID;
        return this;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public PPOEntityEquipment setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    public REnumEquipSlot getEquipSlot() {
        return equipSlot;
    }

    public PPOEntityEquipment setEquipSlot(REnumEquipSlot equipSlot) {
        this.equipSlot = equipSlot;
        return this;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return PACKET.newInstance(stackTrace, entityID, ReflectionUtil.getNMSItemStack(itemStack), equipSlot.getNmsSlot());
    }
}
