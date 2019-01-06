package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.reflection.nms.enums.REnumEquipSlot;
import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class EntityEquipmentPacket implements IPacket {

    private static final SaveConstructor PACKET =
            NMSUtil.getReflections().getDeclaredConstructor(
                    Utils.nms().getNMSClass("PacketPlayOutEntityEquipment"), int.class,
                    Utils.nms().getNMSClass("EnumItemSlot"), Utils.nms().getNMSClass("ItemStack"));

    private int entityID;
    private ItemStack itemStack;
    private REnumEquipSlot equipSlot;

    public EntityEquipmentPacket(int entityID, ItemStack itemStack, REnumEquipSlot equipSlot) {
        this.entityID = entityID;
        this.itemStack = itemStack;
        this.equipSlot = equipSlot;
    }

    private static SaveConstructor getPacket() {
        return EntityEquipmentPacket.PACKET;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return EntityEquipmentPacket.getPacket().newInstance(stackTrace, this.getEntityID(),
                Utils.nms().getNMSItemStack(this.getItemStack()), this.getEquipSlot().getNmsSlot());
    }

    public int getEntityID() {
        return this.entityID;
    }

    public EntityEquipmentPacket setEntityID(int entityID) {
        this.entityID = entityID;
        return this;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public EntityEquipmentPacket setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    public REnumEquipSlot getEquipSlot() {
        return this.equipSlot;
    }

    public EntityEquipmentPacket setEquipSlot(REnumEquipSlot equipSlot) {
        this.equipSlot = equipSlot;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityEquipmentPacket that = (EntityEquipmentPacket) o;
        return this.getEntityID() == that.getEntityID() &&
                Objects.equals(this.getItemStack(), that.getItemStack()) &&
                this.getEquipSlot() == that.getEquipSlot();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getEntityID(), this.getItemStack(), this.getEquipSlot());
    }

    @Override
    public String toString() {
        return "EntityEquipmentPacket{" +
                "                            entityID=" + this.entityID +
                ",                             itemStack=" + this.itemStack +
                ",                             equipSlot=" + this.equipSlot +
                '}';
    }
}
