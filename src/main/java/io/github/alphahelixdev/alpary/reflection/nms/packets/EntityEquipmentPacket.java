package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.reflection.nms.enums.REquipSlot;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.whoisalphahelix.helix.reflection.SaveConstructor;
import lombok.*;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class EntityEquipmentPacket implements IPacket {

    private static final SaveConstructor PACKET =
            Utils.nms().getDeclaredConstructor(
                    Utils.nms().getNMSClass("PacketPlayOutEntityEquipment"), int.class,
                    Utils.nms().getNMSClass("EnumItemSlot"), Utils.nms().getNMSClass("ItemStack"));

    private int entityID;
    private ItemStack itemStack;
    private REquipSlot equipSlot;

    private static SaveConstructor getPacket() {
        return EntityEquipmentPacket.PACKET;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return EntityEquipmentPacket.getPacket().newInstance(stackTrace, this.getEntityID(),
                Utils.nms().getNMSItemStack(this.getItemStack()), this.getEquipSlot().getNmsSlot());
    }
}
